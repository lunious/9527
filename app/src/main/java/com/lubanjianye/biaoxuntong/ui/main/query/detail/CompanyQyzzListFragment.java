package com.lubanjianye.biaoxuntong.ui.main.query.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.bean.CompanyQyzzListBean;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.api.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.util.netStatus.NetUtil;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名:   9527
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.query.detail
 * 文件名:   CompanyQyzzListFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/26  23:04
 * 描述:     TODO
 */

public class CompanyQyzzListFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout llIvBack = null;
    private AppCompatTextView mainBarName = null;

    private RecyclerView companyQyzzRecycler = null;
    private SmartRefreshLayout companyQyzzRefresh = null;


    private CompanyQyzzListAdapter mAdapter;
    private ArrayList<CompanyQyzzListBean> mDataList = new ArrayList<>();


    private View noDataView;

    private int page = 1;
    private int pageSize = 20;

    private static final String ARG_SFID = "ARG_SFID";
    private String sfId = "";

    public static CompanyQyzzListFragment create(@NonNull String entity) {
        final Bundle args = new Bundle();
        args.putString(ARG_SFID, entity);
        final CompanyQyzzListFragment fragment = new CompanyQyzzListFragment();
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
    public Object setLayout() {
        return R.layout.fragment_company_qyzz_more;
    }

    @Override
    public void initView() {
        llIvBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);
        companyQyzzRecycler = getView().findViewById(R.id.company_qyzz_recycler);
        companyQyzzRefresh = getView().findViewById(R.id.company_qyzz_refresh);
        llIvBack.setOnClickListener(this);
    }

    @Override
    public void initData() {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("全部企业资质");

    }

    @Override
    public void initEvent() {
        initRecyclerView();
        initAdapter();
        initRefreshLayout();

        if (!NetUtil.isNetworkConnected(getActivity())) {
            ToastUtil.shortBottonToast(getContext(), "请检查网络设置");
        } else {
            requestData(true);
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


    private void initRefreshLayout() {

        companyQyzzRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                if (!NetUtil.isNetworkConnected(getActivity())) {
                    ToastUtil.shortBottonToast(getContext(), "请检查网络设置");
                    companyQyzzRefresh.finishRefresh(2000, false);
                } else {
                    requestData(true);
                }
            }
        });

        companyQyzzRefresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

                //TODO 去加载更多数据
                if (!NetUtil.isNetworkConnected(getActivity())) {
                    ToastUtil.shortBottonToast(getContext(), "请检查网络设置");
                } else {
                    requestData(false);
                }
            }
        });

//        indexRefresh.autoRefresh();


    }

    private void initRecyclerView() {

        companyQyzzRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        noDataView = getActivity().getLayoutInflater().inflate(R.layout.custom_empty_view, (ViewGroup) companyQyzzRecycler.getParent(), false);
        noDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestData(true);
            }
        });

    }

    private void initAdapter() {
        mAdapter = new CompanyQyzzListAdapter(R.layout.fragment_company_qyzz, mDataList);

        //设置列表动画
//        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        companyQyzzRecycler.setAdapter(mAdapter);


    }


    public void requestData(final boolean isRefresh) {

        List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
        long id = 0;
        String token = "";
        for (int i = 0; i < users.size(); i++) {
            id = users.get(0).getId();
            token = users.get(0).getToken();
        }

        if (isRefresh) {
            page = 1;
            OkGo.<String>post(BiaoXunTongApi.URL_COMPANYQYZZ + sfId)
                    .params("userId", id)
                    .params("token", token)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {

                            final JSONObject object = JSON.parseObject(response.body());
                            final JSONArray array = object.getJSONArray("data");

                            if (array.size() > 0) {
                                page = 2;
                                setData(isRefresh, array);
                            } else {
                                if (mDataList != null) {
                                    mDataList.clear();
                                    mAdapter.notifyDataSetChanged();
                                }
                                //TODO 内容为空的处理
                                mAdapter.setEmptyView(noDataView);
                                if (companyQyzzRefresh != null) {
                                    companyQyzzRefresh.setEnableRefresh(false);
                                }
                            }
                        }
                    });
        } else {
            OkGo.<String>post(BiaoXunTongApi.URL_COMPANYQYZZ + sfId)
                    .params("userId", id)
                    .params("token", token)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {

                            final JSONObject object = JSON.parseObject(response.body());
                            final JSONArray array = object.getJSONArray("data");

                            if (array.size() > 0) {
                                setData(isRefresh, array);
                            } else {
                                if (mDataList != null) {
                                    mDataList.clear();
                                    mAdapter.notifyDataSetChanged();
                                }
                                //TODO 内容为空的处理
                                mAdapter.setEmptyView(noDataView);
                                if (companyQyzzRefresh != null) {
                                    companyQyzzRefresh.setEnableRefresh(false);
                                }
                            }
                        }
                    });
        }


    }


    private void setData(boolean isRefresh, JSONArray data) {

        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            mDataList.clear();
            int d = 1;
            for (int i = 0; i < data.size(); i++) {
                CompanyQyzzListBean bean = new CompanyQyzzListBean();
                JSONObject list = data.getJSONObject(i);
                bean.setLx(d + "、" + list.getString("lx"));
                bean.setZzmc(list.getString("zzmc"));
                mDataList.add(bean);
                d++;
            }
            companyQyzzRefresh.finishRefresh(0, true);
        } else {
            page++;
            if (size > 0) {
                int d = 1;
                for (int i = 0; i < data.size(); i++) {
                    CompanyQyzzListBean bean = new CompanyQyzzListBean();
                    JSONObject list = data.getJSONObject(i);
                    bean.setLx(d + "、" + list.getString("lx"));
                    bean.setZzmc(list.getString("zzmc"));
                    mDataList.add(bean);
                    d++;
                }
            }
            companyQyzzRefresh.finishLoadmore(0, true);
        }

        mAdapter.notifyDataSetChanged();

        if (size < pageSize) {
            //第一页如果不够一页就不显示没有更多数据布局
            companyQyzzRefresh.setEnableLoadmore(false);
        } else {
            companyQyzzRefresh.setLoadmoreFinished(true);
        }

    }
}
