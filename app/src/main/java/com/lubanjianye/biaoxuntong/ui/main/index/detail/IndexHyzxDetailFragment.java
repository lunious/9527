package com.lubanjianye.biaoxuntong.ui.main.index.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.classic.common.MultipleStatusView;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.util.netStatus.NetUtil;

/**
 * 项目名:   LBBXT
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.index.detail
 * 文件名:   IndexHyzxDetailFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/16  10:35
 * 描述:     TODO
 */

public class IndexHyzxDetailFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout llIvBack = null;
    private AppCompatTextView mainBarName = null;
    private MultipleStatusView hyzxDetailStatusView = null;
    private AppCompatTextView tvMainTitle = null;
    private AppCompatTextView tvMainTime = null;
    private AppCompatTextView tvMainContent = null;
    private NestedScrollView detailNsv = null;


    private static final String ARG_TITLE = "ARG_TITLE";
    private static final String ARG_TIME = "ARG_TIME";
    private static final String ARG_CONTENT = "ARG_CONTENT";


    private String mTItle = "";
    private String mTime = "";
    private String mContent = "";


    @Override
    public Object setLayout() {
        return R.layout.fragment_index_hyzx_detail;
    }


    public static IndexHyzxDetailFragment create(String title, String time, String content) {
        final Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_TIME, time);
        args.putString(ARG_CONTENT, content);
        final IndexHyzxDetailFragment fragment = new IndexHyzxDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle args = getArguments();
        if (args != null) {
            mTItle = args.getString(ARG_TITLE);
            mTime = args.getString(ARG_TIME);
            mContent = args.getString(ARG_CONTENT);
        }
    }

    @Override
    public void initView() {
        llIvBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);
        hyzxDetailStatusView = getView().findViewById(R.id.hyzx_detail_status_view);
        tvMainTitle = getView().findViewById(R.id.tv_main_title);
        tvMainTime = getView().findViewById(R.id.tv_main_time);
        tvMainContent = getView().findViewById(R.id.tv_main_content);
        detailNsv = getView().findViewById(R.id.detail_nsv);

        llIvBack.setOnClickListener(this);

    }

    @Override
    public void initData() {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("标讯详情");
        hyzxDetailStatusView.setOnRetryClickListener(mRetryClickListener);
    }

    @Override
    public void initEvent() {
        requestData();
        initNsv();
    }
    //点击重试
    final View.OnClickListener mRetryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            requestData();
        }
    };

    private void initNsv() {
        detailNsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    // 向下滑动
                    mainBarName.setText(mTItle);
                }

                if (scrollY < oldScrollY) {
                    // 向上滑动
                }

                if (scrollY == 0) {
                    // 顶部
                    mainBarName.setText("标讯详情");
                }

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    // 底部
                    mainBarName.setText(mTItle);
                }
            }
        });
    }


    private void requestData() {

        if (!NetUtil.isNetworkConnected(getActivity())) {
            hyzxDetailStatusView.showNoNetwork();
        } else {

            String title = mTItle;
            if (!TextUtils.isEmpty(title)) {
                tvMainTitle.setText(title);
            } else {
                tvMainTitle.setText("/");
            }
            String create_time = mTime;
            if (create_time != null) {
                tvMainTime.setText(create_time);
            } else {
                tvMainTime.setText("/");
            }

            String mobile_context = mContent;
            if (mobile_context != null) {
                tvMainContent.setText(mobile_context);
            } else {
                tvMainContent.setText("/");
            }

        }


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
