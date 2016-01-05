package com.xiaogang.Mine.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.adpter.Pro_type_adapter;
import com.xiaogang.Mine.mobule.CategoryObj;
import com.xiaogang.Mine.mobule.GoodsTypeBig;
import com.xiaogang.Mine.mobule.GoodsTypeSmall;

import java.util.ArrayList;
import java.util.List;

public class Fragment_pro_type extends Fragment {
	private ImageView hint_img;
	private GridView listView;
	private Pro_type_adapter adapter;
	private ProgressBar progressBar;
	private CategoryObj categoryObj;
	ArrayList<CategoryObj> lists = new ArrayList<>();//这个是所有的类别
	private List<CategoryObj> toolsList = new ArrayList<CategoryObj>();//这个是小类别
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_pro_type, null);
		progressBar=(ProgressBar) view.findViewById(R.id.progressBar);
		hint_img=(ImageView) view.findViewById(R.id.hint_img);
		listView = (GridView) view.findViewById(R.id.listView);
		categoryObj= (CategoryObj) getArguments().getSerializable("goodsTypeBig");
		lists = (ArrayList<CategoryObj>) getArguments().getSerializable("lists");
		if(lists != null){
			for(CategoryObj categoryObj1: lists){
				//取出所有符合该大类别的分类
				if(categoryObj1.getUp_id().equals(categoryObj.getType_id())){
					toolsList.add(categoryObj1);
				}
			}
		}
		((TextView)view.findViewById(R.id.toptype)).setText(categoryObj.getType_name());
		GetTypeList();
		adapter=new Pro_type_adapter(getActivity(), toolsList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				Intent intent = new Intent(getActivity(), SearchGoodsByCategoryActivity.class);
//				GoodsTypeSmall goodsTypeSmall = list.get(arg2);
//				intent.putExtra("category_id", goodsTypeSmall.getId());
//				startActivity(intent);
			}
		});
		listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		return view;
	}
	
	
	private void GetTypeList() {
		progressBar.setVisibility(View.GONE);
	}
	
	
	/*private class LoadTask extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... params) {
			String name[]=new String[]{"shopid","type"};
			String value[]=new String[]{"0","store"};
			return NetworkHandle.requestBypost("app=u_favorite&act=index",name,value);
		}
		
		@Override
		protected void onPostExecute(String result) {	
			progressBar.setVisibility(View.GONE);
			list=new ArrayList<Shop>();
			try {
				if(Constant.isDebug)System.out.println("result:"+result);
				JSONObject ob=new JSONObject(result);
				if(ob.getString("state").equals("1")){
					arrayToList(ob.getJSONArray("list"));
					adapter=new Love_shop_adapter(getActivity(), list,listView);
					listView.setAdapter(adapter);
					listView.onRefreshComplete();
					if(list.size()<20)
						listView.onPullUpRefreshFail();
					if(list.size()==0)hint_img.setVisibility(View.VISIBLE);
					else hint_img.setVisibility(View.GONE);
				}else{
					//if(tradestate.equals("0"))
						//ResultUtils.handle((Activity_order)getActivity(), ob);
				}
			} catch (Exception e) {
			//	if(tradestate.equals("0"))
					//ResultUtils.handle((Activity_order)getActivity(), "");
				e.printStackTrace();
			}	
		}
	}
	
	private void arrayToList(JSONArray array) throws JSONException{
		JSONObject ob;
		for (int i = 0; i < array.length(); i++) {
			ob=array.getJSONObject(i);
			shop=new Shop(ob.getString("shopid"),ob.getString("shopname"), ob.getString("shoplogo"), ob.getString("weixin"), ob.getString("shopurl"));
			list.add(shop);	
		   }
		}
	*/
	
	/*private class LoadTaskMore extends AsyncTask<Void, Void, String>{
		@Override
		protected String doInBackground(Void... params) {
			String name[]=new String[]{"shopid","type"};
			String value[]=new String[]{list.get(list.size()-1).getShopid(),"store"};
			return NetworkHandle.requestBypost("app=u_favorite&act=index",name,value);
		}
		@Override
		protected void onPostExecute(String result) {
			if(Constant.isDebug)System.out.println("result:"+result);
			try {
				JSONObject ob=new JSONObject(result);
				if(ob.getString("state").equals("1")){
					JSONArray array=ob.getJSONArray("list");
					arrayToList(array);
					if(array.length()>0)
						adapter.notifyDataSetChanged();
					if(array.length()<20)
						listView.onPullUpRefreshFail();
					else 
						listView.onPullUpRefreshComplete();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	}*/
	
	
	
}
