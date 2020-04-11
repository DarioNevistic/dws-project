package com.dnevi.healthcare.controllers;

import com.dnevi.healthcare.application.AuthService;
import com.dnevi.healthcare.application.InvitationEmailService;
import com.dnevi.healthcare.domain.command.ConfirmInvitation;
import com.dnevi.healthcare.domain.command.LoginRequest;
import com.dnevi.healthcare.domain.command.RegistrationRequest;
import com.dnevi.healthcare.domain.command.SendUserInvitation;
import com.dnevi.healthcare.query.viewmodel.JwtAuthenticationResponse;
import com.dnevi.healthcare.query.viewmodel.ViewModelUser;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final InvitationEmailService invitationEmailService;

    @Autowired
    public AuthController(AuthService authService,
            InvitationEmailService invitationEmailService) {
        this.authService = authService;
        this.invitationEmailService = invitationEmailService;
    }

    /**
     * Entry point for the user log in. Return the jwt auth token, and the refresh token.
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Returned in case of successfully logged in user",
                    response = ViewModelUser.class),
            @ApiResponse(code = 403, message = "Returned in case of authorization error")})
    @PostMapping("/login")
    @ApiOperation(value = "Logs the user in to the system and return the auth tokens")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(
            @ApiParam(value = "The LoginRequest payload")
            @Valid @RequestBody LoginRequest loginRequest) {
        var jwtAuthenticationResponse = this.authService.authenticateUser(loginRequest);
        var responseHeaders = new HttpHeaders();
        responseHeaders
                .add("Authorization", "Bearer " + jwtAuthenticationResponse.getAccessToken());

        return new ResponseEntity<>(jwtAuthenticationResponse, responseHeaders, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Returned in case of successfully registered user",
                    response = ViewModelUser.class),
            @ApiResponse(code = 403, message = "Returned in case of authorization error")})
    @ApiOperation(value = "Register user")
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {
            MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE})
    public ResponseEntity<ViewModelUser> registerIdentity(
            @ApiParam(value = "The RegistrationRequest payload")
            @Valid @RequestBody RegistrationRequest command) {
        var viewModelUser = this.authService.registerUser(command);

        var userInvitation = new SendUserInvitation(viewModelUser.getEmail());
        this.invitationEmailService.inviteUser(userInvitation);

        return new ResponseEntity<>(viewModelUser, HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(code = 201,
                    message = "Returned in case of successfully confirmed invitation",
                    response = ViewModelUser.class)})
    @ApiOperation(value = "Confirm user invitation")
    @GetMapping(value = "/confirm-invitation")
    public ResponseEntity<ViewModelUser> confirmInvitation(
            @RequestParam(value = "token") String invitationToken,
            @RequestParam(value = "context") String hint) {
        var command = new ConfirmInvitation();
        command.setInvitationToken(invitationToken);
        command.setHint(hint);

        var viewModelUser = this.authService.confirmUserInvitation(command);

        return new ResponseEntity<>(viewModelUser, HttpStatus.OK);
    }
}
