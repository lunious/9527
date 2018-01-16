package com.lubanjianye.biaoxuntong.ui.update;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lubanjianye.biaoxuntong.app.BiaoXunTong;
import com.lubanjianye.biaoxuntong.bean.Version;
import com.lubanjianye.biaoxuntong.api.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.util.appinfo.AppApplicationMgr;
import com.lubanjianye.biaoxuntong.util.dialog.DialogHelper;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.update
 * 文件名:   CheckUpdateManager
 * 创建者:   lunious
 * 创建时间: 2017/12/14  19:15
 * 描述:     TODO
 */

public class CheckUpdateManager {

    private ProgressDialog mWaitDialog;
    private Context mContext;
    private boolean mIsShowDialog;
    private RequestPermissions mCaller;

    public CheckUpdateManager(Context context, boolean showWaitingDialog) {
        this.mContext = context;
        mIsShowDialog = showWaitingDialog;
        if (mIsShowDialog) {
            mWaitDialog = DialogHelper.getProgressDialog(mContext);
            mWaitDialog.setMessage("正在检查中...");
            mWaitDialog.setCancelable(false);
            mWaitDialog.setCanceledOnTouchOutside(false);
        }
    }

    public void checkUpdate() {

        if (mIsShowDialog) {
            mWaitDialog.show();
        }

        int versionCode = AppApplicationMgr.getVersionCode(BiaoXunTong.getApplicationContext());

        Log.d("JHBHJASBDASDAS", versionCode + "");

        OkGo.<String>post(BiaoXunTongApi.URL_UPDATE)
                .params("versionCode", versionCode)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final JSONObject object = JSON.parseObject(response.body());
                        String status = object.getString("status");

                        if ("200".equals(status)) {
                            final JSONObject data = object.getJSONObject("data");
                            String name = data.getString("name");
                            String content = data.getString("content");
                            UpdateActivity.show((Activity) mContext, name, content);

                        } else if ("201".equals(status)) {
                            if (mIsShowDialog) {
                                DialogHelper.getMessageDialog(mContext, "已经是最新版本了").show();
                            }
                        } else if ("500".equals(status)) {
                            DialogHelper.getMessageDialog(mContext, "网络异常，无法获取新版本信息").show();
                        }

                        if (mIsShowDialog) {
                            mWaitDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        DialogHelper.getMessageDialog(mContext, "网络异常，无法获取新版本信息").show();
                        if (mIsShowDialog) {
                            mWaitDialog.dismiss();
                        }
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        if (mIsShowDialog) {
                            mWaitDialog.dismiss();
                        }
                        super.onFinish();
                    }
                });

    }

    public void setCaller(RequestPermissions caller) {
        this.mCaller = caller;
    }

    public interface RequestPermissions {
        void call(Version version);
    }
}

