package com.lubanjianye.biaoxuntong.ui.main.index.detail;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.lubanjianye.biaoxuntong.base.BaseActivity;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.api.BiaoXunTongApi;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

/**
 * 项目名:   Lunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.index.detail
 * 文件名:   IndexSggjyDetailActivity
 * 创建者:   lunious
 * 创建时间: 2017/12/7  15:31
 * 描述:     TODO
 */

public class IndexSggjyDetailActivity extends BaseActivity {

    private int mEntityId = -1;
    private String mEntity = "";
    private String ajaxType = "";
    private String mId = "";

    @Override
    public BaseFragment setRootFragment() {

        Intent intent = getIntent();
        if (intent != null) {
            mEntityId = intent.getIntExtra("entityId", -1);
            mEntity = intent.getStringExtra("entity");
            ajaxType = intent.getStringExtra("ajaxlogtype");
            mId = intent.getStringExtra("mId");
        }

        Log.d("JABNDJBSJDJASDA", mId);
        if (!TextUtils.isEmpty(mId)) {
            OkGo.<String>post(BiaoXunTongApi.URL_GETUITASK)
                    .params("type", 2)
                    .params("id", mId)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {

                        }
                    });
        }

        final IndexSggjyDetailFragment fragment = IndexSggjyDetailFragment.create(mEntityId, mEntity,ajaxType);
        return fragment;
    }
}
