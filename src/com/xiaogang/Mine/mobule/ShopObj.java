package com.xiaogang.Mine.mobule;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/18.
 *    "": "17",
 "": " ",
 "": "6",
 "": "安德斯食材店",
 "": "/Uploads/2016-01-10/5692009509a1c.png",
 "": "12：00-19——00",
 "": "1",
 "": " ",
 "": "15880208517",
 "": "重庆市渝北区金童路300号",
 "": "113.210541",
 "": "25.040393",
 "": "1",
 "": "0",
 "": "2",
 "": "0",
 "": "0",
 "": "1452409075",
 "": "0",
 "": "啊水电费",
 "": "1",
 "": "15880208517"
 */
public class ShopObj implements Serializable{
    private String shop_id;
    private String category_id;
    private String uid;
    private String shop_name;
    private String logo;
    private String business_time;
    private String delivery_type;
    private String mobile;
    private String phone;
    private String address;
    private String lng;
    private String lat;
    private String is_open;
    private String product_numbers;
    private String pay_type;
    private String tuijian_numbers;
    private String call_numbers;
    private String dateline;
    private String type;
    private String content;
    private String school_id;
    private String tel;

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBusiness_time() {
        return business_time;
    }

    public void setBusiness_time(String business_time) {
        this.business_time = business_time;
    }

    public String getDelivery_type() {
        return delivery_type;
    }

    public void setDelivery_type(String delivery_type) {
        this.delivery_type = delivery_type;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getIs_open() {
        return is_open;
    }

    public void setIs_open(String is_open) {
        this.is_open = is_open;
    }

    public String getProduct_numbers() {
        return product_numbers;
    }

    public void setProduct_numbers(String product_numbers) {
        this.product_numbers = product_numbers;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getTuijian_numbers() {
        return tuijian_numbers;
    }

    public void setTuijian_numbers(String tuijian_numbers) {
        this.tuijian_numbers = tuijian_numbers;
    }

    public String getCall_numbers() {
        return call_numbers;
    }

    public void setCall_numbers(String call_numbers) {
        this.call_numbers = call_numbers;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
