package com.dnevi.healthcare.integration;

import com.dnevi.healthcare.MessengerApplication;
import com.dnevi.healthcare.application.AuthService;
import com.dnevi.healthcare.application.MessagingService;
import com.dnevi.healthcare.domain.command.CreateConversation;
import com.dnevi.healthcare.domain.command.RegistrationRequest;
import com.dnevi.healthcare.domain.command.SendInstantMessage;
import com.dnevi.healthcare.domain.model.user.UserType;
import com.dnevi.healthcare.domain.repository.ConversationRepository;
import com.dnevi.healthcare.domain.repository.UserRepository;
import com.dnevi.healthcare.query.viewmodel.ViewModelConversation;
import com.dnevi.healthcare.query.viewmodel.ViewModelUser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = MessengerApplication.class)
public class ConversationTest {

    @Autowired
    private AuthService authService;
    @Autowired
    private MessagingService messagingService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConversationRepository conversationRepository;

    @After
    public void tearDown() {
        this.conversationRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Test
    public void shouldCreateConversation() {
        var u1 = this.registerUser1();
        var u2 = this.registerUser2();
        var conversation = this.createConversation(u1.getEmail(), u2.getEmail());
        Assert.assertEquals(u1.getEmail(), conversation.getCreator());
        Assert.assertEquals("title", conversation.getTitle());

        var message = new SendInstantMessage();
        message.setFrom(this.userRepository.findByEmail(u1.getEmail()).get());
        message.setTo(u2.getEmail());
        message.setConversationId(conversation.getId());
        message.setMessage("Example message");

        this.messagingService.sendMessage(message);
        this.messagingService.sendMessage(message);
        this.messagingService.sendMessage(message);

        Pageable pageable = PageRequest.of(0, 10);
        var result = this.messagingService
                .fetchConversationMessages(conversation.getId(), pageable);
        Assert.assertEquals(3, result.getData().size());
    }

    private ViewModelConversation createConversation(String creator, String participant) {
        var command = new CreateConversation();
        command.setTitle("title");
        command.setCreator(creator);
        command.setParticipant(participant);
        return this.messagingService.upsertConversation(command);
    }

    private ViewModelUser registerUser1() {
        var reg1 = new RegistrationRequest();
        reg1.setEmail("test@test.com");
        reg1.setFirstName("test");
        reg1.setLastName("last");
        reg1.setPassword("pass");
        reg1.setRegisterAsAdmin(true);
        reg1.setUserType(UserType.DOCTOR);

        return this.authService.registerUser(reg1);
    }

    private ViewModelUser registerUser2() {
        var reg2 = new RegistrationRequest();
        reg2.setEmail("test2@test.com");
        reg2.setFirstName("test2");
        reg2.setLastName("last2");
        reg2.setPassword("pass");
        reg2.setRegisterAsAdmin(true);
        reg2.setUserType(UserType.PATIENT);
        return this.authService.registerUser(reg2);
    }
}
