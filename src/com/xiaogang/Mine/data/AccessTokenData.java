package com.xiaogang.Mine.data;

/**
 * Created by Administrator on 2016/1/3.
 */
public class AccessTokenData extends Data {
    private String access_token;
    private String session_secret;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getSession_secret() {
        return session_secret;
    }

    public void setSession_secret(String session_secret) {
        this.session_secret = session_secret;
    }
}
