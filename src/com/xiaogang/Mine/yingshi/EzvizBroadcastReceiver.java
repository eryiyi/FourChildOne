package com.xiaogang.Mine.yingshi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.videogo.androidpn.AndroidpnUtils;
import com.videogo.androidpn.Constants;
import com.videogo.constant.Constant;
import com.videogo.constant.IntentConsts;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.EzvizAPI;
import com.videogo.openapi.bean.EZAccessToken;
import com.videogo.util.LogUtil;
import com.videogo.util.Utils;
import com.xiaogang.Mine.R;

/**
 * 监听广播
 * 
 * @author fangzhihua
 * @data 2013-1-17
 */
public class EzvizBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "EzvizBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            EzvizAPI.getInstance().refreshNetwork();
        } else if(action.equals(Constant.ADD_DEVICE_SUCCESS_ACTION)) {
            String deviceId = intent.getStringExtra(IntentConsts.EXTRA_DEVICE_ID);
            Utils.showToast(context, context.getString(R.string.device_is_added, deviceId));
        } else if(action.equals(Constant.OAUTH_SUCCESS_ACTION)) {
//
            //保存token及token超时时间
            EZOpenSDK openSdk = EZOpenSDK.getInstance();
            if(openSdk != null) {
                EZAccessToken token = openSdk.getEZAccessToken();
                //保存token，获取超时时间，在token过期时重新获取
                LogUtil.infoLog(TAG, "t:" + token.getAccessToken().substring(0, 5) + " expire:" + token.getExpire());
            }

        } else if (Constants.NOTIFICATION_RECEIVED_ACTION.equals(action)) {
//            NotifierUtils.showNotification(context, intent);
        }
    }
}
