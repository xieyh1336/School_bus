package com.example.school_bus.Entity;

import java.util.List;

public class NewsData{

    /**
     * code : 200
     * message : 成功!
     * result : [{"path":"https://dy.163.com/article/FNC0M1JJ0514R9P4.html","image":"http://cms-bucket.ws.126.net/2020/0925/4b323c83j00qh6xiq00w9c0009c0070c.jpg?imageView&thumbnail=140y88&quality=85","title":"男子偷情欲逃走被拦跳窗身亡 拦阻者获刑十年半上诉","passtime":"2020-09-25 10:00:43"},{"path":"http://money.163.com/photoview/0BGT0025/36669.html","image":"http://cms-bucket.ws.126.net/2020/0925/5b399014j00qh68mm001vc0005f003rc.jpg?imageView&thumbnail=140y88&quality=85","title":"嫦娥四号新成果 揭示月球内部结构","passtime":"2020-09-25 10:00:43"},{"path":"https://news.163.com/20/0925/08/FNC0HHUG0001899O.html","image":"http://cms-bucket.ws.126.net/2020/0925/ef6ec112p00qh6vwm005dc000b4007ec.png?imageView&thumbnail=140y88&quality=85","title":"中国代表在安理会怒斥美方：给世界制造足够多麻烦 ","passtime":"2020-09-25 10:00:43"},{"path":"https://dy.163.com/article/FNB27KT405258F45.html","image":"http://cms-bucket.ws.126.net/2020/0925/21e60a76j00qh6vbc001vc000s600e3c.jpg?imageView&thumbnail=140y88&quality=85","title":"泰国妖艳选美燃爆各国热搜！网友：救命 我被美瞎了","passtime":"2020-09-25 10:00:43"},{"path":"https://dy.163.com/article/FNAV408Q0517B0EF.html","image":"http://dingyue.ws.126.net/2020/0924/2086f47bj00qh64t7000lc000u000k0m.jpg?imageView&thumbnail=140y88&quality=85","title":"被铁链绑住 注射避孕药 她们取悦上百人后挣扎死去","passtime":"2020-09-25 10:00:43"},{"path":"https://dy.163.com/article/FNBUCKVS0514R9OJ.html","image":"http://cms-bucket.ws.126.net/2020/0925/b727ae9cp00qh6v5u00gsc000s600e3c.png?imageView&thumbnail=140y88&quality=85","title":"不做作业？浙江爸爸把娃＂丢＂坟地罚站 可怕一幕发生","passtime":"2020-09-25 10:00:43"},{"path":"https://tech.163.com/20/0925/07/FNBQFF6H00097U7R.html","image":"http://cms-bucket.ws.126.net/2020/0925/18a25197j00qh6s97006ic000s600e3c.jpg?imageView&thumbnail=140y88&quality=85","title":"对Tiktok禁令，法官给美政府两个选择","passtime":"2020-09-25 10:00:43"}]
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
