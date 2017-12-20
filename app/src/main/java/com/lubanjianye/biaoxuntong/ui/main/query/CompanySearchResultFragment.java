package com.lubanjianye.biaoxuntong.ui.main.query;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.bean.CompanySearchResultListBean;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.loadmore.CustomLoadMoreView;
import com.lubanjianye.biaoxuntong.net.RestClient;
import com.lubanjianye.biaoxuntong.net.api.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.net.callback.ISuccess;
import com.lubanjianye.biaoxuntong.sign.SignInActivity;
import com.lubanjianye.biaoxuntong.ui.main.query.detail.CompanyDetailActivity;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.tosaty.Toasty;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

/**
 * 项目名:   LBBXT
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.fragment.query
 * 文件名:   CompanySearchResultFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/15  23:03
 * 描述:     TODO
 */

public class CompanySearchResultFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout llIvBack = null;
    private AppCompatTextView mainBarName = null;
    private RecyclerView companySearchResultRecycler = null;
    private SwipeRefreshLayout companySearchResultRefresh = null;

    private static final String ARG_PROVINCECODE = "ARG_PROVINCECODE";
    private static final String ARG_QYID = "ARG_QYID";


    private String mProvinceCode = "";
    private String mqyIds = "";

    private CompanySearchResultListAdapter mAdapter;
    private ArrayList<CompanySearchResultListBean> mDataList = new ArrayList<>();

    private int page = 1;

    @Override
    public Object setLayout() {
        return R.layout.fragment_company_search_result;
    }

    public static CompanySearchResultFragment create(@NonNull String provinceCode, String qyId) {
        final Bundle args = new Bundle();
        args.putString(ARG_PROVINCECODE, provinceCode);
        args.putString(ARG_QYID, qyId);
        final CompanySearchResultFragment fragment = new CompanySearchResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle args = getArguments();
        if (args != null) {
            mProvinceCode = args.getString(ARG_PROVINCECODE);
            mqyIds = args.getString(ARG_QYID);

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //取消注册EventBus
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXXXXX(EventMessage message) {

        if (EventMessage.LOGIN_SUCCSS.equals(message.getMessage())) {
            //登陆成功后更新UI
            requestData(true);

        } else {

        }
    }

    @Override
    public void initView() {

        //注册EventBus
        EventBus.getDefault().register(this);

        llIvBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);
        companySearchResultRecycler = getView().findViewById(R.id.company_search_result_recycler);
        companySearchResultRefresh = getView().findViewById(R.id.company_search_result_refresh);

        llIvBack.setOnClickListener(this);


    }

    @Override
    public void initData() {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("查询结果");
    }

    @Override
    public void initEvent() {
        initRecyclerView();
        initAdapter();
        initRefreshLayout();
        companySearchResultRefresh.setRefreshing(true);
        requestData(true);
    }


    private void initRefreshLayout() {
        companySearchResultRefresh.setColorSchemeResources(
                R.color.blue,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        companySearchResultRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO 刷新数据
                requestData(true);

            }
        });
    }

    private void initRecyclerView() {

        companySearchResultRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        companySearchResultRecycler.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

                final CompanySearchResultListBean data = (CompanySearchResultListBean) adapter.getData().get(position);
                final String sfId = data.getSfId();

                Intent intent = new Intent(getActivity(), CompanyDetailActivity.class);
                intent.putExtra("sfId", sfId);
                startActivity(intent);
            }
        });


    }

    private void initAdapter() {
        mAdapter = new CompanySearchResultListAdapter(R.layout.fragment_company_search_item, mDataList);

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                //TODO 去加载更多数据
//                requestData(false);
            }
        });
        //设置列表动画
//        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        companySearchResultRecycler.setAdapter(mAdapter);


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

    public void requestData(final boolean isRefresh) {

        if (isRefresh) {
            page = 1;
        }


        List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
        long id = 0;
        String token = "";
        for (int i = 0; i < users.size(); i++) {
            id = users.get(0).getId();
            token = users.get(0).getToken();
        }

        RestClient.builder()
                .url(BiaoXunTongApi.URL_SUITRESULT)
                .params("userId", id)
                .params("token", token)
                .params("provinceCode", mProvinceCode)
                .params("qyIds", mqyIds)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(Headers headers, String response) {

                        final JSONObject object = JSON.parseObject(response);
                        final String status = object.getString("status");
                        final String message = object.getString("message");
                        final JSONArray array = object.getJSONArray("data");

                        if ("200".equals(status)) {
                            if (array.size() > 0) {
                                setData(isRefresh, array);
                            } else {
                                if (mDataList != null) {
                                    mDataList.clear();
                                    mAdapter.notifyDataSetChanged();
                                }
                                //TODO 内容为空的处理

                                Toasty.info(getContext(), "暂无内容", Toast.LENGTH_SHORT, true).show();
                            }
                        } else {
                            if ("LIMIT_REACHED".equals(message)) {
                                Toasty.info(getContext(), "你今天已达到最大查询次数，请明天再试！", Toast.LENGTH_SHORT, true).show();
                            } else if ("INVALID_TOKEN".equals(message)) {

                                Toasty.info(getContext(), "Token失效，请重新登录!", Toast.LENGTH_SHORT, true).show();

                                List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
                                long id = 0;
                                for (int j = 0; j < users.size(); j++) {
                                    id = users.get(0).getId();
                                }
                                RestClient.builder()
                                        .url(BiaoXunTongApi.URL_LOGOUT)
                                        .params("id", id)
                                        .success(new ISuccess() {
                                            @Override
                                            public void onSuccess(Headers headers, String response) {

                                                DatabaseManager.getInstance().getDao().deleteAll();
                                                AppSharePreferenceMgr.remove(getContext(), EventMessage.LOGIN_SUCCSS);
                                                EventBus.getDefault().post(new EventMessage(EventMessage.LOGIN_OUT));
                                                startActivity(new Intent(getActivity(), SignInActivity.class));

                                            }
                                        })
                                        .build()
                                        .post();
                            }
                        }


                    }
                })
                .build()
                .post();


    }

    private void setData(boolean isRefresh, JSONArray data) {
//        page++;
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            mDataList.clear();
            for (int i = 0; i < data.size(); i++) {
                CompanySearchResultListBean bean = new CompanySearchResultListBean();
                JSONObject list = data.getJSONObject(i);
                bean.setQy(i + 1 + "、" + list.getString("qy"));
                bean.setLxr(list.getString("lxr"));
                bean.setEntrySign(list.getString("entrySign"));
                bean.setSfId(list.getString("sfId"));
                mDataList.add(bean);
            }
            companySearchResultRefresh.setRefreshing(false);
            mAdapter.notifyDataSetChanged();
            mAdapter.loadMoreComplete();
            mAdapter.loadMoreEnd();
        } else {
            if (size > 0) {
                for (int i = 0; i < data.size(); i++) {
                    CompanySearchResultListBean bean = new CompanySearchResultListBean();
                    JSONObject list = data.getJSONObject(i);
                    bean.setQy(i + 1 + "、" + list.getString("qy"));
                    bean.setLxr(list.getString("lxr"));
                    bean.setEntrySign(list.getString("entrySign"));
                    bean.setSfId(list.getString("sfId"));
                    mDataList.add(bean);
                }
                mAdapter.notifyDataSetChanged();
            }
        }


    }
}
