package com.xiaogang.Mine.mobule;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/11.
 */
public class FavourObj  implements Serializable{
    private String name ;
    private String cover;
    private String time;
    private String uid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
