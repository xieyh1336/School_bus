package com.example.school_bus.Mvp;

import com.example.school_bus.Entity.BusListData;

public interface WaitingBusListMvp {
    interface view{
        void getAllWaitingBusResult(BusListData busListData);
        void onError(Throwable e);
    }

    interface presenter{
        void getAllWaitingBus();
    }
}
