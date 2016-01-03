package com.xiaogang.Mine.mobule;

/**
 * Created by Administrator on 2015/12/17.
 */
public class VideosObj {
    private String id;
    private String is_select;

    public String getIs_select() {
        return is_select;
    }

    public void setIs_select(String is_select) {
        this.is_select = is_select;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public VideosObj(String id, String is_select) {
        this.id = id;
        this.is_select = is_select;
    }
}
