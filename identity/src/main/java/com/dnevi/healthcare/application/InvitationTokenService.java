package com.dnevi.healthcare.application;

import com.dnevi.healthcare.domain.exception.InvalidInvitationTokenException;
import com.dnevi.healthcare.domain.exception.InvitationExpiredException;
import com.dnevi.healthcare.domain.exception.InvitationNotFoundException;
import com.dnevi.healthcare.domain.exception.InvitationTokenNotFoundException;
import com.dnevi.healthcare.domain.model.invitation.Invitation;
import com.dnevi.healthcare.domain.model.invitation.InvitationToken;
import com.dnevi.healthcare.domain.repository.InvitationRepository;
import com.dnevi.healthcare.domain.repository.InvitationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Service
public class InvitationTokenService {
    private final InvitationRepository invitationRepository;
    private final InvitationTokenRepository invitationTokenRepository;

    @Autowired
    public InvitationTokenService(
            InvitationRepository invitationRepository,
            InvitationTokenRepository invitationTokenRepository) {
        this.invitationRepository = invitationRepository;
        this.invitationTokenRepository = invitationTokenRepository;
    }

    @Transactional
    public String createInvitationToken(String receiver) {
        var invitation = this.invitationRepository.findOneByReceiver(receiver)
                .orElseThrow(() -> new InvitationNotFoundException(receiver));

        return this.createInvitationToken(invitation);
    }

    /**
     * Not intended for public use
     */
    @Transactional
    public String createInvitationToken(Invitation invitation) {
        Optional<InvitationToken> optionalInvitationToken = this.invitationTokenRepository
                .findOneBy(invitation.getReceiver());

        if (optionalInvitationToken.isPresent()) {
            InvitationToken invitationToken = optionalInvitationToken.get();
            if (invitationToken.isExpired()) {
                invitationToken = this.refreshInvitationToken(invitationToken);
            }
            return invitationToken.getEncodedToken();
        }
        InvitationToken invitationToken;

        try {
            invitationToken = invitation.createToken();
        } catch (InvitationExpiredException e) {
            invitationRepository.delete(invitation);
            throw e;
        }

        invitationTokenRepository.save(invitationToken);

        return invitationToken.getEncodedToken();
    }

    private InvitationToken refreshInvitationToken(InvitationToken invitationToken) {
        InvitationToken newInvitationToken = invitationToken.getInvitation().createToken();

        invitationTokenRepository.delete(invitationToken);

        return invitationTokenRepository.save(newInvitationToken);
    }

    public void assertValidInvitationToken(String invitationToken,
            @NotBlank(message = "User email cannot be null") String email) {
        this.invitationTokenRepository.findOneByEncodedToken(invitationToken)
                .ifPresentOrElse(token -> {
                    if (token.isExpired()) {
                        throw new InvalidInvitationTokenException(email);
                    }
                }, () -> {
                    throw new InvitationTokenNotFoundException(email);
                });

    }

    @Scheduled(fixedDelay = 3_600_000)
    public void deleteInvalidTokens() {
        invitationTokenRepository.deleteAllExpiredOrConfirmed();
    }
}
