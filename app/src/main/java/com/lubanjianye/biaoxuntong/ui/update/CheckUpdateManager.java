package com.lubanjianye.biaoxuntong.ui.update;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lubanjianye.biaoxuntong.app.BiaoXunTong;
import com.lubanjianye.biaoxuntong.bean.Version;
import com.lubanjianye.biaoxuntong.net.RestClient;
import com.lubanjianye.biaoxuntong.net.api.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.net.callback.IFailure;
import com.lubanjianye.biaoxuntong.net.callback.IRequest;
import com.lubanjianye.biaoxuntong.net.callback.ISuccess;
import com.lubanjianye.biaoxuntong.util.appinfo.AppApplicationMgr;
import com.lubanjianye.biaoxuntong.util.dialog.DialogHelper;

import okhttp3.Headers;

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

        RestClient.builder()
                .url(BiaoXunTongApi.URL_UPDATE)
                .params("versionCode", versionCode)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(Headers headers, String response) {
                        final JSONObject object = JSON.parseObject(response);
                        String status = object.getString("status");

                        if ("200".equals(status)) {
                            final JSONObject data = object.getJSONObject("data");
                            String name = data.getString("name");
                            String content = data.getString("content");
//                            UpdateActivity.show((Activity) mContext, name, content);
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
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {
                        DialogHelper.getMessageDialog(mContext, "网络异常，无法获取新版本信息").show();

                        if (mIsShowDialog) {
                            mWaitDialog.dismiss();
                        }
                    }


                })
                .onRequest(new IRequest() {
                    @Override
                    public void onRequestStart() {

                    }

                    @Override
                    public void onRequestEnd() {
                        if (mIsShowDialog) {
                            mWaitDialog.dismiss();
                        }
                    }
                })
                .build()
                .post();

    }

    public void setCaller(RequestPermissions caller) {
        this.mCaller = caller;
    }

    public interface RequestPermissions {
        void call(Version version);
    }
}

