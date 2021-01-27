package com.example.school_bus.Entity;

import java.util.List;

public class NewsData extends BaseData{

    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * path : https://dy.163.com/article/FNC0M1JJ0514R9P4.html
         * image : http://cms-bucket.ws.126.net/2020/0925/4b323c83j00qh6xiq00w9c0009c0070c.jpg?imageView&thumbnail=140y88&quality=85
         * title : 男子偷情欲逃走被拦跳窗身亡 拦阻者获刑十年半上诉
         * passtime : 2020-09-25 10:00:43
         */

        private String path;
        private String image;
        private String title;
        private String passtime;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPasstime() {
            return passtime;
        }

        public void setPasstime(String passtime) {
            this.passtime = passtime;
        }
    }
}
