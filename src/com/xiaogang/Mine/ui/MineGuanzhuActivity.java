package com.xiaogang.Mine.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.adpter.ItemGuanzhuAdapter;
import com.xiaogang.Mine.base.BaseActivity;
import com.xiaogang.Mine.base.InternetURL;
import com.xiaogang.Mine.data.ProductDetailDatas;
import com.xiaogang.Mine.library.PullToRefreshBase;
import com.xiaogang.Mine.library.PullToRefreshListView;
import com.xiaogang.Mine.mobule.ProductDetail;
import com.xiaogang.Mine.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/1.
 */
public class MineGuanzhuActivity extends BaseActivity implements View.OnClickListener{
    private PullToRefreshListView lstv;
    private ImageView mine_no_result_img;
    private TextView mine_no_result_text;
    private ItemGuanzhuAdapter adapter;
    private List<ProductDetail> lists  = new ArrayList<ProductDetail>();
    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;

    private String id;
    ProgressDialog pd = null;
    private boolean progressShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_guanzhu_activity);

        lstv = (PullToRefreshListView) this.findViewById(R.id.lstv);
        mine_no_result_img = (ImageView) this.findViewById(R.id.mine_no_result_img);
        mine_no_result_text = (TextView) this.findViewById(R.id.mine_no_result_text);

        adapter = new ItemGuanzhuAdapter(lists, MineGuanzhuActivity.this);
        lstv.setMode(PullToRefreshBase.Mode.BOTH);
        lstv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(MineGuanzhuActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = true;
                pageIndex = 1;
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(MineGuanzhuActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = false;
                pageIndex++;
                initData();
            }
        });
        lstv.setAdapter(adapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductDetail orderVo = lists.get(position-1);
                Intent detailView = new Intent(MineGuanzhuActivity.this, DetailGoodsActivity.class);
                detailView.putExtra("good",orderVo.getProduct_id());
                startActivity(detailView);
            }
        });

//        if(lists != null && lists.size() > 0){
            mine_no_result_img.setVisibility(View.GONE);
            mine_no_result_text.setVisibility(View.GONE);
            lstv.setVisibility(View.VISIBLE);
//        }else {
//            mine_no_result_img.setVisibility(View.VISIBLE);
//            mine_no_result_text.setVisibility(View.VISIBLE);
//            lstv.setVisibility(View.GONE);
//        }



        progressShow = true;
        pd = new ProgressDialog(MineGuanzhuActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                progressShow = false;
            }
        });
        pd.setMessage(getString(R.string.please_wait));
        pd.show();
        initData();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    public void back(View view){finish();}

    private void initData() {
        StringRequest request = new StringRequest(
                Request.Method.GET,
                InternetURL.MINE_GUANZHU_URL +"?access_token="+  getGson().fromJson(getSp().getString("access_token", ""), String.class)
                        +"&page_index="+ pageIndex,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            ProductDetailDatas data = getGson().fromJson(s, ProductDetailDatas.class);
                            if (Integer.parseInt(data.getCode() )== 200) {
                                if (IS_REFRESH) {
                                    lists.clear();
                                }
                                lists.addAll(data.getData());
                                lstv.onRefreshComplete();
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(MineGuanzhuActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MineGuanzhuActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MineGuanzhuActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
