package com.xiaogang.Mine;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.xiaogang.Mine.base.BaseActivity;
import com.xiaogang.Mine.fragment.*;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    protected static final String TAG = "MainActivity";
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fm;

    private OneFragment oneFragment;
    private TwoFragment twoFragment;
    private ThreeFragment threeFragment;
    private FourFragment fourFragment;
    private FiveFragment fiveFragment;

    private ImageView foot_one;
    private ImageView foot_two;
    private ImageView foot_three;
    private ImageView foot_four;
    private ImageView foot_five;

    private long waitTime = 2000;
    private long touchTime = 0;

    //设置底部图标
    Resources res;

    private int index;
    // 当前fragment的index
    private int currentTabIndex = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        res = getResources();
        fm = getSupportFragmentManager();
        initView();

        switchFragment(R.id.foot_one);

    }

    @Override
    public void onClick(View v) {
        switchFragment(v.getId());
    }

    private void initView() {
        foot_one = (ImageView) this.findViewById(R.id.foot_one);
        foot_two = (ImageView) this.findViewById(R.id.foot_two);
        foot_three = (ImageView) this.findViewById(R.id.foot_three);
        foot_four = (ImageView) this.findViewById(R.id.foot_four);
        foot_five = (ImageView) this.findViewById(R.id.foot_five);
        foot_one.setOnClickListener(this);
        foot_two.setOnClickListener(this);
        foot_three.setOnClickListener(this);
        foot_four.setOnClickListener(this);
        foot_five.setOnClickListener(this);
    }


    public void switchFragment(int id) {
        fragmentTransaction = fm.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (id) {
            case R.id.foot_one:
                if (oneFragment == null) {
                    oneFragment = new OneFragment();
                    fragmentTransaction.add(R.id.content_frame, oneFragment);
                } else {
                    fragmentTransaction.show(oneFragment);
                }
                currentTabIndex = 0;
                foot_one.setImageResource(R.drawable.foot_one_white);
                foot_two.setImageResource(R.drawable.foot_two_red);
                foot_three.setImageResource(R.drawable.foot_three_red);
                foot_four.setImageResource(R.drawable.foot_four_red);
                foot_five.setImageResource(R.drawable.foot_five_red);

                break;
            case R.id.foot_two:
                if (twoFragment == null) {
                    twoFragment = new TwoFragment();
                    fragmentTransaction.add(R.id.content_frame, twoFragment);
                } else {
                    fragmentTransaction.show(twoFragment);
                }

                currentTabIndex = 1;
                foot_one.setImageResource(R.drawable.foot_one_red);
                foot_two.setImageResource(R.drawable.foot_two_white);
                foot_three.setImageResource(R.drawable.foot_three_red);
                foot_four.setImageResource(R.drawable.foot_four_red);
                foot_five.setImageResource(R.drawable.foot_five_red);
                break;
            case R.id.foot_three:
                if (threeFragment == null) {
                    threeFragment = new ThreeFragment();
                    fragmentTransaction.add(R.id.content_frame, threeFragment);
                } else {
                    fragmentTransaction.show(threeFragment);
                }
                currentTabIndex = 3;
                foot_one.setImageResource(R.drawable.foot_one_red);
                foot_two.setImageResource(R.drawable.foot_two_red);
                foot_three.setImageResource(R.drawable.foot_three_white);
                foot_four.setImageResource(R.drawable.foot_four_red);
                foot_five.setImageResource(R.drawable.foot_five_red);
                break;
            case R.id.foot_four:
                if (fourFragment == null) {
                    fourFragment = new FourFragment();
                    fragmentTransaction.add(R.id.content_frame, fourFragment);
                } else {
                    fragmentTransaction.show(fourFragment);
                }
                currentTabIndex = 4;
                foot_one.setImageResource(R.drawable.foot_one_red);
                foot_two.setImageResource(R.drawable.foot_two_red);
                foot_three.setImageResource(R.drawable.foot_three_red);
                foot_four.setImageResource(R.drawable.foot_four_white);
                foot_five.setImageResource(R.drawable.foot_five_red);
                break;
            case R.id.foot_five:
                if (fiveFragment == null) {
                    fiveFragment = new FiveFragment();
                    fragmentTransaction.add(R.id.content_frame, fiveFragment);
                } else {
                    fragmentTransaction.show(fiveFragment);
                }
                currentTabIndex = 4;
                foot_one.setImageResource(R.drawable.foot_one_red);
                foot_two.setImageResource(R.drawable.foot_two_red);
                foot_three.setImageResource(R.drawable.foot_three_red);
                foot_four.setImageResource(R.drawable.foot_four_red);
                foot_five.setImageResource(R.drawable.foot_five_white);
                break;

        }
        fragmentTransaction.commit();
    }

    private void hideFragments(FragmentTransaction ft) {
        if (oneFragment != null) {
            ft.hide(oneFragment);
        }
        if (twoFragment != null) {
            ft.hide(twoFragment);
        }
        if (threeFragment != null) {
            ft.hide(threeFragment);
        }
        if (fourFragment != null) {
            ft.hide(fourFragment);
        }
        if (fiveFragment != null) {
            ft.hide(fiveFragment);
        }
    }



    //再摁退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        PackageManager pm = getPackageManager();
        ResolveInfo homeInfo =
                pm.resolveActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), 0);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ActivityInfo ai = homeInfo.activityInfo;
            Intent startIntent = new Intent(Intent.ACTION_MAIN);
            startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            startIntent.setComponent(new ComponentName(ai.packageName, ai.name));
            startActivitySafely(startIntent);
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }

    private void startActivitySafely(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "null",
                    Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(this, "null",
                    Toast.LENGTH_SHORT).show();
        }
    }

    void logout() {
        final ProgressDialog pd = new ProgressDialog(this);
        String st = getResources().getString(R.string.Are_logged_out);
        // 重新显示登陆页面
        finish();
    }
}
