package com.xiaogang.Mine.mobule;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/8/1.
 *产品分类
 * 大类
 *  * "": "10",
 "": "家电",
 "": "0",
 "up_id": "0"
 */
public class GoodsTypeBig implements Parcelable {
    private String type_id;
    private String type_name;
    private String shop_id;
    private String up_id;

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getUp_id() {
        return up_id;
    }

    public void setUp_id(String up_id) {
        this.up_id = up_id;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

}
