package com.xiaogang.Mine.mobule;

/**
 * Created by Administrator on 2015/8/14.
 */
public class OrderStr {
    private String id;
    private String count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public OrderStr(String id, String count) {
        this.id = id;
        this.count = count;
    }
}
