package com.xiaogang.Mine.adpter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.UniversityApplication;
import com.xiaogang.Mine.base.InternetURL;
import com.xiaogang.Mine.dao.ShoppingCart;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 * 购物车
 */
public class ItemCartAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<ShoppingCart> lists;
    private Context mContect;
    Resources res;
    private OnClickContentItemListener onClickContentItemListener;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }

    public ItemCartAdapter(List<ShoppingCart> lists, Context mContect){
        this.lists = lists;
        this.mContect = mContect;
    }
    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        res = mContect.getResources();
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_cart,null);
            holder.select_is = (ImageView) convertView.findViewById(R.id.select_is);
            holder.item_pic = (ImageView) convertView.findViewById(R.id.item_pic);

            holder.goods_jian = (ImageView) convertView.findViewById(R.id.goods_jian);
            holder.goods_add = (ImageView) convertView.findViewById(R.id.goods_add);
            holder.item_cont = (TextView) convertView.findViewById(R.id.item_cont);
            holder.item_money = (TextView) convertView.findViewById(R.id.item_money);
            holder.item_num = (TextView) convertView.findViewById(R.id.item_num);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final ShoppingCart typeBean = lists.get(position);
        if(typeBean != null){
            holder.item_cont.setText(typeBean.getGoods_name());
            holder.item_money.setText( typeBean.getSell_price());
            holder.item_num.setText( typeBean.getGoods_count());
            imageLoader.displayImage(typeBean.getGoods_cover(), holder.item_pic, UniversityApplication.options, animateFirstListener);

            if("0".equals(typeBean.getIs_select())){
                holder.select_is.setImageResource(R.drawable.select_one);
            }else{
                holder.select_is.setImageResource(R.drawable.select_two);
            }

            holder.select_is.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickContentItemListener.onClickContentItem(position, 1, typeBean);
                }
            });
            holder.goods_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickContentItemListener.onClickContentItem(position, 2, typeBean);
                }
            });
            holder.goods_jian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickContentItemListener.onClickContentItem(position, 3, typeBean);
                }
            });
        }
        return convertView;
    }
    class ViewHolder {
        ImageView select_is;
        ImageView item_pic;
        TextView item_cont;
        TextView item_money;
        TextView item_num;
        ImageView goods_jian;
        ImageView goods_add;
    }
}
