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
import com.xiaogang.Mine.util.RelativeDateFormat;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 */
public class ItemGuanzhuAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<ProducteObj> lists;
    private Context mContect;
    Resources res;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public ItemGuanzhuAdapter(List<ProducteObj> lists, Context mContect){
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
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_mine_gz,null);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.zhekou = (TextView) convertView.findViewById(R.id.zhekou);
            holder.dateline = (TextView) convertView.findViewById(R.id.dateline);
            holder.pic1 = (ImageView) convertView.findViewById(R.id.pic1);
            holder.pic2 = (ImageView) convertView.findViewById(R.id.pic2);
            holder.pic3 = (ImageView) convertView.findViewById(R.id.pic3);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        ProducteObj cell = lists.get(position);
        if(cell != null){
            holder.title.setText(cell.getProduct_name());
            holder.zhekou.setText(cell.getDiscount());
            holder.dateline.setText(RelativeDateFormat.format(Long.parseLong((cell.getDateline() == null ? "" : cell.getDateline()) + "000")));
            imageLoader.displayImage( cell.getProduct_pic(), holder.pic1, UniversityApplication.txOptions, animateFirstListener);
        }
        return convertView;
    }
    class ViewHolder {
        TextView title;
        TextView zhekou;
        TextView dateline;
        ImageView pic1;
        ImageView pic2;
        ImageView pic3;
    }
}
