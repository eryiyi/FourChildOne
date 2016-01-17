package com.xiaogang.Mine.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.UniversityApplication;
import com.xiaogang.Mine.adpter.AnimateFirstDisplayListener;
import com.xiaogang.Mine.base.BaseFragment;
import com.xiaogang.Mine.base.InternetURL;
import com.xiaogang.Mine.data.MemberObjData;
import com.xiaogang.Mine.mobule.MemberObj;
import com.xiaogang.Mine.ui.*;
import com.xiaogang.Mine.util.DataCleanManager;
import com.xiaogang.Mine.util.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class FiveFragment extends BaseFragment implements View.OnClickListener {
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageView mine_head;
    private TextView mine_nickname;
    private TextView mine_age;
    private TextView mine_sign;
    private TextView huncun;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBoradcastReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.five_fragment, null);
        try {
            initView(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getMember();
        return view;
    }

    private void initView(View view) throws Exception {
        view.findViewById(R.id.mine_video_comment).setOnClickListener(this);
        view.findViewById(R.id.mine_shebei).setOnClickListener(this);
        view.findViewById(R.id.mine_guanzhu).setOnClickListener(this);
        view.findViewById(R.id.mine_huancun).setOnClickListener(this);
        view.findViewById(R.id.mine_anquan).setOnClickListener(this);
        view.findViewById(R.id.mine_address).setOnClickListener(this);
        view.findViewById(R.id.relate_one).setOnClickListener(this);
        view.findViewById(R.id.checknews).setOnClickListener(this);

        mine_head = (ImageView) view.findViewById(R.id.mine_head);
        mine_nickname = (TextView) view.findViewById(R.id.mine_nickname);
        mine_age = (TextView) view.findViewById(R.id.mine_age);
        mine_sign = (TextView) view.findViewById(R.id.mine_sign);
        huncun = (TextView) view.findViewById(R.id.huncun);

        TextView version = (TextView) view.findViewById(R.id.version);
        version.setText("我的版本:"+ (getVersionName()==null?"V1.0":getVersionName()));

    }



    void initData(){
        //
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("nick_name", ""), String.class))){
            mine_nickname.setText(getGson().fromJson(getSp().getString("nick_name", ""), String.class));
        }else {
            mine_nickname.setText("未设置昵称");
        }
        mine_age.setText(getGson().fromJson(getSp().getString("birthday", ""), String.class));
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("remark", ""), String.class))){
            mine_sign.setText(getGson().fromJson(getSp().getString("remark", ""), String.class));
        }else {
            mine_sign.setText("暂无签名");
        }

        imageLoader.displayImage(getGson().fromJson(getSp().getString("cover", ""), String.class), mine_head, UniversityApplication.txOptions, animateFirstListener);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_video_comment:
                //视频评论
                Intent mineViedoComment = new Intent(getActivity(), VideoCommentActivity.class);
                startActivity(mineViedoComment);
                break;
            case R.id.mine_shebei:
                Intent mineshebei = new Intent(getActivity(), MineShebeiActivity.class);
                startActivity(mineshebei);
                break;
            case R.id.mine_guanzhu:
                Intent minegz = new Intent(getActivity(), MineGuanzhuActivity.class);
                startActivity(minegz);
                break;
            case R.id.mine_huancun:
                //清除临时文件
                //缓存
                DataCleanManager.cleanInternalCache(getActivity());
                getpkginfo("com.xiaogang.Mine");
                Toast.makeText(getActivity(), "清除成功！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mine_anquan:
                Intent anquanView = new Intent(getActivity(), MineAnquanActivity.class);
                startActivity(anquanView);
                break;
            case R.id.mine_address:
                Intent addressV = new Intent(getActivity(), MineAddressActivity.class);
                startActivity(addressV);
                break;
            case R.id.relate_one:
            {
                //更新信息
                Intent updateV = new Intent(getActivity(), UpdateProfileActivity.class);
                startActivity(updateV);
            }
                break;
            case R.id.checknews:
//                Resources res = getBaseContext().getResources();
//                String message = res.getString(R.string.check_new_version).toString();
//                progressDialog = new ProgressDialog(SetActivity.this);
//                progressDialog.setMessage(message);
//                progressDialog.show();
//
//                UmengUpdateAgent.forceUpdate(this);
//
//                UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
//                    @Override
//                    public void onUpdateReturned(int i, UpdateResponse updateResponse) {
//                        progressDialog.dismiss();
//                        switch (i) {
//                            case UpdateStatus.Yes:
////                                Toast.makeText(mContext, "有新版本发现", Toast.LENGTH_SHORT).show();
//                                break;
//                            case UpdateStatus.No:
//                                Toast.makeText(SetActivity.this, "已是最新版本", Toast.LENGTH_SHORT).show();
//                                break;
//                            case UpdateStatus.Timeout:
//                                Toast.makeText(SetActivity.this, "连接超时", Toast.LENGTH_SHORT).show();
//                                break;
//                        }
//                    }
//                });
                break;
        }
    }


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String infoString="";
                    PackageStats newPs = msg.getData().getParcelable(ATTR_PACKAGE_STATS);
                    if (newPs!=null) {
                        infoString+= formatFileSize(newPs.cacheSize);
                    }
                    huncun.setText(infoString);
                    break;
                default:
                    break;
            }
        }
    };

    private static final String ATTR_PACKAGE_STATS="PackageStats";


    public void getpkginfo(String pkg){
        PackageManager pm = getActivity().getPackageManager();
        try {
            Method getPackageSizeInfo = pm.getClass().getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
            getPackageSizeInfo.invoke(pm, pkg,new PkgSizeObserver());
        } catch (Exception e) {
        }
    }
    class PkgSizeObserver extends IPackageStatsObserver.Stub {
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) {
            Message msg = mHandler.obtainMessage(1);
            Bundle data = new Bundle();
            data.putParcelable(ATTR_PACKAGE_STATS, pStats);
            msg.setData(data);
            mHandler.sendMessage(msg);

        }
    }

    /**
     * 获取文件大小
     *
     * @param length
     * @return
     */
    public static String formatFileSize(long length) {
        String result = null;
        int sub_string = 0;
        if (length >= 1073741824) {
            sub_string = String.valueOf((float) length / 1073741824).indexOf(
                    ".");
            result = ((float) length / 1073741824 + "000").substring(0,
                    sub_string + 3)
                    + "GB";
        } else if (length >= 1048576) {
            sub_string = String.valueOf((float) length / 1048576).indexOf(".");
            result = ((float) length / 1048576 + "000").substring(0,
                    sub_string + 3)
                    + "MB";
        } else if (length >= 1024) {
            sub_string = String.valueOf((float) length / 1024).indexOf(".");
            result = ((float) length / 1024 + "000").substring(0,
                    sub_string + 3)
                    + "KB";
        } else if (length < 1024)
            result = Long.toString(length) + "B";
        return result;
    }

    private MemberObj memberObj;

    void getMember(){
        String uri = InternetURL.GET_MEMBER_URL
                +"?access_token=" + getGson().fromJson(getSp().getString("access_token", ""), String.class);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                uri ,
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
                                    save("sex", memberObj.getSex());
                                    save("user_name", memberObj.getUser_name());
                                    save("birthday", memberObj.getBirthday());
                                    save("remark", memberObj.getRemark());
                                    save("cover", memberObj.getCover());
                                    initData();
                                }
                                else{
                                    Toast.makeText(getActivity(), jo.getString("msg"), Toast.LENGTH_SHORT).show();
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


    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("updateSuccess")) {
                //编辑信息成功
                getMember();
            }
        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("updateSuccess");
        //注册广播
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    public String getVersionName() throws Exception
    {
        // 获取packagemanager的实例
        PackageManager packageManager = getActivity().getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getActivity().getPackageName(),0);
        String version = packInfo.versionName;
        return version;
    }
}
