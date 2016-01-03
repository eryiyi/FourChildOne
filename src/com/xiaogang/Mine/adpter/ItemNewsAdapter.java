package com.xiaogang.Mine.adpter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.mobule.GoodsHot;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 */
public class ItemNewsAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<GoodsHot> lists;
    private Context mContect;
    Resources res;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public ItemNewsAdapter(List<GoodsHot> lists, Context mContect){
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
            holder.item_title = (TextView) convertView.findViewById(R.id.item_title);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final GoodsHot cell = lists.get(position);
//        if(cell != null){
//            String title = cell.getName()==null?"":cell.getName();
//            if(title.length() > 13){
//                title = title.substring(0,12) + "...";
//            }
//            holder.item_title.setText(title);
//            holder.sell_price.setText( res.getString(R.string.money) + cell.getSell_price());
//            holder.item_pic.setImageResource(cell.getImg());
////            imageLoader.displayImage(InternetURL.INTERNAL_PIC + cell.getImg(), holder.item_index_foot_one_pic, UniversityApplication.options, animateFirstListener);
//        }
        return convertView;
    }
    class ViewHolder {
        ImageView item_pic;
        ImageView item_cart;
        TextView item_title;
        TextView sell_price;
    }
}
