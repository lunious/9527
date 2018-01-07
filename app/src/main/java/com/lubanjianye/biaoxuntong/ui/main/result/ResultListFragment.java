package com.lubanjianye.biaoxuntong.ui.main.result;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

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
import com.lubanjianye.biaoxuntong.net.RestClient;
import com.lubanjianye.biaoxuntong.net.api.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.net.callback.ISuccess;
import com.lubanjianye.biaoxuntong.ui.main.result.detail.ResultSggjyzbjgDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.result.detail.ResultXjgggDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.result.search.ResultSearchActivity;
import com.lubanjianye.biaoxuntong.util.aes.AesUtil;
import com.lubanjianye.biaoxuntong.util.netStatus.NetUtil;
import com.lubanjianye.biaoxuntong.util.netStatus.AppSysMgr;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.fragment.result
 * 文件名:   ResultListFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/12  0:26
 * 描述:     TODO
 */

public class ResultListFragment extends BaseFragment {

    private RecyclerView resultRecycler = null;
    private SwipeRefreshLayout resultRefresh = null;
    private MultipleStatusView loadingStatus = null;

    private String mTitle = null;
    private String mType = null;

    private ResultListAdapter mAdapter;
    private ArrayList<ResultListBean> mDataList = new ArrayList<>();

    private int page = 1;
    private String deviceId = AppSysMgr.getPsuedoUniqueID();


    private void initRefreshLayout() {

        resultRefresh.setColorSchemeResources(
                R.color.blue,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        resultRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO 刷新数据
                mAdapter.setEnableLoadMore(false);
                requestData(1);

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

                if ("xjggg".equals(entity) || "sjggg".equals(entity) || "sggjy".equals(entity)) {
                    intent = new Intent(getActivity(), ResultXjgggDetailActivity.class);
                    intent.putExtra("entityId", entityId);
                    intent.putExtra("entity", entity);
                    intent.putExtra("ajaxlogtype", "0");
                    startActivity(intent);

                } else if ("sggjyzbjg".equals(entity) || "sggjycgjgrow".equals(entity) || "sggjyjgcgtable".equals(entity)) {
                    intent = new Intent(getActivity(), ResultSggjyzbjgDetailActivity.class);
                    intent.putExtra("entityId", entityId);
                    intent.putExtra("entity", entity);
                    intent.putExtra("ajaxlogtype", "0");
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
                requestData(2);
            }
        });
        //设置列表动画
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        resultRecycler.setAdapter(mAdapter);


    }

    public static ResultListFragment getInstance(String title) {
        ResultListFragment sf = new ResultListFragment();
        sf.mTitle = title;
        return sf;
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_result_simple;
    }

    @Override
    public void initView() {
        resultRecycler = getView().findViewById(R.id.result_recycler);
        resultRefresh = getView().findViewById(R.id.result_refresh);
        loadingStatus = getView().findViewById(R.id.result_list_status_view);

        if ("政府采购结果公告".equals(mTitle)) {
            mType = "采购";
        } else if ("工程招标中标公示".equals(mTitle)) {
            mType = "工程";
        }

    }

    @Override
    public void initData() {
        initRecyclerView();
        initAdapter();
        addHeadView();
        initRefreshLayout();
        resultRefresh.setRefreshing(false);
        requestData(0);
    }

    @Override
    public void initEvent() {

    }

    private void addHeadView() {
        View headView = getLayoutInflater().inflate(R.layout.result_search_header_view, (ViewGroup) resultRecycler.getParent(), false);
        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击搜索
                startActivity(new Intent(getActivity(), ResultSearchActivity.class));
            }
        });
        mAdapter.addHeaderView(headView);
    }


    private long id = 0;

    public void requestData(final int isRefresh) {

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
                RestClient.builder()
                        .url(BiaoXunTongApi.URL_GETRESULTLIST)
                        .params("type", mType)
                        .params("userid", id)
                        .params("page", page)
                        .params("size", 10)
                        .params("deviceId", deviceId)
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(Headers headers, String response) {
                                String jiemi = AesUtil.aesDecrypt(response, BiaoXunTongApi.PAS_KEY);

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
                        })
                        .build()
                        .post();
            } else {
                //未登录的数据请求
                RestClient.builder()
                        .url(BiaoXunTongApi.URL_GETRESULTLIST)
                        .params("type", mType)
                        .params("page", page)
                        .params("size", 10)
                        .params("deviceId", deviceId)
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(Headers headers, String response) {

                                String jiemi = AesUtil.aesDecrypt(response, BiaoXunTongApi.PAS_KEY);

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
                        })
                        .build()
                        .post();
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
