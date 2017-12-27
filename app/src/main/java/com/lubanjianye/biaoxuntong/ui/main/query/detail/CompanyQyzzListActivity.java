package com.lubanjianye.biaoxuntong.ui.main.query.detail;

import android.content.Intent;

import com.lubanjianye.biaoxuntong.base.BaseActivity;
import com.lubanjianye.biaoxuntong.base.BaseFragment;

/**
 * 项目名:   9527
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.query.detail
 * 文件名:   CompanyQyzzListActivity
 * 创建者:   lunious
 * 创建时间: 2017/12/26  23:03
 * 描述:     TODO
 */

public class CompanyQyzzListActivity extends BaseActivity {

    private String sfId = "";

    @Override
    public BaseFragment setRootFragment() {
        Intent intent = getIntent();
        if (intent != null) {
            sfId = intent.getStringExtra("sfId");
        }

        final CompanyQyzzListFragment fragment = CompanyQyzzListFragment.create(sfId);
        return fragment;
    }
}
