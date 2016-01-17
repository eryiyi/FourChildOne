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
import com.xiaogang.Mine.mobule.ProducteObj;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 */
public class ItemNewsAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<ProducteObj> lists;
    private Context mContect;
    Resources res;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public ItemNewsAdapter(List<ProducteObj> lists, Context mContect){
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
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_news_goods_hot,null);
            holder.item_pic = (ImageView) convertView.findViewById(R.id.item_pic);
            holder.item_cart = (ImageView) convertView.findViewById(R.id.item_cart);
            holder.sell_price = (TextView) convertView.findViewById(R.id.sell_price);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.item_title = (TextView) convertView.findViewById(R.id.item_title);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final ProducteObj cell = lists.get(position);
        if(cell != null){
            String title = cell.getProduct_name()==null?"":cell.getProduct_name();
            if(title.length() > 13){
                title = title.substring(0,12) + "...";
            }
            holder.item_title.setText(title);
            holder.price.setText("好评："+cell.getGood_rate()+"-" +(cell.getSale_num()==null?"0":cell.getSale_num())+"人");
            holder.sell_price.setText(res.getString(R.string.money) + cell.getPrice_tuangou());
            imageLoader.displayImage(cell.getProduct_pic(), holder.item_pic, UniversityApplication.options, animateFirstListener);
        }
        return convertView;
    }
    class ViewHolder {
        ImageView item_pic;
        ImageView item_cart;
        TextView item_title;
        TextView sell_price;
        TextView price;
    }
}
