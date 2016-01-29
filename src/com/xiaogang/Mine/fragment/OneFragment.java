package com.xiaogang.Mine.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
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
import com.xiaogang.Mine.adpter.ItemHotAdapter;
import com.xiaogang.Mine.adpter.OnClickContentItemListener;
import com.xiaogang.Mine.adpter.ViewPagerAdapter;
import com.xiaogang.Mine.base.BaseFragment;
import com.xiaogang.Mine.base.InternetURL;
import com.xiaogang.Mine.data.IndexData;
import com.xiaogang.Mine.mobule.IndexObj;
import com.xiaogang.Mine.mobule.ProducteObj;
import com.xiaogang.Mine.mobule.SlidePic;
import com.xiaogang.Mine.ui.*;
import com.xiaogang.Mine.util.StringUtil;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class OneFragment extends BaseFragment implements View.OnClickListener ,OnClickContentItemListener {
    private final static int SCANNIN_GREQUEST_CODE = 1;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    Resources res;
    //导航
    private ViewPager viewpager;
    private ViewPagerAdapter adapter;
    private LinearLayout viewGroup;
    private ImageView dot, dots[];
    private Runnable runnable;
    private int autoChangeTime = 5000;
    public List<SlidePic> lists = new ArrayList<SlidePic>();

    View view;

    private GridView grid_one;
    private GridView grid_two;

    private ItemHotAdapter adpterNews;
    private ItemHotAdapter adpterHot;
    private List<ProducteObj> listNews = new ArrayList<ProducteObj>();
    private List<ProducteObj> listHots = new ArrayList<ProducteObj>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.one_fragment, null);
        res = getActivity().getResources();
        initView(view);

        getAd();
        return view;
    }

    private void initView(View view) {
        view.findViewById(R.id.index_type_liner_one).setOnClickListener(this);
        view.findViewById(R.id.index_type_liner_two).setOnClickListener(this);
        RelativeLayout index_type_liner_three = (RelativeLayout) view.findViewById(R.id.index_type_liner_three);
        index_type_liner_three.setOnClickListener(this);
        view.findViewById(R.id.index_type_liner_four).setOnClickListener(this);
        view.findViewById(R.id.text_three_oo).setOnClickListener(this);
        view.findViewById(R.id.text_three_ooo).setOnClickListener(this);
    }


    /**
     * 获取首页
     */
    private void getAd() {
        StringRequest request = new StringRequest(
                Request.Method.GET,
                InternetURL.GET_SHOP_INDEX_URL +"?access_token=" + getGson().fromJson(getSp().getString("access_token", ""), String.class),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    IndexData data = getGson().fromJson(s, IndexData.class);
                                    IndexObj indexObj = data.getData();
                                    if(indexObj != null){
                                        if(indexObj.getAds() != null){
                                            lists.clear();
                                            lists.addAll(indexObj.getAds());
                                            initViewPager();
                                            UniversityApplication.listsAd = lists;
                                        }
                                        if(indexObj.getNews() != null){
                                            listNews.clear();
                                            listNews.addAll(indexObj.getNews());
//                                            adpterNews.notifyDataSetChanged();
                                        }
                                        if(indexObj.getHot() != null){
                                            listHots.clear();
                                            listHots.addAll(indexObj.getHot());
//                                            adpterHot.notifyDataSetChanged();
                                        }
                                        setGridView();
                                    }

                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "show");
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

    private void initViewPager() {
        adapter = new ViewPagerAdapter(getActivity());
        adapter.change(lists);
        adapter.setOnClickContentItemListener(this);
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        viewpager.setAdapter(adapter);
        viewpager.setOnPageChangeListener(myOnPageChangeListener);
        initDot();
        runnable = new Runnable() {
            @Override
            public void run() {
                int next = viewpager.getCurrentItem() + 1;
                if (next >= adapter.getCount()) {
                    next = 0;
                }
                viewHandler.sendEmptyMessage(next);
            }
        };
        viewHandler.postDelayed(runnable, autoChangeTime);
    }


    // 初始化dot视图
    private void initDot() {
        viewGroup = (LinearLayout) view.findViewById(R.id.viewGroup);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                20, 20);
        layoutParams.setMargins(4, 3, 4, 3);

        dots = new ImageView[adapter.getCount()];
        for (int i = 0; i < adapter.getCount(); i++) {

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
        if (position < 0 || position > adapter.getCount()) {
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

    SlidePic slidePic;
    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        slidePic = lists.get(position);
        switch (flag){
            case 0:
                Intent webView = new Intent(getActivity(), WebViewActivity.class);
                webView.putExtra("strurl", slidePic.getHref_url());
                startActivity(webView);
                break;
        }
    }



    /**
     * 获取热门商品
     */
//    private void getHots() {
//        StringRequest request = new StringRequest(
//                Request.Method.GET,
//                InternetURL.GET_SHOP_INDEX_URL +"?access_token=" + getGson().fromJson(getSp().getString("access_token", ""), String.class),
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String s) {
//                        if (StringUtil.isJson(s)) {
//                            try {
//                                JSONObject jo = new JSONObject(s);
//                                String code1 =  jo.getString("code");
//                                if(Integer.parseInt(code1) == 200){
////                                    GoodsHotDATA data = getGson().fromJson(s, GoodsHotDATA.class);
////                                    if (data.getCode() == 200) {
////                                        listHots.clear();
////                                        listHots = data.getData();
////                                        setGridView();
////                                    } else {
////                                        Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
////                                    }
//                                }
//                            }catch (Exception e){
//
//                            }
//                        } else {
//                            Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
//                        }
//                        if (progressDialog != null) {
//                            progressDialog.dismiss();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        if (progressDialog != null) {
//                            progressDialog.dismiss();
//                        }
//                        Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("action", "hot");
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
//    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.index_type_liner_one:
                Intent oneView  = new Intent(getActivity(), IndexTypeActivity.class);
                startActivity(oneView);
                break;
            case R.id.index_type_liner_two:
                Intent twoView  = new Intent(getActivity(), MineCartActivity.class);
                startActivity(twoView);
                break;
            case R.id.index_type_liner_three:
                Intent threeView  = new Intent(getActivity(), MineOrdersMngActivity.class);
                startActivity(threeView);
                break;
            case R.id.index_type_liner_four:
                Intent mineShebei = new Intent(getActivity(), MineShebeiActivity.class);
                startActivity(mineShebei);
                break;
            case R.id.text_three_oo:
                Intent moreView  = new Intent(getActivity(), NewsTableViewActivity.class);
                moreView.putExtra("type", "0");
                startActivity(moreView);
                break;
            case R.id.text_three_ooo:
                Intent moreView2  = new Intent(getActivity(), NewsTableViewActivity.class);
                moreView2.putExtra("type", "1");
                startActivity(moreView2);
                break;
        }
    }



    private void setGridView() {
        grid_one = (GridView) view.findViewById(R.id.grid_one);//横向滚动
        grid_two = (GridView) view.findViewById(R.id.grid_two);//横向滚动
        grid_two.setSelector(new ColorDrawable(Color.TRANSPARENT));//去除点击黄色背景
        grid_one.setSelector(new ColorDrawable(Color.TRANSPARENT));//去除点击黄色背景

        adpterNews = new ItemHotAdapter(listNews, getActivity());
        adpterHot = new ItemHotAdapter(listHots, getActivity());


        int size = listNews.size();
        int length = 100;
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        grid_one.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        grid_one.setColumnWidth(itemWidth); // 设置列表项宽
        grid_one.setHorizontalSpacing(5); // 设置列表项水平间距
        grid_one.setStretchMode(GridView.NO_STRETCH);
        grid_one.setNumColumns(size); // 设置列数量=列表集合数
        grid_one.setAdapter(adpterNews);


        int size2 = listHots.size();
        int length2 = 100;
        DisplayMetrics dm2 = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm2);
        float density2 = dm2.density;
        int gridviewWidth2 = (int) (size2 * (length + 4) * density2);
        int itemWidth2 = (int) (length2 * density2);

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                gridviewWidth2, LinearLayout.LayoutParams.FILL_PARENT);
        grid_two.setLayoutParams(params2); // 设置GirdView布局参数,横向布局的关键
        grid_two.setColumnWidth(itemWidth2); // 设置列表项宽
        grid_two.setHorizontalSpacing(5); // 设置列表项水平间距
        grid_two.setStretchMode(GridView.NO_STRETCH);
        grid_two.setNumColumns(size2); // 设置列数量=列表集合数
        grid_two.setAdapter(adpterHot);

        grid_one.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ProducteObj good = listNews.get(position);
                Intent detailView = new Intent(getActivity(), DetailGoodsActivity.class);
                detailView.putExtra("good", good.getProduct_id());
                startActivity(detailView);
            }
        });
        grid_two.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ProducteObj good = listHots.get(position);
                Intent detailView = new Intent(getActivity(), DetailGoodsActivity.class);
                detailView.putExtra("good", good.getProduct_id());
                startActivity(detailView);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if(resultCode == getActivity().RESULT_OK){
                    Bundle bundle = data.getExtras();
                    //显示扫描到的内容
//                    mTextView.setText(bundle.getString("result"));
                    String result = bundle.getString("result");

                    //显示
//                    mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
                }
                break;
        }
    }

    //保存购物车
//    void saveCart(){
//        //先判断是否已经加入购物车
//        if(DBHelper.getInstance(getActivity()).isSaved(goods.getId())){
//            //如果你存在
//            Toast.makeText(getActivity(), R.string.add_cart_is, Toast.LENGTH_SHORT).show();
//        }else {
//            ShoppingCart shoppingCart = new ShoppingCart();
//            shoppingCart.setCartid(StringUtil.getUUID());
//            shoppingCart.setGoods_id(goods.getId());
////            shoppingCart.setEmp_id(getGson().fromJson(getSp().getString("mobile", ""), String.class));
//            shoppingCart.setEmp_id("");
//            shoppingCart.setGoods_name(goods.getName());
//            shoppingCart.setGoods_cover(goods.getImg());
//            shoppingCart.setSell_price(goods.getSell_price());
//            shoppingCart.setGoods_count("1");
//            shoppingCart.setDateline(DateUtil.getCurrentDateTime());
//            shoppingCart.setIs_select("0");
//            DBHelper.getInstance(getActivity()).addShoppingToTable(shoppingCart);
//            Toast.makeText(getActivity(), R.string.add_cart_success, Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent("cart_success");
//            getActivity().sendBroadcast(intent);
//        }
//        if (progressDialog != null) {
//            progressDialog.dismiss();
//        }
//    }


}
