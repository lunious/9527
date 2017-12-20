package com.lubanjianye.biaoxuntong.ui.main.result;


import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.fragment
 * 文件名:   IndexTabFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/9  0:33
 * 描述:     TODO
 */

public class ResultTabFragment extends BaseFragment {

    private AppCompatTextView mainBarName = null;
    private SlidingTabLayout resulttStlTab = null;
    private ViewPager resultVp = null;

    private final List<String> mList = new ArrayList<String>();
    private ResultFragmentAdapter mAdapter;

    @Override
    public Object setLayout() {
        return R.layout.fragment_main_result;
    }

    @Override
    public void initView() {
        mainBarName = getView().findViewById(R.id.main_bar_name);
        resulttStlTab = getView().findViewById(R.id.resultt_stl_tab);
        resultVp = getView().findViewById(R.id.result_vp);
    }

    @Override
    public void initData() {
        mainBarName.setVisibility(View.VISIBLE);
        mainBarName.setText("招标结果");
        mList.add("工程招标中标公示");
        mList.add("政府采购结果公告");
    }

    @Override
    public void initEvent() {
        mAdapter = new ResultFragmentAdapter(mList, getFragmentManager());
        resultVp.setAdapter(mAdapter);
        resulttStlTab.setViewPager(resultVp);

    }


}
