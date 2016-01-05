package com.xiaogang.Mine.ui;

import android.os.Bundle;
import android.text.format.DateUtils;
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
import com.xiaogang.Mine.adpter.ItemMineOrderSjAdapter;
import com.xiaogang.Mine.adpter.OnClickContentItemListener;
import com.xiaogang.Mine.base.BaseActivity;
import com.xiaogang.Mine.base.InternetURL;
import com.xiaogang.Mine.data.OrdersDATA;
import com.xiaogang.Mine.library.PullToRefreshBase;
import com.xiaogang.Mine.library.PullToRefreshListView;
import com.xiaogang.Mine.mobule.Orders;
import com.xiaogang.Mine.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/15.
 * 我的订单
 */
public class MineOrdersMngActivity extends BaseActivity implements View.OnClickListener,OnClickContentItemListener {
    private String order_no;

    private PullToRefreshListView classtype_lstv;//列表
    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;
    private String emp_id = "";//当前登陆者UUID

    private ItemMineOrderSjAdapter adapter;
    private List<Orders> orderVos = new ArrayList<Orders>();
    private TextView text_one;
    private TextView text_two;
    private TextView text_three;
    private TextView text_four;
    private String status="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_order_activity);
        initView();

        initData();
    }

    private void initView() {
        classtype_lstv = (PullToRefreshListView) this.findViewById(R.id.lstv);
        adapter = new ItemMineOrderSjAdapter(orderVos,MineOrdersMngActivity.this);
        adapter.setOnClickContentItemListener(this);

        classtype_lstv.setMode(PullToRefreshBase.Mode.BOTH);
        classtype_lstv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(MineOrdersMngActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = true;
                pageIndex = 1;
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(MineOrdersMngActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = false;
                pageIndex++;
                initData();
            }
        });
        classtype_lstv.setAdapter(adapter);
        classtype_lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Orders orderVo = orderVos.get(position-1);
//                Intent detailView = new Intent(MineOrdersMngActivity.this, DetailOrderMngActivity.class);
//                detailView.putExtra("orderVo",orderVo);
//                startActivity(detailView);
            }
        });

        text_one = (TextView) this.findViewById(R.id.text_one);
        text_two = (TextView) this.findViewById(R.id.text_two);
        text_three = (TextView) this.findViewById(R.id.text_three);
        text_four = (TextView) this.findViewById(R.id.text_four);
        text_one.setOnClickListener(this);
        text_two.setOnClickListener(this);
        text_three.setOnClickListener(this);
        text_four.setOnClickListener(this);
    }

    public void back(View view){
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.text_one:
                text_one.setTextColor(getResources().getColor(R.color.red));
                text_two.setTextColor(getResources().getColor(R.color.white));
                text_three.setTextColor(getResources().getColor(R.color.white));
                text_four.setTextColor(getResources().getColor(R.color.white));
                status = "";
                initData();
                break;
            case R.id.text_two:
                text_one.setTextColor(getResources().getColor(R.color.black_text_color));
                text_two.setTextColor(getResources().getColor(R.color.button_color_orange_p));
                text_three.setTextColor(getResources().getColor(R.color.black_text_color));
                text_four.setTextColor(getResources().getColor(R.color.black_text_color));
                status = "1";
                initData();
                break;
            case R.id.text_three:
                text_one.setTextColor(getResources().getColor(R.color.black_text_color));
                text_two.setTextColor(getResources().getColor(R.color.black_text_color));
                text_three.setTextColor(getResources().getColor(R.color.button_color_orange_p));
                text_four.setTextColor(getResources().getColor(R.color.black_text_color));
                status = "2";
                initData();
                break;
            case R.id.text_four:
                text_one.setTextColor(getResources().getColor(R.color.black_text_color));
                text_two.setTextColor(getResources().getColor(R.color.black_text_color));
                text_three.setTextColor(getResources().getColor(R.color.black_text_color));
                text_four.setTextColor(getResources().getColor(R.color.button_color_orange_p));
                status = "5";
                initData();
                break;
        }
    }


    Orders orderVoTmp;
    int tmpPosition;
    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        orderVoTmp = orderVos.get(position);
        //1已发货  2 已收货 3取消  4 发送货物
        tmpPosition = position;
        switch (flag){
            case 1:
                break;
            case 2:
                break;
            case 3:

                break;
            case 4:
                break;
            case 5:
                break;
        }
    }

    //取订单
    private void initData() {
        StringRequest request = new StringRequest(
                Request.Method.GET,
                InternetURL.LIST_ORDER_URL +"?access_token="+  getGson().fromJson(getSp().getString("access_token", ""), String.class)
                +"&uid=" + getGson().fromJson(getSp().getString("uid", ""), String.class)
                +"&page_index="+ pageIndex,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            OrdersDATA data = getGson().fromJson(s, OrdersDATA.class);
                            if (Integer.parseInt(data.getCode() )== 200) {
                                if (IS_REFRESH) {
                                    orderVos.clear();
                                }
                                orderVos.addAll(data.getData());
                                classtype_lstv.onRefreshComplete();
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(MineOrdersMngActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MineOrdersMngActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MineOrdersMngActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("uid", getGson().fromJson(getSp().getString("uid", ""), String.class));
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
