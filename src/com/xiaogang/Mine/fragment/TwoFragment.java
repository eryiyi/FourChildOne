package com.xiaogang.Mine.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.*;
import android.widget.*;
import com.videogo.constant.Constant;
import com.videogo.constant.IntentConsts;
import com.videogo.exception.BaseException;
import com.videogo.exception.ErrorCode;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.bean.EZCameraInfo;
import com.videogo.util.ConnectionDetector;
import com.videogo.util.DateTimeUtil;
import com.videogo.util.LogUtil;
import com.videogo.util.Utils;
import com.videogo.widget.TitleBar;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.adpter.EZCameraListAdapter;
import com.xiaogang.Mine.adpter.ItemVideosAdapter;
import com.xiaogang.Mine.base.BaseFragment;
import com.xiaogang.Mine.library.PullToRefreshBase;
import com.xiaogang.Mine.library.PullToRefreshListView;
import com.xiaogang.Mine.library.internal.LoadingLayout;
import com.xiaogang.Mine.mobule.VideosObj;
import com.xiaogang.Mine.ui.EZRealPlayActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 */
public class TwoFragment extends BaseFragment implements View.OnClickListener {


//    private ItemVideosAdapter adapter;
//    List<VideosObj> lists = new ArrayList<>();

    protected static final String TAG = "CameraListActivity";
    /** 删除设备 */
    private final static int SHOW_DIALOG_DEL_DEVICE = 1;

    //private EzvizAPI mEzvizAPI = null;
    private BroadcastReceiver mReceiver = null;

    private PullToRefreshListView mListView = null;
    private View mNoMoreView;
    private EZCameraListAdapter mAdapter = null;

    private LinearLayout mNoCameraTipLy = null;
    private LinearLayout mGetCameraFailTipLy = null;
    private TextView mCameraFailTipTv = null;

    private EZOpenSDK mEZOpenSDK = null;
    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.two_fragment, null);
        initData();
        initView(view);

        Utils.clearAllNotification(getActivity());
        getCameraInfoList(IS_REFRESH);
        return view;
    }

    private void initView(View view) {
        ImageView iv = new ImageView(getActivity());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(5, 5);
        iv.setLayoutParams(lp);
        iv.setImageDrawable(getResources().getDrawable(R.drawable.user));
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                popLogoutDialog();
            }
        });

        mNoMoreView = getActivity().getLayoutInflater().inflate(R.layout.no_device_more_footer, null);

        mAdapter = new EZCameraListAdapter(getActivity());
        mAdapter.setOnClickListener(new EZCameraListAdapter.OnClickListener() {

            @Override
            public void onPlayClick(BaseAdapter adapter, View view, int position) {
                EZCameraInfo cameraInfo = mAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), EZRealPlayActivity.class);
                //Intent intent = new Intent(CameraListActivity.this, SimpleRealPlayActivity.class);
                intent.putExtra(IntentConsts.EXTRA_CAMERA_INFO, cameraInfo);
                startActivity(intent);
            }

            @Override
            public void onRemotePlayBackClick(BaseAdapter adapter, View view, int position) {
//                EZCameraInfo cameraInfo = mAdapter.getItem(position);
//                Intent intent = new Intent(EZCameraListActivity.this, PlayBackListActivity.class);
//                intent.putExtra(RemoteListContant.DEVICESERIAL_INTENT_KEY, cameraInfo.getDeviceSerial());
//                intent.putExtra(RemoteListContant.CHANNELNO_INTENT_KEY, cameraInfo.getChannelNo());
//                intent.putExtra(RemoteListContant.QUERY_DATE_INTENT_KEY, DateTimeUtil.getNow());
//                intent.putExtra("com.vidego.CAMERAID", cameraInfo.getCameraId());
//                intent.putExtra("com.vidego.CAMERAINFO", cameraInfo);
//                intent.putExtra("com.videogo" + ".EXTRA_NETWORK_TIP", "0");
//                startActivity(intent);
            }

            @Override
            public void onSetDeviceClick(BaseAdapter adapter, View view, int position) {
//                EZCameraInfo cameraInfo = mAdapter.getItem(position);
//                Intent intent = new Intent(EZCameraListActivity.this, EZDeviceSettingActivity.class);
//                intent.putExtra(IntentConsts.EXTRA_CAMERA_INFO, cameraInfo);
//                startActivity(intent);
            }

            @Override
            public void onDeleteClick(BaseAdapter adapter, View view, int position) {
//                showDialog(SHOW_DIALOG_DEL_DEVICE);
            }

            @Override
            public void onAlarmListClick(BaseAdapter adapter, View view, int position) {
//                EZCameraInfo cameraInfo = mAdapter.getItem(position);
//                Intent intent = new Intent(EZCameraListActivity.this, EZMessageActivity2.class);
//                intent.putExtra(IntentConsts.EXTRA_CAMERA_INFO, cameraInfo);
//                startActivity(intent);
            }

            @Override
            public void onDevicePictureClick(BaseAdapter adapter, View view, int position) {}

            @Override
            public void onDeviceVideoClick(BaseAdapter adapter, View view, int position) {}

            @Override
            public void onDeviceDefenceClick(BaseAdapter adapter, View view,
                                             int position) {}

        });
        mListView = (PullToRefreshListView) view.findViewById(R.id.camera_listview);
//        mListView.setLoadingLayoutCreator(new LoadingLayoutCreator() {
//
//            @Override
//            public LoadingLayout create(Context context, boolean headerOrFooter, Orientation orientation) {
//                if (headerOrFooter)
//                    return new PullToRefreshHeader(context);
//                else
//                    return new PullToRefreshFooter(context, Style.EMPTY_NO_MORE);
//            }
//        });

        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = true;
                pageIndex = 1;
                getCameraInfoList(IS_REFRESH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = false;
                pageIndex++;
                getCameraInfoList(IS_REFRESH);
            }
        });
        mListView.getRefreshableView().addFooterView(mNoMoreView);
        mListView.setAdapter(mAdapter);
        mListView.getRefreshableView().removeFooterView(mNoMoreView);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });


        mNoCameraTipLy = (LinearLayout) view.findViewById(R.id.no_camera_tip_ly);
        mGetCameraFailTipLy = (LinearLayout) view.findViewById(R.id.get_camera_fail_tip_ly);
        mCameraFailTipTv = (TextView) view.findViewById(R.id.get_camera_list_fail_tv);
    }

    private void initData() {
        mEZOpenSDK = EZOpenSDK.getInstance();
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                LogUtil.debugLog(TAG, "onReceive:" + action);
                if (action.equals(Constant.ADD_DEVICE_SUCCESS_ACTION)) {
                    refreshButtonClicked();
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ADD_DEVICE_SUCCESS_ACTION);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mAdapter != null && mAdapter.getCount() == 0) {
            refreshButtonClicked();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mAdapter != null) {
            mAdapter.shutDownExecutorService();
            mAdapter.clearImageCache();
        }
    }

    /**
     * 从服务器获取最新事件消息
     */
    private void getCameraInfoList(boolean headerOrFooter) {
        if(getActivity().isFinishing()) {
            return;
        }
        new GetCamersInfoListTask(headerOrFooter).execute();
    }

    /**
     * 获取事件消息任务
     */
    private class GetCamersInfoListTask extends AsyncTask<Void, Void, List<EZCameraInfo>> {
        private boolean mHeaderOrFooter;
        private int mErrorCode = 0;

        public GetCamersInfoListTask(boolean headerOrFooter) {
            mHeaderOrFooter = headerOrFooter;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mListView.setFooterRefreshEnabled(true);
            mListView.getRefreshableView().removeFooterView(mNoMoreView);
        }

        @Override
        protected List<EZCameraInfo> doInBackground(Void... params) {
            if(getActivity().isFinishing()) {
                return null;
            }
            if (!ConnectionDetector.isNetworkAvailable(getActivity())) {
                mErrorCode = ErrorCode.ERROR_WEB_NET_EXCEPTION;
                return null;
            }

            try {
                List<EZCameraInfo> result = null;
                if(mHeaderOrFooter) {
                    result = mEZOpenSDK.getCameraList(0, 10);
                } else {
                    result = mEZOpenSDK.getCameraList(mAdapter.getCount()/10, 10);
                }

                return result;

            } catch (BaseException e) {
                mErrorCode = e.getErrorCode();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<EZCameraInfo> result) {
            super.onPostExecute(result);
            mListView.onRefreshComplete();
            if(getActivity().isFinishing()) {
                return;
            }

            if (result != null) {
                if (mHeaderOrFooter) {
                    CharSequence dateText = DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date());
//                    for (LoadingLayout layout : mListView.getLoadingLayoutProxy(true, false).getLayouts()) {
////                        ((PullToRefreshHeader) layout).setLastRefreshTime(":" + dateText);
//                    }
                    mAdapter.clearItem();
                }
                if(mAdapter.getCount() == 0 && result.size() == 0) {
                    mListView.setVisibility(View.GONE);
                    mNoCameraTipLy.setVisibility(View.VISIBLE);
                    mGetCameraFailTipLy.setVisibility(View.GONE);
                    mListView.getRefreshableView().removeFooterView(mNoMoreView);
                } else if(result.size() < 10){
//                    mListView.setFooterRefreshEnabled(false);
                    mListView.getRefreshableView().addFooterView(mNoMoreView);
                } else if(mHeaderOrFooter) {
//                    mListView.setFooterRefreshEnabled(true);
                    mListView.getRefreshableView().removeFooterView(mNoMoreView);
                }
                addCameraList(result);
                mAdapter.notifyDataSetChanged();
            }

            if (mErrorCode != 0) {
                onError(mErrorCode);
            }
        }

        protected void onError(int errorCode) {
            switch (errorCode) {
                case ErrorCode.ERROR_WEB_SESSION_ERROR:
                case ErrorCode.ERROR_WEB_SESSION_EXPIRE:
                case ErrorCode.ERROR_WEB_HARDWARE_SIGNATURE_ERROR:
                    mEZOpenSDK.openLoginPage();
                    break;
                default:
                    if(mAdapter.getCount() == 0) {
                        mListView.setVisibility(View.GONE);
                        mNoCameraTipLy.setVisibility(View.GONE);
                        mCameraFailTipTv.setText(Utils.getErrorTip(getActivity(), R.string.get_camera_list_fail, errorCode));
                        mGetCameraFailTipLy.setVisibility(View.VISIBLE);
                    } else {
                        Utils.showToast(getActivity(), R.string.get_camera_list_fail,  errorCode);
                    }
                    break;
            }
        }
    }

    private void addCameraList(List<EZCameraInfo> result) {
        mAdapter.clearItem();
        int count = result.size();
        EZCameraInfo item = null;
        for (int i = 0; i < count; i++) {
            item = result.get(i);
            mAdapter.addItem(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mReceiver != null) {
            getActivity().unregisterReceiver(mReceiver);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.camera_list_refresh_btn:
            case R.id.no_camera_tip_ly:
                refreshButtonClicked();
                break;
            case R.id.camera_list_gc_ly:
//                Intent intent = new Intent(EZCameraListActivity.this, SquareColumnActivity.class);
//                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 刷新点击
     */
    private void refreshButtonClicked() {
        mListView.setVisibility(View.VISIBLE);
        mNoCameraTipLy.setVisibility(View.GONE);
        mGetCameraFailTipLy.setVisibility(View.GONE);
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        mListView.setRefreshing();
    }

//    @Override
//    public Dialog onCreateDialog(int id) {
//        Dialog dialog = null;
//        switch (id) {
//            case SHOW_DIALOG_DEL_DEVICE:
//                break;
//        }
//        return dialog;
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        menu.add(0, 1, 1, R.string.update_exit).setIcon(R.drawable.exit_selector);
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    protected void onPrepareDialog(int id, Dialog dialog) {
//        if (dialog != null) {
//            removeDialog(id);
//            TextView tv = (TextView) dialog.findViewById(android.R.id.message);
//            tv.setGravity(Gravity.CENTER);
//        }
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {// 得到被点击的item的itemId
            case 1:// 对应的ID就是在add方法中所设定的Id
//                popLogoutDialog();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    /**
//     * 弹出登出对话框
//     *
//     * @see
//     * @since V1.0
//     */
//    private void popLogoutDialog() {
//        AlertDialog.Builder exitDialog = new AlertDialog.Builder(EZCameraListActivity.this);
//        exitDialog.setTitle(R.string.exit);
//        exitDialog.setMessage(R.string.exit_tip);
//        exitDialog.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                new LogoutTask().execute();
//            }
//        });
//        exitDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        exitDialog.show();
//    }

//    private class LogoutTask extends AsyncTask<Void, Void, Void> {
//        private Dialog mWaitDialog;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            mWaitDialog = new WaitDialog(EZCameraListActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
//            mWaitDialog.setCancelable(false);
//            mWaitDialog.show();
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            mEZOpenSDK.logout();
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            mWaitDialog.dismiss();
//            mEZOpenSDK.openLoginPage();
//            finish();
//        }
//    }


}
