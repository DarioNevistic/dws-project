package com.dnevi.healthcare.application;

import com.dnevi.healthcare.domain.repository.ActiveWebSocketUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessagingService {
    private final ActiveWebSocketUserRepository activeWsUserRepository;

    @Autowired
    public MessagingService(
            ActiveWebSocketUserRepository activeWsUserRepository) {
        this.activeWsUserRepository = activeWsUserRepository;
    }
    // add message entity *--1 user

    // do checks in messaging service
    // (can user send message to specific user?,
    // init conversation only if type=DOCTOR
    // is user online? either send a notification)
}
