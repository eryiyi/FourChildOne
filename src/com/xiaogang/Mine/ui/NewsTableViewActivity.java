package com.xiaogang.Mine.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.adpter.ItemNewsAdapter;
import com.xiaogang.Mine.base.BaseActivity;
import com.xiaogang.Mine.mobule.GoodsHot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/17.
 */
public class NewsTableViewActivity extends BaseActivity implements View.OnClickListener {
    private ListView lstv;
    private ItemNewsAdapter adapter;
    List<GoodsHot> lists = new ArrayList<GoodsHot>();

    private String type;
    private TextView detail_title;
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
    }

    void initView(){
        lists.add(new GoodsHot("", "【新品】爱卡呀宝宝婴儿安全座椅", "360", "500", R.drawable.item_one));
        lists.add(new GoodsHot("", "【新品】爱卡呀宝宝婴儿安全座椅", "360", "500", R.drawable.item_three));
        lists.add(new GoodsHot("", "【新品】爱卡呀宝宝婴儿安全座椅", "360", "500", R.drawable.item_two));
        lists.add(new GoodsHot("", "【新品】爱卡呀宝宝婴儿安全座椅", "360", "500", R.drawable.item_four));
        lists.add(new GoodsHot("", "【新品】爱卡呀宝宝婴儿安全座椅", "360", "500", R.drawable.item_one));
        lists.add(new GoodsHot("", "【新品】爱卡呀宝宝婴儿安全座椅", "360", "500", R.drawable.item_three));
        lists.add(new GoodsHot("", "【新品】爱卡呀宝宝婴儿安全座椅", "360", "500", R.drawable.item_two));
        lists.add(new GoodsHot("", "【新品】爱卡呀宝宝婴儿安全座椅", "360", "500", R.drawable.item_four));
        lists.add(new GoodsHot("", "【新品】爱卡呀宝宝婴儿安全座椅", "360", "500", R.drawable.item_one));
        lists.add(new GoodsHot("", "【新品】爱卡呀宝宝婴儿安全座椅", "360", "500", R.drawable.item_three));
        lists.add(new GoodsHot("", "【新品】爱卡呀宝宝婴儿安全座椅", "360", "500", R.drawable.item_two));
        lists.add(new GoodsHot("", "【新品】爱卡呀宝宝婴儿安全座椅", "360", "500", R.drawable.item_four));
        lists.add(new GoodsHot("", "【新品】爱卡呀宝宝婴儿安全座椅", "360", "500", R.drawable.item_one));
        lists.add(new GoodsHot("", "【新品】爱卡呀宝宝婴儿安全座椅", "360", "500", R.drawable.item_three));
        lists.add(new GoodsHot("", "【新品】爱卡呀宝宝婴儿安全座椅", "360", "500", R.drawable.item_two));
        lists.add(new GoodsHot("", "【新品】爱卡呀宝宝婴儿安全座椅", "360", "500", R.drawable.item_four));

        lstv = (ListView) this.findViewById(R.id.lstv);
        adapter = new ItemNewsAdapter(lists, NewsTableViewActivity.this);
        lstv.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {

    }

    public void back(View view){
        finish();
    }
}
