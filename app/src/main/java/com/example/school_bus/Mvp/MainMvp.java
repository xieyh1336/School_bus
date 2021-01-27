package com.example.school_bus.Mvp;

import com.example.school_bus.Entity.UserData;


public interface MainMvp {
    interface view{
        void tokenLoginResult(UserData userData);
        void onError(Throwable e, String type);
    }

    interface presenter{
        void tokenLogin();
    }
}
