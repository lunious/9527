package com.lubanjianye.biaoxuntong.ui.launcher;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.api.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.base.MainActivity;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.sign.SignInActivity;
import com.lubanjianye.biaoxuntong.util.dialog.PromptButton;
import com.lubanjianye.biaoxuntong.util.dialog.PromptButtonListener;
import com.lubanjianye.biaoxuntong.util.dialog.PromptDialog;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;


import org.greenrobot.eventbus.EventBus;

import java.util.List;

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
    private long userId = 0;
    private String token = null;


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

        //如果登录，检查token是否有效
        if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
            //得到用个户userId
            List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
            for (int i = 0; i < users.size(); i++) {
                userId = users.get(0).getId();
                token = users.get(0).getToken();
            }
            OkGo.<String>post(BiaoXunTongApi.URL_CHECKTOKEN)
                    .params("userId", userId)
                    .params("token", token)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            if ("200".equals(response.body()) || "400".equals(response.body())) {
                                //token有效
                                checkIsShowScroll();
                            } else {
                                EventBus.getDefault().post(new EventMessage(EventMessage.TOKEN_FALSE));

                                //清除登录信息
                                DatabaseManager.getInstance().getDao().deleteAll();
                                AppSharePreferenceMgr.remove(getContext(), EventMessage.LOGIN_SUCCSS);
                                AppSharePreferenceMgr.put(getContext(), EventMessage.TOKEN_FALSE, true);
                                checkIsShowScroll();

                            }
                        }

                        @Override
                        public void onError(Response<String> response) {
                            checkIsShowScroll();
                        }
                    });

        } else {
            checkIsShowScroll();
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
