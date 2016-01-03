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
public class TwoFragment extends BaseFragment implements View.OnClickListener ,OnClickContentItemListener{

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
        adapter.setOnClickContentItemListener(this);
        lstv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
    VideosObj videosObj;
    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        switch (flag){
            case 1:
                //播放按钮
                String videoUrl = "http://7xlyf1.com2.z0.glb.qiniucdn.com/042bc2df740c4b6a822467644c275811";
                Intent intent = new Intent(getActivity(), VideoPlayerActivity2.class);
                VideoPlayer video = new VideoPlayer(videoUrl);
                intent.putExtra(Constants.EXTRA_LAYOUT, "0");
                intent.putExtra(VideoPlayer.class.getName(), video);
                startActivity(intent);
                break;
            case 10:
                //
                videosObj = lists.get(position);
                for(VideosObj videosObj1 : lists){
                    if(videosObj1.getId().equals(videosObj.getId())){
                        //当前点击的哪一个
                        if("1".equals(videosObj.getIs_select())){
                            videosObj1.setIs_select("0");
                        }else {
                            videosObj1.setIs_select("1");
                        }
                    }else {
                        videosObj1.setIs_select("0");
                    }
                }
                adapter.notifyDataSetChanged();
                break;
            case 2:
                //赞
                videosObj = lists.get(position);
                break;
            case 3:
                //评论
                videosObj = lists.get(position);
                break;
        }
    }
}
