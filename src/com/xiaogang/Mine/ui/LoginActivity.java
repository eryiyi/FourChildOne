package com.xiaogang.Mine.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.easemob.EMCallBack;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.User;
import com.easemob.chatuidemo.utils.CommonUtils;

import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.bean.EZAccessToken;
import com.videogo.util.LogUtil;
import com.xiaogang.Mine.MainActivity;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.UniversityApplication;
import com.xiaogang.Mine.base.BaseActivity;
import com.xiaogang.Mine.base.InternetURL;
import com.xiaogang.Mine.data.AccessTokenData;
import com.xiaogang.Mine.data.EmpData;
import com.xiaogang.Mine.data.STokenObjData;
import com.xiaogang.Mine.mobule.Emp;
import com.xiaogang.Mine.mobule.STokenObj;
import com.xiaogang.Mine.util.HttpUtils;
import com.xiaogang.Mine.util.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/20.
 * 登陆
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText mobile;
    private EditText password;

    private boolean progressShow;
    boolean isMobileNet, isWifiNet;
    ProgressDialog pd = null;

//    UMSocialService mController = UMShareListener.getUMSocialService("com.umeng.login");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        initView();
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("mobile", ""), String.class))){
            mobile.setText(getGson().fromJson(getSp().getString("mobile", ""), String.class));
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("password", ""), String.class))){
            password.setText(getGson().fromJson(getSp().getString("password", ""), String.class));
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("mobile", ""), String.class)) &&!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("password", ""), String.class))){
            loginData();
        }
    }

    @Override
    public void onClick(View v) {
        try {
            isMobileNet = HttpUtils.isMobileDataEnable(getApplicationContext());
            isWifiNet = HttpUtils.isWifiDataEnable(getApplicationContext());
            if (!isMobileNet && !isWifiNet) {
                Toast.makeText(this, "当前网络连接不可用", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (v.getId()){
            case R.id.btn:
                //登陆
                if(StringUtil.isNullOrEmpty(mobile.getText().toString())){
                    showMsg(LoginActivity.this, "请输入账号");
                    return;
                }
                if(StringUtil.isNullOrEmpty(password.getText().toString())){
                    showMsg(LoginActivity.this, "请输入密码");
                    return;
                }
                progressShow = true;
                pd = new ProgressDialog(LoginActivity.this);
                pd.setCanceledOnTouchOutside(false);
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        progressShow = false;
                    }
                });
                pd.setMessage(getString(R.string.Is_landing));
                pd.show();
                loginData();
                break;
            case R.id.forget:
                //忘记密码
                Intent forgetView = new Intent(LoginActivity.this, ForgetActivity.class);
                startActivity(forgetView);
                break;
            case R.id.reg:
                //注册
                Intent reg = new Intent(LoginActivity.this ,SelectSchoolActivity.class);
                startActivity(reg);
                break;
        }
    }

    void initView(){
        this.findViewById(R.id.btn).setOnClickListener(this);
        this.findViewById(R.id.forget).setOnClickListener(this);
        this.findViewById(R.id.reg).setOnClickListener(this);

        mobile = (EditText) this.findViewById(R.id.mobile);
        password = (EditText) this.findViewById(R.id.password);
    }

    Emp emp;
    void loginData(){
        StringRequest request = new StringRequest(
                Request.Method.GET,
                InternetURL.LOGIN_URL+"?user_name=" + mobile.getText().toString()+"&password="+password.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200){
                                    EmpData data = getGson().fromJson(s, EmpData.class);
                                    emp = data.getData();
                                    saveAccount(data.getData());
//                                    Intent main = new Intent(LoginActivity.this, MainActivity.class);
//                                    startActivity(main);
                                    login(emp.getUser_name(), emp.getPassword());
//                                    getAccessToken();
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        getRequestQueue().add(request);
    }

    public void saveAccount(Emp emp) {
        // 登陆成功，保存用户名密码
        save("access_token", emp.getAccess_token());
        save("qq_openid", emp.getQq_openid());
        save("weixin_openid", emp.getWeixin_openid());
        save("birthday", emp.getBirthday());
        save("sex", emp.getSex());
        save("remark", emp.getRemark());
        save("student_name", emp.getStudent_name());
        save("open", emp.getOpen());
        save("create_time", emp.getCreate_time());
        save("is_logined", emp.getIs_logined());
        save("token", emp.getToken());
        save("scode", emp.getScode());
        save("group_id", emp.getGroup_id());
        save("dept", emp.getDept());
        save("m_cover", emp.getM_cover());
        save("f_cover", emp.getF_cover());
        save("school_id", emp.getSchool_id());
        save("class_id", emp.getClass_id());
        save("rule1_name", emp.getRule1_name());
        save("rule2_name", emp.getRule2_name());
        save("age", emp.getAge());
        save("email", emp.getEmail());
        save("user_name", emp.getUser_name());
        save("uid", emp.getUid());
        save("mobile", emp.getMobile());
        save("address", emp.getAddress());
        save("nick_name", emp.getNick_name());
        save("cover", emp.getCover());
        save("reg_time", emp.getReg_time());
        save("hx_id", emp.getHx_id());
        save("lng", emp.getLng());
        save("lat", emp.getLat());
        save("password", password.getText().toString());
        save("password_hx", emp.getPassword());

        //yingshi  login
//        EZOpenSDK.getInstance().openLoginPage();
        yslogin();
    }

    void getAccessToken(){
        StringRequest request = new StringRequest(
                Request.Method.GET,
                InternetURL.GET_TOKEN_URL+"?user_name=" + mobile.getText().toString()+"&password="+password.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200){
                                    AccessTokenData data = getGson().fromJson(s, AccessTokenData.class);
                                    save("access_token", data.getAccess_token());
                                    save("session_secret", data.getSession_secret());
//                                    Intent main = new Intent(LoginActivity.this, MainActivity.class);
//                                    startActivity(main);
                                    login(emp.getHx_id(), "123456");
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        getRequestQueue().add(request);
    }

    //huanxin
    public void login(final String currentUsername ,final String currentPassword) {
        if (!CommonUtils.isNetWorkConnected(this)) {
            Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(currentUsername)) {
            Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(currentPassword)) {
            Toast.makeText(this, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        final long start = System.currentTimeMillis();
        // 调用sdk登陆方法登陆聊天服务器
        EMChatManager.getInstance().login(currentUsername, currentPassword, new EMCallBack() {
            @Override
            public void onSuccess() {
                if (!progressShow) {
                    return;
                }
                // 登陆成功，保存用户名密码
                UniversityApplication.getInstance().setUserName(currentUsername);
                UniversityApplication.getInstance().setPassword(currentPassword);
                try {
                    // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                    // ** manually load all local groups and
                    EMGroupManager.getInstance().loadAllGroups();
                    EMChatManager.getInstance().loadAllConversations();
                    // 处理好友和群组
                    initializeContacts();
                } catch (Exception e) {
                    e.printStackTrace();
                    // 取好友或者群聊失败，不让进入主页面
                    runOnUiThread(new Runnable() {
                        public void run() {
//                            pd.dismiss();
                            DemoHXSDKHelper.getInstance().logout(true,null);
                            Toast.makeText(getApplicationContext(), R.string.login_failure_failed, Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                // 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
                boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(
                        UniversityApplication.currentUserNick.trim());
                if (!updatenick) {
                    Log.e("LoginActivity", "update current user nick fail");
                }
                if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
                    pd.dismiss();
                }
                // 进入主页面
//                Intent intent = new Intent(LoginActivity.this,
//                        com.easemob.chatuidemo.activity.MainActivity.class);
//                startActivity(intent);
                Intent mainView = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(mainView);
            }

            @Override
            public void onProgress(int progress, String status) {
            }
            @Override
            public void onError(final int code, final String message) {
                if (!progressShow) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void initializeContacts() {
        Map<String, User> userlist = new HashMap<String, User>();
        // 添加user"申请与通知"
        User newFriends = new User();
        newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
        String strChat = getResources().getString(
                R.string.Application_and_notify);
        newFriends.setNick(strChat);

        userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
        // 添加"群聊"
        User groupUser = new User();
        String strGroup = getResources().getString(R.string.group_chat);
        groupUser.setUsername(Constant.GROUP_USERNAME);
        groupUser.setNick(strGroup);
        groupUser.setHeader("");
        userlist.put(Constant.GROUP_USERNAME, groupUser);

        // 添加"Robot"
        User robotUser = new User();
        String strRobot = getResources().getString(R.string.robot_chat);
        robotUser.setUsername(Constant.CHAT_ROBOT);
        robotUser.setNick(strRobot);
        robotUser.setHeader("");
        userlist.put(Constant.CHAT_ROBOT, robotUser);

        // 存入内存
        ((DemoHXSDKHelper) HXSDKHelper.getInstance()).setContactList(userlist);
        // 存入db
        UserDao dao = new UserDao(LoginActivity.this);
        List<User> users = new ArrayList<User>(userlist.values());
        dao.saveContactList(users);
    }



//    void getYingShi(){
//        StringRequest request = new StringRequest(
//                Request.Method.POST,
//                InternetURL.GET_YS_TOKEN,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String s) {
//                        if (StringUtil.isJson(s)) {
//                            try {
//                                JSONObject jo = new JSONObject(s);
//                                String code =  jo.getString("code");
//                                if(Integer.parseInt(code) == 200){
//
//                                }
//                                else{
//                                    Toast.makeText(LoginActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("", );
//                params.put("", );
//                params.put("", );
//                params.put("", );
//                params.put("", );
//                params.put("", );
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/x-www-form-urlencoded");
//                return params;
//            }
//        };
//        getRequestQueue().add(request);
//    }

    private static final String TAG = "LoginActivity";
    void yslogin(){
        StringRequest request = new StringRequest(
                Request.Method.GET,
                InternetURL.GET_TOKEN_YS_URL+
                        "?access_token=" + getGson().fromJson(getSp().getString("access_token", ""), String.class),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200){
                                    STokenObjData data = getGson().fromJson(s, STokenObjData.class);
                                    STokenObj sTokenObj = data.getData();
                                    save("ys_token", sTokenObj.getYs_token());
                                    save("token_expires_time", sTokenObj.getToken_expires_time());
                                    if(!StringUtil.isNullOrEmpty(sTokenObj.getYs_token())){
                                        LogUtil.infoLog(TAG, "t:" + sTokenObj.getYs_token().substring(0, 5) + " expire:" + sTokenObj.getToken_expires_time());
//                                        LogUtil.infoLog(TAG, "t:" + sTokenObj.getYs_token() + " expire:" + sTokenObj.getToken_expires_time());
                                    }


                                    //保存token及token超时时间
//                                    EZOpenSDK openSdk = EZOpenSDK.getInstance();
//                                    if(openSdk != null) {
//                                        EZAccessToken token = openSdk.getEZAccessToken();
//                                        //保存token，获取超时时间，在token过期时重新获取
////                                        LogUtil.infoLog(TAG, "t:" + token.getAccessToken().substring(0, 5) + " expire:" + token.getExpire());
//                                        LogUtil.infoLog(TAG, "t:" + token.getAccessToken() + " expire:" + token.getExpire());
//                                    }

                                }
                                else{
                                    Toast.makeText(LoginActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        getRequestQueue().add(request);
    }


//    void Qq(){
//        // 添加微信平台
//        UMWXHandler wxHandler = new UMWXHandler(LoginActivity.this,appId,appSecret);
//        wxHandler.addToSocialSDK();
//
//        mController.doOauthVerify(mContext, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
//            @Override
//            public void onStart(SHARE_MEDIA platform) {
//                Toast.makeText(mContext, "授权开始", Toast.LENGTH_SHORT).show();
//            }
//            @Override
//            public void onError(SocializeException e, SHARE_MEDIA platform) {
//                Toast.makeText(mContext, "授权错误", Toast.LENGTH_SHORT).show();
//            }
//            @Override
//            public void onComplete(Bundle value, SHARE_MEDIA platform) {
//                Toast.makeText(mContext, "授权完成", Toast.LENGTH_SHORT).show();
//                //获取相关授权信息
//                mController.getPlatformInfo(MainActivity.this, SHARE_MEDIA.WEIXIN, new UMDataListener() {
//                    @Override
//                    public void onStart() {
//                        Toast.makeText(MainActivity.this, "获取平台数据开始...", Toast.LENGTH_SHORT).show();
//                    }
//                    @Override
//                    public void onComplete(int status, Map<String, Object> info) {
//                        if(status == 200 && info != null){
//                            StringBuilder sb = new StringBuilder();
//                            Set<String> keys = info.keySet();
//                            for(String key : keys){
//                                sb.append(key+"="+info.get(key).toString()+"\r\n");
//                            }
//                            Log.d("TestData",sb.toString());
//                        }else{
//                            Log.d("TestData","发生错误："+status);
//                        }
//                    }
//                });
//            }
//            @Override
//            public void onCancel(SHARE_MEDIA platform) {
//                Toast.makeText(mContext, "授权取消", Toast.LENGTH_SHORT).show();
//            }
//        } );
//    }
}
