package com.dnevi.healthcare.application;

import com.dnevi.healthcare.domain.command.CreateConversation;
import com.dnevi.healthcare.domain.command.SendInstantMessage;
import com.dnevi.healthcare.domain.exception.CannotCreateConversationException;
import com.dnevi.healthcare.domain.exception.ConversationNotFoundException;
import com.dnevi.healthcare.domain.model.conversation.Conversation;
import com.dnevi.healthcare.domain.model.conversation.InstantMessage;
import com.dnevi.healthcare.domain.model.conversation.MessageType;
import com.dnevi.healthcare.domain.model.conversation.Participant;
import com.dnevi.healthcare.domain.model.user.UserType;
import com.dnevi.healthcare.domain.repository.ActiveWebSocketUserRepository;
import com.dnevi.healthcare.domain.repository.ConversationRepository;
import com.dnevi.healthcare.domain.repository.InstantMessageRepository;
import com.dnevi.healthcare.query.viewmodel.ViewModelConversation;
import com.dnevi.healthcare.query.viewmodel.ViewModelConversationResultSetBuilder;
import com.dnevi.healthcare.query.viewmodel.ViewModelMessage;
import com.dnevi.healthcare.query.viewmodel.ViewModelMessageResultSetBuilder;
import com.nsoft.chiwava.spring.pagination.results.ResultSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class MessagingService {
    private final SimpMessageSendingOperations messagingTemplate;
    private final ActiveWebSocketUserRepository activeWsUserRepository;
    private final ConversationRepository conversationRepository;
    private final InstantMessageRepository instantMessageRepository;
    private final AuthService authService;

    @Autowired
    public MessagingService(
            SimpMessageSendingOperations messagingTemplate,
            ActiveWebSocketUserRepository activeWsUserRepository,
            ConversationRepository conversationRepository,
            InstantMessageRepository instantMessageRepository,
            AuthService authService) {
        this.messagingTemplate = messagingTemplate;
        this.activeWsUserRepository = activeWsUserRepository;
        this.conversationRepository = conversationRepository;
        this.instantMessageRepository = instantMessageRepository;
        this.authService = authService;
    }

    @Transactional
    public ViewModelConversation upsertConversation(CreateConversation command) {
        var creatorUser = this.authService.getUserByEmail(command.getCreator());
        var participantUser = this.authService.getUserByEmail(command.getParticipant());

        if (creatorUser.getUserType() == UserType.PATIENT) {
            throw CannotCreateConversationException.notAuthorized(creatorUser.getUserType());
        }

        var conversation = new Conversation(command.getTitle(), creatorUser);
        conversation.addParticipant(new Participant(creatorUser));
        conversation.addParticipant(new Participant(participantUser));

        return ViewModelConversationResultSetBuilder
                .buildSingle(this.conversationRepository.save(conversation));
    }

    @Transactional
    public void sendMessage(SendInstantMessage command) {
        var conversation = this.conversationRepository
                .findByIdAndUserId(command.getConversationId(), command.getFrom().getId())
                .orElseThrow(() -> new ConversationNotFoundException(command.getConversationId()));

        var message = new InstantMessage(command.getFrom(), command.getMessage(),
                MessageType.TEXT);
        conversation.addMessage(message);
        this.conversationRepository.save(conversation);

        this.handleMessage(command.getTo(), message);
    }

    @Transactional
    public ResultSet<ViewModelMessage> fetchConversationMessages(Long conversationId,
            Pageable pageable) {
        var result = this.instantMessageRepository
                .fetchMessagesByConversationId(conversationId, pageable);

        return ViewModelMessageResultSetBuilder.build(result);
    }

    private void handleMessage(String recipientEmail, InstantMessage instantMessage) {
        var activeWsUser = this.activeWsUserRepository.findByUsername(recipientEmail);
        if (activeWsUser.isPresent()) {
            var viewModelMessage = ViewModelMessageResultSetBuilder.buildOne(instantMessage);
            this.messagingTemplate
                    .convertAndSendToUser(recipientEmail, "/queue/private/messages",
                            viewModelMessage);
        } else {
            // TODO send notification to user
            log.info("User {} is offline, sending notification...", recipientEmail);
        }
    }
}
