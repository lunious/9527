package com.lubanjianye.biaoxuntong.ui.main.query.detail;

import android.content.Intent;

import com.lubanjianye.biaoxuntong.base.BaseActivity;
import com.lubanjianye.biaoxuntong.base.BaseFragment;

/**
 * 项目名:   LBBXT
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.fragment.query.detail
 * 文件名:   CompanyDetailActivity
 * 创建者:   lunious
 * 创建时间: 2017/12/15  23:38
 * 描述:     TODO
 */

public class CompanyDetailActivity extends BaseActivity {

    private String sfId = "";


    @Override
    public BaseFragment setRootFragment() {

        Intent intent = getIntent();
        if (intent != null) {
            sfId = intent.getStringExtra("sfId");
        }

        final CompanyDetailFragment fragment = CompanyDetailFragment.create(sfId);
        return fragment;

    }
}
