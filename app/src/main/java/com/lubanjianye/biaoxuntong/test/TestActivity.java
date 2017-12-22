package com.lubanjianye.biaoxuntong.test;

import com.lubanjianye.biaoxuntong.base.BaseActivity;
import com.lubanjianye.biaoxuntong.base.BaseFragment;

/**
 * 项目名:   9527
 * 包名:     com.lubanjianye.biaoxuntong.test
 * 文件名:   TestActivity
 * 创建者:   lunious
 * 创建时间: 2017/12/22  9:04
 * 描述:     TODO
 */

public class TestActivity extends BaseActivity {
    @Override
    public BaseFragment setRootFragment() {
        return new TestFragment();
    }
}
