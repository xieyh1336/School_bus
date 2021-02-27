package com.example.school_bus.Mvp;

import com.example.school_bus.Entity.UserData;

import okhttp3.MultipartBody;

public interface MapSideMvp {
    interface view{
        void upHead(UserData userData);
        void onError(Throwable e);
    }

    interface presenter{
        void upHead(MultipartBody.Part body);
    }
}
