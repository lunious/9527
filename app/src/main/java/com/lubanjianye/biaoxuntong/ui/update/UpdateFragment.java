package com.lubanjianye.biaoxuntong.ui.update;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.util.dialog.DialogHelper;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.update
 * 文件名:   UpdateFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/14  19:58
 * 描述:     TODO
 */

public class UpdateFragment extends BaseFragment implements View.OnClickListener,
        EasyPermissions.PermissionCallbacks {

    private static final int RC_EXTERNAL_STORAGE = 0x04;//存储权限

    private AppCompatTextView tvUpdateInfo = null;
    private AppCompatTextView tvUpdateVersionName = null;
    private AppCompatButton button = null;
    private ImageButton close = null;


    static String mUrl = "http://101.37.30.136/bxtajax/VersionAjax/getNewVersion";
    static String versionName = "";
    static String mContent = "";


    private static final String VERSION = "version";
    private static final String CONTENT = "content";

    public static UpdateFragment create(@NonNull String version, String content) {
        final Bundle args = new Bundle();
        args.putString(VERSION, version);
        args.putString(CONTENT, content);
        final UpdateFragment fragment = new UpdateFragment();
        fragment.setArguments(args);
        versionName = version;
        mContent = content;
        return fragment;
    }


    @Override
    public Object setLayout() {
        return R.layout.fragment_update;
    }

    @Override
    public void initView() {
        tvUpdateInfo = getView().findViewById(R.id.tv_update_info);
        tvUpdateVersionName = getView().findViewById(R.id.tv_update_version_name);
        button = getView().findViewById(R.id.btn_update);
        close = getView().findViewById(R.id.btn_close);
        button.setOnClickListener(this);
        close.setOnClickListener(this);


    }

    @Override
    public void initData() {
        getActivity().setTitle("");
        tvUpdateVersionName.setText(versionName);
        tvUpdateInfo.setText(mContent);
        getActivity().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_update:
                requestExternalStorage();
                getActivity().onBackPressed();
                break;
            case R.id.btn_close:
                getActivity().onBackPressed();
                break;
            default:
                break;
        }
    }

    @AfterPermissionGranted(RC_EXTERNAL_STORAGE)
    public void requestExternalStorage() {
        if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            DownloadService.startService(getActivity(), mUrl);
        } else {
            EasyPermissions.requestPermissions(this, "", RC_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        DialogHelper.getConfirmDialog(getActivity(), "温馨提示", "需要开启鲁班标讯通对您手机的存储权限才能下载安装，是否现在开启", "去开启", "取消", true, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_APPLICATION_SETTINGS));
            }
        }, null).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
