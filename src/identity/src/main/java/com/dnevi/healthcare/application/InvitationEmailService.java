package com.dnevi.healthcare.application;

import com.dnevi.healthcare.domain.command.SendUserInvitation;
import com.dnevi.healthcare.domain.exception.InvitationEmailCouldNotBeSentException;
import com.dnevi.healthcare.domain.exception.UserNotFoundException;
import com.dnevi.healthcare.domain.model.invitation.InvitationContextHint;
import com.dnevi.healthcare.domain.model.invitation.InvitationStatus;
import com.dnevi.healthcare.domain.repository.UserRepository;
import com.dnevi.healthcare.query.viewmodel.ViewModelInvitation;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
public class InvitationEmailService {
    private final InvitationTokenService invitationTokenService;
    private final UserRepository userRepository;
    private final InvitationService invitationService;

    @Value(value = "${com.dnevi.hcs.iam.accounts-base-url}")
    private String baseUrl;

    @Value(value = "${com.sendgrid.invite.from}")
    private String inviteFromEmail;

    @Value(value = "${com.sendgrid.invite.subject}")
    private String emailSubject;

    @Value(value = "${com.sendgrid.api.key}")
    private String sendGridApiKey;

    @Value(value = "${im.rest.user-invite-endpoint-url-template}")
    private String userInviteUrlTemplate;

    @Autowired
    public InvitationEmailService(
            InvitationTokenService invitationTokenService,
            UserRepository userRepository,
            InvitationService invitationService) {
        this.invitationTokenService = invitationTokenService;
        this.userRepository = userRepository;
        this.invitationService = invitationService;
    }

    @Transactional
    public void inviteUser(SendUserInvitation command) {
        this.userRepository.findByEmail(command.getReceiver())
                .orElseThrow(() -> new UserNotFoundException(command.getReceiver()));

        ViewModelInvitation invitation = this.invitationService
                .createInvitation(command.getReceiver());

        String invitationToken = invitationTokenService
                .createInvitationToken(command.getReceiver());

        InvitationContextHint hint = new InvitationContextHint(invitation.getReceiver(),
                invitation.getExpiration());

        this.invitationService
                .setInvitationStatus(invitation.getReceiver(), InvitationStatus.SENT);

        this.sendEmailInvitation(invitation, invitationToken, hint);
    }

    private void sendEmailInvitation(ViewModelInvitation invitation, String token,
            InvitationContextHint hint) {
        Mail mail = prepareEmailContents(invitation, token, hint);

        try {
            sendMail(mail);
        } catch (IOException e) {
            throw new InvitationEmailCouldNotBeSentException(hint.getReceiver());
        }
    }

    private void sendMail(Mail mail) throws IOException {
        SendGrid sg = new SendGrid(sendGridApiKey);

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        sg.api(request);
    }


    private Mail prepareEmailContents(ViewModelInvitation invitation, String token,
            InvitationContextHint hint) {
        Email from = new Email(inviteFromEmail);
        Email to = new Email(invitation.getReceiver());

        String body = "Hey there," + '\n'
                + '\n'
                + "You are just registered to the DNevi Healthcare system!" + '\n'
                + '\n'
                + "You must confirm invitation by clicking link below:"
                + '\n' + '\n'
                + String.format(userInviteUrlTemplate, baseUrl, token, hint.toBase64());

        Content content = new Content("text/plain", body);

        return new Mail(from, emailSubject, to, content);
    }
}
