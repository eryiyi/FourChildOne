package com.xiaogang.Mine.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.adpter.AnimateFirstDisplayListener;
import com.xiaogang.Mine.adpter.ItemRecordsAdapter;
import com.xiaogang.Mine.adpter.OnClickContentItemListener;
import com.xiaogang.Mine.base.BaseFragment;
import com.xiaogang.Mine.mobule.VideosObj;
import com.xiaogang.Mine.widget.ContentListView;
import com.xiaogang.Mine.widget.SelectPhoPopWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 搜索
 */
public class ThreeFragment extends BaseFragment implements View.OnClickListener,ContentListView.OnRefreshListener,
        ContentListView.OnLoadListener,OnClickContentItemListener {

    private ContentListView lstv;
    private ItemRecordsAdapter adapter;
    List<VideosObj> lists = new ArrayList<>();
    private ImageView photoBtn;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类


    private RelativeLayout commentLayout;//头部
    private int pageIndex = 1;


    private SelectPhoPopWindow deleteWindow;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.three_fragment, null);
        initView(view);


        loadData(ContentListView.REFRESH);
        return view;
    }

    private void initView(View view) {
        for(int i=0;i<20;i++){
            lists.add(new VideosObj(String.valueOf(i),"0"));
        }
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
//        String uri = InternetURL.EXCHANGE_REPLY_ARTICLE_URL + "?exchange_id="+goods.getExchange_id();
//        StringRequest request = new StringRequest(
//                Request.Method.GET,
//                uri,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String s) {
//                        detail_lstv.onRefreshComplete();
//                        detail_lstv.onLoadComplete();
//                        if (StringUtil.isJson(s)) {
//                            try {
//                                JSONObject jo = new JSONObject(s);
//                                String code =  jo.getString("code");
//                                if(Integer.parseInt(code) == 1){
//                                    GoodsCommentData data = getGson().fromJson(s, GoodsCommentData.class);
//                                    if (data.getCode() == 1 && data.getData()!=null) {
//                                        if (ContentListView.REFRESH == currentid) {
//                                            listComment.clear();
//                                            listComment.addAll(data.getData());
//                                            detail_lstv.setResultSize(data.getData().size());
//                                            adapter.notifyDataSetChanged();
//                                        }
//                                        if (ContentListView.LOAD == currentid) {
//                                            listComment.clear();
//                                            listComment.addAll(data.getData());
//                                            detail_lstv.setResultSize(data.getData().size());
//                                            adapter.notifyDataSetChanged();
//                                        }
//                                    }else {
//                                        if (ContentListView.REFRESH == currentid) {
//                                            listComment.clear();
//                                            detail_lstv.setResultSize(0);
//                                            adapter.notifyDataSetChanged();
//                                        }
//                                        if (ContentListView.LOAD == currentid) {
//                                            listComment.clear();
//                                            detail_lstv.setResultSize(0);
//                                            adapter.notifyDataSetChanged();
//                                        }
//                                    }
//                                }
//                                else {
//                                    if (ContentListView.REFRESH == currentid) {
//                                        listComment.clear();
//                                        detail_lstv.setResultSize(0);
//                                        adapter.notifyDataSetChanged();
//                                    }
//                                    if (ContentListView.LOAD == currentid) {
//                                        listComment.clear();
//                                        detail_lstv.setResultSize(0);
//                                        adapter.notifyDataSetChanged();
//                                    }
//                                    Toast.makeText(DetailGoodsActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            if (ContentListView.REFRESH == currentid) {
//                                listComment.clear();
//                                detail_lstv.setResultSize(0);
//                                adapter.notifyDataSetChanged();
//                            }
//                            if (ContentListView.LOAD == currentid) {
//                                listComment.clear();
//                                detail_lstv.setResultSize(0);
//                                adapter.notifyDataSetChanged();
//                            }
//                            Toast.makeText(DetailGoodsActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
//                        }
//                        if (progressDialog != null) {
//                            progressDialog.dismiss();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        detail_lstv.onRefreshComplete();
//                        detail_lstv.onLoadComplete();
//                        if (progressDialog != null) {
//                            progressDialog.dismiss();
//                        }
//                        Toast.makeText(DetailGoodsActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/x-www-form-urlencoded");
//                return params;
//            }
//        };
//        getRequestQueue().add(request);
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
                    Intent camera = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    //下面这句指定调用相机拍照后的照片存储的路径
                    camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                            .fromFile(new File(Environment
                                    .getExternalStorageDirectory(),
                                    "ppCover.jpg")));
                    startActivityForResult(camera, 2);
                }
                break;
                case R.id.mapstorage: {
                    Intent mapstorage = new Intent(Intent.ACTION_PICK, null);
                    mapstorage.setDataAndType(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            "image/*");
                    startActivityForResult(mapstorage, 1);
                }
                break;
                case R.id.video:
                {
                    //视频
                }
                    break;
                default:
                    break;
            }
        }
    };

    VideosObj videosObj ;
    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        videosObj = lists.get(position);
        switch (flag){
            case 10:
                //

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
                break;
            case 3:
                //评论
                break;
        }
    }
}
