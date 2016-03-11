package com.xiaogang.Mine.adpter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.mobule.ClassObj;
import com.xiaogang.Mine.mobule.SchoolObj;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 */
public class ItemClassAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<ClassObj> lists;
    private Context mContect;
    Resources res;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public ItemClassAdapter(List<ClassObj> lists, Context mContect){
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
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_school,null);
            holder.title = (TextView) convertView.findViewById(R.id.title);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final ClassObj cell = lists.get(position);
        if(cell != null){

            holder.title.setText(cell.getClass_name());
//            holder.sell_price.setText( res.getString(R.string.money) + cell.getPrice_tuangou());
//            holder.price.setText( res.getString(R.string.money) + cell.getPrice());
//            holder.price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
////            holder.item_pic.setImageResource(cell.getImg());
//            imageLoader.displayImage( cell.getProduct_pic(), holder.item_pic, UniversityApplication.options, animateFirstListener);
        }
        return convertView;
    }
    class ViewHolder {
        TextView title;
    }
}
