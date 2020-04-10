package com.dnevi.healthcare.query.viewmodel;

import com.dnevi.healthcare.domain.model.user.User;

public class ViewModeUserResultSetBuilder {
    private ViewModeUserResultSetBuilder() {

    }

    public static ViewModelUser buildSingle(User user) {
        ViewModelUser viewModelUser = new ViewModelUser();
        viewModelUser.setId(user.getId());
        viewModelUser.setFirstName(user.getFirstName());
        viewModelUser.setLastName(user.getLastName());
        viewModelUser.setEmail(user.getEmail());
        viewModelUser.setActive(user.getActive());
        viewModelUser.setEmailVerified(user.getIsEmailVerified());
        viewModelUser.setUserType(user.getUserType());

        return viewModelUser;
    }
}
