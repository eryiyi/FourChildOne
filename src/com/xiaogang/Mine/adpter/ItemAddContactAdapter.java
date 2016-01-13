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
import com.xiaogang.Mine.base.InternetURL;
import com.xiaogang.Mine.mobule.MemberObj;


import java.lang.reflect.Member;
import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2015/3/9
 * Time: 8:42
 * 类的功能、说明写在此处.
 */
public class ItemAddContactAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<MemberObj> findEmps;
    private Context mContext;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    public ItemAddContactAdapter(List<MemberObj> findEmps, Context mContext) {
        this.findEmps = findEmps;
        this.mContext = mContext;
    }


    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_add_contact, null);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.indicator = (TextView) convertView.findViewById(R.id.indicator);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final MemberObj cell = findEmps.get(position);
        if (findEmps != null) {
            imageLoader.displayImage( cell.getCover(), holder.avatar, UniversityApplication.txOptions, animateFirstListener);
            holder.name.setText(cell.getNick_name()+cell.getUser_name());
            holder.indicator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickContentItemListener.onClickContentItem(position, 1, null);
                }
            });

        }
        return convertView;
    }

    class ViewHolder {
        ImageView avatar;
        TextView indicator;
        TextView name;
    }
}
