package com.xiaogang.Mine.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.xiaogang.Mine.R;
import com.xiaogang.Mine.adpter.Publish_mood_GridView_Adapter;
import com.xiaogang.Mine.base.BaseActivity;
import com.xiaogang.Mine.base.InternetURL;
import com.xiaogang.Mine.upload.CommonUtil;
import com.xiaogang.Mine.util.*;
import com.xiaogang.Mine.widget.CustomProgressDialog;
import com.xiaogang.Mine.widget.NoScrollGridView;
import com.xiaogang.Mine.widget.PublishPopWindow;
import com.xiaogang.Mine.widget.SelectPhoTwoPopWindow;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: ${zhanghailong}
 * Date: 2015/2/4
 * Time: 19:42
 * 类的功能、说明写在此处.
 */
public class PublishPicActivity extends BaseActivity implements View.OnClickListener {
    private String typeid = "";
    private final static int SELECT_LOCAL_PHOTO = 110;
    private ImageView publis_pic_back;//返回
    private NoScrollGridView publish_moopd_gridview_image;//图片
    private Publish_mood_GridView_Adapter adapter;
    private TextView publish_pic_run;
    private TextView title;
    private ImageView add_pic;//添加图片按钮

    private PublishPopWindow publishPopWindow;

    private ArrayList<String> dataList = new ArrayList<String>();
    private ArrayList<String> tDataList = new ArrayList<String>();
    private List<String> uploadPaths = new ArrayList<String>();

    private static int REQUEST_CODE = 1;

    private Uri uri;
    public EditText et_sendmessage;
    private SelectPhoTwoPopWindow deleteWindow;

    private String id;
    ProgressDialog pd = null;
    private boolean progressShow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_pic_xml);
        typeid = getIntent().getExtras().getString(Constants.SELECT_PHOTOORPIIC);
        initView();
        if ("1".equals(typeid)) {
            openCamera();
        }else if("2".equals(typeid)){
            openPhoto();
        }
        else{
            title.setText("发说说");
        }
    }

    private void initView() {
        publis_pic_back = (ImageView) this.findViewById(R.id.publis_pic_back);
        publis_pic_back.setOnClickListener(this);
        publish_pic_run = (TextView) this.findViewById(R.id.publish_pic_run);
        publish_pic_run.setOnClickListener(this);
        title = (TextView) this.findViewById(R.id.title);
        publish_moopd_gridview_image = (NoScrollGridView) this.findViewById(R.id.publish_moopd_gridview_image);
        adapter = new Publish_mood_GridView_Adapter(this, dataList);
        publish_moopd_gridview_image.setAdapter(adapter);
        publish_moopd_gridview_image.setOnItemClickListener(new GridView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String path = dataList.get(position);
                if (path.contains("camera_default") && position == dataList.size() - 1 && dataList.size() - 1 != 9) {
                    showSelectImageDialog();
                } else {
                    Intent intent = new Intent(PublishPicActivity.this, ImageDelActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("path", dataList.get(position));
                    startActivityForResult(intent, CommonDefine.DELETE_IMAGE);
                }
            }
        });
        add_pic = (ImageView) this.findViewById(R.id.add_pic);
        add_pic.setOnClickListener(this);
        et_sendmessage = (EditText) findViewById(R.id.face_content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.publis_pic_back:
                if (!TextUtils.isEmpty(et_sendmessage.getText().toString().trim())|| dataList.size()!=0) {   //这里trim()作用是去掉首位空格，防止不必要的错误
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et_sendmessage.getWindowToken(), 0); //强制隐藏键盘
                    showSelectPublishDialog();
                } else {
                    finish();
                }
                break;
            case R.id.publish_pic_run:
                uploadPaths.clear();
                if (dataList.size() == 0) {
                    if (StringUtil.isNullOrEmpty(et_sendmessage.getText().toString())) {
                        Toast.makeText(this, R.string.commetn_isnull, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                progressShow = true;
                pd = new ProgressDialog(PublishPicActivity.this);
                pd.setCanceledOnTouchOutside(false);
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        progressShow = false;
                    }
                });
                pd.setMessage(getString(R.string.please_wait));
                pd.show();

                //检查有没有选择图片
                if (dataList.size() == 0) {
                    publishMood();
                    return;
                } else {
                    sendCover();
                }
                break;
            case R.id.add_pic://打开选择框 ，相机还是相册
                showSelectImageDialog();
                break;
        }
    }

    private List<String> listpic = new ArrayList<String>();
    private List<Integer> tmplist = new ArrayList<Integer>();//存储失败的
    private int tmpInt = 0;

    public void sendCover(){
        for (int i = 0; i < dataList.size(); i++) {
            tmpInt = i;
            Bitmap bm = FileUtils.getSmallBitmap(dataList.get(i));
            String cameraImagePath = FileUtils.saveBitToSD(bm, System.currentTimeMillis() + ".jpg");
            File f = new File(cameraImagePath);
            Map<String, File> files = new HashMap<String, File>();
            files.put("file", f);
            Map<String, String> params = new HashMap<String, String>();
            CommonUtil.addPutUploadFileRequest(
                    this,
                    InternetURL.UPLOAD_FILE_URL,    //http://115.29.200.169/api/test/uploadfile
                    files,
                    params,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            if (StringUtil.isJson(s)) {
                                try {
                                    JSONObject jo = new JSONObject(s);
                                    String code1 = jo.getString("code");
                                    if (Integer.parseInt(code1) == 200) {
                                        listpic.add(jo.getString("url"));
                                        if (listpic.size() == (dataList.size() - tmplist.size()) && listpic.size() > 0) {
                                            publishAll();
                                        }
                                    } else {
                                        tmplist.add(tmpInt);//失败的添加到集合当中，记录下来
                                    }
                                } catch (JSONException e) {
                                    tmplist.add(tmpInt);//失败的添加到集合当中，记录下来
                                    if (listpic.size() == (dataList.size() - tmplist.size() - 1) && listpic.size() > 0) {
                                        publishAll();
                                    }
                                    if (pd != null && pd.isShowing()) {
                                        pd.dismiss();
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            tmplist.add(tmpInt);//失败的添加到集合当中，记录下来
                            if (listpic.size() == (dataList.size() - tmplist.size()) && listpic.size() > 0) {
                                publishAll();
                            }
                            if (pd != null && pd.isShowing()) {
                                pd.dismiss();
                            }
                        }
                    },
                    null);
        }
    }



    // 选择是否退出发布
    private void showSelectPublishDialog() {
        publishPopWindow = new PublishPopWindow(PublishPicActivity.this, itemOnClick);
        publishPopWindow.showAtLocation(PublishPicActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            publishPopWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_sure:
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键
            if (!TextUtils.isEmpty(et_sendmessage.getText().toString().trim())|| dataList.size()!=0) {
                showSelectPublishDialog();
                return true;
            } else {
                finish();
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    //发布
    private void publishMood() {
        final String contentStr = et_sendmessage.getText().toString();
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.ADD_RECORD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200){
                                    Toast.makeText(PublishPicActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                                    //调用广播，刷新主页
                                    Intent intent1 = new Intent(Constants.SEND_INDEX_SUCCESS);
                                    sendBroadcast(intent1);
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(PublishPicActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", getGson().fromJson(getSp().getString("uid", ""), String.class));
                params.put("type", "0");
                params.put("content", contentStr);
                params.put("url", "");
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

    //上传完图片后开始发布
    private void publishAll() {
        final String contentStr = et_sendmessage.getText().toString();
        final StringBuffer filePath = new StringBuffer();
        for (int i = 0; i < listpic.size(); i++) {
            filePath.append(listpic.get(i));
            if (i != listpic.size() - 1) {
                filePath.append(",");
            }
        }

        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.ADD_RECORD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200){
                                    Toast.makeText(PublishPicActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                                    //调用广播，刷新主页
                                    Intent intent1 = new Intent(Constants.SEND_INDEX_SUCCESS);
//                                    intent1.putExtra("addRecord", data.getData());
                                    sendBroadcast(intent1);
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                        Toast.makeText(PublishPicActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid",getGson().fromJson(getSp().getString("uid", ""), String.class));
                params.put("type", "1");
                params.put("content", contentStr);
                params.put("url", String.valueOf(filePath));
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

    // 选择相册，相机
    private void showSelectImageDialog() {
        deleteWindow = new SelectPhoTwoPopWindow(PublishPicActivity.this, itemsOnClick);
        //显示窗口
        deleteWindow.showAtLocation(PublishPicActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void openCamera() {
        Intent cameraIntent = new Intent();
        cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.addCategory(Intent.CATEGORY_DEFAULT);
        // 根据文件地址创建文件
        File file = new File(CommonDefine.FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
        uri = Uri.fromFile(file);
        // 设置系统相机拍摄照片完成后图片文件的存放地址
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        // 开启系统拍照的Activity
        startActivityForResult(cameraIntent, CommonDefine.TAKE_PICTURE_FROM_CAMERA);
    }
    private void openPhoto() {
        Intent intent = new Intent(PublishPicActivity.this, AlbumActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("dataList", getIntentArrayList(dataList));
//                    bundle.putString("editContent", et_sendmessage.getText().toString());
        intent.putExtras(bundle);
        startActivityForResult(intent, CommonDefine.TAKE_PICTURE_FROM_GALLERY);
    }

    private ArrayList<String> getIntentArrayList(ArrayList<String> dataList) {

        ArrayList<String> tDataList = new ArrayList<String>();

        for (String s : dataList) {
//            if (!s.contains("camera_default")) {
            tDataList.add(s);
//            }
        }
        return tDataList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case SELECT_LOCAL_PHOTO:
                    tDataList = data.getStringArrayListExtra("datalist");
                    if (tDataList != null) {
                        for (int i = 0; i < tDataList.size(); i++) {
                            String string = tDataList.get(i);
                            dataList.add(string);
                        }
//                        if (dataList.size() < 9) {
//                            dataList.add("camera_default");
//                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        finish();
                    }
                    break;
                case CommonDefine.TAKE_PICTURE_FROM_CAMERA:
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                        return;
                    }
                    Bitmap bitmap = ImageUtils.getUriBitmap(this, uri, 400, 400);
                    String cameraImagePath = FileUtils.saveBitToSD(bitmap, System.currentTimeMillis() + ".jpg");

                    dataList.add(cameraImagePath);
                    adapter.notifyDataSetChanged();
                    break;
                case CommonDefine.TAKE_PICTURE_FROM_GALLERY:
                    tDataList = data.getStringArrayListExtra("datalist");
                    if (tDataList != null) {
                        dataList.clear();
                        for (int i = 0; i < tDataList.size(); i++) {
                            String string = tDataList.get(i);
                            dataList.add(string);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case CommonDefine.DELETE_IMAGE:
                    int position = data.getIntExtra("position", -1);
                    dataList.remove(position);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }


    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            deleteWindow.dismiss();
            switch (v.getId()) {
                case R.id.camera: {
                    Intent cameraIntent = new Intent();
                    cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    // 根据文件地址创建文件
                    File file = new File(CommonDefine.FILE_PATH);
                    if (file.exists()) {
                        file.delete();
                    }
                    uri = Uri.fromFile(file);
                    // 设置系统相机拍摄照片完成后图片文件的存放地址
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                    // 开启系统拍照的Activity
                    startActivityForResult(cameraIntent, CommonDefine.TAKE_PICTURE_FROM_CAMERA);
                }
                break;
                case R.id.mapstorage: {
                    Intent intent = new Intent(PublishPicActivity.this, AlbumActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("dataList", getIntentArrayList(dataList));
//                    bundle.putString("editContent", et_sendmessage.getText().toString());
                    intent.putExtras(bundle);
                    startActivityForResult(intent, CommonDefine.TAKE_PICTURE_FROM_GALLERY);
                }
                break;
                default:
                    break;
            }
        }

    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
