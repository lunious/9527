package com.lubanjianye.biaoxuntong.base;


public class MainActivity extends BaseActivity {

    @Override
    public BaseFragment setRootFragment() {
        setSwipeBackEnable(false);
        return new MainFragment();
    }

}
