package com.xiaogang.Mine.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.adpter.AnimateFirstDisplayListener;
import com.xiaogang.Mine.base.BaseFragment;
import com.xiaogang.Mine.ui.MineAnquanActivity;
import com.xiaogang.Mine.ui.MineGuanzhuActivity;
import com.xiaogang.Mine.ui.MineShebeiActivity;
import com.xiaogang.Mine.ui.VideoCommentActivity;

/**
 */
public class FiveFragment extends BaseFragment implements View.OnClickListener {


    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.five_fragment, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        view.findViewById(R.id.mine_video_comment).setOnClickListener(this);
        view.findViewById(R.id.mine_shebei).setOnClickListener(this);
        view.findViewById(R.id.mine_guanzhu).setOnClickListener(this);
        view.findViewById(R.id.mine_huancun).setOnClickListener(this);
        view.findViewById(R.id.mine_anquan).setOnClickListener(this);
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
