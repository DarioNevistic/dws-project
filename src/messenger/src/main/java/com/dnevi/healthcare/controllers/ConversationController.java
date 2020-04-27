package com.dnevi.healthcare.controllers;

import com.dnevi.healthcare.application.MessagingService;
import com.dnevi.healthcare.domain.command.CreateConversation;
import com.dnevi.healthcare.query.viewmodel.ViewModelConversation;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/conversations")
public class ConversationController {

    @Autowired
    private MessagingService messagingService;

    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Returned in case of successfully created conversation",
                    response = ViewModelConversation.class),
            @ApiResponse(code = 403, message = "Returned in case of authorization error")})
    @ApiOperation(value = "Creates conversation to be able to exchange messages")
    public ResponseEntity<ViewModelConversation> createConversation(@Valid @RequestBody
            CreateConversation createConversation) {
        var conversationViewModel = this.messagingService.upsertConversation(createConversation);

        return new ResponseEntity<>(conversationViewModel, HttpStatus.CREATED);
    }
}
