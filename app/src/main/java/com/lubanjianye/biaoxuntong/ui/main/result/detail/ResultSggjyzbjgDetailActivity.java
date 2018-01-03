package com.lubanjianye.biaoxuntong.ui.main.result.detail;

import android.content.Intent;

import com.lubanjianye.biaoxuntong.base.BaseActivity;
import com.lubanjianye.biaoxuntong.base.BaseFragment;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.fragment.result.detail
 * 文件名:   ResultSggjyzbjgDetailActivity
 * 创建者:   lunious
 * 创建时间: 2017/12/12  21:28
 * 描述:     TODO
 */

public class ResultSggjyzbjgDetailActivity extends BaseActivity {

    private int mEntityId = -1;
    private String mEntity = "";
    private String ajaxType = "";


    @Override
    public BaseFragment setRootFragment() {
        Intent intent = getIntent();
        if (intent != null) {
            mEntityId = intent.getIntExtra("entityId", -1);
            mEntity = intent.getStringExtra("entity");
            ajaxType = intent.getStringExtra("ajaxlogtype");
        }

        final ResultSggjyzbjgDetailFragment fragment = ResultSggjyzbjgDetailFragment.create(mEntityId, mEntity,ajaxType);
        return fragment;
    }

}
