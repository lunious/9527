package com.lubanjianye.biaoxuntong.ui.main.query.detail;

import android.content.Intent;

import com.lubanjianye.biaoxuntong.base.BaseActivity;
import com.lubanjianye.biaoxuntong.base.BaseFragment;

/**
 * 项目名:   LBBXT
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.fragment.query.detail
 * 文件名:   CompanySgyjListActivity
 * 创建者:   lunious
 * 创建时间: 2017/12/16  0:10
 * 描述:     TODO
 */

public class CompanySgyjListActivity extends BaseActivity {
    private String sfId = "";
    @Override
    public BaseFragment setRootFragment() {
        Intent intent = getIntent();
        if (intent != null) {
            sfId = intent.getStringExtra("sfId");
        }

        final CompanySgyjListFragment fragment = CompanySgyjListFragment.create(sfId);
        return fragment;
    }
}
