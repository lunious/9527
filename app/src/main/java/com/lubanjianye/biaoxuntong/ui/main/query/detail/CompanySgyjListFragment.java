package com.lubanjianye.biaoxuntong.ui.main.query.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.lubanjianye.biaoxuntong.bean.CompanySgyjListBean;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.loadmore.CustomLoadMoreView;
import com.lubanjianye.biaoxuntong.net.RestClient;
import com.lubanjianye.biaoxuntong.net.api.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.net.callback.ISuccess;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

/**
 * 项目名:   LBBXT
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.fragment.query.detail
 * 文件名:   CompanySgyjListFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/16  0:12
 * 描述:     TODO
 */

public class CompanySgyjListFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout llIvBack = null;
    private AppCompatTextView mainBarName = null;
    private RecyclerView companySgyjRecycler = null;
    private SwipeRefreshLayout companySgyjRefresh = null;


    private CompanySgyjListAdapter mAdapter;
    private ArrayList<CompanySgyjListBean> mDataList = new ArrayList<>();

    private View noDataView;

    private int page = 1;
    private int pageSize = 20;

    private static final String ARG_SFID = "ARG_SFID";
    private String sfId = "";

    @Override
    public Object setLayout() {
        return R.layout.fragment_company_sgyj_list;
    }

    public static CompanySgyjListFragment create(@NonNull String entity) {
        final Bundle args = new Bundle();
        args.putString(ARG_SFID, entity);
        final CompanySgyjListFragment fragment = new CompanySgyjListFragment();
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
        companySgyjRecycler = getView().findViewById(R.id.company_sgyj_recycler);
        companySgyjRefresh = getView().findViewById(R.id.company_sgyj_refresh);

        llIvBack.setOnClickListener(this);
    }

    @Override
    public void initData() {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("全部企业业绩");
    }

    @Override
    public void initEvent() {
        initRecyclerView();
        initAdapter();
        initRefreshLayout();
        companySgyjRefresh.setRefreshing(true);
        requestData();
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
        companySgyjRefresh.setColorSchemeResources(
                R.color.blue,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        companySgyjRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO 刷新数据
                mAdapter.setEnableLoadMore(false);
                requestData();

            }
        });
    }

    private void initRecyclerView() {
        companySgyjRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        noDataView = getActivity().getLayoutInflater().inflate(R.layout.custom_empty_view, (ViewGroup) companySgyjRecycler.getParent(), false);
        noDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestData();
            }
        });

    }

    private void initAdapter() {
        mAdapter = new CompanySgyjListAdapter(R.layout.fragment_company_sgyj_list_item, mDataList);
        //设置列表动画
//        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        companySgyjRecycler.setAdapter(mAdapter);


    }


    private long id = 0;
    private String token = "";

    public void requestData() {


        List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
        long id = 0;
        for (int i = 0; i < users.size(); i++) {
            id = users.get(0).getId();
            token = users.get(0).getToken();
        }

        RestClient.builder()
                .url(BiaoXunTongApi.URL_COMPANYSGYJ + sfId)
                .params("userId", id)
                .params("token", token)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(Headers headers, String response) {

                        final JSONObject object = JSON.parseObject(response);
                        String status = object.getString("status");
                        final JSONArray array = object.getJSONArray("data");


                        if (array.size() > 0) {
                            setData(array);
                        } else {
                            if (mDataList != null) {
                                mDataList.clear();
                                mAdapter.notifyDataSetChanged();
                            }
                            //TODO 内容为空的处理
                            mAdapter.setEmptyView(noDataView);
                            if (companySgyjRefresh != null) {
                                companySgyjRefresh.setRefreshing(false);
                            }
                        }


                    }
                })
                .build()
                .post();


    }

    private void setData(JSONArray data) {
        final int size = data == null ? 0 : data.size();
        mDataList.clear();
        int d = 1;
        for (int i = 0; i < data.size(); i++) {
            CompanySgyjListBean bean = new CompanySgyjListBean();
            JSONObject list = data.getJSONObject(i);
            bean.setXmmc(d + "、" + list.getString("xmmc"));
            bean.setZbsj(list.getString("zbsj"));
            bean.setXmfzr(list.getString("xmfzr"));

            String zbje = list.getString("zbje");
            if ("0.0".equals(zbje)) {
                bean.setZbje("暂无");
            } else {
                bean.setZbje(list.getString("zbje")+"万元");
            }
            mDataList.add(bean);
            d++;
        }
        companySgyjRefresh.setRefreshing(false);
        mAdapter.setEnableLoadMore(true);
        mAdapter.notifyDataSetChanged();

        if (size < pageSize) {
            //第一页如果不够一页就不显示没有更多数据布局
            mAdapter.loadMoreEnd();
        } else {
            mAdapter.loadMoreComplete();
        }


    }

}
