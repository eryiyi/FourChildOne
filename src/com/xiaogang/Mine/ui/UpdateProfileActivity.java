package com.xiaogang.Mine.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
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
import com.xiaogang.Mine.base.BaseActivity;
import com.xiaogang.Mine.base.InternetURL;
import com.xiaogang.Mine.upload.CommonUtil;
import com.xiaogang.Mine.util.CompressPhotoUtil;
import com.xiaogang.Mine.util.FileUtils;
import com.xiaogang.Mine.util.StringUtil;
import com.xiaogang.Mine.util.TimeUtils;
import com.xiaogang.Mine.widget.DateTimePickDialogUtil;
import com.xiaogang.Mine.widget.SelectPhoWindow;
import com.xiaogang.Mine.widget.SexPopWindow;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/16.
 */
public class UpdateProfileActivity extends BaseActivity implements View.OnClickListener {
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private SexPopWindow sexPopWindow;
    private SelectPhoWindow deleteWindow;

    private String pics = "";
    private String sex = "-1";
    private static final File PHOTO_CACHE_DIR = new File(Environment.getExternalStorageDirectory() + "/liangxun/PhotoCache");
    Bitmap photo;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式

    private TextView mine_birth;
    private TextView mine_sex;
    private ImageView mine_head;
    private EditText mine_name;
    private EditText mine_sign;
    private EditText mine_address;
    private String picStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile_activity);

        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.liner_sex).setOnClickListener(this);
        this.findViewById(R.id.liner_birth).setOnClickListener(this);
        mine_sex = (TextView) this.findViewById(R.id.mine_sex);
        mine_sex.setOnClickListener(this);
        mine_birth = (TextView) this.findViewById(R.id.mine_birth);
        mine_head = (ImageView) this.findViewById(R.id.mine_head);
        mine_head.setOnClickListener(this);

        mine_address = (EditText) this.findViewById(R.id.mine_address);
        mine_sign = (EditText) this.findViewById(R.id.mine_sign);
        mine_name = (EditText) this.findViewById(R.id.mine_name);

        imageLoader.displayImage(getGson().fromJson(getSp().getString("cover", ""), String.class), mine_head, UniversityApplication.options, animateFirstListener);
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("birthday", ""), String.class))){
            mine_birth.setText(getGson().fromJson(getSp().getString("birthday", ""), String.class));
        }

        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("nick_name", ""), String.class))){
            mine_name.setText(getGson().fromJson(getSp().getString("nick_name", ""), String.class));
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("remark", ""), String.class))){
            mine_sign.setText(getGson().fromJson(getSp().getString("remark", ""), String.class));
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("address", ""), String.class))){
            mine_address.setText(getGson().fromJson(getSp().getString("address", ""), String.class));
        }

        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("sex", ""), String.class))){
            switch (Integer.parseInt(getGson().fromJson(getSp().getString("sex", ""), String.class))){
                case -1:
                    mine_sex.setText("未设置");
                    break;
                case 0:
                    mine_sex.setText("男");
                    break;
                case 1:
                    mine_sex.setText("女");
                    break;
            }
        }

        mine_birth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                DateTimePickDialogUtil dateTimePicKDialog = null;
                try {
                    dateTimePicKDialog = new DateTimePickDialogUtil(
                            UpdateProfileActivity.this,  StringUtil.getFrontBackStrDate(String.valueOf(mine_birth.getText().toString()), "yyyy-MM-dd",0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dateTimePicKDialog.dateTimePicKDialog(mine_birth);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.liner_sex:
                //性别
                ShowSexDialog();
                break;
            case R.id.mine_head:
            {
                // //头像
                ShowPickDialog();
            }
                break;
        }
    }
    // 性别选择
    private void ShowSexDialog() {
        sexPopWindow = new SexPopWindow(UpdateProfileActivity.this, itemsOnClick);
        //显示窗口
        sexPopWindow.showAtLocation(UpdateProfileActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            sexPopWindow.dismiss();
            switch (v.getId()) {
                case R.id.sex_man: {
                    sex = "0";
                    mine_sex.setText("男");
                }
                break;
                case R.id.sex_woman: {
                    sex = "1";
                    mine_sex.setText("女");
                }
                break;
                default:
                    break;
            }
        }
    };

    public void addSave (View view) throws ParseException {
        //
        if(StringUtil.isNullOrEmpty(mine_name.getText().toString())){
            showMsg(UpdateProfileActivity.this, "请输入昵称");
            return;
        }
        if(!StringUtil.isNullOrEmpty(pics)){
            //有头像
            sendCover();
        }else {
            //没有头像
            saveNoPic();
        }
    }


    public void sendCover(){
            Bitmap bm = FileUtils.getSmallBitmap(pics);
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
                                        picStr = jo.getString("url");
                                        saveNoPic();
                                    }
                                } catch (JSONException e) {

                                    if (progressDialog != null) {
                                        progressDialog.dismiss();
                                    }
                                    e.printStackTrace();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
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
                    },
                    null);

    }

    void saveNoPic() throws ParseException {
        String uri = InternetURL.UPDATE_MEMBER_URL
                +"?access_token="+ getGson().fromJson(getSp().getString("access_token", ""), String.class)
                ;
        String userStr = mine_name.getText().toString() ;
        try {
            userStr = URLEncoder.encode(userStr, "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
       if(!StringUtil.isNullOrEmpty(userStr)){
           uri += "&nick_name="+userStr;
       }
        if(!StringUtil.isNullOrEmpty(picStr)){
            uri += "&cover="+picStr;
        }

        if(!StringUtil.isNullOrEmpty(sex)){
            uri += "&sex="+sex;
        }
        if(!StringUtil.isNullOrEmpty(mine_birth.getText().toString())){
            try {
                uri += "&birthday="+TimeUtils.getCurrentMillion(mine_birth.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        String remarkstr = mine_sign.getText().toString() ;
        try {
            remarkstr = URLEncoder.encode(remarkstr, "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!StringUtil.isNullOrEmpty(remarkstr)){
            uri += "&remark="+remarkstr;
        }
        String addressstr = mine_address.getText().toString() ;
        try {
            addressstr = URLEncoder.encode(addressstr, "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!StringUtil.isNullOrEmpty(addressstr)){
            uri += "&address="+addressstr;
        }

        if(!StringUtil.isNullOrEmpty(sex)){
            uri +="&sex=" +sex;
        }
        if(!StringUtil.isNullOrEmpty(mine_birth.getText().toString())){
            uri +="&birthday=" + mine_birth.getText().toString();
        }
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
                                    Toast.makeText(UpdateProfileActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                                    if(!StringUtil.isNullOrEmpty(picStr)){
                                        save("cover", picStr);
                                    }
                                    if(!StringUtil.isNullOrEmpty(sex)){
                                        save("sex", sex);
                                    }
                                    save("birthday", mine_birth.getText().toString());
                                    save("remark", mine_sign.getText().toString());
                                    save("address", mine_address.getText().toString());
                                    save("nick_name", mine_name.getText().toString());
                                    Intent intent = new Intent("updateSuccess");
                                    sendBroadcast(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(UpdateProfileActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(UpdateProfileActivity.this, "获得数据失败", Toast.LENGTH_SHORT).show();
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

    public void back(View view){finish();}

    // 选择相册，相机
    private void ShowPickDialog() {
        deleteWindow = new SelectPhoWindow(UpdateProfileActivity.this, itemsOnClickPic);
        //显示窗口
        deleteWindow.showAtLocation(UpdateProfileActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClickPic = new View.OnClickListener() {

        public void onClick(View v) {
            deleteWindow.dismiss();
            switch (v.getId()) {
                case R.id.camera: {
                    Intent camera = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    //下面这句指定调用相机拍照后的照片存储的路径
                    camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                            .fromFile(new File(Environment
                                    .getExternalStorageDirectory(),
                                    "ppCover.jpg")));
                    startActivityForResult(camera, 2);
                }
                break;
                case R.id.mapstorage: {
                    Intent mapstorage = new Intent(Intent.ACTION_PICK, null);
                    mapstorage.setDataAndType(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            "image/*");
                    startActivityForResult(mapstorage, 1);
                }
                break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 如果是直接从相册获取
            case 1:
                if (data != null) {
                    startPhotoZoom(data.getData());
                }
                break;
            // 如果是调用相机拍照时
            case 2:
                File temp = new File(Environment.getExternalStorageDirectory()
                        + "/ppCover.jpg");
                startPhotoZoom(Uri.fromFile(temp));
                break;
            // 取得裁剪后的图片
            case 3:
                if (data != null) {
                    setPicToView(data);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            if (photo != null) {
                pics = CompressPhotoUtil.saveBitmap2file(photo, System.currentTimeMillis() + ".jpg", PHOTO_CACHE_DIR);
                if(!StringUtil.isNullOrEmpty(pics)){
//                    sendCover();
                    mine_head.setImageBitmap(photo);
                }
            }
        }
    }
}
