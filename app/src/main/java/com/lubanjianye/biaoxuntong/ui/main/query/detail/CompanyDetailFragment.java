package com.lubanjianye.biaoxuntong.ui.main.query.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.classic.common.MultipleStatusView;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.bean.CompanyQyzzListBean;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.loadmore.CustomLoadMoreView;
import com.lubanjianye.biaoxuntong.net.RestClient;
import com.lubanjianye.biaoxuntong.net.api.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.net.callback.ISuccess;
import com.lubanjianye.biaoxuntong.ui.main.user.company.MyCompanyQyzzAllListActivity;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

/**
 * 项目名:   LBBXT
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.fragment.query.detail
 * 文件名:   CompanyDetailFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/15  23:37
 * 描述:     TODO
 */

public class CompanyDetailFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout llIvBack = null;
    private AppCompatTextView mainBarName = null;
    private AppCompatTextView tvMainTitle = null;
    private AppCompatTextView tvFaren = null;
    private AppCompatTextView tvZcData = null;
    private AppCompatTextView tvZcType = null;
    private AppCompatTextView tvJyArea = null;
    private AppCompatTextView tvPuNum = null;
    private MultipleStatusView companyDetailStatusView = null;
    private RelativeLayout llRyzz = null;
    private RelativeLayout llSgyj = null;
    private RecyclerView companyQyzzRecycler = null;

    private LinearLayout llMoreQyzz = null;
    private AppCompatTextView qyzzCount = null;


    private CompanyQyzzListAdapter mAdapter;
    private ArrayList<CompanyQyzzListBean> mDataList = new ArrayList<>();

    private static final String ARG_SFID = "ARG_SFID";
    private String sfId = "";

    @Override
    public Object setLayout() {
        return R.layout.fragment_company_detail;
    }

    public static CompanyDetailFragment create(@NonNull String entity) {
        final Bundle args = new Bundle();
        args.putString(ARG_SFID, entity);
        final CompanyDetailFragment fragment = new CompanyDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            sfId = args.getString(ARG_SFID);
        }
    }


    @Override
    public void initView() {
        llIvBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);
        tvMainTitle = getView().findViewById(R.id.tv_main_title);
        tvFaren = getView().findViewById(R.id.tv_faren);
        tvZcData = getView().findViewById(R.id.tv_zc_data);
        tvZcType = getView().findViewById(R.id.tv_zc_type);
        tvJyArea = getView().findViewById(R.id.tv_jy_area);
        tvPuNum = getView().findViewById(R.id.tv_pu_num);
        companyDetailStatusView = getView().findViewById(R.id.company_detail_status_view);
        llRyzz = getView().findViewById(R.id.ll_ryzz);
        llSgyj = getView().findViewById(R.id.ll_sgyj);
        companyQyzzRecycler = getView().findViewById(R.id.company_qyzz_recycler);

        llMoreQyzz = getView().findViewById(R.id.ll_more_qyzz);
        qyzzCount = getView().findViewById(R.id.qyzz_count);
        llMoreQyzz.setOnClickListener(this);
        llIvBack.setOnClickListener(this);
        llRyzz.setOnClickListener(this);
        llSgyj.setOnClickListener(this);

        companyQyzzRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new CompanyQyzzListAdapter(R.layout.fragment_company_qyzz_list, mDataList);
        //设置列表动画
//        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        companyQyzzRecycler.setAdapter(mAdapter);


    }

    @Override
    public void initData() {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("公司详情");
        companyDetailStatusView.setOnRetryClickListener(mRetryClickListener);
    }

    @Override
    public void initEvent() {
        requestData();
    }

    //点击重试
    final View.OnClickListener mRetryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            requestData();
        }
    };

    private long id = 0;
    private String token = "";

    public void requestData() {

        companyDetailStatusView.showLoading();
        List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();

        for (int i = 0; i < users.size(); i++) {
            id = users.get(0).getId();
            token = users.get(0).getToken();
        }

        RestClient.builder()
                .url(BiaoXunTongApi.URL_SUITRESULTDETAIl + sfId)
                .params("token", token)
                .params("userId", id)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(Headers headers, String response) {
                        final JSONObject object = JSON.parseObject(response);
                        String status = object.getString("status");
                        if ("200".equals(status)) {
                            companyDetailStatusView.showContent();
                            final JSONObject data = object.getJSONObject("data");
                            String qy = data.getString("qy");
                            if (!TextUtils.isEmpty(qy)) {
                                tvMainTitle.setText(qy);
                            } else {
                                tvMainTitle.setText("/");
                            }
                            String fr = data.getString("fr");
                            if (!TextUtils.isEmpty(fr)) {
                                tvFaren.setText(fr);
                            } else {
                                tvFaren.setText("/");
                            }
                            String zcd = data.getString("zcd");
                            if (!TextUtils.isEmpty(zcd)) {
                                tvZcData.setText(zcd);
                            } else {
                                tvZcData.setText("/");
                            }
                            String zclx = data.getString("zclx");
                            if (!TextUtils.isEmpty(zclx)) {
                                tvZcType.setText(zclx);
                            } else {
                                tvZcType.setText("/");
                            }
                            String dz = data.getString("dz");
                            if (!TextUtils.isEmpty(dz)) {
                                tvJyArea.setText(dz);
                            } else {
                                tvJyArea.setText("/");
                            }
                            String tyshxydm = data.getString("tyshxydm");
                            if (!TextUtils.isEmpty(tyshxydm)) {
                                tvPuNum.setText(tyshxydm);
                            } else {
                                tvPuNum.setText("/");
                            }

                            requestQyzzData();
                        }

                    }
                })
                .build()
                .post();

    }

    @Override
    public void onClick(View view) {

        Intent intent;
        switch (view.getId()) {
            case R.id.ll_iv_back:
                getActivity().onBackPressed();
                break;
            case R.id.ll_ryzz:
                intent = new Intent(getActivity(), CompanyRyzzListActivity.class);
                intent.putExtra("sfId", sfId);
                startActivity(intent);
                break;
            case R.id.ll_sgyj:
                intent = new Intent(getActivity(), CompanySgyjListActivity.class);
                intent.putExtra("sfId", sfId);
                startActivity(intent);
                break;
            case R.id.ll_more_qyzz:
                intent = new Intent(getActivity(), CompanyQyzzListActivity.class);
                intent.putExtra("sfId", sfId);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    public void requestQyzzData() {

        List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
        long id = 0;
        for (int i = 0; i < users.size(); i++) {
            id = users.get(0).getId();
            token = users.get(0).getToken();
        }
        RestClient.builder()
                .url(BiaoXunTongApi.URL_COMPANYQYZZ + sfId)
                .params("userId", id)
                .params("token", token)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(Headers headers, String response) {

                        final JSONObject object = JSON.parseObject(response);
                        final JSONArray array = object.getJSONArray("data");

                        if (array.size() > 0) {
                            setData(array);
                        } else {
                            if (mDataList != null) {
                                mDataList.clear();
                                mAdapter.notifyDataSetChanged();
                            }
                            //TODO 内容为空的处理

                        }


                    }
                })
                .build()
                .post();


    }


    private void setData(JSONArray data) {
        mDataList.clear();

        if (data.size() >= 5) {
            qyzzCount.setText(data.size() + "");
            if (llMoreQyzz != null) {
                llMoreQyzz.setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < 5; i++) {
                CompanyQyzzListBean bean = new CompanyQyzzListBean();
                JSONObject list = data.getJSONObject(i);
                bean.setLx(list.getString("lx"));
                bean.setZzmc(list.getString("zzmc"));
                mDataList.add(bean);
            }

            mAdapter.notifyDataSetChanged();
            mAdapter.loadMoreEnd();
        } else {
            if (llMoreQyzz != null) {
                llMoreQyzz.setVisibility(View.GONE);
            }
            for (int i = 0; i < data.size(); i++) {
                CompanyQyzzListBean bean = new CompanyQyzzListBean();
                JSONObject list = data.getJSONObject(i);
                bean.setLx(list.getString("lx"));
                bean.setZzmc(list.getString("zzmc"));
                mDataList.add(bean);
            }

            mAdapter.notifyDataSetChanged();
            mAdapter.loadMoreEnd();
        }


    }


}
