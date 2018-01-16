package com.lubanjianye.biaoxuntong.ui.main.user.setting;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.bean.Version;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.api.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.ui.update.CheckUpdateManager;
import com.lubanjianye.biaoxuntong.ui.update.DownloadService;
import com.lubanjianye.biaoxuntong.util.cache.AppCleanMgr;
import com.lubanjianye.biaoxuntong.util.dialog.DialogHelper;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.fragment.user.setting
 * 文件名:   Settingfragment
 * 创建者:   lunious
 * 创建时间: 2017/12/11  23:13
 * 描述:     TODO
 */

public class Settingfragment extends BaseFragment implements View.OnClickListener, EasyPermissions.PermissionCallbacks, CheckUpdateManager.RequestPermissions {

    private LinearLayout llBack = null;
    private AppCompatTextView mainBarName = null;
    private AppCompatTextView llCancel = null;
    private AppCompatTextView tvCacheSize = null;
    private LinearLayout llUpdate = null;
    private LinearLayout llCacheSize = null;
    private AppCompatTextView tvUpdate = null;

    private Version mVersion;

    //存储权限
    private static final int RC_EXTERNAL_STORAGE = 0x04;

    @Override
    public Object setLayout() {
        return R.layout.fragment_setting;
    }

    @Override
    public void initView() {
        llBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);
        llCancel = getView().findViewById(R.id.ll_cancel);
        tvCacheSize = getView().findViewById(R.id.tv_cache_size);
        llUpdate = getView().findViewById(R.id.ll_update);
        llCacheSize = getView().findViewById(R.id.ll_cache_size);
        tvUpdate = getView().findViewById(R.id.tv_update);

        llBack.setOnClickListener(this);
        llCancel.setOnClickListener(this);
        llUpdate.setOnClickListener(this);
        llCacheSize.setOnClickListener(this);

    }

    @Override
    public void initData() {
        llBack.setVisibility(View.VISIBLE);
        mainBarName.setText("系统设置");
        String cacheSize = AppCleanMgr.getAppClearSize(getContext());
        tvCacheSize.setText(cacheSize);

        if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
            llCancel.setVisibility(View.VISIBLE);
        } else {
            llCancel.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_iv_back:
                getActivity().onBackPressed();
                break;
            case R.id.ll_cache_size:
                DialogHelper.getConfirmDialog(getActivity(), "是否清空缓存?", new DialogInterface.OnClickListener
                        () {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AppCleanMgr.cleanInternalCache(getContext());
                        tvCacheSize.setText("");
                        ToastUtil.shortBottonToast(getContext(), "缓存清理成功");
                    }
                }).show();
                break;
            case R.id.ll_cancel:

                DialogHelper.getConfirmDialog(getActivity(), "你确定要退出登录吗？", new DialogInterface.OnClickListener
                        () {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
                        long id = 0;
                        for (int j = 0; j < users.size(); j++) {
                            id = users.get(0).getId();
                        }
                        OkGo.<String>post(BiaoXunTongApi.URL_LOGOUT)
                                .params("id", id)
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        if (llCacheSize != null) {
                                            llCancel.setVisibility(View.INVISIBLE);
                                        }
                                        DatabaseManager.getInstance().getDao().deleteAll();
                                        AppSharePreferenceMgr.remove(getContext(), EventMessage.LOGIN_SUCCSS);
                                        EventBus.getDefault().post(new EventMessage(EventMessage.LOGIN_OUT));
                                        ToastUtil.shortBottonToast(getContext(), "退出成功");
                                    }
                                });
                    }
                }).show();

                break;
            case R.id.ll_update:
                //更新界面
                onClickUpdate();
                break;
            default:
                break;
        }
    }

    //检查更新
    private void onClickUpdate() {
        CheckUpdateManager manager = new CheckUpdateManager(getActivity(), true);
        manager.setCaller(this);
        manager.checkUpdate();
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        DialogHelper.getConfirmDialog(getActivity(), "温馨提示", "需要开启鲁班标讯通对您手机的存储权限才能下载安装，是否现在开启", "去开启", "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_APPLICATION_SETTINGS));
            }
        }).show();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void call(Version version) {
        this.mVersion = version;
        requestExternalStorage();
    }

    @SuppressLint("InlinedApi")
    @AfterPermissionGranted(RC_EXTERNAL_STORAGE)
    public void requestExternalStorage() {
        if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            DownloadService.startService(getActivity(), mVersion.getData());
        } else {
            EasyPermissions.requestPermissions(this, "", RC_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

}
