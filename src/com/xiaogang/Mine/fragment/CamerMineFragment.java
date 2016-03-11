package com.xiaogang.Mine.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.videogo.constant.IntentConsts;
import com.videogo.openapi.bean.EZCameraInfo;
import com.videogo.openapi.bean.resp.CameraInfo;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.UniversityApplication;
import com.xiaogang.Mine.adpter.AnimateFirstDisplayListener;
import com.xiaogang.Mine.adpter.ItemMineCameraAdapter;
import com.xiaogang.Mine.base.BaseFragment;
import com.xiaogang.Mine.base.InternetURL;
import com.xiaogang.Mine.data.CameraObjData;
import com.xiaogang.Mine.data.CanSeeObjData;
import com.xiaogang.Mine.data.IndexData;
import com.xiaogang.Mine.mobule.CameraObj;
import com.xiaogang.Mine.mobule.CameraResult;
import com.xiaogang.Mine.mobule.CanSeeObj;
import com.xiaogang.Mine.mobule.IndexObj;
import com.xiaogang.Mine.ui.SearchTableViewActivity;
import com.xiaogang.Mine.ui.play.EZRealPlayActivity;
import com.xiaogang.Mine.ui.play.SimpleRealPlayActivity;
import com.xiaogang.Mine.util.StringUtil;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhanghailong on 2016/3/6.
 */
public class CamerMineFragment extends BaseFragment implements View.OnClickListener {
    Resources res;
    View view;

    private ListView camera_listview;
    ItemMineCameraAdapter adapter;
    private List<CameraObj> lists = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.camrea_mine_fragment, null);
        res = getActivity().getResources();
        initView();
        getCanSee();
        return view;
    }

    void initView(){
        //
        camera_listview = (ListView) view.findViewById(R.id.camera_listview);
        adapter = new ItemMineCameraAdapter(lists, getActivity());
        camera_listview.setAdapter(adapter);
        camera_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CameraObj cameraObj = lists.get(i);
                EZCameraInfo cameraInfo = new EZCameraInfo();
                Intent intent = new Intent(getActivity(), EZRealPlayActivity.class);
                cameraInfo.setCameraId(cameraObj.getCameraId());
                cameraInfo.setCameraName(cameraObj.getCameraName());
                cameraInfo.setDeviceName(cameraObj.getDeviceName());
                cameraInfo.setDeviceId(cameraObj.getDeviceId());
                cameraInfo.setDeviceSerial(cameraObj.getDeviceSerial());
                cameraInfo.setOnlineStatus(Integer.parseInt(cameraObj.getStatus()==null?"0":cameraObj.getStatus()));
                cameraInfo.setDisplayStatus(0);
                cameraInfo.setShareStatus(Integer.parseInt(cameraObj.getIsShared()==null?"0":cameraObj.getIsShared()));
                cameraInfo.setEncryptStatus(Integer.parseInt((cameraObj.getIsEncrypt()==null?"0":cameraObj.getIsEncrypt())));
                intent.putExtra(IntentConsts.EXTRA_CAMERA_INFO, cameraInfo);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    private void getData() {
        StringRequest request = new StringRequest(
                Request.Method.GET,
                InternetURL.GET_CAMERAS_URL
                        +"?access_token=" + getGson().fromJson(getSp().getString("access_token", ""), String.class),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    CameraObjData data = getGson().fromJson(s, CameraObjData.class);
                                    CameraResult cameraResult = data.getData();
                                    lists.clear();
                                    lists.addAll(cameraResult.getResult().getData());
                                    adapter.notifyDataSetChanged();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "show");
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


    private void getCanSee() {
        StringRequest request = new StringRequest(
                Request.Method.GET,
                InternetURL.CAN_SEE_URL
                        +"?access_token=" + getGson().fromJson(getSp().getString("access_token", ""), String.class),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    CanSeeObjData data = getGson().fromJson(s, CanSeeObjData.class);
                                    CanSeeObj canSeeObj = data.getData();
                                    if("1".equals(canSeeObj.getState())){
                                        //chegngong
                                        getData();
                                    }else {
                                        Toast.makeText(getActivity(),jo.getString("msg") ,Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "show");
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
