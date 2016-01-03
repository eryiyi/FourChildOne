package com.xiaogang.Mine.mobule;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2015/8/1.
 *产品分类
 * 大类
 */
public class GoodsTypeBig implements Parcelable {
    private String id;
    private String name;
    private List<GoodsTypeSmall> son;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GoodsTypeSmall> getSon() {
        return son;
    }

    public void setSon(List<GoodsTypeSmall> son) {
        this.son = son;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
