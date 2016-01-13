package com.xiaogang.Mine.mobule;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/25.
 * "id": null,
 "user_id": null,
 "accept_name": null,
 "zip": null,
 "telephone": null,
 "country": null,
 "province": null,
 "city": null,
 "area": null,
 "address": null,
 "mobile": null,
 "is_default_address": null,
 "province_name": null,
 "city_name": null,
 "area_name": null
 */
public class ShoppingAddress implements Serializable{
    private String id;
    private String user_id;
    private String accept_name;
    private String zip;
    private String telephone;
    private String country;
    private String province;
    private String city;
    private String area;
    private String address;
    private String mobile;
    private String is_default_address;
    private String province_name;
    private String city_name;
    private String area_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAccept_name() {
        return accept_name;
    }

    public void setAccept_name(String accept_name) {
        this.accept_name = accept_name;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIs_default_address() {
        return is_default_address;
    }

    public void setIs_default_address(String is_default_address) {
        this.is_default_address = is_default_address;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }
}
