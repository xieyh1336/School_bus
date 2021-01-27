package com.example.school_bus.Mvp;

import com.example.school_bus.Entity.UserData;


public interface RegisterMvp {
    interface view{
        void registerResult(UserData userData);
        void onError(Throwable e);
    }

    interface presenter{
        void register(String username, String password, String phone);
    }
}
