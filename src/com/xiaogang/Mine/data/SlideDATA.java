package com.xiaogang.Mine.data;


import com.xiaogang.Mine.mobule.SlidePic;

import java.util.List;

/**
 * 幻灯片导航栏三张图片
 */
public class SlideDATA extends Data {
    private List<SlidePic> data;

    public List<SlidePic> getData() {
        return data;
    }

    public void setData(List<SlidePic> data) {
        this.data = data;
    }
}
