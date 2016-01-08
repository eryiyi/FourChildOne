package com.xiaogang.Mine.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.adpter.ItemVideosAdapter;
import com.xiaogang.Mine.adpter.OnClickContentItemListener;
import com.xiaogang.Mine.base.BaseFragment;
import com.xiaogang.Mine.mobule.VideoPlayer;
import com.xiaogang.Mine.mobule.VideosObj;
import com.xiaogang.Mine.ui.VideoPlayerActivity2;
import com.xiaogang.Mine.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class TwoFragment extends BaseFragment implements View.OnClickListener {

    private ListView lstv;
    private ItemVideosAdapter adapter;
    List<VideosObj> lists = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.two_fragment, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        for(int i=0;i<20;i++){
            lists.add(new VideosObj(String.valueOf(i),"0"));
        }
        lstv = (ListView) view.findViewById(R.id.lstv);
        adapter = new ItemVideosAdapter(lists, getActivity());
        lstv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }


}
