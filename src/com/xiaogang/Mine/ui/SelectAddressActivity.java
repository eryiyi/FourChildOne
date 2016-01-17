package com.xiaogang.Mine.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.xiaogang.Mine.adpter.ItemMineAddressAdapter;
import com.xiaogang.Mine.base.BaseActivity;
import com.xiaogang.Mine.base.InternetURL;
import com.xiaogang.Mine.data.ShoppingAddressDATA;
import com.xiaogang.Mine.mobule.AddressObj;
import com.xiaogang.Mine.util.StringUtil;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/25.
 * 我的收货地址列表
 */
public class SelectAddressActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private TextView no_goods;
    private ListView lstv;
    private ItemMineAddressAdapter adapter;
    private List<AddressObj> lists  = new ArrayList<AddressObj>();
    private TextView add;
    private ProgressDialog progressDialog;
    private String address_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBoradcastReceiver();
        address_id  = getIntent().getExtras().getString("address_id");
        setContentView(R.layout.mine_address_activity);
        initView();
        Resources res = getBaseContext().getResources();
        String message = res.getString(R.string.please_wait).toString();
        progressDialog = new ProgressDialog(SelectAddressActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();
        getData();
    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);
        no_goods = (TextView) this.findViewById(R.id.no_goods);
        no_goods.setVisibility(View.GONE);
        lstv = (ListView) this.findViewById(R.id.lstv);
        back.setOnClickListener(this);
        adapter = new ItemMineAddressAdapter(lists, SelectAddressActivity.this);
        lstv.setAdapter(adapter);
        add = (TextView) this.findViewById(R.id.add);
        add.setOnClickListener(this);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AddressObj goodsAddress = lists.get(i);
                Intent intent = new Intent(SelectAddressActivity.this, OrderMakeActivity.class);
                intent.putExtra("select_address", goodsAddress);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.add:
                //添加收货地址
                Intent  intentAddressAdd = new Intent(SelectAddressActivity.this, MineAddressAddActivity.class);
                startActivity(intentAddressAdd);
                break;
        }
    }
    void getData(){
        //获得收货地址列表
        String uri = InternetURL.ADDRESS_LIST_URL +"?access_token="+  getGson().fromJson(getSp().getString("access_token", ""), String.class);
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
                                    ShoppingAddressDATA data = getGson().fromJson(s, ShoppingAddressDATA.class);
                                    lists.clear();
                                    lists.addAll(data.getData());
                                    adapter.notifyDataSetChanged();
                                    if(lists.size() == 0){
                                        no_goods.setVisibility(View.VISIBLE);
                                        lstv.setVisibility(View.GONE);
                                    }else {
                                        no_goods.setVisibility(View.GONE);
                                        lstv.setVisibility(View.VISIBLE);
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }


                        } else {
                            Toast.makeText(SelectAddressActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SelectAddressActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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


    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("address_success")) {
                getData();
            }if (action.equals("update_address_success")) {
                getData();
            }

        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("address_success");//设置默认收货地址成功
        myIntentFilter.addAction("update_address_success");//更新收货地址成功
        //注册广播
        this.registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mBroadcastReceiver);
    }
}
