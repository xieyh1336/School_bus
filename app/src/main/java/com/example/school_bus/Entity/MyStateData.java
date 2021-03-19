package com.example.school_bus.Entity;

public class MyStateData extends BaseData {

    /**
     * data : {"state":"0","bus_name":null,"plates":null,"run_time":null}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * state : 0
         * bus_name : null
         * plates : null
         * run_time : null
         */

        private String state;
        private String onBus;
        private String bus_id;
        private String bus_name;
        private String plates;
        private String run_time;
        private String bus_type;

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getOnBus() {
            return onBus;
        }

        public void setOnBus(String onBus) {
            this.onBus = onBus;
        }

        public String getBus_id() {
            return bus_id;
        }

        public void setBus_id(String bus_id) {
            this.bus_id = bus_id;
        }

        public String getBus_name() {
            return bus_name;
        }

        public void setBus_name(String bus_name) {
            this.bus_name = bus_name;
        }

        public String getPlates() {
            return plates;
        }

        public void setPlates(String plates) {
            this.plates = plates;
        }

        public String getRun_time() {
            return run_time;
        }

        public void setRun_time(String run_time) {
            this.run_time = run_time;
        }

        public String getBus_type() {
            return bus_type;
        }

        public void setBus_type(String bus_type) {
            this.bus_type = bus_type;
        }
    }
}
