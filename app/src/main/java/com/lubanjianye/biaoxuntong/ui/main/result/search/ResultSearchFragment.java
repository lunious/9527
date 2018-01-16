package com.lubanjianye.biaoxuntong.ui.main.result.search;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.classic.common.MultipleStatusView;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.bean.ResultListBean;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.loadmore.CustomLoadMoreView;
import com.lubanjianye.biaoxuntong.api.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.searchview.DCallBack;
import com.lubanjianye.biaoxuntong.searchview.ICallBack;
import com.lubanjianye.biaoxuntong.searchview.SCallBack;
import com.lubanjianye.biaoxuntong.searchview.SearchView;
import com.lubanjianye.biaoxuntong.searchview.bCallBack;
import com.lubanjianye.biaoxuntong.ui.main.result.ResultListAdapter;
import com.lubanjianye.biaoxuntong.ui.main.result.detail.ResultSggjyzbjgDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.result.detail.ResultXjgggDetailActivity;
import com.lubanjianye.biaoxuntong.util.aes.AesUtil;
import com.lubanjianye.biaoxuntong.util.netStatus.NetUtil;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名:   LBBXT
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.result.search
 * 文件名:   ResultSearchFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/19  10:47
 * 描述:     TODO
 */

public class ResultSearchFragment extends BaseFragment {

    private SearchView searchView = null;

    private RecyclerView resultRecycler = null;
    private SwipeRefreshLayout resultRefresh = null;
    private MultipleStatusView loadingStatus = null;

    private ResultListAdapter mAdapter;
    private ArrayList<ResultListBean> mDataList = new ArrayList<>();

    private int page = 1;
    private int pageSize = 20;

    private String mKeyword = "";

    private void initRefreshLayout() {

        resultRefresh.setColorSchemeResources(
                R.color.main_theme_color,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        resultRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO 刷新数据
                mAdapter.setEnableLoadMore(false);
                requestData(1, mKeyword);
            }
        });
    }


    private void initRecyclerView() {
        resultRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));


        resultRecycler.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                final ResultListBean data = (ResultListBean) adapter.getData().get(position);
                final int entityId = data.getEntityid();
                final String entity = data.getEntity();

                Log.d("JASBHDBHSABDSADSAD", entityId + "___" + entity);

                Intent intent = null;

                if ("xjggg".equals(entity) || "sjggg".equals(entity)) {
                    intent = new Intent(getActivity(), ResultXjgggDetailActivity.class);
                    intent.putExtra("entityId", entityId);
                    intent.putExtra("entity", entity);
                    intent.putExtra("ajaxlogtype", "0");
                    intent.putExtra("mId", "");
                    startActivity(intent);

                } else if ("sggjyzbjg".equals(entity) || "sggjycgjgrow".equals(entity) || "sggjyjgcgtable".equals(entity)) {
                    intent = new Intent(getActivity(), ResultSggjyzbjgDetailActivity.class);
                    intent.putExtra("entityId", entityId);
                    intent.putExtra("entity", entity);
                    intent.putExtra("ajaxlogtype", "0");
                    intent.putExtra("mId", "");
                    startActivity(intent);
                }

            }
        });

    }

    private void initAdapter() {
        mAdapter = new ResultListAdapter(R.layout.fragment_result_item, mDataList);

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                //TODO 去加载更多数据
                requestData(2, mKeyword);
            }
        });
        //设置列表动画
//        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        resultRecycler.setAdapter(mAdapter);


    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_result_search;
    }

    @Override
    public void initView() {
        searchView = getView().findViewById(R.id.result_search_view);
        resultRecycler = getView().findViewById(R.id.result_search_recycler);
        resultRefresh = getView().findViewById(R.id.result_search_refresh);
        loadingStatus = getView().findViewById(R.id.result_search_list_status_view);
    }

    @Override
    public void initData() {
        initSearchView();
        initRecyclerView();
        initAdapter();
        initRefreshLayout();
        resultRefresh.setRefreshing(false);
        resultRefresh.setEnabled(false);
    }

    @Override
    public void initEvent() {

    }

    private void initSearchView() {
        searchView.setOnClickBack(new bCallBack() {
            @Override
            public void BackAciton() {
                getActivity().onBackPressed();
            }
        });

        searchView.setOnClickSearch(new ICallBack() {
            @Override
            public void SearchAciton(String string) {
                mKeyword = string;
                if (TextUtils.isEmpty(string)) {
                    ToastUtil.shortToast(getContext(), "请输入关键字");
                } else {
                    resultRefresh.setVisibility(View.VISIBLE);
                    requestData(0, string);

                    hideSoftInput();
                }
            }
        });

        searchView.setOnClickDele(new DCallBack() {
            @Override
            public void DeleAciton() {
                resultRefresh.setVisibility(View.GONE);
                resultRefresh.setEnabled(false);

            }
        });

        searchView.setOnClickStartSearch(new SCallBack() {
            @Override
            public void StartSearch(String string) {
                if (TextUtils.isEmpty(string)) {
                    ToastUtil.shortToast(getContext(), "请输入关键字");
                } else {
                    resultRefresh.setVisibility(View.VISIBLE);
                    requestData(0, string);

                    hideSoftInput();
                }
            }
        });

    }

    private long id = 0;

    public void requestData(final int isRefresh, final String keyword) {

        if (!NetUtil.isNetworkConnected(getActivity())) {
            loadingStatus.showNoNetwork();
            resultRefresh.setEnabled(false);
        } else {
            if (isRefresh == 0) {
                loadingStatus.showLoading();
            }
            if (isRefresh == 0 || isRefresh == 1) {
                page = 1;
            }

            if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
                //已登录的数据请求
                List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();

                for (int i = 0; i < users.size(); i++) {
                    id = users.get(0).getId();
                }

                OkGo.<String>post(BiaoXunTongApi.URL_GETRESULTLIST)
                        .params("keyWord", keyword)
                        .params("userid", id)
                        .params("page", page)
                        .params("size", 20)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String jiemi = AesUtil.aesDecrypt(response.body(), BiaoXunTongApi.PAS_KEY);

                                final JSONObject object = JSON.parseObject(jiemi);
                                final JSONObject data = object.getJSONObject("data");
                                final JSONArray array = data.getJSONArray("list");
                                final boolean nextPage = data.getBoolean("nextpage");

                                if (array.size() > 0) {
                                    setData(isRefresh, array, nextPage);
                                } else {
                                    if (mDataList != null) {
                                        mDataList.clear();
                                        mAdapter.notifyDataSetChanged();
                                    }
                                    //TODO 内容为空的处理
                                    loadingStatus.showEmpty();
                                    resultRefresh.setEnabled(false);
                                }
                            }
                        });

            } else {
                //未登录的数据请求

                OkGo.<String>post(BiaoXunTongApi.URL_GETRESULTLIST)
                        .params("keyWord", keyword)
                        .params("page", page)
                        .params("size", 20)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String jiemi = AesUtil.aesDecrypt(response.body(), BiaoXunTongApi.PAS_KEY);

                                final JSONObject object = JSON.parseObject(jiemi);
                                final JSONObject data = object.getJSONObject("data");
                                final JSONArray array = data.getJSONArray("list");
                                final boolean nextPage = data.getBoolean("nextpage");

                                if (array.size() > 0) {
                                    setData(isRefresh, array, nextPage);
                                } else {
                                    if (mDataList != null) {
                                        mDataList.clear();
                                        mAdapter.notifyDataSetChanged();
                                    }
                                    //TODO 内容为空的处理
                                    loadingStatus.showEmpty();
                                    resultRefresh.setEnabled(false);
                                }
                            }
                        });
            }

        }


    }

    private void setData(int isRefresh, JSONArray data, boolean nextPage) {
        page++;
        final int size = data == null ? 0 : data.size();
        if (isRefresh == 0 || isRefresh == 1) {
            loadingStatus.showContent();
            mDataList.clear();
            for (int i = 0; i < data.size(); i++) {
                ResultListBean bean = new ResultListBean();
                JSONObject list = data.getJSONObject(i);
                bean.setEntryName(list.getString("entryName"));
                bean.setSysTime(list.getString("sysTime"));
                bean.setEntityid(list.getInteger("entityid"));
                bean.setEntity(list.getString("entity"));
                mDataList.add(bean);
            }
            resultRefresh.setRefreshing(false);
            mAdapter.setEnableLoadMore(true);
            mAdapter.notifyDataSetChanged();
        } else {
            loadingStatus.showContent();
            if (size > 0) {
                for (int i = 0; i < data.size(); i++) {
                    ResultListBean bean = new ResultListBean();
                    JSONObject list = data.getJSONObject(i);
                    bean.setEntryName(list.getString("entryName"));
                    bean.setType(list.getString("type"));
                    bean.setSysTime(list.getString("sysTime"));
                    bean.setEntityid(list.getInteger("entityid"));
                    bean.setEntity(list.getString("entity"));
                    mDataList.add(bean);
                }
                mAdapter.notifyDataSetChanged();
            }
        }
        if (!nextPage) {
            //第一页如果不够一页就不显示没有更多数据布局
            mAdapter.loadMoreEnd();
        } else {
            mAdapter.loadMoreComplete();
        }


    }
}
