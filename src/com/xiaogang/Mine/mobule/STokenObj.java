package com.xiaogang.Mine.mobule;

/**
 * Created by zhanghailong on 2016/3/6.
 *    "id": "2",
 "name": "宁德市玲珑幼儿园",
 "address": "asdf asdf ",
 "tel": "asdf",
 "uid": "118",
 "user_name": "admin1",
 "password": "123456",
 "ys_user_name": " ",
 "ys_user_id": " ",
 "ys_appid": " ",
 "ys_appsecreat": " ",
 "ys_token": " ",
 "token_expires_time": " "
 */
public class STokenObj {

    private String ys_token;
    private String token_expires_time;


    public String getYs_token() {
        return ys_token;
    }

    public void setYs_token(String ys_token) {
        this.ys_token = ys_token;
    }

    public String getToken_expires_time() {
        return token_expires_time;
    }

    public void setToken_expires_time(String token_expires_time) {
        this.token_expires_time = token_expires_time;
    }
}
