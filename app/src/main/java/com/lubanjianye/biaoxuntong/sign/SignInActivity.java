package com.lubanjianye.biaoxuntong.sign;

import com.lubanjianye.biaoxuntong.base.BaseActivity;
import com.lubanjianye.biaoxuntong.base.BaseFragment;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.sign
 * 文件名:   SignInActivity
 * 创建者:   lunious
 * 创建时间: 2017/12/13  0:40
 * 描述:     TODO
 */

public class SignInActivity extends BaseActivity {
    @Override
    public BaseFragment setRootFragment() {
        return new SignInFragment();
    }
}
