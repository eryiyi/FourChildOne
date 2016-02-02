package com.xiaogang.Mine.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMContactManager;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.activity.AlertDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.UniversityApplication;
import com.xiaogang.Mine.adpter.AnimateFirstDisplayListener;
import com.xiaogang.Mine.base.BaseActivity;
import com.xiaogang.Mine.base.InternetURL;
import com.xiaogang.Mine.data.MemberObjData;
import com.xiaogang.Mine.data.MemberObjDatas;
import com.xiaogang.Mine.mobule.MemberObj;
import com.xiaogang.Mine.util.StringUtil;
import com.xiaogang.Mine.widget.SelectPhoWindow;
import com.xiaogang.Mine.widget.SexPopWindow;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/16.
 */
public class ProfileActivity extends BaseActivity implements View.OnClickListener {
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private SexPopWindow sexPopWindow;
    private SelectPhoWindow deleteWindow;

    private String uid;

    private String pics = "";
    private String sex = "-1";
    private static final File PHOTO_CACHE_DIR = new File(Environment.getExternalStorageDirectory() + "/liangxun/PhotoCache");
    Bitmap photo;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式

    private TextView mine_birth;
    private TextView mine_sex;
    private ImageView mine_head;
    private TextView mine_name;
    private TextView mine_sign;
    private TextView mine_address;
    private String picStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        uid = getIntent().getExtras().getString("uid");

        this.findViewById(R.id.back).setOnClickListener(this);

        mine_sex = (TextView) this.findViewById(R.id.mine_sex);

        mine_birth = (TextView) this.findViewById(R.id.mine_birth);
        mine_head = (ImageView) this.findViewById(R.id.mine_head);


        mine_address = (TextView) this.findViewById(R.id.mine_address);
        mine_sign = (TextView) this.findViewById(R.id.mine_sign);
        mine_name = (TextView) this.findViewById(R.id.mine_name);

        getMemById();




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.liner_sex:
                //性别

                break;
            case R.id.mine_head:
            {
                // //头像

            }
                break;
        }
    }




    public void back(View view){finish();}
    MemberObj memberObj ;
    void getMemById(){
        String uri = InternetURL.GET_MEMBER_BYID_URL+"?access_token="+getGson().fromJson(getSp().getString("access_token", ""), String.class)
                +"&uid=" + uid;
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
                                    MemberObjData data = getGson().fromJson(s, MemberObjData.class);
                                    memberObj = data.getData();
                                    initData(data.getData());
                                }
                                else{
                                    Toast.makeText(ProfileActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(ProfileActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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

    void initData(MemberObj memberObj){
        //

        imageLoader.displayImage(memberObj.getCover(), mine_head, UniversityApplication.options, animateFirstListener);

        mine_birth.setText(memberObj.getBirthday());

        mine_name.setText(memberObj.getNick_name());


        mine_sign.setText(memberObj.getRemark());



        if(!StringUtil.isNullOrEmpty(memberObj.getSex())){
            switch (Integer.parseInt(memberObj.getSex())){
                case -1:
                    mine_sex.setText("未设置");
                    break;
                case 0:
                    mine_sex.setText("男");
                    break;
                case 1:
                    mine_sex.setText("女");
                    break;
                default:
                    mine_sex.setText("未设置");
                    break;
            }
        }

    }

    public void addmine(View view){
        if(memberObj != null){
            addContact(memberObj);
        }

    }

    public void addContact(final MemberObj member){
        if(getGson().fromJson(getSp().getString("hx_id", ""), String.class).equals(member.getHx_id())) {
            //登陆者id和要添加的用户id相同
            String str = getString(R.string.not_add_myself);
            startActivity(new Intent(this, AlertDialog.class).putExtra("msg", str));
            return;
        }

        if(((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList().containsKey(member.getHx_id())){
            //提示已在好友列表中，无需添加
            if(EMContactManager.getInstance().getBlackListUsernames().contains(member.getHx_id())){
                startActivity(new Intent(this, AlertDialog.class).putExtra("msg", "此用户已是你好友(被拉黑状态)，从黑名单列表中移出即可"));
                return;
            }
            String strin = getString(R.string.This_user_is_already_your_friend);
            startActivity(new Intent(this, AlertDialog.class).putExtra("msg", strin));
            return;
        }



        new Thread(new Runnable() {
            public void run() {

                try {
                    //demo写死了个reason，实际应该让用户手动填入
                    String s = getResources().getString(R.string.Add_a_friend);
                    EMContactManager.getInstance().addContact(member.getHx_id(), s);
                    runOnUiThread(new Runnable() {
                        public void run() {

                            String s1 = getResources().getString(R.string.send_successful);
                            Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {

                            String s2 = getResources().getString(R.string.Request_add_buddy_failure);
                            Toast.makeText(getApplicationContext(), s2 + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }



}
