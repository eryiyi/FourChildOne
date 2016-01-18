package com.xiaogang.Mine.ui;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaogang.Mine.MainActivity;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.base.ActivityTack;
import com.xiaogang.Mine.base.BaseActivity;
import com.xiaogang.Mine.base.InternetURL;
import com.xiaogang.Mine.upload.CommonUtil;
import com.xiaogang.Mine.util.Constants;
import com.xiaogang.Mine.util.StringUtil;
import com.xiaogang.Mine.widget.PublishPopWindow;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuzwei on 2014/11/24.
 * <p/>
 * 发布视频
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class PublishVideoActivity extends BaseActivity implements  View.OnClickListener, MediaPlayer.OnCompletionListener, TextureView.SurfaceTextureListener {
    private ImageView back;
    private TextView publish;
    private String path;

    public static final int VIDEO_CODE = 112;

    private PublishPopWindow publishPopWindow;

    private String videoUrl = "";


    //播放视频部分
    private TextureView surfaceView;
    private MediaPlayer mediaPlayer;
    private ImageView imagePlay;

    private EditText face_content;
    private String id;
    ProgressDialog pd = null;
    private boolean progressShow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_video_xml);
        initView();
        //跳转到录制页面
        Init_View();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        surfaceView = (TextureView) findViewById(R.id.preview_video);

        RelativeLayout preview_video_parent = (RelativeLayout) findViewById(R.id.preview_video_parent);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) preview_video_parent
                .getLayoutParams();
        layoutParams.width = displaymetrics.widthPixels;
        layoutParams.height = displaymetrics.widthPixels;
        preview_video_parent.setLayoutParams(layoutParams);

        surfaceView.setSurfaceTextureListener(this);
        surfaceView.setOnClickListener(this);

        path = getIntent().getStringExtra("path");

        imagePlay = (ImageView) findViewById(R.id.previre_play);
        imagePlay.setOnClickListener(this);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);

    }

    private void initView() {
        back = (ImageView) findViewById(R.id.publis_video_back);
        publish = (TextView) findViewById(R.id.publish_video_run);
        back.setOnClickListener(this);
        publish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.previre_play://播放视频
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
                imagePlay.setVisibility(View.GONE);
                break;
            case R.id.preview_video://暂停
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    imagePlay.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.publis_video_back://返回
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(face_content.getWindowToken(), 0); //强制隐藏键盘
                    showSelectImageDialog();
                break;
            case R.id.publish_video_run://发布
                progressShow = true;
                pd = new ProgressDialog(PublishVideoActivity.this);
                pd.setCanceledOnTouchOutside(false);
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        progressShow = false;
                    }
                });
                pd.setMessage(getString(R.string.please_wait));
                pd.show();
                uploadFile();
                break;
        }
    }

    // 选择是否退出发布
    private void showSelectImageDialog() {
        publishPopWindow = new PublishPopWindow(PublishVideoActivity.this, itemsOnClick);
        publishPopWindow.showAtLocation(PublishVideoActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

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
        if(keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键
            showSelectImageDialog();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void uploadFile(){
        if (StringUtil.isNullOrEmpty(path)) {
            Toast.makeText(this, R.string.video_error, Toast.LENGTH_SHORT).show();
            return;
        }
//            Bitmap bm = FileUtils.getSmallBitmap(path);
//            String cameraImagePath = FileUtils.saveBitToSD(bm, System.currentTimeMillis() + ".jpg");
            File f = new File(path);
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
                                        videoUrl = jo.getString("url");
                                        publishRun(videoUrl);
                                    }
                                } catch (JSONException e) {
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

                            if (pd != null && pd.isShowing()) {
                                pd.dismiss();
                            }
                        }
                    },
                    null);
    }


    private void publishRun(final String key) {
        final String contentStr = face_content.getText().toString();
        String uri = InternetURL.ADD_RECORD_URL + "?uid=" + getGson().fromJson(getSp().getString("uid", ""), String.class)
                +"&type=" +"2"
                +"&content=" + contentStr
                +"&url="+ key
                ;
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
                                    Toast.makeText(PublishVideoActivity.this, "发布视频成功", Toast.LENGTH_SHORT).show();
                                    //调用广播，刷新主页
                                    Intent intent1 = new Intent(Constants.SEND_INDEX_SUCCESS);
                                    sendBroadcast(intent1);
                                    ActivityTack.getInstanse().popUntilActivity(MainActivity.class);
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
                        Toast.makeText(PublishVideoActivity.this, "发布视频失败", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid" ,getGson().fromJson(getSp().getString("uid", ""), String.class));
                params.put("type", "2");
                params.put("content", contentStr);
                params.put("url", key);
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
     * 初始化控件
     */
    private void Init_View() {
        face_content = (EditText) this.findViewById(R.id.face_content);

    }

    //视频部分
    @Override
    protected void onStop() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        }
        super.onStop();
    }

    private void prepare(Surface surface) {
        try {
            if (!StringUtil.isNullOrEmpty(path) && mediaPlayer != null) {
                mediaPlayer.reset();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                // 设置需要播放的视频
                mediaPlayer.setDataSource(path);
                // 把视频画面输出到Surface
                mediaPlayer.setSurface(surface);
                mediaPlayer.setLooping(true);
                mediaPlayer.prepare();
                mediaPlayer.seekTo(0);
            }

        } catch (Exception e) {
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture arg0, int arg1,
                                          int arg2) {
        prepare(new Surface(arg0));
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture arg0) {
        return false;
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture arg0, int arg1,
                                            int arg2) {

    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture arg0) {

    }

    private void stop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        ActivityTack.getInstanse().popUntilActivity(MainActivity.class);
    }

    @Override
    public void onBackPressed() {
        stop();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

}
