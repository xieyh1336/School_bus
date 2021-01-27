package com.example.school_bus.Entity;

import java.util.List;

public class PicturesData extends BaseData {

    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * id : 674
         * time : 2020-02-16 04:00:00
         * img : https://img.lijinshan.site/images/70def23833844b0fa0e6e74041421e37
         */

        private int id;
        private String time;
        private String img;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
}
