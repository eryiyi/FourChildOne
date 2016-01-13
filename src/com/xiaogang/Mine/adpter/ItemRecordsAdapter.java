package com.xiaogang.Mine.adpter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.UniversityApplication;
import com.xiaogang.Mine.base.InternetURL;
import com.xiaogang.Mine.mobule.RecordObj;
import com.xiaogang.Mine.mobule.VideosObj;
import com.xiaogang.Mine.ui.GalleryUrlActivity;
import com.xiaogang.Mine.util.Constants;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 */
public class ItemRecordsAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<RecordObj> lists;
    private Context mContect;
    Resources res;

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }


    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public ItemRecordsAdapter(List<RecordObj> lists, Context mContect){
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
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_records,null);
            holder.gridview_detail_picture = (GridView) convertView.findViewById(R.id.gridview_detail_picture);
            holder.head = (ImageView) convertView.findViewById(R.id.head);
            holder.commentBtn = (ImageView) convertView.findViewById(R.id.commentBtn);
            holder.zanBtnImg = (ImageView) convertView.findViewById(R.id.zanBtnImg);
            holder.commentBtnImg = (ImageView) convertView.findViewById(R.id.commentBtnImg);
            holder.video_pic = (ImageView) convertView.findViewById(R.id.video_pic);
            holder.video_play = (ImageView) convertView.findViewById(R.id.video_play);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.dateline = (TextView) convertView.findViewById(R.id.dateline);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.comment_liner = (LinearLayout) convertView.findViewById(R.id.comment_liner);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final RecordObj cell = lists.get(position);
        if(cell != null){
            holder.name.setText(cell.getPublisher() == null ? "" : cell.getPublisher());
            holder.dateline.setText(cell.getTime() == null ? "未知" : cell.getTime());
            holder.content.setText(cell.getDept()==null?"":cell.getDept());
            imageLoader.displayImage(InternetURL.INTERNAL + cell.getPublisher_cover(), holder.head, UniversityApplication.options, animateFirstListener);
            switch (Integer.parseInt(cell.getType()==null?"0":cell.getType())){
                case 0:
                    //文字
                    holder.gridview_detail_picture.setVisibility(View.GONE);
                    holder.video_pic.setVisibility(View.GONE);
                    holder.video_play.setVisibility(View.GONE);
                    break;
                case 1:
                    //照片
                    holder.gridview_detail_picture.setVisibility(View.VISIBLE);
                    holder.video_pic.setVisibility(View.GONE);
                    holder.video_play.setVisibility(View.GONE);
                    final String[] picUrls = cell.getUrl().split(",");
                    holder.gridview_detail_picture.setAdapter(new ImageGridViewAdapter(picUrls, mContect));
                    holder.gridview_detail_picture.setClickable(true);
                    holder.gridview_detail_picture.setPressed(true);
                    holder.gridview_detail_picture.setEnabled(true);
                    holder.gridview_detail_picture.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(mContect, GalleryUrlActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            intent.putExtra(Constants.IMAGE_URLS, picUrls);
                            intent.putExtra(Constants.IMAGE_POSITION, position);
                            mContect.startActivity(intent);
                        }
                    });
                    break;
                case 2:
                    //视频
                    holder.gridview_detail_picture.setVisibility(View.GONE);
                    holder.video_pic.setVisibility(View.VISIBLE);
                    holder.video_play.setVisibility(View.VISIBLE);
                    imageLoader.displayImage( cell.getUrl(), holder.video_pic, UniversityApplication.options, animateFirstListener);
                    break;
            }
        }

        if("1".equals(cell.getIs_select())){
            holder.comment_liner.setVisibility(View.VISIBLE);
        }else {
            holder.comment_liner.setVisibility(View.GONE);
        }

        holder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickContentItemListener.onClickContentItem(position, 10, null);
            }
        });
        holder.zanBtnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickContentItemListener.onClickContentItem(position, 2, null);
            }
        });
        holder.commentBtnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickContentItemListener.onClickContentItem(position, 3, null);
            }
        });
        return convertView;
    }
    class ViewHolder {
        GridView gridview_detail_picture;
        ImageView commentBtn;
        ImageView head;
        ImageView zanBtnImg;
        ImageView commentBtnImg;
        ImageView video_pic;
        ImageView video_play;
        TextView content;
        TextView name;
        TextView dateline;
        LinearLayout comment_liner;
    }
}
