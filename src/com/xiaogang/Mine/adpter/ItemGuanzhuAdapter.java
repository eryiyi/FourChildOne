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
import com.xiaogang.Mine.UniversityApplication;
import com.xiaogang.Mine.base.InternetURL;
import com.xiaogang.Mine.mobule.ProductDetail;
import com.xiaogang.Mine.util.RelativeDateFormat;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 */
public class ItemGuanzhuAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<ProductDetail> lists;
    private Context mContect;
    Resources res;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public ItemGuanzhuAdapter(List<ProductDetail> lists, Context mContect){
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
            holder.product_name = (TextView) convertView.findViewById(R.id.product_name);
            holder.price_one = (TextView) convertView.findViewById(R.id.price_one);
            holder.price_two = (TextView) convertView.findViewById(R.id.price_two);
            holder.unit = (TextView) convertView.findViewById(R.id.unit);
            holder.pic1 = (ImageView) convertView.findViewById(R.id.pic1);
            holder.pic2 = (ImageView) convertView.findViewById(R.id.pic2);
            holder.pic3 = (ImageView) convertView.findViewById(R.id.pic3);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.price_two.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线

        ProductDetail cell = lists.get(position);
        if(cell != null){
            holder.title.setText(cell.getProduct_name());
            holder.zhekou.setText(cell.getDiscount());
            holder.dateline.setText(RelativeDateFormat.format(Long.parseLong((cell.getDateline() == null ? "" : cell.getDateline()) + "000")));
            imageLoader.displayImage(InternetURL.INTERNAL + cell.getProduct_pic(), holder.pic1, UniversityApplication.options, animateFirstListener);
            imageLoader.displayImage(InternetURL.INTERNAL + cell.getProduct_pic(), holder.pic2, UniversityApplication.options, animateFirstListener);
            imageLoader.displayImage(InternetURL.INTERNAL + cell.getProduct_pic(), holder.pic3, UniversityApplication.options, animateFirstListener);
            holder.product_name.setText(cell.getProduct_name());
            holder.price_one.setText(cell.getPrice_tuangou());
            holder.price_two.setText(cell.getPrice());
            holder.unit.setText(cell.getUnit());
        }
        return convertView;
    }
    class ViewHolder {
        TextView title;
        TextView zhekou;
        TextView dateline;
        TextView product_name;
        TextView price_one;
        TextView unit;
        TextView price_two;
        ImageView pic1;
        ImageView pic2;
        ImageView pic3;
    }
}
