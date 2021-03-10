package com.example.school_bus.Mvp;

import com.example.school_bus.Entity.BaseData;
import com.example.school_bus.Entity.DriverStateData;

public interface DriverMvp {
    interface view{
        void runBusResult(BaseData baseData);
        void getDriverStateResult(DriverStateData driverStateData);
        void arriveBusResult(BaseData baseData);
        void onError(Throwable e, String type);
    }

    interface presenter{
        void runBus(String id);
        void getDriverState();
        void arriveBus();
    }
}
