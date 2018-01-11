package com.lubanjianye.biaoxuntong.ui.launcher;

import android.content.Intent;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.base.MainActivity;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;


import static com.lubanjianye.biaoxuntong.app.BiaoXunTong.getApplicationContext;


/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.launcher
 * 文件名:   LauncherFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/9  15:59
 * 描述:     TODO
 */

public class LauncherFragment extends BaseFragment implements BDLocationListener {


    public LocationClient mLocationClient = null;


    @Override
    public Object setLayout() {
        return R.layout.fragment_launcher;
    }

    @Override
    public void initView() {

        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(this);
        //注册监听函数
        LocationClientOption option = new LocationClientOption();

        option.setIsNeedAddress(true);
        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true

        mLocationClient.setLocOption(option);

    }

    @Override
    public void initData() {
        mLocationClient.start();
    }

    @Override
    public void initEvent() {

        checkIsShowScroll();


    }


    //判断是否显示滑动启动页
    private void checkIsShowScroll() {

        if (!AppSharePreferenceMgr.contains(getContext(), "first_into_app")) {
            getSupportDelegate().start(new LauncherScrollFragment(), SINGLETASK);

            //进入引导页
            Intent intent = new Intent(getActivity(), LauncherScrollActivity.class);
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        } else {
            //进入主页
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }

        }


    }


    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        String city = bdLocation.getCity();
        Log.d("BDASBDASDA", city);

    }

}
