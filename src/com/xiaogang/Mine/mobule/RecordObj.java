package com.xiaogang.Mine.mobule;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/1/11.
 *  "id": "284",
 "": "111",
 "": "1",
 "dept": "",
 "url": "/Public/index/image/default_cover.png",
 "type": "0",
 "publisher": "merlin的爸爸",
 "publisher_cover": "/Public/index/image/default_cover.png",
 "publish_uid": "111",
 "is_share": "0",
 "school_id": " ",
 "class_id": " ",
 "pt": "0",
 "dateline": "1452174017",
 "user_type": "0",
 "testtest": "/Public/index/image/default_cover.png",
 "fid": "111",
 "time": "2016-01-07 21:40:17",
 "is_favoured": "0",
 "comments": [],
 "favours": {
 "count": "0",
 "list": []
 }
 */
public class RecordObj implements Serializable{
    private String id;
    private String fid;
    private String uid;
    private String use_type;
    private String dept;
    private String type;
    private String publisher;
    private String publisher_cover;
    private String publish_uid;
    private String is_share;
    private String school_id;
    private String class_id;
    private String pt;
    private String dateline;
    private String user_type;
    private String time;
    private String url;
    private String is_favoured;
    private String is_select;

    private Favours favours;
    private List<CommentObj> comments;


    public String getIs_select() {
        return is_select;
    }

    public void setIs_select(String is_select) {
        this.is_select = is_select;
    }



    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUse_type() {
        return use_type;
    }

    public void setUse_type(String use_type) {
        this.use_type = use_type;
    }

    public String getPublish_uid() {
        return publish_uid;
    }

    public void setPublish_uid(String publish_uid) {
        this.publish_uid = publish_uid;
    }

    public String getIs_share() {
        return is_share;
    }

    public void setIs_share(String is_share) {
        this.is_share = is_share;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getPt() {
        return pt;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public Favours getFavours() {
        return favours;
    }

    public void setFavours(Favours favours) {
        this.favours = favours;
    }

    public List<CommentObj> getComments() {
        return comments;
    }

    public void setComments(List<CommentObj> comments) {
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublisher_cover() {
        return publisher_cover;
    }

    public void setPublisher_cover(String publisher_cover) {
        this.publisher_cover = publisher_cover;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIs_favoured() {
        return is_favoured;
    }

    public void setIs_favoured(String is_favoured) {
        this.is_favoured = is_favoured;
    }
}
