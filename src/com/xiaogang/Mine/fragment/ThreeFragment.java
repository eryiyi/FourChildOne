package com.xiaogang.Mine.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.*;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.UniversityApplication;
import com.xiaogang.Mine.adpter.AnimateFirstDisplayListener;
import com.xiaogang.Mine.adpter.ItemRecordsAdapter;
import com.xiaogang.Mine.adpter.OnClickContentItemListener;
import com.xiaogang.Mine.adpter.ViewPagerAdapter;
import com.xiaogang.Mine.base.BaseFragment;
import com.xiaogang.Mine.base.InternetURL;
import com.xiaogang.Mine.data.RecordObjData;
import com.xiaogang.Mine.mobule.*;
import com.xiaogang.Mine.ui.CommentRecordActivity;
import com.xiaogang.Mine.ui.PublishPicActivity;
import com.xiaogang.Mine.ui.VideoPlayerActivity2;
import com.xiaogang.Mine.ui.WebViewActivity;
import com.xiaogang.Mine.util.Constants;
import com.xiaogang.Mine.util.StringUtil;
import com.xiaogang.Mine.widget.ContentListView;
import com.xiaogang.Mine.widget.SelectPhoPopWindow;
//import com.yixia.camera.demo.ui.record.MediaRecorderActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 搜索
 */
public class ThreeFragment extends BaseFragment implements View.OnClickListener,ContentListView.OnRefreshListener,
        ContentListView.OnLoadListener,OnClickContentItemListener {
    private ContentListView lstv;
    private ItemRecordsAdapter adapter;
    List<RecordObj> lists = new ArrayList<RecordObj>();
    private ImageView photoBtn;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private RelativeLayout commentLayout;//头部
    private int pageIndex = 1;
    private SelectPhoPopWindow deleteWindow;

    private String id;
    ProgressDialog pd = null;
    private boolean progressShow;

    private String cancelStr = "0";


    //导航
    private ViewPager viewpager;
    private ViewPagerAdapter adapterDao;
    private LinearLayout viewGroup;
    private ImageView dot, dots[];
    private Runnable runnable;
    private int autoChangeTime = 5000;
    private List<SlidePic> listsDao = new ArrayList<SlidePic>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBoradcastReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.three_fragment, null);
        initView(view);
        progressShow = true;
        pd = new ProgressDialog(getActivity());
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressShow = false;
            }
        });
        pd.setMessage(getString(R.string.please_wait));
        pd.show();

        loadData(ContentListView.REFRESH);

        getAd();
        return view;
    }


    void getAd(){
        listsDao = UniversityApplication.listsAd;
        initViewPager();
    }


    private void initViewPager() {
        adapterDao = new ViewPagerAdapter(getActivity());
        adapterDao.change(listsDao);
        adapterDao.setOnClickContentItemListener(this);
        viewpager = (ViewPager) commentLayout.findViewById(R.id.viewpager);
        viewpager.setAdapter(adapterDao);
        viewpager.setOnPageChangeListener(myOnPageChangeListener);
        initDot();
        runnable = new Runnable() {
            @Override
            public void run() {
                int next = viewpager.getCurrentItem() + 1;
                if (next >= adapterDao.getCount()) {
                    next = 0;
                }
                viewHandler.sendEmptyMessage(next);
            }
        };
        viewHandler.postDelayed(runnable, autoChangeTime);
    }


    // 初始化dot视图
    private void initDot() {
        viewGroup = (LinearLayout) commentLayout.findViewById(R.id.viewGroup);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                20, 20);
        layoutParams.setMargins(4, 3, 4, 3);

        dots = new ImageView[adapterDao.getCount()];
        for (int i = 0; i < adapterDao.getCount(); i++) {

            dot = new ImageView(getActivity());
            dot.setLayoutParams(layoutParams);
            dots[i] = dot;
            dots[i].setTag(i);
            dots[i].setOnClickListener(onClick);

            if (i == 0) {
                dots[i].setBackgroundResource(R.drawable.dotc);
            } else {
                dots[i].setBackgroundResource(R.drawable.dotn);
            }

            viewGroup.addView(dots[i]);
        }
    }

    ViewPager.OnPageChangeListener myOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            setCurDot(arg0);
            viewHandler.removeCallbacks(runnable);
            viewHandler.postDelayed(runnable, autoChangeTime);
        }

    };
    // 实现dot点击响应功能,通过点击事件更换页面
    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            setCurView(position);
        }

    };

    /**
     * 设置当前的引导页
     */
    private void setCurView(int position) {
        if (position < 0 || position > adapterDao.getCount()) {
            return;
        }
        viewpager.setCurrentItem(position);
//        if (!StringUtil.isNullOrEmpty(lists.get(position).getNewsTitle())){
//            titleSlide = lists.get(position).getNewsTitle();
//            if(titleSlide.length() > 13){
//                titleSlide = titleSlide.substring(0,12);
//                article_title.setText(titleSlide);//当前新闻标题显示
//            }else{
//                article_title.setText(titleSlide);//当前新闻标题显示
//            }
//        }

    }

    /**
     * 选中当前引导小点
     */
    private void setCurDot(int position) {
        for (int i = 0; i < dots.length; i++) {
            if (position == i) {
                dots[i].setBackgroundResource(R.drawable.dotc);
            } else {
                dots[i].setBackgroundResource(R.drawable.dotn);
            }
        }
    }

    /**
     * 每隔固定时间切换广告栏图片
     */
    @SuppressLint("HandlerLeak")
    private final Handler viewHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setCurView(msg.what);
        }

    };

    private void initView(View view) {
        lstv = (ContentListView) view.findViewById(R.id.lstv);
        adapter = new ItemRecordsAdapter(lists, getActivity());
        commentLayout = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.three_top, null);
        lstv.addHeaderView(commentLayout);//添加头部
        lstv.setAdapter(adapter);
        lstv.setOnRefreshListener(this);
        lstv.setOnLoadListener(this);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        lstv.setResultSize(lists.size());
        adapter.setOnClickContentItemListener(this);
        adapter.notifyDataSetChanged();

        photoBtn = (ImageView) view.findViewById(R.id.photoBtn);
        photoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photoBtn:
                //弹出底部狂
                ShowPickDialog();
                break;
        }
    }

    private void loadData(final int currentid) {
        String uri = InternetURL.LIST_RECORD_URL
                + "?uid="+ getGson().fromJson(getSp().getString("uid", ""), String.class)
                +"&pageIndex="+pageIndex+"&pageSize=20";
        StringRequest request = new StringRequest(
                Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        lstv.onRefreshComplete();
                        lstv.onLoadComplete();
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200){
                                    RecordObjData data = getGson().fromJson(s, RecordObjData.class);
                                    if (Integer.parseInt(code)==200 && data.getData()!=null) {
                                        if (ContentListView.REFRESH == currentid) {
                                            lists.clear();
                                            lists.addAll(data.getData());
                                            lstv.setResultSize(data.getData().size());
                                            adapter.notifyDataSetChanged();
                                        }
                                        if (ContentListView.LOAD == currentid) {
                                            lists.clear();
                                            lists.addAll(data.getData());
                                            lstv.setResultSize(data.getData().size());
                                            adapter.notifyDataSetChanged();
                                        }
                                    }else {
                                        if (ContentListView.REFRESH == currentid) {
                                            lists.clear();
                                            lstv.setResultSize(0);
                                            adapter.notifyDataSetChanged();
                                        }
                                        if (ContentListView.LOAD == currentid) {
                                            lists.clear();
                                            lstv.setResultSize(0);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                                else {
                                    if (ContentListView.REFRESH == currentid) {
                                        lists.clear();
                                        lstv.setResultSize(0);
                                        adapter.notifyDataSetChanged();
                                    }
                                    if (ContentListView.LOAD == currentid) {
                                        lists.clear();
                                        lstv.setResultSize(0);
                                        adapter.notifyDataSetChanged();
                                    }
                                    Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (ContentListView.REFRESH == currentid) {
                                lists.clear();
                                lstv.setResultSize(0);
                                adapter.notifyDataSetChanged();
                            }
                            if (ContentListView.LOAD == currentid) {
                                lists.clear();
                                lstv.setResultSize(0);
                                adapter.notifyDataSetChanged();
                            }
                            Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        lstv.onRefreshComplete();
                        lstv.onLoadComplete();
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                        Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        getRequestQueue().add(request);
    }

    @Override
    public void onLoad() {
        pageIndex++;
        loadData(ContentListView.LOAD);
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        loadData(ContentListView.REFRESH);
    }

    // 选择相册，相机
    private void ShowPickDialog() {
        deleteWindow = new SelectPhoPopWindow(getActivity(), itemsOnClick);
        //显示窗口
        deleteWindow.showAtLocation(getActivity().findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }
    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            deleteWindow.dismiss();
            switch (v.getId()) {
                case R.id.picture: {
                    Intent wenzi = new Intent(getActivity(), PublishPicActivity.class);
                    wenzi.putExtra(Constants.SELECT_PHOTOORPIIC, "1");
                    startActivity(wenzi);
                }
                break;
                case R.id.mapstorage: {
                    Intent wenzi = new Intent(getActivity(), PublishPicActivity.class);
                    wenzi.putExtra(Constants.SELECT_PHOTOORPIIC, "2");
                    startActivity(wenzi);
                }
                break;
                case R.id.video:
                {
                    //视频
//                    save(Constants.PK_ADD_VIDEO_TYPE, "0");
//                    Intent intent = new Intent(getActivity(), MediaRecorderActivity.class);
//                    startActivity(intent);
//                    getActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                }
                    break;
                case R.id.wenzi:
                {
                    //文字
                    Intent wenzi = new Intent(getActivity(), PublishPicActivity.class);
                    wenzi.putExtra(Constants.SELECT_PHOTOORPIIC, "0");
                    startActivity(wenzi);
                }
                    break;
                default:
                    break;
            }
        }
    };

    SlidePic slidePic;
    RecordObj videosObj ;
    private int tmpPosition;
    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        String objectstr = (String) object;
        if("000".equals(objectstr)){
            slidePic = listsDao.get(position);
            switch (flag){
                case 0:
                    Intent webView = new Intent(getActivity(), WebViewActivity.class);
                    webView.putExtra("strurl", slidePic.getHref_url());
                    startActivity(webView);
                    break;
            }
        }
        if("111".equals(objectstr)){
            videosObj = lists.get(position);
            tmpPosition = position;
            switch (flag){
                case 10:
                    //
                    for(RecordObj videosObj1 : lists){
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
                {
                    progressShow = true;
                    pd = new ProgressDialog(getActivity());
                    pd.setCanceledOnTouchOutside(false);
                    pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

                        @Override
                        public void onCancel(DialogInterface dialog) {
                            progressShow = false;
                        }
                    });
                    pd.setMessage(getString(R.string.please_wait));
                    pd.show();
                    if(videosObj.getIs_favoured().equals("0")){
                        //没有收藏过 收藏
                        cancelStr = "0";
                    }
                    else {
                        //收藏过了 取消吧
                        cancelStr = "1";
                    }
                    setFavour();
                }
                break;
                case 3:
                    //评论
                {
                    Intent commentV = new Intent(getActivity(), CommentRecordActivity.class);
                    commentV.putExtra("info", videosObj);
                    startActivity(commentV);
                }
                break;
                case 5:
                    //视频点击
                {
                    String videoUrl = videosObj.getUrl();
                    Intent intent = new Intent(getActivity(), VideoPlayerActivity2.class);
                    VideoPlayer video = new VideoPlayer(videoUrl);
                    intent.putExtra(Constants.EXTRA_LAYOUT, "0");
                    intent.putExtra(VideoPlayer.class.getName(), video);
                    startActivity(intent);
                }
                break;
            }
        }
    }

    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(Constants.SEND_INDEX_SUCCESS)){
                pageIndex = 1;
                loadData(ContentListView.REFRESH);
            }
            if(action.equals("add_comment_record_success")){
                //添加评论成功
                loadData(ContentListView.REFRESH);
            }

        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.SEND_INDEX_SUCCESS);
        myIntentFilter.addAction("add_comment_record_success");
        //注册广播
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    private void setFavour() {
        String uri = InternetURL.ADD_FAVOUR_RECORD_URL
                +"?access_token=" + getGson().fromJson(getSp().getString("access_token", ""), String.class)
                +"&growing_id=" +videosObj.getId()
                +"&uid=" + getGson().fromJson(getSp().getString("uid", ""), String.class)
                +"&cancel=" + cancelStr;
        StringRequest request = new StringRequest(
                Request.Method.GET,
                uri
                ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    if("0".equals(cancelStr)){
                                        cancelStr = "1";
                                        Toast.makeText(getActivity(), "点赞成功", Toast.LENGTH_SHORT).show();
                                        Favours favours = lists.get(tmpPosition).getFavours();//当前的所有收藏
                                        List<FavourObj>  favourObjs = favours.getList();
                                        FavourObj favourObj = new FavourObj();
                                        favourObj.setCover(getGson().fromJson(getSp().getString("cover", ""), String.class));
                                        favourObjs.add(favourObj);
                                        favours.setList(favourObjs);
                                        lists.get(tmpPosition).setFavours(favours);
                                        lists.get(tmpPosition).setIs_select("0");
                                        lists.get(tmpPosition).setIs_favoured("1");
                                        adapter.notifyDataSetChanged();
                                    }else {
                                        cancelStr = "0";
                                        Favours favours = lists.get(tmpPosition).getFavours();//当前的所有收藏
                                        List<FavourObj>  favourObjs = favours.getList();
                                       for(FavourObj favourObj1:favourObjs){
                                           if(favourObj1.getCover().equals(getGson().fromJson(getSp().getString("cover", ""), String.class))){
                                               favourObjs.remove(favourObj1);
                                           }
                                       }
                                        favours.setList(favourObjs);
                                        lists.get(tmpPosition).setFavours(favours);
                                        lists.get(tmpPosition).setIs_select("0");
                                        lists.get(tmpPosition).setIs_favoured("0");
                                        adapter.notifyDataSetChanged();
                                        Toast.makeText(getActivity(), "取消成功", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(getActivity(), jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                        Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        getRequestQueue().add(request);
    }

}
