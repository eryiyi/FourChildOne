package com.xiaogang.Mine.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.xiaogang.Mine.MainActivity;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.adpter.ItemGuanzhuAdapter;
import com.xiaogang.Mine.adpter.ItemMineShebeiAdapter;
import com.xiaogang.Mine.base.ActivityTack;
import com.xiaogang.Mine.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/1.
 */
public class MineGuanzhuActivity extends BaseActivity implements View.OnClickListener{
    private ListView lstv;
    private ImageView mine_no_result_img;
    private TextView mine_no_result_text;
    private ItemGuanzhuAdapter adapter;
    private List<String> lists  = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_guanzhu_activity);

        lstv = (ListView) this.findViewById(R.id.lstv);
        mine_no_result_img = (ImageView) this.findViewById(R.id.mine_no_result_img);
        mine_no_result_text = (TextView) this.findViewById(R.id.mine_no_result_text);
        lists.add("");
        lists.add("");
        lists.add("");
        lists.add("");
        lists.add("");
        lists.add("");
        lists.add("");
        lists.add("");
        lists.add("");
        adapter = new ItemGuanzhuAdapter(lists, MineGuanzhuActivity.this);
        lstv.setAdapter(adapter);

        if(lists != null && lists.size() > 0){
            mine_no_result_img.setVisibility(View.GONE);
            mine_no_result_text.setVisibility(View.GONE);
            lstv.setVisibility(View.VISIBLE);
        }else {
            mine_no_result_img.setVisibility(View.VISIBLE);
            mine_no_result_text.setVisibility(View.VISIBLE);
            lstv.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    public void back(View view){finish();}


}
