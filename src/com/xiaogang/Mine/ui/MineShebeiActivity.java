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
import com.xiaogang.Mine.adpter.ItemMineShebeiAdapter;
import com.xiaogang.Mine.base.ActivityTack;
import com.xiaogang.Mine.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/1.
 */
public class MineShebeiActivity extends BaseActivity implements View.OnClickListener{
    private ListView lstv;
    private ImageView mine_no_result_img;
    private TextView mine_no_result_text;
    private ItemMineShebeiAdapter adapter;
    private List<String> lists  = new ArrayList<>();
    private LinearLayout mine_line_top;
    private TextView mine_shebei_index;
    private TextView mine_shebei_add;
    private TextView mine_shebei_shuaxin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_shebei_activity);

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
        adapter = new ItemMineShebeiAdapter(lists, MineShebeiActivity.this);
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

        mine_line_top = (LinearLayout) this.findViewById(R.id.mine_line_top);
        mine_line_top.setVisibility(View.GONE);
        mine_shebei_index = (TextView) this.findViewById(R.id.mine_shebei_index);
        mine_shebei_add = (TextView) this.findViewById(R.id.mine_shebei_add);
        mine_shebei_shuaxin = (TextView) this.findViewById(R.id.mine_shebei_shuaxin);

        mine_shebei_index.setOnClickListener(this);
        mine_shebei_add.setOnClickListener(this);
        mine_shebei_shuaxin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mine_shebei_index:
                //首页
                ActivityTack.getInstanse().popUntilActivity(MainActivity.class);
                break;
            case R.id.mine_shebei_add:
                //添加
                Intent addView = new Intent(MineShebeiActivity.this, AddShebeiActivity.class);
                startActivity(addView);
                break;
            case R.id.mine_shebei_shuaxin:
                //刷新
                mine_line_top.setVisibility(View.GONE);
                break;
        }
    }

    public void back(View view){finish();}

    public void rightClick(View view){
        //右上角
        mine_line_top.setVisibility(View.VISIBLE);
    }
}
