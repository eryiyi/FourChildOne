package com.xiaogang.Mine.ui;

import android.app.ProgressDialog;
import android.content.*;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.adpter.AnimateFirstDisplayListener;
import com.xiaogang.Mine.base.BaseActivity;
import com.xiaogang.Mine.base.InternetURL;
import com.xiaogang.Mine.dao.DBHelper;
import com.xiaogang.Mine.dao.ShoppingCart;
import com.xiaogang.Mine.data.CategoryObjData;
import com.xiaogang.Mine.fragment.Fragment_pro_type;
import com.xiaogang.Mine.mobule.CategoryObj;
import com.xiaogang.Mine.util.StringUtil;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/30.
 */
public class IndexTypeActivity extends BaseActivity implements View.OnClickListener {
    private final static int SCANNIN_GREQUEST_CODE = 1;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    private ArrayList<CategoryObj> lists = new ArrayList<CategoryObj>();
    private List<CategoryObj> toolsList = new ArrayList<CategoryObj>();
    private TextView toolsTextViews[];
    private View views[];
    private LayoutInflater inflaters;
    private ScrollView scrollView;
    private int scrllViewWidth = 0, scrollViewMiddle = 0;
    private ViewPager shop_pager;
    private int currentItem=0;
    private ShopAdapter shopAdapter;

    //搜索框
    private EditText search_editext;
    private ImageView mine_cart;

    private String id;
    ProgressDialog pd = null;
    private boolean progressShow;
    private TextView number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index_type_activity);

        registerBoradcastReceiver();
        scrollView=(ScrollView) this.findViewById(R.id.tools_scrlllview);
        shopAdapter=new ShopAdapter(getSupportFragmentManager());
        inflaters=LayoutInflater.from(this);
        initView();
        //获得大类
        progressShow = true;
        pd = new ProgressDialog(IndexTypeActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                progressShow = false;
            }
        });
        pd.setMessage(getString(R.string.please_wait));
        pd.show();

        getBigType();

        List<ShoppingCart> lists = DBHelper.getInstance(IndexTypeActivity.this).getShoppingList();
        number.setText(String.valueOf(lists.size()));
    }
    public void back(View view){finish();}

    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("cart_success")) {
                //
                List<ShoppingCart> lists = DBHelper.getInstance(IndexTypeActivity.this).getShoppingList();
                number.setText(lists.size());
            }

        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("cart_success");//
        //注册广播
        this.registerReceiver(mBroadcastReceiver, myIntentFilter);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mBroadcastReceiver != null){
            this.unregisterReceiver(mBroadcastReceiver);
        }

    }
    private void initView() {
        number = (TextView) this.findViewById(R.id.number);
        mine_cart = (ImageView) this.findViewById(R.id.mine_cart);
        mine_cart.setOnClickListener(this);
        search_editext = (EditText) this.findViewById(R.id.search_editext);
        search_editext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!StringUtil.isNullOrEmpty(search_editext.getText().toString())){
                    //开始执行
//                    Intent intent = new Intent(getActivity(), SearchGoodsActivity.class);
//                    intent.putExtra("cont", search_editext.getText().toString());
//                    startActivity(intent);
                }
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.mine_cart:
                Intent carV = new Intent(IndexTypeActivity.this, MineCartActivity.class);
                startActivity(carV);
                break;
        }
    }

    /**
     * 动态生成显示items中的textview
     */
    private void showToolsView() {
        LinearLayout toolsLayout=(LinearLayout) this.findViewById(R.id.tools);
        toolsTextViews=new TextView[toolsList.size()];
        views=new View[toolsList.size()];

        for (int i = 0; i < toolsList.size(); i++) {
            View view=inflaters.inflate(R.layout.item_b_top_nav_layout, null);
            view.setId(i);
            view.setOnClickListener(toolsItemListener);
            TextView textView=(TextView) view.findViewById(R.id.text);
            textView.setText(toolsList.get(i).getType_name());
            toolsLayout.addView(view);
            toolsTextViews[i]=textView;
            views[i]=view;
        }
        changeTextColor(0);

        initPager();
    }

    private View.OnClickListener toolsItemListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            shop_pager.setCurrentItem(v.getId());
        }
    };



    /**
     * initPager<br/>
     * 初始化ViewPager控件相关内容
     */
    private void initPager() {
        shop_pager=(ViewPager) this.findViewById(R.id.goods_pager);
        shop_pager.setAdapter(shopAdapter);
        shop_pager.setOnPageChangeListener(onPageChangeListener);


    }

    /**
     * OnPageChangeListener<br/>
     * 监听ViewPager选项卡变化事的事件
     */

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int arg0) {
            if(shop_pager.getCurrentItem()!=arg0)shop_pager.setCurrentItem(arg0);
            if(currentItem!=arg0){
                changeTextColor(arg0);
                changeTextLocation(arg0);
            }
            currentItem=arg0;
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    /**
     * ViewPager 加载选项卡
     * @author Administrator
     *
     */
    private class ShopAdapter extends FragmentPagerAdapter {
        public ShopAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int arg0) {
            Fragment fragment =new Fragment_pro_type();
            Bundle bundle=new Bundle();
            CategoryObj goodsTypeBig=toolsList.get(arg0);
            bundle.putSerializable("goodsTypeBig", goodsTypeBig);
//            bundle.putSerializable("lists", lists);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return toolsList.size();
        }
    }


    /**
     * 改变textView的颜色
     * @param id
     */
    private void changeTextColor(int id) {
        for (int i = 0; i < toolsTextViews.length; i++) {
            if(i!=id){
                toolsTextViews[i].setBackgroundResource(android.R.color.transparent);
                toolsTextViews[i].setTextColor(0xff000000);
            }
        }
        toolsTextViews[id].setBackgroundResource(android.R.color.white);
        toolsTextViews[id].setTextColor(0xffff5d5e);
    }


    /**
     * 改变栏目位置
     *
     * @param clickPosition
     */
    private void changeTextLocation(int clickPosition) {
        int x = (views[clickPosition].getTop() - getScrollViewMiddle() + (getViewheight(views[clickPosition]) / 2));
        scrollView.smoothScrollTo(0, x);
    }

    /**
     * 返回scrollview的中间位置
     *
     * @return
     */
    private int getScrollViewMiddle() {
        if (scrollViewMiddle == 0)
            scrollViewMiddle = getScrollViewheight() / 2;
        return scrollViewMiddle;
    }

    /**
     * 返回ScrollView的宽度
     *
     * @return
     */
    private int getScrollViewheight() {
        if (scrllViewWidth == 0)
            scrllViewWidth = scrollView.getBottom() - scrollView.getTop();
        return scrllViewWidth;
    }

    /**
     * 返回view的宽度
     *
     * @param view
     * @return
     */
    private int getViewheight(View view) {
        return view.getBottom() - view.getTop();
    }

    //获得类别
    private void getBigType() {
        StringRequest request = new StringRequest(
                Request.Method.GET,
                InternetURL.GET_TYPE_URL +"?access_token=" + getGson().fromJson(getSp().getString("access_token", ""), String.class),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    CategoryObjData data = getGson().fromJson(s, CategoryObjData.class);
                                    lists.addAll(data.getData());
                                    if(lists != null && lists.size()>0){
                                        for(int i=0;i<lists.size();i++){
                                            CategoryObj categoryObj = lists.get(i);
                                            if(categoryObj!= null  && "0".equals(categoryObj.getUp_id())){
                                                //取顶级分类
                                                toolsList.add(categoryObj);
                                            }
                                        }
                                    }
                                    showToolsView();
                                }else {
                                    Toast.makeText(IndexTypeActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(IndexTypeActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(IndexTypeActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if(resultCode == RESULT_OK){
                    Bundle bundle = data.getExtras();
                    //显示扫描到的内容
//                    mTextView.setText(bundle.getString("result"));
                    String result = bundle.getString("result");

                    //显示
                    // mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
                }
                break;
        }
    }
}
