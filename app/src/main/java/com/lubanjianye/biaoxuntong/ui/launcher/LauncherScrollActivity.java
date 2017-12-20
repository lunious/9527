package com.lubanjianye.biaoxuntong.ui.launcher;

import com.lubanjianye.biaoxuntong.base.BaseActivity;
import com.lubanjianye.biaoxuntong.base.BaseFragment;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.launcher
 * 文件名:   LauncherScrollActivity
 * 创建者:   lunious
 * 创建时间: 2017/12/10  22:51
 * 描述:     TODO
 */

public class LauncherScrollActivity extends BaseActivity {
    @Override
    public BaseFragment setRootFragment() {
        return new LauncherScrollFragment();
    }

}
