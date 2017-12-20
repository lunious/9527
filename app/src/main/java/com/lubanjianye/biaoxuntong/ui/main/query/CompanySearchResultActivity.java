package com.lubanjianye.biaoxuntong.ui.main.query;

import android.content.Intent;

import com.lubanjianye.biaoxuntong.base.BaseActivity;
import com.lubanjianye.biaoxuntong.base.BaseFragment;

/**
 * 项目名:   LBBXT
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.fragment.query
 * 文件名:   CompanySearchResultActivity
 * 创建者:   lunious
 * 创建时间: 2017/12/15  23:03
 * 描述:     TODO
 */

public class CompanySearchResultActivity extends BaseActivity {

    private String provinceCode = "";
    private String qyIds = "";

    @Override
    public BaseFragment setRootFragment() {

        Intent intent = getIntent();
        if (intent != null) {
            provinceCode = intent.getStringExtra("provinceCode");
            qyIds = intent.getStringExtra("qyIds");
        }

        final CompanySearchResultFragment fragment = CompanySearchResultFragment.create(provinceCode, qyIds);
        return fragment;

    }
}
