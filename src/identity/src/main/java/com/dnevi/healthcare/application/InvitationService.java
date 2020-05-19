package com.dnevi.healthcare.application;

import com.dnevi.healthcare.domain.exception.InvitationNotFoundException;
import com.dnevi.healthcare.domain.model.invitation.Invitation;
import com.dnevi.healthcare.domain.model.invitation.InvitationStatus;
import com.dnevi.healthcare.domain.repository.InvitationRepository;
import com.dnevi.healthcare.query.viewmodel.ViewModelInvitation;
import com.dnevi.healthcare.query.viewmodel.ViewModelInvitationResultSetBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class InvitationService {

    private final InvitationRepository invitationRepository;

    @Autowired
    public InvitationService(
            InvitationRepository invitationRepository) {
        this.invitationRepository = invitationRepository;
    }

    @Transactional
    public ViewModelInvitation createInvitation(String receiver) {
        Optional<Invitation> optionalInvitation = invitationRepository
                .findOneByReceiver(receiver);

        if (optionalInvitation.isPresent()) {
            Invitation invitation = optionalInvitation.get();
            if (invitation.isExpired()) {
                invitation = this.refreshInvitation(invitation);
            }
            return ViewModelInvitationResultSetBuilder.buildSingle(invitation);
        }

        var invitation = new Invitation(receiver);

        invitationRepository.save(invitation);

        return ViewModelInvitationResultSetBuilder.buildSingle(invitation);
    }

    @Transactional
    public void setInvitationStatus(String receiver, InvitationStatus status) {
        Invitation invitation = invitationRepository.findOneByReceiver(receiver)
                .orElseThrow(() -> new InvitationNotFoundException(receiver));

        invitation.setStatus(status);

        invitationRepository.save(invitation);
    }

    private Invitation refreshInvitation(Invitation invitation) {
        Invitation newInvitation = new Invitation(invitation.getReceiver());

        invitationRepository.delete(invitation);

        return invitationRepository.save(newInvitation);
    }
}
