package com.xiaogang.Mine.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
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
 * Created by Administrator on 2016/1/1.
 */
public class MineUpdatePwrActivity extends BaseActivity implements View.OnClickListener {
    private EditText oldpwr;
    private EditText newpwr;
    private EditText surepwr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_update_pwr_activity);

        oldpwr = (EditText) this.findViewById(R.id.oldpwr);
        newpwr = (EditText) this.findViewById(R.id.newpwr);
        surepwr = (EditText) this.findViewById(R.id.surepwr);
        this.findViewById(R.id.btnSure).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSure:
                //确定按钮
                if(StringUtil.isNullOrEmpty(oldpwr.getText().toString())){
                    showMsg(MineUpdatePwrActivity.this, "请输入旧密码");
                    return;
                }
                if(StringUtil.isNullOrEmpty(newpwr.getText().toString())){
                    showMsg(MineUpdatePwrActivity.this, "请输入新密码");
                    return;
                }
                if(StringUtil.isNullOrEmpty(surepwr.getText().toString())){
                    showMsg(MineUpdatePwrActivity.this, "请输入确认密码");
                    return;
                }
                if (!newpwr.getText().toString().equals(surepwr.getText().toString())){
                    showMsg(MineUpdatePwrActivity.this, "两次输入密码不一致");
                    return;
                }
                progressDialog = new CustomProgressDialog(MineUpdatePwrActivity.this , "正在加载中", R.anim.frame_paopao);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                reset();
                break;
        }
    }

    public void back(View view){finish();}

    void reset(){
        StringRequest request = new StringRequest(
                Request.Method.GET,
                InternetURL.UPDATE_PWR_URL +"?access_token="+ getGson().fromJson(getSp().getString("access_token", ""), String.class)+"&old=" +oldpwr.getText().toString() +"&new="+newpwr.getText().toString() ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200) {
                                    Toast.makeText(MineUpdatePwrActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                   finish();
                                    //huanxin
//                                    register();
                                }else {
                                    Toast.makeText(MineUpdatePwrActivity.this, jo.getString("msg") , Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(MineUpdatePwrActivity.this, R.string.get_cart_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MineUpdatePwrActivity.this, R.string.get_cart_error, Toast.LENGTH_SHORT).show();
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
