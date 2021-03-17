package com.example.school_bus.Mvp;

import com.example.school_bus.Entity.BaseData;
import com.example.school_bus.Entity.BusListData;

public interface OrderMvp {
    interface view{
        void getAllWaitingBusResult(BusListData busListData);
        void orderBusResult(BaseData baseData);
        void onError(Throwable e, String type);
    }

    interface presenter{
        void getAllWaitingBus();
        void orderBus(String id);
    }
}
