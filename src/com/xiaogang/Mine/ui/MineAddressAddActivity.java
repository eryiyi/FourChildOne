package com.xiaogang.Mine.ui;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.base.BaseActivity;
import com.xiaogang.Mine.mobule.City;
import com.xiaogang.Mine.mobule.Country;
import com.xiaogang.Mine.mobule.Province;
import com.xiaogang.Mine.util.StringUtil;
import com.xiaogang.Mine.widget.CustomerSpinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/10.
 * 收货地址
 */
public class MineAddressAddActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private EditText nickname;
    private EditText telephone;
    private EditText address;
    private Button sure;
    private EditText mobile;
    private EditText codeyb;
    Resources res;

    private CustomerSpinner province;//选择教学楼
    private CustomerSpinner city;
    private CustomerSpinner country;

    private List<Province> provinces = new ArrayList<Province>();//省
    private ArrayList<String> provinceNames = new ArrayList<String>();//省份名称

    private List<City> citys = new ArrayList<City>();//市
    private ArrayList<String> cityNames = new ArrayList<String>();//市名称

    private List<Country> countrys = new ArrayList<Country>();//区
    private ArrayList<String> countrysNames = new ArrayList<String>();//区名称


    ArrayAdapter<String> ProvinceAdapter;

    ArrayAdapter<String> cityAdapter;

    ArrayAdapter<String> countryAdapter;


    private String provinceName = "";
    private String cityName = "";
    private String countryName = "";

    private String provinceCode = "";
    private String cityCode = "";
    private String countryCode = "";

    private CheckBox checkbox;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_address_add_activity);
        res = getResources();
        initView();
        Resources res = getBaseContext().getResources();
        String message = res.getString(R.string.please_wait).toString();
        progressDialog = new ProgressDialog(MineAddressAddActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();
        getProvince();
    }

    private void initView() {
        checkbox = (CheckBox) this.findViewById(R.id.checkbox);
        back = (ImageView) this.findViewById(R.id.back);
        back.setOnClickListener(this);
        nickname = (EditText) this.findViewById(R.id.nickname);
        codeyb = (EditText) this.findViewById(R.id.codeyb);
        mobile = (EditText) this.findViewById(R.id.mobile);
        telephone = (EditText) this.findViewById(R.id.telephone);
        address = (EditText) this.findViewById(R.id.address);
        sure = (Button) this.findViewById(R.id.sure);
        sure.setOnClickListener(this);


        ProvinceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, provinceNames);
        ProvinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        province = (CustomerSpinner) findViewById(R.id.province);
        province.setAdapter(ProvinceAdapter);
        province.setList(provinceNames);
        province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citys.clear();
                    cityNames.clear();
                cityNames.add(getResources().getString(R.string.select_city_address));
                    cityAdapter.notifyDataSetChanged();
                    Province province = provinces.get(position);
                    provinceName = province.getProvince_name();
                    provinceCode = province.getProvince_code();
                    try {
                        Resources res = getBaseContext().getResources();
                        String message = res.getString(R.string.please_wait).toString();
                        progressDialog = new ProgressDialog(MineAddressAddActivity.this);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.setMessage(message);
                        progressDialog.show();
                        getCitys();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        cityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cityNames);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city = (CustomerSpinner) findViewById(R.id.city);
        city.setAdapter(cityAdapter);
        city.setList(cityNames);
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    countrys.clear();
                    countrysNames.clear();
                    countrysNames.add(getResources().getString(R.string.select_country));
                    City city = citys.get(position - 1);
                    cityName = city.getCity_name();
                    cityCode = city.getCity_code();
                    try {
                        Resources res = getBaseContext().getResources();
                        String message = res.getString(R.string.please_wait).toString();
                        progressDialog = new ProgressDialog(MineAddressAddActivity.this);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.setMessage(message);
                        progressDialog.show();
                        getArea();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    country.setEnabled(true);
                    countrysNames.clear();
                    countrysNames.add(res.getString(R.string.select_area));
                    countrys.clear();
                    countryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        countryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countrysNames);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country = (CustomerSpinner) findViewById(R.id.country);
        country.setAdapter(countryAdapter);
        country.setList(countrysNames);
        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    Country country = countrys.get(position - 1);
                    countryCode = country.getCounty_code();
                    countryName = country.getCounty_name();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.sure:
                //确定按钮
                if(StringUtil.isNullOrEmpty(nickname.getText().toString())){
                    Toast.makeText(MineAddressAddActivity.this, R.string.address_error_one, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(StringUtil.isNullOrEmpty(telephone.getText().toString())){
                    Toast.makeText(MineAddressAddActivity.this, R.string.address_error_two, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(StringUtil.isNullOrEmpty(mobile.getText().toString())){
                    Toast.makeText(MineAddressAddActivity.this, R.string.address_error_threee, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(StringUtil.isNullOrEmpty(address.getText().toString())){
                    Toast.makeText(MineAddressAddActivity.this, R.string.address_error_three, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(StringUtil.isNullOrEmpty(provinceCode)){
                    Toast.makeText(MineAddressAddActivity.this, R.string.address_error_four, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(StringUtil.isNullOrEmpty(cityCode)){
                    Toast.makeText(MineAddressAddActivity.this, R.string.address_error_four, Toast.LENGTH_SHORT).show();
                    return;
                }
                Resources res = getBaseContext().getResources();
                String message = res.getString(R.string.please_wait).toString();
                progressDialog = new ProgressDialog(MineAddressAddActivity.this);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage(message);
                progressDialog.show();
                setAddress();
                break;

        }
    }

    //获得省份
    public void getProvince(){
//        StringRequest request = new StringRequest(
//                Request.Method.POST,
//                InternetURL.GET_AREA_PROVINCE,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String s) {
//                        if (StringUtil.isJson(s)) {
//                            try {
//                                JSONObject jo = new JSONObject(s);
//                                String code1 =  jo.getString("code");
//                                if(Integer.parseInt(code1) == 1){
//                                    ProvinceDATA data = getGson().fromJson(s, ProvinceDATA.class);
//                                    if(data.getCode() == 1){
//                                        provinces = data.getData();
//                                        if(provinces != null){
//                                            for(int i=0;i<provinces.size();i++){
//                                                provinceNames.add(provinces.get(i).getProvince_name());
//                                            }
//                                        }
//                                        ProvinceAdapter.notifyDataSetChanged();
//                                    } else{
//                                        Toast.makeText(MineAddressAddActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
//                                    }
//
//                                }
//                            }catch (Exception e){
//                                e.printStackTrace();
//                            }
//                        }else {
//                            Toast.makeText(MineAddressAddActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
//                        }
//                        if (progressDialog != null) {
//                            progressDialog.dismiss();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        if (progressDialog != null) {
//                            progressDialog.dismiss();
//                        }
//                        Toast.makeText(MineAddressAddActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
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
    }

    //获得城市
    public void getCitys(){
//        StringRequest request = new StringRequest(
//                Request.Method.POST,
//                InternetURL.GET_AREA_CITY,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String s) {
//                        if (StringUtil.isJson(s)) {
//                            CitysDATA data = getGson().fromJson(s, CitysDATA.class);
//                            if(data.getCode() == 1){
//                                citys = data.getData();
//                                if(citys != null){
//                                    for(int i=0;i<citys.size();i++){
//                                        cityNames.add(citys.get(i).getCity_name());
//                                    }
//                                }
//                                cityAdapter.notifyDataSetChanged();
//                            } else{
//                                Toast.makeText(MineAddressAddActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
//                            }
//
//                        }else {
//                            Toast.makeText(MineAddressAddActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
//                        }
//                        if (progressDialog != null) {
//                            progressDialog.dismiss();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        if (progressDialog != null) {
//                            progressDialog.dismiss();
//                        }
//                        Toast.makeText(MineAddressAddActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("province_code", provinceCode);
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
    }

    //获得地区
    public void getArea(){
//        StringRequest request = new StringRequest(
//                Request.Method.POST,
//                InternetURL.GET_AREA_COUNTRY,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String s) {
//                        if (StringUtil.isJson(s)) {
//                            CountrysDATA data = getGson().fromJson(s, CountrysDATA.class);
//                            if(data.getCode() == 1){
//                                countrys = data.getData();
//                                if(countrys != null){
//                                    for(int i=0;i<countrys.size();i++){
//                                        countrysNames.add(countrys.get(i).getCounty_name());
//                                    }
//                                }
//                                countryAdapter.notifyDataSetChanged();
//                            } else{
//                                Toast.makeText(MineAddressAddActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
//                            }
//
//                        }else {
//                            Toast.makeText(MineAddressAddActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
//                        }
//                        if (progressDialog != null) {
//                            progressDialog.dismiss();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        if (progressDialog != null) {
//                            progressDialog.dismiss();
//                        }
//                        Toast.makeText(MineAddressAddActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("action", "county");
//                params.put("city_code", cityCode);
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
    }

    //设置默认收货地址
    public void setAddress(){
//        StringRequest request = new StringRequest(
//                Request.Method.POST,
//                InternetURL.ADDRESS_SET_COUNTRY,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String s) {
//                        if (StringUtil.isJson(s)) {
//                            try {
//                                JSONObject jo = new JSONObject(s);
//                                String code1 =  jo.getString("code");
//                                if(Integer.parseInt(code1) == 1){
//                                    Intent intent = new Intent("address_success");
//                                    MineAddressAddActivity.this.sendBroadcast(intent);
//                                    finish();
//                                }
//                            }catch (Exception e){
//                                e.printStackTrace();
//                            }
//                        }else {
//                            Toast.makeText(MineAddressAddActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
//                        }
//                        if (progressDialog != null) {
//                            progressDialog.dismiss();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        if (progressDialog != null) {
//                            progressDialog.dismiss();
//                        }
//                        Toast.makeText(MineAddressAddActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("uid", getGson().fromJson(getSp().getString("uid", ""), String.class));
//                params.put("accept_name", nickname.getText().toString());
//                params.put("zip", codeyb.getText().toString());
//                params.put("telephone", telephone.getText().toString());
//                params.put("mobile", mobile.getText().toString());
//                params.put("province", provinceCode);
//                params.put("city", cityCode);
//                params.put("area", countryCode);
//                params.put("address", address.getText().toString());
//                if(checkbox.isChecked()){
//                    params.put("is_default_address", "1");
//                }else {
//                    params.put("is_default_address", "0");
//                }
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
    }

}
