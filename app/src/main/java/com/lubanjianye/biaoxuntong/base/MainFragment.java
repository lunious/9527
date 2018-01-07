package com.lubanjianye.biaoxuntong.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.bean.Version;
import com.lubanjianye.biaoxuntong.ui.main.index.IndexTabFragment;
import com.lubanjianye.biaoxuntong.ui.main.query.QueryFragment;
import com.lubanjianye.biaoxuntong.ui.main.user.UserTabFragment;
import com.lubanjianye.biaoxuntong.ui.main.result.ResultTabFragment;
import com.lubanjianye.biaoxuntong.ui.main.collection.CollectionTabFragment;
import com.lubanjianye.biaoxuntong.ui.update.CheckUpdateManager;
import com.lubanjianye.biaoxuntong.ui.update.DownloadService;
import com.lubanjianye.biaoxuntong.ui.view.botton.BottomBar;
import com.lubanjianye.biaoxuntong.ui.view.botton.BottomBarTab;
import com.lubanjianye.biaoxuntong.util.dialog.DialogHelper;
import com.lubanjianye.biaoxuntong.util.netStatus.NetUtil;
import java.util.List;
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

public class MainFragment extends MainTabFragment implements EasyPermissions.PermissionCallbacks, CheckUpdateManager.RequestPermissions {

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

            onClickUpdate();
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

    //检查更新
    private void onClickUpdate() {

        CheckUpdateManager manager = new CheckUpdateManager(getActivity(), false);
        manager.setCaller(this);
        manager.checkUpdate();
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
