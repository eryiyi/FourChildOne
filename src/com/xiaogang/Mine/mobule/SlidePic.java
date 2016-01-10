package com.xiaogang.Mine.mobule;

/**
 * Created by Administrator on 2015/7/31.
 * 三张图片
 * "id": "5",
 "community_id": "0",
 "pic": "http://yey.xqb668.com//Uploads/2016-01-08/568f676f2fe92.png",
 "href_url": "http://www.baidu.com",
 "title": "百度首页"
 */
public class SlidePic {

    private String id;
    private String community_id;
    private String title;
    private String href_url;
    private String pic;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(String community_id) {
        this.community_id = community_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHref_url() {
        return href_url;
    }

    public void setHref_url(String href_url) {
        this.href_url = href_url;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
