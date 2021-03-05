package com.example.school_bus.Mvp;

public interface MapMvp {
    interface view{

    }

    interface presenter{
        void upLocation(double latitude, double longitude);
    }
}
