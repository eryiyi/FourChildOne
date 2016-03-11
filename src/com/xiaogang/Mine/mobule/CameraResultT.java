package com.xiaogang.Mine.mobule;

import java.util.List;

/**
 * Created by zhanghailong on 2016/3/6.
 */
public class CameraResultT {
    private CameraPage page;
    private List<CameraObj> data;
    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public CameraPage getPage() {
        return page;
    }

    public void setPage(CameraPage page) {
        this.page = page;
    }

    public List<CameraObj> getData() {
        return data;
    }

    public void setData(List<CameraObj> data) {
        this.data = data;
    }
}
