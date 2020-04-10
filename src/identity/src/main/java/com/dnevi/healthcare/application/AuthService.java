package com.dnevi.healthcare.application;

import com.dnevi.healthcare.domain.command.ConfirmInvitation;
import com.dnevi.healthcare.domain.command.LoginRequest;
import com.dnevi.healthcare.domain.command.RegistrationRequest;
import com.dnevi.healthcare.domain.exception.AuthenticationFailed;
import com.dnevi.healthcare.domain.exception.InvalidUserTypeException;
import com.dnevi.healthcare.domain.exception.MissingInvitationTokenException;
import com.dnevi.healthcare.domain.exception.ResourceAlreadyInUseException;
import com.dnevi.healthcare.domain.exception.UserLoginException;
import com.dnevi.healthcare.domain.model.invitation.InvitationContextHint;
import com.dnevi.healthcare.domain.model.invitation.InvitationStatus;
import com.dnevi.healthcare.domain.model.role.Role;
import com.dnevi.healthcare.domain.model.user.User;
import com.dnevi.healthcare.domain.model.user.UserType;
import com.dnevi.healthcare.domain.model.user.employee.Doctor;
import com.dnevi.healthcare.domain.model.user.patient.Patient;
import com.dnevi.healthcare.domain.repository.EmployeeRepository;
import com.dnevi.healthcare.domain.repository.PatientRepository;
import com.dnevi.healthcare.domain.repository.UserRepository;
import com.dnevi.healthcare.query.viewmodel.JwtAuthenticationResponse;
import com.dnevi.healthcare.query.viewmodel.ViewModeUserResultSetBuilder;
import com.dnevi.healthcare.query.viewmodel.ViewModelUser;
import com.dnevi.healthcare.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final EmployeeRepository employeeRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final InvitationTokenService invitationTokenService;
    private final InvitationService invitationService;

    @Autowired
    public AuthService(
            UserRepository userRepository,
            PatientRepository patientRepository,
            EmployeeRepository employeeRepository,
            JwtTokenProvider jwtTokenProvider,
            RoleService roleService,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            InvitationTokenService invitationTokenService,
            InvitationService invitationService) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.employeeRepository = employeeRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.roleService = roleService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.invitationTokenService = invitationTokenService;
        this.invitationService = invitationService;
    }

    /**
     * Registers a new user in the database by performing a series of quick checks.
     *
     * @return A user object if successfully created
     */
    public ViewModelUser registerUser(RegistrationRequest command) {
        String newRegistrationRequestEmail = command.getEmail();
        if (this.emailAlreadyExists(newRegistrationRequestEmail)) {
            log.error("Email already exists: " + newRegistrationRequestEmail);
            throw new ResourceAlreadyInUseException(newRegistrationRequestEmail);
        }
        log.info("Trying to register new user [" + newRegistrationRequestEmail + "]");
        User newUser = this.createUser(command);
        var user = this.userRepository.save(newUser);
        this.handleUserType(command.getUserType(), user);

        return ViewModeUserResultSetBuilder.buildSingle(user);
    }

    private void handleUserType(UserType userType, User user) {
        switch (userType) {
            case PATIENT:
                var patient = new Patient(user);
                this.patientRepository.save(patient);
                break;
            case NURSE:
                // implement nurse entity
                break;
            case DOCTOR:
                var doctor = new Doctor(user);
                this.employeeRepository.save(doctor);
                break;
            default:
                throw new InvalidUserTypeException(userType);
        }
    }

    /**
     * Authenticate user and log them in given a loginRequest
     */
    public JwtAuthenticationResponse authenticateUser(LoginRequest command) {
        try {
            var authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(command.getEmail(),
                            command.getPassword()));

            if (authentication == null) {
                throw new UserLoginException(command.getEmail());
            }
            this.assertUser(authentication);

            var userDetails = (User) authentication.getPrincipal();
            log.info("Logged in User returned [API]: " + userDetails.getUsername());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            var jwtToken = this.generateToken(userDetails);
            return new JwtAuthenticationResponse(jwtToken,
                    this.jwtTokenProvider.getExpiryDuration());
        } catch (BadCredentialsException e) {
            throw new AuthenticationFailed(command.getEmail());
        }
    }

    /**
     * Creates and persists the refresh token for the user device. If device exists already, we
     * don't care. Unused devices with expired tokens should be cleaned with a cron job. The
     * generated token would be encapsulated within the jwt. Remove the existing refresh token as
     * the old one should not remain valid.
     */
    public void assertUser(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        this.userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User with the id %s not found.", currentUser.getId())));
    }

    /**
     * Checks if the given email already exists in the database repository or not
     *
     * @return true if the email exists else false
     */
    public boolean emailAlreadyExists(String email) {
        return this.userRepository.existsByEmail(email);
    }

    /**
     * Creates a new user from the registration request
     */
    public User createUser(RegistrationRequest registerRequest) {
        User newUser = new User();
        Boolean isNewUserAsAdmin = registerRequest.getRegisterAsAdmin();
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setFirstName(registerRequest.getFirstName());
        newUser.setLastName(registerRequest.getLastName());
        newUser.addRoles(getRolesForNewUser(isNewUserAsAdmin));
        newUser.setActive(true);
        newUser.setIsEmailVerified(false);
        newUser.setUserType(registerRequest.getUserType());
        return newUser;
    }

    /**
     * Performs a quick check to see what roles the new user could be assigned to.
     *
     * @return list of roles for the new user
     */
    private Set<Role> getRolesForNewUser(boolean isToBeMadeAdmin) {
        Set<Role> newUserRoles = new HashSet<>(roleService.findAll());
        if (!isToBeMadeAdmin) {
            newUserRoles.removeIf(Role::isAdminRole);
        }
        log.info("Setting user roles: " + newUserRoles);
        return newUserRoles;
    }

    /**
     * Generates a JWT token for the validated client
     */
    public String generateToken(User userDetails) {
        return this.jwtTokenProvider.generateToken(userDetails);
    }

    @Transactional
    public ViewModelUser confirmUserInvitation(ConfirmInvitation command) {
        InvitationContextHint invitationContextHint = InvitationContextHint
                .fromBase64(command.getHint());

        var user = this.userRepository.findByEmail(invitationContextHint.getReceiver())
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User with the email %s not found.",
                                invitationContextHint.getReceiver())));
        if (command.getInvitationToken() == null) {
            throw new MissingInvitationTokenException();
        }

        this.invitationTokenService
                .assertValidInvitationToken(command.getInvitationToken(), user.getEmail());

        this.invitationService.setInvitationStatus(user.getEmail(), InvitationStatus.CONFIRMED);

        user.markVerificationConfirmed();

        return ViewModeUserResultSetBuilder.buildSingle(user);
    }
}
