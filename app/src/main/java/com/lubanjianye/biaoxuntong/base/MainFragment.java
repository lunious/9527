package com.lubanjianye.biaoxuntong.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.api.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.app.BiaoXunTong;
import com.lubanjianye.biaoxuntong.bean.Version;
import com.lubanjianye.biaoxuntong.ui.main.index.IndexTabFragment;
import com.lubanjianye.biaoxuntong.ui.main.query.QueryFragment;
import com.lubanjianye.biaoxuntong.ui.main.user.UserTabFragment;
import com.lubanjianye.biaoxuntong.ui.main.result.ResultTabFragment;
import com.lubanjianye.biaoxuntong.ui.main.collection.CollectionTabFragment;
import com.lubanjianye.biaoxuntong.ui.update.UpdateAppBean;
import com.lubanjianye.biaoxuntong.ui.update.UpdateAppManager;
import com.lubanjianye.biaoxuntong.ui.update.UpdateCallback;
import com.lubanjianye.biaoxuntong.ui.update.utils.CProgressDialogUtils;
import com.lubanjianye.biaoxuntong.ui.update.utils.OkGoUpdateHttpUtil;
import com.lubanjianye.biaoxuntong.ui.view.botton.BottomBar;
import com.lubanjianye.biaoxuntong.ui.view.botton.BottomBarTab;
import com.lubanjianye.biaoxuntong.util.appinfo.AppApplicationMgr;
import com.lubanjianye.biaoxuntong.util.dialog.DialogHelper;
import com.lubanjianye.biaoxuntong.util.netStatus.NetUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.app
 * 文件名:   MainFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/9  0:13
 * 描述:     TODO
 */

public class MainFragment extends MainTabFragment implements EasyPermissions.PermissionCallbacks {

    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOUR = 3;
    public static final int FIVE = 4;

    private BaseFragment[] mFragments = new BaseFragment[5];

    private BottomBar mBottomBar;


    @Override
    public Object setLayout() {
        return R.layout.fragment_main;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BaseFragment firstFragment = findChildFragment(IndexTabFragment.class);
        if (firstFragment == null) {
            mFragments[FIRST] = new IndexTabFragment();
            mFragments[SECOND] = new QueryFragment();
            mFragments[THIRD] = new CollectionTabFragment();
            mFragments[FOUR] = new ResultTabFragment();
            mFragments[FIVE] = new UserTabFragment();

            loadMultipleRootFragment(R.id.main_tab_container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FOUR],
                    mFragments[FIVE]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用
            mFragments[FIRST] = firstFragment;
            mFragments[SECOND] = findChildFragment(QueryFragment.class);
            mFragments[THIRD] = findChildFragment(CollectionTabFragment.class);
            mFragments[FOUR] = findChildFragment(ResultTabFragment.class);
            mFragments[FIVE] = findChildFragment(UserTabFragment.class);
        }

    }

    @Override
    public void initView() {
        mBottomBar = getView().findViewById(R.id.bottomBar);
        mBottomBar
                .addItem(new BottomBarTab(_mActivity, R.mipmap.main_index_tab, getString(R.string.first)))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.main_query_tab, getString(R.string.second)))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.main_collection_tab, getString(R.string.third)))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.main_result_tab, getString(R.string.four)))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.main_user_tab, getString(R.string.five)));


        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                showHideFragment(mFragments[position], mFragments[prePosition]);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                // 在FirstPagerFragment,FirstHomeFragment中接收, 因为是嵌套的Fragment
                // 主要为了交互: 重选tab 如果列表不在顶部则移动到顶部,如果已经在顶部,则刷新
            }
        });

        if (NetUtil.isNetworkConnected(getActivity())) {
            updateDiy();
        }
    }

    /**
     * start other BrotherFragment
     */
    public void startBrotherFragment(BaseFragment targetFragment) {
        start(targetFragment);
    }


    private static final int RC_EXTERNAL_STORAGE = 0x04;//存储权限

    private Version mVersion;

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


    @SuppressLint("InlinedApi")
    @AfterPermissionGranted(RC_EXTERNAL_STORAGE)
    public void requestExternalStorage() {
        if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {

        } else {
            EasyPermissions.requestPermissions(this, "", RC_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    /**
     * 自定义接口协议
     */
    public void updateDiy() {

        int versionCode = AppApplicationMgr.getVersionCode(BiaoXunTong.getApplicationContext());

        Map<String, String> params = new HashMap<String, String>();

        params.put("versionCode", String.valueOf(versionCode));


        new UpdateAppManager
                .Builder()
                //必须设置，当前Activity
                .setActivity(getActivity())
                //必须设置，实现httpManager接口的对象
                .setHttpManager(new OkGoUpdateHttpUtil())
                //必须设置，更新地址
                .setUpdateUrl(BiaoXunTongApi.URL_UPDATE)

                //以下设置，都是可选
                //设置请求方式，默认get
                .setPost(true)
                //不显示通知栏进度条
//                .dismissNotificationProgress()
                //是否忽略版本
//                .showIgnoreVersion()
                //添加自定义参数，默认version=1.0.0（app的versionName）；apkKey=唯一表示（在AndroidManifest.xml配置）
                .setParams(params)
                //设置点击升级后，消失对话框，默认点击升级后，对话框显示下载进度
                .hideDialogOnDownloading(false)
                //设置头部，不设置显示默认的图片，设置图片后自动识别主色调，然后为按钮，进度条设置颜色
//                .setTopPic(R.mipmap.top_8)
                //为按钮，进度条设置颜色。
                .setThemeColor(0xffffac5d)
                //设置apk下砸路径，默认是在下载到sd卡下/Download/1.0.0/test.apk
//                .setTargetPath(path)
                //设置appKey，默认从AndroidManifest.xml获取，如果，使用自定义参数，则此项无效
//                .setAppKey("ab55ce55Ac4bcP408cPb8c1Aaeac179c5f6f")

                .build()
                //检测是否有新版本
                .checkNewApp(new UpdateCallback() {
                    /**
                     * 解析json,自定义协议
                     *
                     * @param json 服务器返回的json
                     * @return UpdateAppBean
                     */
                    @Override
                    protected UpdateAppBean parseJson(String json) {
                        final JSONObject object = JSON.parseObject(json);
                        String status = object.getString("status");
                        UpdateAppBean updateAppBean = new UpdateAppBean();

                        if ("200".equals(status)) {
                            final JSONObject data = object.getJSONObject("data");
                            String name = data.getString("name");
                            String content = data.getString("content");
                            String url = data.getString("downloadUrl");


                            String doUrl = "http://openbox.mobilem.360.cn/index/d/sid/3958155";

                            updateAppBean
                                    //（必须）是否更新Yes,No
                                    .setUpdate("Yes")
                                    //（必须）新版本号，
                                    .setNewVersion(name)
                                    //（必须）下载地址
                                    .setApkFileUrl(doUrl)
                                    //（必须）更新内容
                                    .setUpdateLog(content)
                                    //是否强制更新，可以不设置
                                    .setConstraint(false);

                        }
                        return updateAppBean;
                    }

                    @Override
                    protected void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
                        updateAppManager.showDialogFragment();
                    }

                    /**
                     * 网络请求之前
                     */
                    @Override
                    public void onBefore() {
                    }

                    /**
                     * 网路请求之后
                     */
                    @Override
                    public void onAfter() {
                    }

                    /**
                     * 没有新版本
                     */
                    @Override
                    public void noNewApp() {
                    }
                });

    }

}
