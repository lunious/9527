package com.lubanjianye.biaoxuntong.ui.main.result.detail;

import android.content.Intent;

import com.lubanjianye.biaoxuntong.base.BaseActivity;
import com.lubanjianye.biaoxuntong.base.BaseFragment;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.fragment.result.detail
 * 文件名:   ResultXjgggDetailActivity
 * 创建者:   lunious
 * 创建时间: 2017/12/12  20:48
 * 描述:     TODO
 */

public class ResultXjgggDetailActivity extends BaseActivity {

    private int mEntityId = -1;
    private String mEntity = "";

    @Override
    public BaseFragment setRootFragment() {
        Intent intent = getIntent();
        if (intent != null) {
            mEntityId = intent.getIntExtra("entityId", -1);
            mEntity = intent.getStringExtra("entity");
        }

        final ResultXjgggDetailFragment fragment = ResultXjgggDetailFragment.create(mEntityId, mEntity);
        return fragment;
    }

}
