package com.xiaogang.Mine.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.base.BaseActivity;
import com.xiaogang.Mine.base.InternetURL;
import com.xiaogang.Mine.mobule.AddressObj;
import com.xiaogang.Mine.util.StringUtil;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/12.
 */
public class MineAddressUpdateActivity extends BaseActivity implements View.OnClickListener {
    private AddressObj goodsAddress;
    private EditText update_name;
    private EditText add_phone;
    private EditText add_address_two;
    private Button button_add_address;
    private ImageView back;
    private ProgressDialog progressDialog;
    private String is_default = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_address_update_activity);
        goodsAddress = (AddressObj) getIntent().getExtras().get("goodsAddress");
//        is_default = goodsAddress.getIs_default_address();
        initView();
    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);
        update_name = (EditText) this.findViewById(R.id.update_name);
        add_phone = (EditText) this.findViewById(R.id.add_phone);
        add_address_two = (EditText) this.findViewById(R.id.add_address_two);
        button_add_address = (Button) this.findViewById(R.id.button_add_address);
        back.setOnClickListener(this);
        button_add_address.setOnClickListener(this);

        update_name.setText(goodsAddress.getContact_name());
        add_phone.setText(goodsAddress.getContact_mobile());
        add_address_two.setText(goodsAddress.getAddress());


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.button_add_address:
                //修改地址
                if (StringUtil.isNullOrEmpty(update_name.getText().toString())) {
                    Toast.makeText(MineAddressUpdateActivity.this, R.string.add_address_error_one, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (StringUtil.isNullOrEmpty(add_phone.getText().toString())) {
                    Toast.makeText(MineAddressUpdateActivity.this, R.string.add_address_error_two, Toast.LENGTH_SHORT).show();
                    return;
                }


                if (StringUtil.isNullOrEmpty(add_address_two.getText().toString())) {
                    Toast.makeText(MineAddressUpdateActivity.this, R.string.add_address_error_three, Toast.LENGTH_SHORT).show();
                    return;
                }

                Resources res = getBaseContext().getResources();
                String message = res.getString(R.string.is_dengluing).toString();
                progressDialog = new ProgressDialog(MineAddressUpdateActivity.this);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage(message);
                progressDialog.show();
                saveAddress();
                break;
        }
    }
    //保存收货地址
    public void saveAddress(){
        String uri = InternetURL.ADDRESS_ADD_UPDATE_URL
                +"?access_token=" + getGson().fromJson(getSp().getString("access_token", ""), String.class)
                +"&address="+add_address_two.getText().toString()
                +"&contact_name="+update_name.getText().toString()
                +"&contact_mobile="+add_phone.getText().toString()
                +"&id="+ goodsAddress.getId()
                +"&op="+"1";
        StringRequest request = new StringRequest(
                Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    Intent intent = new Intent("address_success");
                                    MineAddressUpdateActivity.this.sendBroadcast(intent);
                                    finish();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(MineAddressUpdateActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MineAddressUpdateActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
