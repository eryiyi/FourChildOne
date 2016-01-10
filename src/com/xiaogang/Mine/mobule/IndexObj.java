package com.xiaogang.Mine.mobule;

import java.util.List;

/**
 * Created by Administrator on 2016/1/10.
 */
public class IndexObj {
    private List<SlidePic> ads;
    private List<ProducteObj> hot;
    private List<ProducteObj> news;

    public List<SlidePic> getAds() {
        return ads;
    }

    public void setAds(List<SlidePic> ads) {
        this.ads = ads;
    }

    public List<ProducteObj> getHot() {
        return hot;
    }

    public void setHot(List<ProducteObj> hot) {
        this.hot = hot;
    }

    public List<ProducteObj> getNews() {
        return news;
    }

    public void setNews(List<ProducteObj> news) {
        this.news = news;
    }
}
