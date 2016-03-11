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
import com.xiaogang.Mine.mobule.CameraObj;
import com.xiaogang.Mine.mobule.ClassObj;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 */
public class ItemMineCameraAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<CameraObj> lists;
    private Context mContect;
    Resources res;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public ItemMineCameraAdapter(List<CameraObj> lists, Context mContect){
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
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_mine_camera,null);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.img = (ImageView) convertView.findViewById(R.id.img);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final CameraObj cell = lists.get(position);
        if(cell != null){

            holder.title.setText(cell.getCameraName());
            imageLoader.displayImage( cell.getPicUrl(), holder.img, UniversityApplication.options, animateFirstListener);
        }
        return convertView;
    }
    class ViewHolder {
        TextView title;
        ImageView img;
    }
}
