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
import com.xiaogang.Mine.mobule.FavourObj;

import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2015/2/6
 * Time: 14:06
 * 类的功能、说明写在此处.
 */
public class FavourAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<FavourObj> findEmps;
    private Context mContext;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }

    public FavourAdapter(List<FavourObj> findEmps, Context mContext) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.detail_favour_item_xml, null);
            holder.detail_favour_item_cover = (ImageView) convertView.findViewById(R.id.detail_favour_item_cover);
            holder.detail_favour_item_nickname = (TextView) convertView.findViewById(R.id.detail_favour_item_nickname);
            holder.detail_favour_item_dateline = (TextView) convertView.findViewById(R.id.detail_favour_item_dateline);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final FavourObj favour = findEmps.get(position);
        if (findEmps != null) {
            imageLoader.displayImage(favour.getCover(), holder.detail_favour_item_cover, UniversityApplication.txOptions, animateFirstListener);
            holder.detail_favour_item_nickname.setText(favour.getName());
            holder.detail_favour_item_dateline.setText(favour.getTime());
        }
//        holder.detail_favour_item_cover.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onClickContentItemListener.onClickContentItem(position, 1, null);
//            }
//        });
        return convertView;
    }

    class ViewHolder {
        ImageView detail_favour_item_cover;
        TextView detail_favour_item_nickname;
        TextView detail_favour_item_dateline;
    }

}