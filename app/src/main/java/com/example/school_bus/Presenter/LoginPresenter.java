package com.example.school_bus.Presenter;

import com.example.school_bus.Mvp.LoginAMvp;

public class LoginPresenter implements LoginAMvp.presenter{

    private LoginAMvp.view view;

    public LoginPresenter(LoginAMvp.view view) {
        this.view = view;
    }

}
