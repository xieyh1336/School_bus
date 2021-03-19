package com.example.school_bus.Mvp;

import com.example.school_bus.Entity.BaseData;
import com.example.school_bus.Entity.MyStateData;
import com.example.school_bus.Entity.TestBus;
import com.example.school_bus.Entity.UserData;

public interface MapMvp {
    interface view{
        void testGetBusResult(TestBus testBus);
        void getMyStateResult(MyStateData myStateData);
        void upBusResult(BaseData baseData);
        void downBusResult(BaseData baseData);
        void onError(Throwable e, String type);
    }

    interface presenter{
        void upLocation(double latitude, double longitude);
        void getMyState();
        void upBus();
        void downBus();
        void testInsertBus(double latitude, double longitude);
        void testGetBus();
    }
}
