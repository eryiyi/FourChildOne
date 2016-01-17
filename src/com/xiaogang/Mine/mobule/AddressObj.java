package com.xiaogang.Mine.mobule;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/15.
 */
public class AddressObj implements Serializable{
    private String id;
    private String address;
    private String contact_name;
    private String contact_mobile;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_mobile() {
        return contact_mobile;
    }

    public void setContact_mobile(String contact_mobile) {
        this.contact_mobile = contact_mobile;
    }
}
