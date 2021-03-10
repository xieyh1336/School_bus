package com.example.school_bus.Entity;

import java.util.List;

public class BusListData extends BaseData{

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * name : 雁山-王城
         * latitude : 0
         * longitude : 0
         * total : 60
         * num_of_people : 0
         * num_of_order : 0
         * is_running : 0
         * type : 1
         * run_time : null
         */

        private String id;
        private String name;
        private String latitude;
        private String longitude;
        private String total;
        private String num_of_people;
        private String num_of_order;
        private String is_running;
        private String type;
        private String run_time;
        private String plates;
        private String driver;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getNum_of_people() {
            return num_of_people;
        }

        public void setNum_of_people(String num_of_people) {
            this.num_of_people = num_of_people;
        }

        public String getNum_of_order() {
            return num_of_order;
        }

        public void setNum_of_order(String num_of_order) {
            this.num_of_order = num_of_order;
        }

        public String getIs_running() {
            return is_running;
        }

        public void setIs_running(String is_running) {
            this.is_running = is_running;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getRun_time() {
            return run_time;
        }

        public void setRun_time(String run_time) {
            this.run_time = run_time;
        }

        public String getPlates() {
            return plates;
        }

        public void setPlates(String plates) {
            this.plates = plates;
        }

        public String getDriver() {
            return driver;
        }

        public void setDriver(String driver) {
            this.driver = driver;
        }
    }
}
