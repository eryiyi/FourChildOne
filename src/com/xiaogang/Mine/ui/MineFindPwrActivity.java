package com.xiaogang.Mine.ui;

import android.os.Bundle;
import android.view.View;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.base.BaseActivity;

/**
 * Created by Administrator on 2016/1/1.
 */
public class MineFindPwrActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_find_pwr_activity);
    }

    @Override
    public void onClick(View v) {

    }

    public void back(View view){finish();}
}
