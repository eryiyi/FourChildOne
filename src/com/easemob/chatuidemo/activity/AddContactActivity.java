/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easemob.chatuidemo.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMContactManager;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.adpter.ItemAddContactAdapter;
import com.xiaogang.Mine.adpter.OnClickContentItemListener;
import com.xiaogang.Mine.base.BaseActivity;
import com.xiaogang.Mine.base.InternetURL;
import com.xiaogang.Mine.data.MemberObjDatas;
import com.xiaogang.Mine.mobule.MemberObj;
import com.xiaogang.Mine.ui.ProfileActivity;
import com.xiaogang.Mine.util.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddContactActivity extends BaseActivity implements OnClickContentItemListener {
	private EditText editText;
	private TextView nameText,mTextView;
	private Button searchBtn;
	private InputMethodManager inputMethodManager;

	private ProgressDialog progressDialog;
	List<MemberObj> lists = new ArrayList<MemberObj>();
	private ItemAddContactAdapter adapter;
	private ListView lstv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		mTextView = (TextView) findViewById(R.id.add_list_friends);

		editText = (EditText) findViewById(R.id.edit_note);
		String strAdd = getResources().getString(R.string.add_friend);
		mTextView.setText(strAdd);
		String strUserName = getResources().getString(R.string.user_name);
		editText.setHint(strUserName);
		nameText = (TextView) findViewById(R.id.name);
		searchBtn = (Button) findViewById(R.id.search);
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		adapter = new ItemAddContactAdapter(lists, AddContactActivity.this);
		lstv = (ListView) this.findViewById(R.id.lstv);
		lstv.setAdapter(adapter);
		adapter.setOnClickContentItemListener(this);
	}


	/**
	 * 查找contact
	 * @param v
	 */
	public void searchContact(View v) {
		final String name = editText.getText().toString();
		String saveText = searchBtn.getText().toString();

		if (getString(R.string.button_search).equals(saveText)) {
//			toAddUsername = name;
			if(TextUtils.isEmpty(name)) {
				String st = getResources().getString(R.string.Please_enter_a_username);
				startActivity(new Intent(this, AlertDialog.class).putExtra("msg", st));
				return;
			}

			// TODO 从服务器获取此contact,如果不存在提示不存在此用户
			getdata(name);
			//服务器存在此用户，显示此用户和添加按钮
//			searchedUserLayout.setVisibility(View.VISIBLE);
//			nameText.setText(toAddUsername);

		}
	}



	void getdata(final String name){
		String uri = InternetURL.FIND_MEMBER_URL+"?access_token="+getGson().fromJson(getSp().getString("access_token", ""), String.class)
				+"&name=" + name;
		StringRequest request = new StringRequest(
				Request.Method.GET,
				uri,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						if (StringUtil.isJson(s)) {
							try {
								JSONObject jo = new JSONObject(s);
								String code =  jo.getString("code");
								if(Integer.parseInt(code) == 200){
									MemberObjDatas data = getGson().fromJson(s, MemberObjDatas.class);
									lists.clear();
									lists.addAll(data.getData());
									adapter.notifyDataSetChanged();
								}
								else{
									Toast.makeText(AddContactActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						if (progressDialog != null) {
							progressDialog.dismiss();
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						if (progressDialog != null) {
							progressDialog.dismiss();
						}
						Toast.makeText(AddContactActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
					}
				}
		) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("access_token", getGson().fromJson(getSp().getString("access_token", ""), String.class));
				params.put("keyword", name);
				return params;
			}

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("Content-Type", "application/x-www-form-urlencoded");
				return params;
			}
		};
		getRequestQueue().add(request);
	}

	/**
	 *  添加contact
	 */
	public void addContact(final MemberObj member){
		if(getGson().fromJson(getSp().getString("hx_id", ""), String.class).equals(member.getHx_id())) {
			//登陆者id和要添加的用户id相同
			String str = getString(R.string.not_add_myself);
			startActivity(new Intent(this, AlertDialog.class).putExtra("msg", str));
			return;
		}

		if(((DemoHXSDKHelper)HXSDKHelper.getInstance()).getContactList().containsKey(member.getHx_id())){
			//提示已在好友列表中，无需添加
			if(EMContactManager.getInstance().getBlackListUsernames().contains(member.getHx_id())){
				startActivity(new Intent(this, AlertDialog.class).putExtra("msg", "此用户已是你好友(被拉黑状态)，从黑名单列表中移出即可"));
				return;
			}
			String strin = getString(R.string.This_user_is_already_your_friend);
			startActivity(new Intent(this, AlertDialog.class).putExtra("msg", strin));
			return;
		}

		progressDialog = new ProgressDialog(this);
		String stri = getResources().getString(R.string.Is_sending_a_request);
		progressDialog.setMessage(stri);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		AddFriends(member.getUser_name());
//		new Thread(new Runnable() {
//			public void run() {
//
//				try {
//					//demo写死了个reason，实际应该让用户手动填入
//					String s = getResources().getString(R.string.Add_a_friend);
//					EMContactManager.getInstance().addContact(member.getUser_name(), s);
//					runOnUiThread(new Runnable() {
//						public void run() {
//							progressDialog.dismiss();
//							String s1 = getResources().getString(R.string.send_successful);
//							Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_SHORT).show();
//						}
//					});
//				} catch (final Exception e) {
//					runOnUiThread(new Runnable() {
//						public void run() {
//							progressDialog.dismiss();
//							String s2 = getResources().getString(R.string.Request_add_buddy_failure);
//							Toast.makeText(getApplicationContext(), s2 + e.getMessage(), Toast.LENGTH_SHORT).show();
//						}
//					});
//				}
//			}
//		}).start();
	}

	public void back(View v) {
		finish();
	}

	@Override
	public void onClickContentItem(int position, int flag, Object object) {
		switch (flag){
			case 1:
				MemberObj member = lists.get(position);
				if(member != null){
					addContact(member);
				}
				break;
			case 2:MemberObj member1 = lists.get(position);
				if(member1 != null){
					Intent intent = new Intent(AddContactActivity.this, ProfileActivity.class);
					intent.putExtra("uid", member1.getUid() );
					startActivity(intent);
				}
				break;
		}
	}



	void AddFriends(final String username){
		String uri = InternetURL.ADD_FRIENDS_URL
				+"?access_token="+getGson().fromJson(getSp().getString("access_token", ""), String.class)
				+"&username=" + username;
		StringRequest request = new StringRequest(
				Request.Method.GET,
				uri,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						if (StringUtil.isJson(s)) {
							try {
								JSONObject jo = new JSONObject(s);
								String code =  jo.getString("code");
								if(Integer.parseInt(code) == 200){
									Toast.makeText(AddContactActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
								}
								else{
									Toast.makeText(AddContactActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						if (progressDialog != null) {
							progressDialog.dismiss();
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						if (progressDialog != null) {
							progressDialog.dismiss();
						}
						Toast.makeText(AddContactActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
					}
				}
		) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				return params;
			}

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("Content-Type", "application/x-www-form-urlencoded");
				return params;
			}
		};
		getRequestQueue().add(request);
	}



}