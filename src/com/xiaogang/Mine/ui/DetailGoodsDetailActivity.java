package com.xiaogang.Mine.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.adpter.AnimateFirstDisplayListener;
import com.xiaogang.Mine.adpter.MineGoodsPicAdapter;
import com.xiaogang.Mine.adpter.OnClickContentItemListener;
import com.xiaogang.Mine.adpter.ViewPagerAdapterGoods;
import com.xiaogang.Mine.base.BaseActivity;
import com.xiaogang.Mine.base.InternetURL;
import com.xiaogang.Mine.dao.DBHelper;
import com.xiaogang.Mine.dao.ShoppingCart;
import com.xiaogang.Mine.data.ProductDetailData;
import com.xiaogang.Mine.mobule.ProductDetail;
import com.xiaogang.Mine.mobule.ShopObj;
import com.xiaogang.Mine.util.DateUtil;
import com.xiaogang.Mine.util.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/17.
 */
public class DetailGoodsDetailActivity extends BaseActivity  implements View.OnClickListener ,OnClickContentItemListener {

    private TextView title;

    private ProductDetail productDetail;
    private ListView lstv;
    private MineGoodsPicAdapter adapter;
    private List<String> lists = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_detail_detail);
        productDetail = (ProductDetail) getIntent().getExtras().get("productDetail");
        if(productDetail != null){
            lists = productDetail.getPices();
        }

    }

    void initView(){
        title = (TextView) this.findViewById(R.id.title);
        title.setText(Html.fromHtml(productDetail.getInfo()));
        lstv = (ListView) this.findViewById(R.id.lstv);
        adapter = new MineGoodsPicAdapter(lists, DetailGoodsDetailActivity.this);
        lstv.setAdapter(adapter);
    }


    @Override
    public void onClickContentItem(int position, int flag, Object object) {

    }

    @Override
    public void onClick(View v) {

    }

    public void back(View view){finish();}
}

