package com.example.school_bus.Mvp;

import com.example.school_bus.Entity.TestBus;

public interface MapMvp {
    interface view{
        void testGetBusResult(TestBus testBus);
    }

    interface presenter{
        void upLocation(double latitude, double longitude);
        void testInsertBus(double latitude, double longitude);
        void testGetBus();
    }
}
