package com.lubanjianye.biaoxuntong.ui.launcher;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.base.MainActivity;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;


/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.launcher
 * 文件名:   LauncherFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/9  15:59
 * 描述:     TODO
 */

public class LauncherFragment extends BaseFragment implements View.OnClickListener {

    private AppCompatTextView atv_timer = null;

    private CountDownTimer mTimer = null;

    private ILauncherListener mILauncherListener = null;


    @Override
    public Object setLayout() {
        return R.layout.fragment_launcher;
    }

    @Override
    public void initView() {
        atv_timer = getView().findViewById(R.id.tv_launcher_timer);
        atv_timer.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        initTimer();
    }

    private void initTimer() {

        mTimer = new CountDownTimer(5 * 1000, 1000) {

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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ILauncherListener) {
            mILauncherListener = (ILauncherListener) activity;
        }
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


}
