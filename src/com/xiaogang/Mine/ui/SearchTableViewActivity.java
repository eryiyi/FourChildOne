package com.xiaogang.Mine.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.adpter.ItemNewsAdapter;
import com.xiaogang.Mine.base.BaseActivity;
import com.xiaogang.Mine.base.InternetURL;
import com.xiaogang.Mine.data.ProducteTypeObjData;
import com.xiaogang.Mine.mobule.ProducteObj;
import com.xiaogang.Mine.mobule.ProducteTypeObj;
import com.xiaogang.Mine.mobule.ProducteTypeObjSmall;
import com.xiaogang.Mine.util.StringUtil;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/17.
 */
public class SearchTableViewActivity extends BaseActivity implements View.OnClickListener {
    private ListView lstv;
    private ItemNewsAdapter adapter;
    List<ProducteTypeObj> listTypePro= new ArrayList<ProducteTypeObj>();
    List<ProducteObj> list =  new ArrayList<ProducteObj>();
    ProducteTypeObj producteTypeObj;
    private EditText searchText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        initView();


        searchText = (EditText) this.findViewById(R.id.searchText);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
               String content = searchText.getText().toString();//要搜的内容
                if (!StringUtil.isNullOrEmpty(content)) {
                    getData();
                }

            }
        });

    }

    void initView(){
        lstv = (ListView) this.findViewById(R.id.lstv);
        adapter = new ItemNewsAdapter(list, SearchTableViewActivity.this);
        lstv.setAdapter(adapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProducteObj producteObj = list.get(position);
                Intent detailView = new Intent(SearchTableViewActivity.this, DetailGoodsActivity.class);
                detailView.putExtra("good", producteObj.getProduct_id());
                startActivity(detailView);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    public void back(View view){
        finish();
    }

    private void getData() {
        StringRequest request = new StringRequest(
                Request.Method.GET,
                InternetURL.GET_KEY_URL +"?access_token=" + getGson().fromJson(getSp().getString("access_token", ""), String.class)+"&key="+searchText.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    ProducteTypeObjData data = getGson().fromJson(s, ProducteTypeObjData.class);
                                   List<ProducteTypeObjSmall> producteTypeObjSmalls = data.getData().getProduct_types();
                                    list.clear();
                                    list.addAll(producteTypeObjSmalls.get(0).getProductes());
                                    adapter.notifyDataSetChanged();
                                }
                            }catch (Exception e){

                            }
                        } else {
                            Toast.makeText(SearchTableViewActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Toast.makeText(SearchTableViewActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "hot");
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
