package com.xiaogang.Mine.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.base.BaseActivity;
import com.xiaogang.Mine.base.InternetURL;
import com.xiaogang.Mine.mobule.RecordObj;
import com.xiaogang.Mine.util.StringUtil;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/18.
 */
public class CommentRecordActivity extends BaseActivity implements View.OnClickListener{
    private EditText content;

    private String id;
    ProgressDialog pd = null;
    private boolean progressShow;

    private RecordObj info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_record_activity);
        content = (EditText) this.findViewById(R.id.content);
        info = (RecordObj) getIntent().getExtras().get("info");

    }

    @Override
    public void onClick(View v) {

    }

    public void back(View view){finish();}

    public void btn(View view){
        //提交
        if(StringUtil.isNullOrEmpty(content.getText().toString())){
            showMsg(CommentRecordActivity.this, "请输入内容");
            return;
        }
        progressShow = true;
        pd = new ProgressDialog(CommentRecordActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                progressShow = false;
            }
        });
        pd.setMessage(getString(R.string.please_wait));
        pd.show();
        setData();
    }

    private void setData() {
        String uri = InternetURL.ADD_COMMENT_RECORD_URL
//                +"?access_token=" + getGson().fromJson(getSp().getString("access_token", ""), String.class)
                +"?growing_id=" +info.getId()
                +"&uid=" + getGson().fromJson(getSp().getString("uid", ""), String.class)
                +"&content=" +content.getText().toString();
        StringRequest request = new StringRequest(
                Request.Method.GET,
                uri
                ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    //
                                    showMsg(CommentRecordActivity.this, "添加评论成功");
                                    Intent intent1 = new Intent("add_comment_record_success");
                                    sendBroadcast(intent1);
                                    finish();
                                }else {
                                    Toast.makeText(CommentRecordActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(CommentRecordActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(CommentRecordActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
