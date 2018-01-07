package com.lubanjianye.biaoxuntong.ui.main.index;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.classic.common.MultipleStatusView;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.app.BiaoXunTong;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.bean.IndexHyzxListBean;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.loadmore.CustomLoadMoreView;
import com.lubanjianye.biaoxuntong.net.RestClient;
import com.lubanjianye.biaoxuntong.net.api.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.net.callback.ISuccess;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.IndexHyzxDetailActivity;
import com.lubanjianye.biaoxuntong.util.netStatus.NetUtil;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

/**
 * 项目名:   LBBXT
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.index
 * 文件名:   IndexHyzxListFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/16  10:23
 * 描述:     TODO
 */

public class IndexHyzxListFragment extends BaseFragment {

    private RecyclerView indexHyzxRecycler = null;
    private SwipeRefreshLayout indexHyzxRefresh = null;
    private MultipleStatusView loadingStatus = null;


    private IndexHyzxListAdapter mAdapter;
    private ArrayList<IndexHyzxListBean> mDataList = new ArrayList<>();

    private int page = 1;

    @Override
    public Object setLayout() {
        return R.layout.fragment_index_hyzx;
    }

    @Override
    public void initView() {
        indexHyzxRecycler = getView().findViewById(R.id.index_hyzx_recycler);
        indexHyzxRefresh = getView().findViewById(R.id.index_hyzx_refresh);
        loadingStatus = getView().findViewById(R.id.index_hyzx_list_status_view);


    }

    @Override
    public void initData() {
        initRecyclerView();
        initAdapter();
        initRefreshLayout();
        indexHyzxRefresh.setRefreshing(false);
        requestData(0);
    }

    @Override
    public void initEvent() {

    }

    private void initRefreshLayout() {
        indexHyzxRefresh.setColorSchemeResources(
                R.color.blue,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        indexHyzxRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO 刷新数据
                mAdapter.setEnableLoadMore(false);
                requestData(1);

            }
        });
    }


    private void initRecyclerView() {

        indexHyzxRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        indexHyzxRecycler.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                final IndexHyzxListBean data = (IndexHyzxListBean) adapter.getData().get(position);
                final String createTime = data.getCreate_time();
                final String title = data.getTitle();
                final String mobile_context = data.getMobile_context();

                Intent intent = new Intent(BiaoXunTong.getApplicationContext(), IndexHyzxDetailActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("createTime", createTime);
                intent.putExtra("mobile_context", mobile_context);
                startActivity(intent);
            }
        });

    }

    private void initAdapter() {
        mAdapter = new IndexHyzxListAdapter(R.layout.fragment_index_hyzx_item, mDataList);

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
        indexHyzxRecycler.setAdapter(mAdapter);


    }

    private long id = 0;

    public void requestData(final int isRefresh) {

        if (!NetUtil.isNetworkConnected(getActivity())) {
            loadingStatus.showNoNetwork();
            indexHyzxRefresh.setEnabled(false);
        } else {
            if (isRefresh == 0) {
                loadingStatus.showLoading();
            }
            if (isRefresh == 0 || isRefresh == 1) {
                page = 1;
            }

            if (AppSharePreferenceMgr.contains(getContext(), "login_status")) {
                //已登录的数据请求
                List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();

                for (int i = 0; i < users.size(); i++) {
                    id = users.get(0).getId();
                }

                RestClient.builder()
                        .url(BiaoXunTongApi.URL_GETINDEXHYZXLIST)
                        .params("userid", id)
                        .params("page", page)
                        .params("size", 10)
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(Headers headers, String response) {
                                final JSONObject object = JSON.parseObject(response);
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
                                    indexHyzxRefresh.setEnabled(false);
                                }


                            }
                        })
                        .build()
                        .post();
            } else {
                //未登录的数据请求
                RestClient.builder()
                        .url(BiaoXunTongApi.URL_GETINDEXHYZXLIST)
                        .params("page", page)
                        .params("size", 10)
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(Headers headers, String response) {

                                final JSONObject object = JSON.parseObject(response);
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
                                    indexHyzxRefresh.setEnabled(false);
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
        List<Integer> imgs = new ArrayList<>();
        final int size = data == null ? 0 : data.size();
        if (isRefresh == 0 || isRefresh == 1) {
            loadingStatus.showContent();
            mDataList.clear();
            for (int i = 0; i < data.size(); i++) {

                imgs.add(R.mipmap.hyzx_1);
                imgs.add(R.mipmap.hyzx_2);
                imgs.add(R.mipmap.hyzx_3);
                imgs.add(R.mipmap.hyzx_4);
                imgs.add(R.mipmap.hyzx_5);

                IndexHyzxListBean bean = new IndexHyzxListBean();
                JSONObject list = data.getJSONObject(i);
                bean.setId(list.getInteger("id"));
                bean.setTitle(list.getString("title"));
                bean.setCreate_time(list.getString("create_time"));
                bean.setMobile_img(list.getString("mobile_img"));
                bean.setMobile_context(list.getString("mobile_context"));
                bean.setImg(imgs.get(i));
                mDataList.add(bean);
            }
            indexHyzxRefresh.setRefreshing(false);
            mAdapter.setEnableLoadMore(true);
            mAdapter.notifyDataSetChanged();
        } else {
            loadingStatus.showContent();
            if (size > 0) {
                for (int i = 0; i < data.size(); i++) {
                    imgs.add(R.mipmap.hyzx_1);
                    imgs.add(R.mipmap.hyzx_2);
                    imgs.add(R.mipmap.hyzx_3);
                    imgs.add(R.mipmap.hyzx_4);
                    imgs.add(R.mipmap.hyzx_5);

                    IndexHyzxListBean bean = new IndexHyzxListBean();
                    JSONObject list = data.getJSONObject(i);
                    bean.setId(list.getInteger("id"));
                    bean.setTitle(list.getString("title"));
                    bean.setCreate_time(list.getString("create_time"));
                    bean.setMobile_img(list.getString("mobile_img"));
                    bean.setMobile_context(list.getString("mobile_context"));
                    bean.setImg(imgs.get(i));
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
