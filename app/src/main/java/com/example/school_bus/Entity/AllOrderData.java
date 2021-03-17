package com.example.school_bus.Entity;

import java.util.List;

public class AllOrderData extends BaseData {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * bus_id : 1
         * type : 1
         * create_time : 2021-03-15 21:09:21
         * plates : 桂C_E2058
         */

        private String bus_id;
        private String type;
        private String create_time;
        private String plates;

        public String getBus_id() {
            return bus_id;
        }

        public void setBus_id(String bus_id) {
            this.bus_id = bus_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getPlates() {
            return plates;
        }

        public void setPlates(String plates) {
            this.plates = plates;
        }
    }
}
