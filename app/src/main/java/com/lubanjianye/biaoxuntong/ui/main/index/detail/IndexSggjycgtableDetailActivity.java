package com.lubanjianye.biaoxuntong.ui.main.index.detail;

import android.content.Intent;

import com.lubanjianye.biaoxuntong.base.BaseActivity;
import com.lubanjianye.biaoxuntong.base.BaseFragment;

/**
 * 项目名:   Lunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.index.detail
 * 文件名:   IndexSggjycgtableDetailActivity
 * 创建者:   lunious
 * 创建时间: 2017/12/7  15:27
 * 描述:     TODO
 */

public class IndexSggjycgtableDetailActivity extends BaseActivity {
    private int mEntityId = -1;
    private String mEntity = "";

    @Override
    public BaseFragment setRootFragment() {

        Intent intent = getIntent();
        if (intent != null) {
            mEntityId = intent.getIntExtra("entityId", -1);
            mEntity = intent.getStringExtra("entity");
        }

        final IndexSggjycgtableDetailFragment fragment = IndexSggjycgtableDetailFragment.create(mEntityId, mEntity);
        return fragment;
    }
}
