package com.dnevi.healthcare.security;

import com.dnevi.healthcare.domain.model.user.User;
import com.dnevi.healthcare.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<User> dbUser = userRepository.findByEmail(email);
        log.info("Fetched user : " + dbUser + " by " + email);
        return dbUser
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Couldn't find a matching user email in the database for " + email));
    }

    public UserDetails loadUserById(Long id) {
        Optional<User> dbUser = this.userRepository.findById(id);
        log.info("Fetched user : " + dbUser + " by " + id);
        return dbUser
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Couldn't find a matching user id in the database for " + id));
    }
}