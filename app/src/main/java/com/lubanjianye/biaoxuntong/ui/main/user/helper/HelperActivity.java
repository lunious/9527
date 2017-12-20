package com.lubanjianye.biaoxuntong.ui.main.user.helper;

import com.lubanjianye.biaoxuntong.base.BaseActivity;
import com.lubanjianye.biaoxuntong.base.BaseFragment;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.fragment.user.helper
 * 文件名:   HelperActivity
 * 创建者:   lunious
 * 创建时间: 2017/12/11  23:04
 * 描述:     TODO
 */

public class HelperActivity extends BaseActivity {
    @Override
    public BaseFragment setRootFragment() {
        return new Helperfragment();
    }

}
