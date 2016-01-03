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
import com.xiaogang.Mine.base.InternetURL;
import com.xiaogang.Mine.mobule.Orders;

import java.util.List;

/**
 * 我的订单
 */
public class ItemMineOrderSjAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<Orders> findEmps;
    private Context mContext;
    Resources res;

    private OnClickContentItemListener onClickContentItemListener;
    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    public ItemMineOrderSjAdapter(List<Orders> findEmps, Context mContext) {
        this.findEmps = findEmps;
        this.mContext = mContext;
        res = mContext.getResources();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_mine_order_sj_adapter, null);
            holder.item_head = (ImageView) convertView.findViewById(R.id.item_head);
            holder.item_nickname = (TextView) convertView.findViewById(R.id.item_nickname);
            holder.item_status = (TextView) convertView.findViewById(R.id.item_status);
            holder.item_pic = (ImageView) convertView.findViewById(R.id.item_pic);
            holder.item_content = (TextView) convertView.findViewById(R.id.item_content);
            holder.item_prices = (TextView) convertView.findViewById(R.id.item_prices);
            holder.item_count = (TextView) convertView.findViewById(R.id.item_count);
            holder.item_money = (TextView) convertView.findViewById(R.id.item_money);
            holder.button_one = (TextView) convertView.findViewById(R.id.button_one);
            holder.button_two = (TextView) convertView.findViewById(R.id.button_two);
            holder.button_three = (TextView) convertView.findViewById(R.id.button_three);
            holder.button_four = (TextView) convertView.findViewById(R.id.button_four);
            holder.button_five = (TextView) convertView.findViewById(R.id.button_five);
            holder.item_dateline = (TextView) convertView.findViewById(R.id.item_dateline);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        final Orders cell = findEmps.get(position);//获得元素
//        if (cell != null) {
////            `status`
////            `pay_status` '支付状态 0：未支付，1：已支付，2：退款',
////            `distribution_status`  '配送状态 0：未发送,1：已发送,2：部分发送',
//
////            imageLoader.displayImage(InternetURL.INTERNAL + cell.getImg(), holder.item_pic, PropertyApplication.txOptions, animateFirstListener);
////            imageLoader.displayImage(cell.getGoodsCover(), holder.item_pic, PropertyApplication.txOptions, animateFirstListener);
//            holder.item_nickname.setText(cell.getOrder_no());
////           '订单状态 1生成订单,2支付订单,3取消订单,4作废订单,5完成订单',
//            switch (Integer.parseInt(cell.getStatus())){
//                //1已发货  2 已收货 3取消  4 发送货物
//                case 1:
//                    holder.item_status.setText("生成订单");
//                    holder.button_one.setVisibility(View.GONE);
//                    holder.button_two.setVisibility(View.GONE);
//                    holder.button_three.setVisibility(View.VISIBLE);
//                    holder.button_four.setVisibility(View.GONE);
//                    holder.button_five.setVisibility(View.GONE);
//                    break;
//                case 2:
//                    holder.item_status.setText("订单已支付");
//                    if("0".equals(cell.getDistribution_status())){
//                        //未发送货物
//                        holder.button_one.setVisibility(View.GONE);
//                        holder.button_two.setVisibility(View.GONE);
//                        holder.button_three.setVisibility(View.GONE);
//                        holder.button_four.setVisibility(View.VISIBLE);
//                    }else if("1".equals(cell.getDistribution_status())){
//                        //已发送货物
//                        holder.button_one.setVisibility(View.VISIBLE);
//                        holder.button_two.setVisibility(View.GONE);
//                        holder.button_three.setVisibility(View.GONE);
//                        holder.button_four.setVisibility(View.GONE);
//                    }
//                    holder.button_five.setVisibility(View.GONE);
//                    break;
//                case 3:
//                    holder.item_status.setText("交易已取消");
//                    holder.button_one.setVisibility(View.GONE);
//                    holder.button_two.setVisibility(View.GONE);
//                    holder.button_three.setVisibility(View.GONE);
//                    holder.button_four.setVisibility(View.GONE);
//                    holder.button_five.setVisibility(View.VISIBLE);
//                    break;
//                case 4:
//                    holder.item_status.setText("交易已删除");
//                    holder.button_one.setVisibility(View.GONE);
//                    holder.button_two.setVisibility(View.GONE);
//                    holder.button_three.setVisibility(View.GONE);
//                    holder.button_four.setVisibility(View.GONE);
//                    holder.button_five.setVisibility(View.GONE);
//                    break;
//                case 5:
//                    holder.item_status.setText("交易完成");
//                    holder.button_one.setVisibility(View.GONE);
//                    holder.button_two.setVisibility(View.VISIBLE);
//                    holder.button_three.setVisibility(View.GONE);
//                    holder.button_four.setVisibility(View.GONE);
//                    holder.button_five.setVisibility(View.GONE);
//                    break;
//            }
//            holder.item_content.setText(cell.getAddress() + "-收货人：" +cell.getAccept_name() +"--电话：" +cell.getMobile());
//            holder.item_prices.setText(res.getString(R.string.money) +cell.getGoods_price());
//            holder.item_count.setText(String.format(res.getString(R.string.item_count_adapter),cell.getGoods_nums()));
//            holder.item_money.setText(String.format(res.getString(R.string.item_money_adapter), Double.valueOf(cell.getReal_amount())));
//            if(!"任意".equals(cell.getAccept_time())){
//                holder.item_dateline.setText(RelativeDateFormat.format(Long.parseLong((cell.getAccept_time()==null?"":cell.getAccept_time())+"000")));
//            }else {
//                holder.item_dateline.setText(res.getString(R.string.create_time)+ cell.getCreate_time());
//            }
////            holder.item_dateline.setText(res.getString(R.string.create_time) +cell.getCreate_time());
//            holder.button_one.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    onClickContentItemListener.onClickContentItem(position, 1, null);
//                }
//            });
//            holder.button_two.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    onClickContentItemListener.onClickContentItem(position, 2, null);
//                }
//            });
//            holder.button_three.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    onClickContentItemListener.onClickContentItem(position, 3, null);
//                }
//            });
//            holder.button_four.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    onClickContentItemListener.onClickContentItem(position, 4, null);
//                }
//            });
//            holder.button_five.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    onClickContentItemListener.onClickContentItem(position, 5, null);
//                }
//            });
//        }
        return convertView;
    }

    class ViewHolder {
        ImageView item_head;
        TextView item_nickname;
        TextView item_status;
        ImageView item_pic;
        TextView item_content;
        TextView item_prices;
        TextView item_count;
        TextView item_money;
        TextView button_one;//已发货
        TextView button_two;//已收货
        TextView button_three;//取消订单
        TextView button_four;//确认发货
        TextView button_five;//删除订单
        TextView item_dateline;
    }
}
