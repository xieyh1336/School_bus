package com.example.school_bus.Entity;

import com.google.gson.annotations.SerializedName;

public class DriverStateData extends BaseData{


    /**
     * data : {"state":"1","plate":"桂C_B4585","message":"行驶中"}
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
         * state : 1
         * plate : 桂C_B4585
         * message : 行驶中
         */

        private String state;
        private String plate;
        @SerializedName("message")
        private String messageX;

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getPlate() {
            return plate;
        }

        public void setPlate(String plate) {
            this.plate = plate;
        }

        public String getMessageX() {
            return messageX;
        }

        public void setMessageX(String messageX) {
            this.messageX = messageX;
        }
    }
}
