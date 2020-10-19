package com.example.school_bus.Entity;

import java.util.List;

public class PicturesData {

    /**
     * code : 200
     * message : 成功!
     * result : [{"id":674,"time":"2020-02-16 04:00:00","img":"https://img.lijinshan.site/images/70def23833844b0fa0e6e74041421e37"},{"id":675,"time":"2020-02-16 04:00:00","img":"https://img.lijinshan.site/images/999cf7f9728b45deacc0740de53aaff1"},{"id":676,"time":"2020-02-16 04:00:00","img":"https://img.lijinshan.site/images/300f3da4f8644deb8ae453bc63bdff7c"},{"id":677,"time":"2020-02-16 04:00:00","img":"https://img.lijinshan.site/images/a72c784f6e6048f48916e13e35bbffe0"},{"id":678,"time":"2020-02-16 04:00:00","img":"https://img.lijinshan.site/images/b98509f26b1e49679b8b89012b3a576b"},{"id":679,"time":"2020-02-16 04:00:00","img":"https://img.lijinshan.site/images/3fa310fb9ada4b68a65254aa07c4bb9c"},{"id":680,"time":"2020-02-16 04:00:00","img":"https://img.lijinshan.site/images/65a4da36c01b4835b2327c47523d2a2c"},{"id":681,"time":"2020-02-16 04:00:00","img":"https://img.lijinshan.site/images/c9e4d064cfe340fea3f41e2ef5034596"}]
     */

    private int code;
    private String message;
    private List<ResultBean> result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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
