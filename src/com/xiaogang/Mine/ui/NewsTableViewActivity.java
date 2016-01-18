package com.xiaogang.Mine.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
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
public class NewsTableViewActivity extends BaseActivity implements View.OnClickListener {
    private ListView lstv;
    private ItemNewsAdapter adapter;
    List<ProducteTypeObj> listTypePro= new ArrayList<ProducteTypeObj>();
    List<ProducteObj> list =  new ArrayList<ProducteObj>();
    ProducteTypeObj producteTypeObj;
    private String type;
    private TextView detail_title;
    private String id;
    ProgressDialog pd = null;
    private boolean progressShow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);
        type = getIntent().getExtras().getString("type");
        detail_title = (TextView) this.findViewById(R.id.detail_title);
        if("0".equals(type)){
            detail_title.setText("新品专区");
        }else {
            detail_title.setText("热卖专区");
        }
        initView();

        progressShow = true;
        pd = new ProgressDialog(NewsTableViewActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                progressShow = false;
            }
        });
        pd.setMessage(getString(R.string.please_wait));
        pd.show();
        getData();
    }

    void initView(){
        lstv = (ListView) this.findViewById(R.id.lstv);
        adapter = new ItemNewsAdapter(list, NewsTableViewActivity.this);
        lstv.setAdapter(adapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProducteObj producteObj = list.get(position);
                Intent detailView = new Intent(NewsTableViewActivity.this, DetailGoodsActivity.class);
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
                InternetURL.GET_SHOP_PRODUCT_URL +"?access_token=" + getGson().fromJson(getSp().getString("access_token", ""), String.class)+"&type_id="+"1",
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
                            Toast.makeText(NewsTableViewActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                        Toast.makeText(NewsTableViewActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
