package com.example.school_bus.Entity;

public class UserData extends BaseData {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * username : test2
         * password : e10adc3949ba59abbe56e057f20f883e
         * phone : 1
         * time_out : 2020-11-20 11:56:09
         * token : dc17f670dc76edad9bd3513e772374962ed4af3a
         */

        private String username;
        private String password;
        private String is_student;
        private String phone;
        private String time_out;
        private String token;
        private String head;
        private String state;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getIs_student() {
            return is_student;
        }

        public void setIs_student(String is_student) {
            this.is_student = is_student;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getTime_out() {
            return time_out;
        }

        public void setTime_out(String time_out) {
            this.time_out = time_out;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }
}
