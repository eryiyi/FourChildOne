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
import com.xiaogang.Mine.mobule.ShoppingAddress;
import com.xiaogang.Mine.util.StringUtil;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/12.
 */
public class MineAddressUpdateActivity extends BaseActivity implements View.OnClickListener {
    private ShoppingAddress goodsAddress;
    private EditText update_name;
    private EditText add_tel;
    private EditText add_phone;
    private TextView add_address_one;
    private EditText add_address_two;
    private EditText add_youbian;
    private Button button_add_address;
    private ImageView back;
    private ProgressDialog progressDialog;
    private CheckBox checkbox;
    private String is_default = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_address_update_activity);
        goodsAddress = (ShoppingAddress) getIntent().getExtras().get("goodsAddress");
        is_default = goodsAddress.getIs_default_address();
        initView();
    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);
        update_name = (EditText) this.findViewById(R.id.update_name);
        add_tel = (EditText) this.findViewById(R.id.add_tel);
        add_phone = (EditText) this.findViewById(R.id.add_phone);
        add_address_one = (TextView) this.findViewById(R.id.add_address_one);
        add_address_two = (EditText) this.findViewById(R.id.add_address_two);
        add_youbian = (EditText) this.findViewById(R.id.add_youbian);
        button_add_address = (Button) this.findViewById(R.id.button_add_address);
        back.setOnClickListener(this);
        button_add_address.setOnClickListener(this);
        checkbox = (CheckBox) this.findViewById(R.id.checkbox);

        update_name.setText(goodsAddress.getAccept_name());
        add_tel.setText(goodsAddress.getTelephone());
        add_phone.setText(goodsAddress.getMobile());
        add_youbian.setText(goodsAddress.getZip());
        add_address_one.setText(goodsAddress.getProvince_name()+goodsAddress.getCity_name()+goodsAddress.getArea_name());
        add_address_two.setText(goodsAddress.getAddress());
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    is_default = "1";
                } else {
                    is_default = "0";
                }
            }
        });
        if (is_default.equals("0")){
            //未选中
            checkbox.setChecked(false);
        }else{
            //选中
            checkbox.setChecked(true);
        }
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
                if (StringUtil.isNullOrEmpty(add_tel.getText().toString())) {
                    Toast.makeText(MineAddressUpdateActivity.this, R.string.add_address_error_two, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtil.isNullOrEmpty(add_phone.getText().toString())) {
                    Toast.makeText(MineAddressUpdateActivity.this, R.string.add_address_error_two, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtil.isNullOrEmpty(add_youbian.getText().toString())) {
                    Toast.makeText(MineAddressUpdateActivity.this, R.string.add_address_error_four, Toast.LENGTH_SHORT).show();
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
        StringRequest request = new StringRequest(
                Request.Method.POST,
//                InternetURL.ADDRESS_UPDATE_COUNTRY,
                "",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 1){
                                        //成功
                                        Intent intent = new Intent("update_address_success");
                                        sendBroadcast(intent);
                                        finish();
                                } else {
                                    Toast.makeText(MineAddressUpdateActivity.this,jo.getString("msg") , Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        } else {
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
                params.put("id", goodsAddress.getId());
                params.put("uid",  getGson().fromJson(getSp().getString("uid", ""), String.class));
                params.put("accept_name", update_name.getText().toString());
                params.put("zip", add_youbian.getText().toString());
                params.put("mobile", add_phone.getText().toString());
                params.put("province", goodsAddress.getProvince());
                params.put("city", goodsAddress.getCity());
                params.put("area", goodsAddress.getArea());
                params.put("address", add_address_two.getText().toString());
                params.put("telephone", add_tel.getText().toString());
                if(checkbox.isChecked()){
                    params.put("is_default_address", "1");
                }else {
                    params.put("is_default_address", "0");
                }
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
