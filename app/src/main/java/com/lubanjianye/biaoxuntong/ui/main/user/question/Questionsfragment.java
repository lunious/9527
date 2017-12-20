package com.lubanjianye.biaoxuntong.ui.main.user.question;

import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;

import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.fragment.user.question
 * 文件名:   Questionsfragment
 * 创建者:   lunious
 * 创建时间: 2017/12/11  22:34
 * 描述:     TODO
 */

public class Questionsfragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout llBack = null;
    private AppCompatTextView mainBarName = null;

    @Override
    public Object setLayout() {
        return R.layout.fragment_question;
    }

    @Override
    public void initView() {
        llBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);
        llBack.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        llBack.setVisibility(View.VISIBLE);
        mainBarName.setText("常见问题");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_iv_back:
                getActivity().onBackPressed();
                break;
            default:
                break;
        }
    }
}
