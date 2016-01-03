package com.xiaogang.Mine.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.xiaogang.Mine.MainActivity;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.base.BaseActivity;

/**
 * Created by Administrator on 2015/12/20.
 * 登陆
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn:
                //登陆
                Intent main = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(main);
                break;
            case R.id.forget:
                //忘记密码
                Intent forgetView = new Intent(LoginActivity.this, ForgetActivity.class);
                startActivity(forgetView);
                break;
            case R.id.reg:
                //注册
                Intent reg = new Intent(LoginActivity.this ,RegActivity.class);
                startActivity(reg);
                break;
        }
    }

    void initView(){
        this.findViewById(R.id.btn).setOnClickListener(this);
        this.findViewById(R.id.forget).setOnClickListener(this);
        this.findViewById(R.id.reg).setOnClickListener(this);
    }
}
