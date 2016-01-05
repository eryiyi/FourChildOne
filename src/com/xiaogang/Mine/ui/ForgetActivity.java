package com.xiaogang.Mine.ui;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.base.ActivityTack;
import com.xiaogang.Mine.base.BaseActivity;
import com.xiaogang.Mine.base.InternetURL;
import com.xiaogang.Mine.util.StringUtil;
import com.xiaogang.Mine.widget.CustomProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/20.
 */
public class ForgetActivity extends BaseActivity implements View.OnClickListener {
    private EditText mobile;
    private EditText password;
    private EditText card;
    private Button btnCard;
    Resources res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_activity);
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
                    showMsg(ForgetActivity.this, "请输入手机号");
                    return;
                }
                if(StringUtil.isNullOrEmpty(password.getText().toString())){
                    showMsg(ForgetActivity.this, "请输入密码");
                    return;
                }
                if(StringUtil.isNullOrEmpty(card.getText().toString())){
                    showMsg(ForgetActivity.this, "请输入验证码");
                    return;
                }
                progressDialog = new CustomProgressDialog(ForgetActivity.this , "正在加载中", R.anim.frame_paopao);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                reset();
                break;
            case R.id.btnCard:
                if(StringUtil.isNullOrEmpty(mobile.getText().toString())){
                    showMsg(ForgetActivity.this, "请输入手机号");
                    return;
                }
                btnCard.setClickable(false);//不可点击
                MyTimer myTimer = new MyTimer(60000,1000);
                myTimer.start();

                progressDialog = new CustomProgressDialog(ForgetActivity.this , "正在加载中", R.anim.frame_paopao);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(true);
                progressDialog.show();

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
                                    Toast.makeText(ForgetActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(ForgetActivity.this, jo.getString("msg") , Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(ForgetActivity.this, R.string.get_cart_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ForgetActivity.this, R.string.get_cart_error, Toast.LENGTH_SHORT).show();
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

    void reset(){
        StringRequest request = new StringRequest(
                Request.Method.GET,
                InternetURL.FIND_PWR_URL +"?mobile="+mobile.getText().toString() +"&password="+password.getText().toString() +"&code="+card.getText().toString() ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200) {
                                    Toast.makeText(ForgetActivity.this, jo.getString("msg") , Toast.LENGTH_SHORT).show();
                                    ActivityTack.getInstanse().popUntilActivity(LoginActivity.class);
                                    //huanxin
//                                    register();
                                }else {
                                    Toast.makeText(ForgetActivity.this, jo.getString("msg") , Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(ForgetActivity.this, R.string.get_cart_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ForgetActivity.this, R.string.get_cart_error, Toast.LENGTH_SHORT).show();
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

}
