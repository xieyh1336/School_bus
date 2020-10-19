package com.example.school_bus.Mvp;

public interface LoginAMvp {
    interface view{
        void onComplete(String type);
        void onError(Throwable e, String type);
    }

    interface presenter{
    }
}
