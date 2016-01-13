package com.xiaogang.Mine.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.adpter.AnimateFirstDisplayListener;
import com.xiaogang.Mine.adpter.OnClickContentItemListener;
import com.xiaogang.Mine.adpter.ViewPagerAdapterGoods;
import com.xiaogang.Mine.base.BaseActivity;
import com.xiaogang.Mine.dao.DBHelper;
import com.xiaogang.Mine.dao.ShoppingCart;
import com.xiaogang.Mine.mobule.ProducteObj;
import com.xiaogang.Mine.util.DateUtil;
import com.xiaogang.Mine.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/17.
 */
public class DetailGoodsActivity extends BaseActivity  implements View.OnClickListener ,OnClickContentItemListener {
    ProducteObj goodsHot;
    private final static int SCANNIN_GREQUEST_CODE = 1;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    Resources res;

    //导航
    private ViewPager viewpager;
    private ViewPagerAdapterGoods adapter;
    private LinearLayout viewGroup;
    private ImageView dot, dots[];
    private Runnable runnable;
    private int autoChangeTime = 5000;
    private List<String> lists = new ArrayList<String>();

    private TextView price;
    private TextView sell_price;
    private TextView title;
    private TextView address;
    private TextView sumry;
    private TextView number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_detail);
        goodsHot = (ProducteObj) getIntent().getExtras().get("good");
        res = this.getResources();
        initView();
        if(goodsHot != null){
            lists.add(goodsHot.getProduct_pic()==null?"":goodsHot.getProduct_pic());
        }
        initViewPager();

        title.setText(goodsHot.getProduct_name()==null?"":goodsHot.getProduct_name());
        sumry.setText(Html.fromHtml(goodsHot.getInfo()==null?"":goodsHot.getInfo()));
        sell_price.setText("￥"+(goodsHot.getPrice_tuangou()==null?"":goodsHot.getPrice_tuangou()));
        price.setText("￥"+(goodsHot.getPrice()==null?"":goodsHot.getPrice()));
        address.setText(goodsHot.getUnit()==null?"":goodsHot.getUnit());

        getCartNum();
    }

    void initView(){
        price = (TextView) this.findViewById(R.id.price);
        sell_price = (TextView) this.findViewById(R.id.sell_price);
        price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
        title = (TextView) this.findViewById(R.id.title);
        sumry = (TextView) this.findViewById(R.id.sumry);
        address = (TextView) this.findViewById(R.id.address);
        number = (TextView) this.findViewById(R.id.number);
        this.findViewById(R.id.foot_mine_cart).setOnClickListener(this);
        this.findViewById(R.id.foot_tel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.foot_mine_cart:
                Intent mineCart = new Intent(DetailGoodsActivity.this, MineCartActivity.class);
                startActivity(mineCart);
                break;
            case R.id.foot_tel:
            {
                //电话：
                if(!StringUtil.isNullOrEmpty(goodsHot.getTel())){
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + goodsHot.getTel()));
                    startActivity(intent);
                }
            }
                break;
        }
    }

    private void initViewPager() {
        adapter = new ViewPagerAdapterGoods(DetailGoodsActivity.this);
        adapter.change(lists);
        adapter.setOnClickContentItemListener(this);
        viewpager = (ViewPager) this.findViewById(R.id.viewpager);
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
        viewGroup = (LinearLayout) this.findViewById(R.id.viewGroup);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                20, 20);
        layoutParams.setMargins(4, 3, 4, 3);

        dots = new ImageView[adapter.getCount()];
        for (int i = 0; i < adapter.getCount(); i++) {
            dot = new ImageView(DetailGoodsActivity.this);
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

    @Override
    public void onClickContentItem(int position, int flag, Object object) {

    }

    public void back(View view){
        finish();
    }

    public void addCart(View view){
        //
        //先判断是否已经加入购物车
        if(DBHelper.getInstance(DetailGoodsActivity.this).isSaved(goodsHot.getProduct_id())){
            //如果你存在
            Toast.makeText(DetailGoodsActivity.this, R.string.add_cart_is, Toast.LENGTH_SHORT).show();
        }else {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setCartid(StringUtil.getUUID());
            shoppingCart.setGoods_id(goodsHot.getProduct_id()==null?"":goodsHot.getProduct_id());
            shoppingCart.setEmp_id(getGson().fromJson(getSp().getString("uid", ""), String.class));
            shoppingCart.setGoods_name(goodsHot.getProduct_name()==null?"":goodsHot.getProduct_name());
            shoppingCart.setGoods_cover(goodsHot.getProduct_pic()==null?"":goodsHot.getProduct_pic());
            shoppingCart.setSell_price(goodsHot.getPrice_tuangou()==null?"":goodsHot.getPrice_tuangou());
            shoppingCart.setGoods_count("1");
            shoppingCart.setDateline(DateUtil.getCurrentDateTime());
            shoppingCart.setIs_select("0");
            DBHelper.getInstance(DetailGoodsActivity.this).addShoppingToTable(shoppingCart);
            Toast.makeText(DetailGoodsActivity.this, R.string.add_cart_success, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent("cart_success");
            DetailGoodsActivity.this.sendBroadcast(intent);
            getCartNum();
        }
    }

    void getCartNum(){
        //获得购物车商品数量
        number.setVisibility(View.VISIBLE);
        List<ShoppingCart> listCart = DBHelper.getInstance(DetailGoodsActivity.this).getShoppingList();
        if(listCart!=null){
            number.setText(String.valueOf(listCart.size()));
        }else {
            number.setText("0");
        }
    }

}

