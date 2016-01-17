package com.xiaogang.Mine.ui;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.UniversityApplication;
import com.xiaogang.Mine.base.ActivityTack;
import com.xiaogang.Mine.base.BaseActivity;
import com.xiaogang.Mine.base.InternetURL;
import com.xiaogang.Mine.data.EmpData;
import com.xiaogang.Mine.mobule.Emp;
import com.xiaogang.Mine.util.StringUtil;
import com.xiaogang.Mine.widget.CustomProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/20.
 */
public class RegActivity extends BaseActivity implements View.OnClickListener {
    private EditText mobile;
    private EditText password;
    private EditText card;
    private Button btnCard;
    Resources res;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_activity);
        res = getResources();
        initView();

    }

    void initView(){
        //
        this.findViewById(R.id.btn).setOnClickListener(this);
        mobile = (EditText) this.findViewById(R.id.mobile);
        password = (EditText) this.findViewById(R.id.password);
        card = (EditText) this.findViewById(R.id.card);
        btnCard = (Button) this.findViewById(R.id.btnCard);
        btnCard.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn:
                if(StringUtil.isNullOrEmpty(mobile.getText().toString())){
                    showMsg(RegActivity.this, "请输入手机号");
                    return;
                }
                if(StringUtil.isNullOrEmpty(password.getText().toString())){
                    showMsg(RegActivity.this, "请输入密码");
                    return;
                }
                if(StringUtil.isNullOrEmpty(card.getText().toString())){
                    showMsg(RegActivity.this, "请输入验证码");
                    return;
                }

                reg();
                break;
            case R.id.btnCard:
                if(StringUtil.isNullOrEmpty(mobile.getText().toString())){
                    showMsg(RegActivity.this, "请输入手机号");
                    return;
                }
                btnCard.setClickable(false);//不可点击
                MyTimer myTimer = new MyTimer(60000,1000);
                myTimer.start();

                getCard();

                break;
        }
    }


    void getCard(){
        StringRequest request = new StringRequest(
                Request.Method.GET,
                InternetURL.REG_CARD_URL+"?mobile=" +mobile.getText().toString()  ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200) {
                                    Toast.makeText(RegActivity.this, "发送验证码成功，请注意查收", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(RegActivity.this, jo.getString("msg") , Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(RegActivity.this, "获得数据失败", Toast.LENGTH_SHORT).show();
                        }
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(RegActivity.this, R.string.get_cart_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", mobile.getText().toString());
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

    class MyTimer extends CountDownTimer {

        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            btnCard.setText(res.getString(R.string.daojishi_three));
            btnCard.setClickable(true);//可点击
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btnCard.setText(res.getString(R.string.daojishi_one) + millisUntilFinished / 1000 + res.getString(R.string.daojishi_two));
        }
    }

    void reg(){
        final String hx_id =  System.currentTimeMillis()+"" +mobile.getText().toString();
        StringRequest request = new StringRequest(
                Request.Method.GET,
                InternetURL.REG_URL
                        +"?user_name="+mobile.getText().toString()
                        +"&password="+password.getText().toString()
                        +"&code="+card.getText().toString()
                ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200) {
                                    Toast.makeText(RegActivity.this, "注册成功" , Toast.LENGTH_SHORT).show();
                                    EmpData data = getGson().fromJson(s, EmpData.class);
                                    saveAccount(data.getData());
                                    //huanxin
//                                    register(hx_id);
                                    ActivityTack.getInstanse().popUntilActivity(LoginActivity.class);
                                }else {
                                    Toast.makeText(RegActivity.this, jo.getString("msg") , Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(RegActivity.this, R.string.get_cart_error, Toast.LENGTH_SHORT).show();
                        }
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(RegActivity.this, R.string.get_cart_error, Toast.LENGTH_SHORT).show();
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
    }

    /**
     * 注册
     *
     */
    public void register(String hx_id) {
        final String username = hx_id;
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, getResources().getString(R.string.User_name_cannot_be_empty), Toast.LENGTH_SHORT).show();
            mobile.requestFocus();
            return;
        }

        if (!TextUtils.isEmpty(username)) {
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage(getResources().getString(R.string.Is_the_registered));
            pd.show();

            new Thread(new Runnable() {
                public void run() {
                    try {
                        // 调用sdk注册方法
                        EMChatManager.getInstance().createAccountOnServer(username, "123456");
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (!RegActivity.this.isFinishing())
                                    pd.dismiss();
                                // 保存用户名
                                UniversityApplication.getInstance().setUserName(username);
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registered_successfully), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    } catch (final EaseMobException e) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (!RegActivity.this.isFinishing())
                                    pd.dismiss();
                                int errorCode=e.getErrorCode();
                                if(errorCode== EMError.NONETWORK_ERROR){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
                                }else if(errorCode == EMError.USER_ALREADY_EXISTS){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
                                }else if(errorCode == EMError.UNAUTHORIZED){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
                                }else if(errorCode == EMError.ILLEGAL_USER_NAME){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name),Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registration_failed) + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }).start();

        }
    }


}
