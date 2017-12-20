package com.lubanjianye.biaoxuntong.ui.browser;

import android.content.Intent;

import com.lubanjianye.biaoxuntong.base.BaseActivity;
import com.lubanjianye.biaoxuntong.base.BaseFragment;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.browser
 * 文件名:   BrowserActivity
 * 创建者:   lunious
 * 创建时间: 2017/12/11  12:58
 * 描述:     TODO
 */

public class BrowserActivity extends BaseActivity {

    private String mUrl = "";
    private String mTitle = "";

    @Override
    public BaseFragment setRootFragment() {
        Intent intent = getIntent();
        if (intent != null) {
            mUrl = intent.getStringExtra("url");
            mTitle = intent.getStringExtra("title");
        }

        final BrowserFragment fragment = BrowserFragment.create(mUrl, mTitle);
        return fragment;
    }

}
