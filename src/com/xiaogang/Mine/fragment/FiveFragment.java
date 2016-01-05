package com.xiaogang.Mine.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.UniversityApplication;
import com.xiaogang.Mine.adpter.AnimateFirstDisplayListener;
import com.xiaogang.Mine.base.BaseFragment;
import com.xiaogang.Mine.ui.MineAnquanActivity;
import com.xiaogang.Mine.ui.MineGuanzhuActivity;
import com.xiaogang.Mine.ui.MineShebeiActivity;
import com.xiaogang.Mine.ui.VideoCommentActivity;
import com.xiaogang.Mine.util.StringUtil;

/**
 */
public class FiveFragment extends BaseFragment implements View.OnClickListener {
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageView mine_head;
    private TextView mine_nickname;
    private TextView mine_age;
    private TextView mine_sign;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.five_fragment, null);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        view.findViewById(R.id.mine_video_comment).setOnClickListener(this);
        view.findViewById(R.id.mine_shebei).setOnClickListener(this);
        view.findViewById(R.id.mine_guanzhu).setOnClickListener(this);
        view.findViewById(R.id.mine_huancun).setOnClickListener(this);
        view.findViewById(R.id.mine_anquan).setOnClickListener(this);

        mine_head = (ImageView) view.findViewById(R.id.mine_head);
        mine_nickname = (TextView) view.findViewById(R.id.mine_nickname);
        mine_age = (TextView) view.findViewById(R.id.mine_age);
        mine_sign = (TextView) view.findViewById(R.id.mine_sign);

    }

    void initData(){
        //
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("nick_name", ""), String.class))){
            mine_nickname.setText(getGson().fromJson(getSp().getString("nick_name", ""), String.class));
        }else {
            mine_nickname.setText("未设置昵称");
        }
        mine_age.setText(getGson().fromJson(getSp().getString("birthday", ""), String.class));
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("remark", ""), String.class))){
            mine_sign.setText(getGson().fromJson(getSp().getString("remark", ""), String.class));
        }else {
            mine_sign.setText("暂无签名");
        }

        imageLoader.displayImage(getGson().fromJson(getSp().getString("cover", ""), String.class), mine_head, UniversityApplication.txOptions, animateFirstListener);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_video_comment:
                //视频评论
                Intent mineViedoComment = new Intent(getActivity(), VideoCommentActivity.class);
                startActivity(mineViedoComment);
                break;
            case R.id.mine_shebei:
                Intent mineshebei = new Intent(getActivity(), MineShebeiActivity.class);
                startActivity(mineshebei);
                break;
            case R.id.mine_guanzhu:
                Intent minegz = new Intent(getActivity(), MineGuanzhuActivity.class);
                startActivity(minegz);
                break;
            case R.id.mine_huancun:
                break;
            case R.id.mine_anquan:
                Intent anquanView = new Intent(getActivity(), MineAnquanActivity.class);
                startActivity(anquanView);
                break;
        }
    }



}
