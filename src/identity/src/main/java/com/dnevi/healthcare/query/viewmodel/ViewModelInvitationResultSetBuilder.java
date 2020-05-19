package com.dnevi.healthcare.query.viewmodel;


import com.dnevi.healthcare.domain.model.invitation.Invitation;

public class ViewModelInvitationResultSetBuilder {
    private ViewModelInvitationResultSetBuilder() {
    }

    public static ViewModelInvitation buildSingle(Invitation invitation) {
        ViewModelInvitation model = new ViewModelInvitation();
        model.setReceiver(invitation.getReceiver());
        model.setExpiration(invitation.getExpiration());

        return model;
    }
}
