package com.xiaogang.Mine.fragment;

import android.app.ProgressDialog;
import android.content.*;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.adpter.AnimateFirstDisplayListener;
import com.xiaogang.Mine.adpter.ItemRecordsAdapter;
import com.xiaogang.Mine.adpter.OnClickContentItemListener;
import com.xiaogang.Mine.base.BaseFragment;
import com.xiaogang.Mine.base.InternetURL;
import com.xiaogang.Mine.data.RecordObjData;
import com.xiaogang.Mine.mobule.RecordObj;
import com.xiaogang.Mine.mobule.VideoPlayer;
import com.xiaogang.Mine.ui.CommentRecordActivity;
import com.xiaogang.Mine.ui.PublishPicActivity;
import com.xiaogang.Mine.ui.VideoPlayerActivity2;
import com.xiaogang.Mine.util.Constants;
import com.xiaogang.Mine.util.StringUtil;
import com.xiaogang.Mine.widget.ContentListView;
import com.xiaogang.Mine.widget.SelectPhoPopWindow;
import com.yixia.camera.demo.ui.record.MediaRecorderActivity;
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
        return view;
    }

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
//                + "?uid="+ "113"
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
                    save(Constants.PK_ADD_VIDEO_TYPE, "0");
                    Intent intent = new Intent(getActivity(), MediaRecorderActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
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

    RecordObj videosObj ;
    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        videosObj = lists.get(position);
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
//                +"?access_token=" + getGson().fromJson(getSp().getString("access_token", ""), String.class)
                +"?growing_id=" +videosObj.getId()
                +"&uid=" + getGson().fromJson(getSp().getString("uid", ""), String.class)
                +"&cancel=" + "0";
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
                                    //

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
