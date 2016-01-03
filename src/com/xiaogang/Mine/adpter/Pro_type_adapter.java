package com.xiaogang.Mine.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.mobule.GoodsTypeSmall;

import java.util.List;


public class Pro_type_adapter extends BaseAdapter {
	// ∂®“ÂContext
		private LayoutInflater mInflater;
	    private List<GoodsTypeSmall> list;
	    private Context context;
	    private GoodsTypeSmall type;
		public Pro_type_adapter(Context context, List<GoodsTypeSmall> list){
			mInflater= LayoutInflater.from(context);
			this.list=list;
			this.context=context;
		}
		
		@Override
		public int getCount() {
			if(list!=null&&list.size()>0)
				return list.size();
			else
			    return 0;
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final MyView view;
			if(convertView==null){
				view=new MyView();
				convertView=mInflater.inflate(R.layout.list_pro_type_item, null);
//				view.icon=(ImageView)convertView.findViewById(R.id.typeicon);
				view.name=(TextView)convertView.findViewById(R.id.typename);
				convertView.setTag(view);
			}else{
				view=(MyView) convertView.getTag();
			}
//			if(list!=null&&list.size()>0)
//			{
//				type=list.get(position);
//				view.name.setText(type.getName());
////				view.icon.setBackgroundResource(R.drawable.diannao);
//			}
	        return convertView;
		}

		private class MyView{
//			private ImageView icon;
			private TextView name;
		}
		
}
