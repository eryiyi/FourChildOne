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
import com.xiaogang.Mine.mobule.FavourObj;

import java.util.List;

/**
 * 动态
 */
public class CommentAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<CommentObj> findEmps;
    private Context mContext;
    private int countTmp;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    public CommentAdapter(List<CommentObj> findEmps, Context mContext, int countTmp) {
        this.findEmps = findEmps;
        this.mContext = mContext;
        this.countTmp = countTmp;

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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.comment_item, null);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.comment = (TextView) convertView.findViewById(R.id.comment);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final CommentObj cell = findEmps.get(position);//获得元素
        if(cell != null){
            holder.title.setText(cell.getName()+":");
            holder.comment.setText(cell.getContent());
        }
        return convertView;
    }

    class ViewHolder {
        TextView title;
        TextView comment;
    }

}
