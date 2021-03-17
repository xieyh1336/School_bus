package com.example.school_bus.Mvp;

import com.example.school_bus.Entity.AllOrderData;
import com.example.school_bus.Entity.BaseData;
import com.example.school_bus.Entity.MyStateData;

public interface MyOrderMvp {
    interface view{
        void getMyStateResult(MyStateData myStateData);
        void getAllOrderResult(AllOrderData allOrderData);
        void cancelOrderResult(BaseData baseData);
        void onError(Throwable e, String type);
    }

    interface presenter{
        void getMyState();
        void getAllOrder();
        void cancelOrder();
    }
}
