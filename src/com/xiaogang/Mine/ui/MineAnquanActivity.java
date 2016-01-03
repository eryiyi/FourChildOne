package com.xiaogang.Mine.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.base.BaseActivity;

/**
 * Created by Administrator on 2016/1/1.
 */
public class MineAnquanActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_anquan_activity);

        this.findViewById(R.id.updatePwr).setOnClickListener(this);
        this.findViewById(R.id.findPwr).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.updatePwr:
                Intent updatePwr = new Intent(MineAnquanActivity.this , MineUpdatePwrActivity.class);
                startActivity(updatePwr);
                break;
            case R.id.findPwr:
                Intent findpwr = new Intent(MineAnquanActivity.this , MineFindPwrActivity.class);
                startActivity(findpwr);
                break;

        }
    }

    public void back(View view){finish();}
}
