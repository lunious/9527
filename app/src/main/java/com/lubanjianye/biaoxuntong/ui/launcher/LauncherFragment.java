package com.lubanjianye.biaoxuntong.ui.launcher;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.base.MainActivity;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;

import pub.devrel.easypermissions.EasyPermissions;

import static com.lubanjianye.biaoxuntong.app.BiaoXunTong.getApplicationContext;


/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.launcher
 * 文件名:   LauncherFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/9  15:59
 * 描述:     TODO
 */

public class LauncherFragment extends BaseFragment implements View.OnClickListener, BDLocationListener {

    private AppCompatTextView atv_timer = null;

    private CountDownTimer mTimer = null;


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


        atv_timer = getView().findViewById(R.id.tv_launcher_timer);
        atv_timer.setOnClickListener(this);
    }

    @Override
    public void initData() {
        mLocationClient.start();
    }

    @Override
    public void initEvent() {
        initTimer();


    }

    private void initTimer() {

        mTimer = new CountDownTimer(2 * 1000, 1000) {

            @SuppressLint("DefaultLocale")
            @Override
            public void onTick(long millisUntilFinished) {

                atv_timer.setText(String.format("%s%s%d%s",
                        "跳过", "(", millisUntilFinished / 1000, ")"));
            }

            @Override
            public void onFinish() {
                checkIsShowScroll();

            }
        }.start();

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_launcher_timer:
                if (mTimer != null) {
                    mTimer.cancel();
                    mTimer = null;
                }
                checkIsShowScroll();
                break;
            default:
        }
    }


    //判断是否显示滑动启动页
    private void checkIsShowScroll() {


        if (!AppSharePreferenceMgr.contains(getContext(), "first_into_app")) {
            getSupportDelegate().start(new LauncherScrollFragment(), SINGLETASK);

            //进入引导页
            Intent intent = new Intent(getActivity(), LauncherScrollActivity.class);
            startActivity(intent);
            if (getActivity() != null) {
                if (mTimer != null) {
                    mTimer.cancel();
                    mTimer = null;
                }
                getActivity().finish();
            }
        } else {
            //进入主页
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            if (getActivity() != null) {
                if (mTimer != null) {
                    mTimer.cancel();
                    mTimer = null;
                }
                getActivity().finish();
            }

        }


    }


    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        String city = bdLocation.getCity();

        Log.d("CITY", "city==" + city);

        AppSharePreferenceMgr.put(getContext(), "Location", city);

    }

}
