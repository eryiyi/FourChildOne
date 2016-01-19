package com.xiaogang.Mine.ui;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.adpter.FavourAdapter;
import com.xiaogang.Mine.base.BaseActivity;
import com.xiaogang.Mine.library.PullToRefreshBase;
import com.xiaogang.Mine.library.PullToRefreshListView;
import com.xiaogang.Mine.mobule.FavourObj;
import com.xiaogang.Mine.mobule.Favours;

import java.util.ArrayList;
import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2015/2/6
 * Time: 13:50
 * 类的功能、说明写在此处.
 */
public class DetailFavourActivity extends BaseActivity implements View.OnClickListener {
    private PullToRefreshListView detail_favour_lstv;//列表
    private ImageView detail_favour_back;//返回
    private TextView detail_favour_title;//标题
    private FavourAdapter adapter;
    List<FavourObj> favours;
    private static boolean IS_REFRESH = true;
    private Favours Favour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_favour_xml);
        Favour = (Favours) getIntent().getExtras().get("minerecordfavour");
        initView();
        favours.addAll(Favour.getList());
//        getFavour();
    }

    private void initView() {
        favours = new ArrayList<FavourObj>();
        detail_favour_lstv = (PullToRefreshListView) this.findViewById(R.id.detail_favour_lstv);
        detail_favour_back = (ImageView) this.findViewById(R.id.detail_favour_back);
        detail_favour_back.setOnClickListener(this);
        detail_favour_title = (TextView) this.findViewById(R.id.detail_favour_title);
        adapter = new FavourAdapter(favours, this);
//        adapter.setOnClickContentItemListener(this);
        detail_favour_lstv.setAdapter(adapter);
        detail_favour_lstv.setMode(PullToRefreshBase.Mode.BOTH);
        detail_favour_lstv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(DetailFavourActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = true;
                detail_favour_lstv.onRefreshComplete();
//                getFavour();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(DetailFavourActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = false;
                detail_favour_lstv.onRefreshComplete();
//                getFavour();
            }
        });
        detail_favour_lstv.setAdapter(adapter);
        detail_favour_lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Favour favour = favours.get(i-1);
//                if (!emp_id.equals(favour.getEmpId())) {
//                    Intent profile = new Intent(DetailFavourActivity.this, ProfilePersonalActivity.class);
//                    profile.putExtra(Constants.EMPID, favour.getEmpId());
//                    startActivity(profile);
//                } else {
//                    Intent profile = new Intent(DetailFavourActivity.this, UpdateProfilePersonalActivity.class);
//                    startActivity(profile);
//                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_favour_back:
                finish();
                break;
        }
    }

//    private void getFavour() {
//        StringRequest request = new StringRequest(
//                Request.Method.POST,
//                InternetURL.GET_FAVOUR_URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String s) {
//                        if (StringUtil.isJson(s)) {
//                            FavoursDATA data = getGson().fromJson(s, FavoursDATA.class);
//                            if (data.getCode() == 200) {
//                                favours.clear();
//                                detail_favour_lstv.onRefreshComplete();
//                                favours.addAll(data.getData());
//                                detail_favour_title.setText(favours.size() + "人觉得它很赞");
//                                adapter.notifyDataSetChanged();
//                            } else {
//                                Toast.makeText(DetailFavourActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            Toast.makeText(DetailFavourActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(DetailFavourActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("recordId", recordId);
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
//    }
//
//    Favour favour;

//    @Override
//    public void onClickContentItem(int position, int flag, Object object) {
//        switch (flag) {
//            case 1:
//                favour = favours.get(position);
//                if (!emp_id.equals(favour.getEmpId())) {
//                    Intent profile = new Intent(DetailFavourActivity.this, ProfilePersonalActivity.class);
//                    profile.putExtra(Constants.EMPID, favour.getEmpId());
//                    startActivity(profile);
//                } else {
//                    Intent profile = new Intent(DetailFavourActivity.this, UpdateProfilePersonalActivity.class);
//                    startActivity(profile);
//                }
//                break;
//        }
//    }

}
