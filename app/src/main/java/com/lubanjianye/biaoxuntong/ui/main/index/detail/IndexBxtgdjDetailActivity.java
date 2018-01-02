package com.lubanjianye.biaoxuntong.ui.main.index.detail;

import android.content.Intent;
import com.lubanjianye.biaoxuntong.base.BaseActivity;
import com.lubanjianye.biaoxuntong.base.BaseFragment;

/**
 * 项目名:   Lunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.index.detail
 * 文件名:   IndexBxtgdjDetailActivity
 * 创建者:   lunious
 * 创建时间: 2017/12/7  15:12
 * 描述:     TODO
 */

public class IndexBxtgdjDetailActivity extends BaseActivity {

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

        final IndexBxtgdjDetailFragment fragment = IndexBxtgdjDetailFragment.create(mEntityId, mEntity,ajaxType);
        return fragment;
    }
}
