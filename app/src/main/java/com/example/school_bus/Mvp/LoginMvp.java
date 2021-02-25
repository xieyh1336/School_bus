package com.example.school_bus.Mvp;

import com.example.school_bus.Entity.UserData;


public interface LoginMvp {
    interface view{
        void loginResult(UserData userData);
        void onError(Throwable e);
    }

    interface presenter{
        void login(String username, String password, String phone, int type);

    }
}
