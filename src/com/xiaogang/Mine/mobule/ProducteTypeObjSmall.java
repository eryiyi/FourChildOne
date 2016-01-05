package com.xiaogang.Mine.mobule;

import java.util.List;

/**
 * Created by Administrator on 2016/1/4.
 */
public class ProducteTypeObjSmall {
    private String type_id;
    private String type_name;
    private List<ProducteObj> productes;

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

    public List<ProducteObj> getProductes() {
        return productes;
    }

    public void setProductes(List<ProducteObj> productes) {
        this.productes = productes;
    }
}
