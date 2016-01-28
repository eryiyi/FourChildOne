package com.xiaogang.Mine.adpter;

import android.content.Context;
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
import com.xiaogang.Mine.mobule.CommentObj;

import java.util.List;

/**
 */
public class MineGoodsPicAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<String> findEmps;
    private Context mContext;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    public MineGoodsPicAdapter(List<String> findEmps, Context mContext) {
        this.findEmps = findEmps;
        this.mContext = mContext;

    }

    @Override
    public int getCount() {
        return findEmps.size();
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
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.mine_goods_pic_item, null);
            holder.pic = (ImageView) convertView.findViewById(R.id.pic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final String cell = findEmps.get(position);//获得元素
        if(cell != null){
            imageLoader.displayImage(cell, holder.pic, UniversityApplication.options, animateFirstListener);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView pic;
    }

}
