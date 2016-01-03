package com.xiaogang.Mine.mobule;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/31.
 * 热卖商品
 */
public class GoodsHot implements Serializable {
    private String id;
    private String name;
    private String sell_price;
    private String price;
    private int img;

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

    public String getSell_price() {
        return sell_price;
    }

    public void setSell_price(String sell_price) {
        this.sell_price = sell_price;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public GoodsHot(String id, String name, String sell_price, String price, int img) {
        this.id = id;
        this.name = name;
        this.sell_price = sell_price;
        this.price = price;
        this.img = img;
    }
}
