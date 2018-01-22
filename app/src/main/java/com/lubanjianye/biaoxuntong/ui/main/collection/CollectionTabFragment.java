package com.lubanjianye.biaoxuntong.ui.main.collection;


import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.classic.common.MultipleStatusView;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.app.BiaoXunTong;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.bean.CollectionListBean;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.loadmore.CustomLoadMoreView;
import com.lubanjianye.biaoxuntong.api.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.sign.SignInActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.IndexBxtgdjDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.IndexSggjyDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.IndexSggjycgrowDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.IndexSggjycgtableDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.IndexXcgggDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.result.detail.ResultSggjyzbjgDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.result.detail.ResultXjgggDetailActivity;
import com.lubanjianye.biaoxuntong.util.netStatus.NetUtil;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

public class CollectionTabFragment extends BaseFragment implements View.OnClickListener {

    private AppCompatTextView mainBarName = null;
    private AppCompatButton btnToLogin = null;
    private LinearLayout llShow = null;
    private RecyclerView collectRecycler = null;
    private SmartRefreshLayout collectRefresh = null;
    private MultipleStatusView loadingStatus = null;


    private CollectionListAdapter mAdapter;
    private ArrayList<CollectionListBean> mDataList = new ArrayList<>();

    private int page = 1;
    private boolean isInitCache = false;
    private long id = 0;

    @Override
    public Object setLayout() {
        return R.layout.fragment_collection_list;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXXXXX(EventMessage message) {

        if (EventMessage.LOGIN_SUCCSS.equals(message.getMessage()) || EventMessage.CLICK_FAV.equals(message.getMessage())) {
            //登陆成功后更新UI

            if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
                if (llShow != null) {
                    llShow.setVisibility(View.GONE);
                }

                initAdapter();
                initRefreshLayout();
                mAdapter.setEnableLoadMore(false);
                requestData(true);
            } else {
                if (llShow != null) {
                    llShow.setVisibility(View.VISIBLE);
                }
                mainBarName.setText("我的收藏");
            }

        } else if (EventMessage.LOGIN_OUT.equals(message.getMessage())) {
            if (llShow != null) {
                llShow.setVisibility(View.VISIBLE);
            }
            mainBarName.setText("我的收藏");
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //取消注册EventBus
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView() {
        //注册EventBus
        EventBus.getDefault().register(this);

        mainBarName = getView().findViewById(R.id.main_bar_name);
        btnToLogin = getView().findViewById(R.id.btn_to_login);
        llShow = getView().findViewById(R.id.ll_show);
        collectRecycler = getView().findViewById(R.id.collect_recycler);
        collectRefresh = getView().findViewById(R.id.collect_refresh);
        loadingStatus = getView().findViewById(R.id.collection_list_status_view);

        btnToLogin.setOnClickListener(this);


    }

    @Override
    public void initData() {
        mainBarName.setVisibility(View.VISIBLE);
        mainBarName.setText("我的收藏");

    }


    @Override
    public void initEvent() {
        initRecyclerView();
        initAdapter();
        initRefreshLayout();

        if (!NetUtil.isNetworkConnected(getActivity())) {
            ToastUtil.shortBottonToast(getContext(), "请检查网络设置");
            requestData(true);
            mAdapter.setEnableLoadMore(false);
        } else {
            requestData(true);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_to_login:
                //未登录去登陆
                startActivity(new Intent(getActivity(), SignInActivity.class));
                break;
            default:
                break;
        }
    }

    private void initRefreshLayout() {


        collectRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                if (!NetUtil.isNetworkConnected(getActivity())) {
                    ToastUtil.shortBottonToast(getContext(), "请检查网络设置");
                    collectRefresh.finishRefresh(2000, false);
                    mAdapter.setEnableLoadMore(false);
                } else {
                    requestData(true);
                }
            }
        });

//        collectRefresh.autoRefresh();
    }

    private void initRecyclerView() {

        collectRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        collectRecycler.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                final CollectionListBean data = (CollectionListBean) adapter.getData().get(position);
                final int entityId = data.getEntityId();
                final String entity = data.getEntity();

                Intent intent = null;

                if ("sggjy".equals(entity)) {
                    intent = new Intent(BiaoXunTong.getApplicationContext(), IndexSggjyDetailActivity.class);
                    intent.putExtra("entityId", entityId);
                    intent.putExtra("entity", entity);
                    intent.putExtra("ajaxlogtype", "0");
                    intent.putExtra("mId", "");
                    startActivity(intent);
                } else if ("xcggg".equals(entity)) {
                    intent = new Intent(BiaoXunTong.getApplicationContext(), IndexXcgggDetailActivity.class);
                    intent.putExtra("entityId", entityId);
                    intent.putExtra("entity", entity);
                    intent.putExtra("ajaxlogtype", "0");
                    intent.putExtra("mId", "");
                    startActivity(intent);
                } else if ("bxtgdj".equals(entity)) {
                    intent = new Intent(BiaoXunTong.getApplicationContext(), IndexBxtgdjDetailActivity.class);
                    intent.putExtra("entityId", entityId);
                    intent.putExtra("entity", entity);
                    intent.putExtra("ajaxlogtype", "0");
                    intent.putExtra("mId", "");
                    startActivity(intent);
                } else if ("sggjycgtable".equals(entity)) {

                    intent = new Intent(BiaoXunTong.getApplicationContext(), IndexSggjycgtableDetailActivity.class);
                    intent.putExtra("entityId", entityId);
                    intent.putExtra("entity", entity);
                    intent.putExtra("ajaxlogtype", "0");
                    intent.putExtra("mId", "");
                    startActivity(intent);

                } else if ("xjggg".equals(entity) || "sjggg".equals(entity)) {
                    intent = new Intent(BiaoXunTong.getApplicationContext(), ResultXjgggDetailActivity.class);
                    intent.putExtra("entityId", entityId);
                    intent.putExtra("entity", entity);
                    intent.putExtra("ajaxlogtype", "0");
                    intent.putExtra("mId", "");
                    startActivity(intent);

                } else if ("sggjyzbjg".equals(entity) || "sggjycgjgrow".equals(entity) || "sggjyjgcgtable".equals(entity)) {
                    intent = new Intent(BiaoXunTong.getApplicationContext(), ResultSggjyzbjgDetailActivity.class);
                    intent.putExtra("entityId", entityId);
                    intent.putExtra("entity", entity);
                    intent.putExtra("ajaxlogtype", "0");
                    intent.putExtra("mId", "");
                    startActivity(intent);
                } else if ("t_hyzx".equals(entity)) {

                } else if ("sggjycgrow".equals(entity)) {
                    intent = new Intent(BiaoXunTong.getApplicationContext(), IndexSggjycgrowDetailActivity.class);
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
        mAdapter = new CollectionListAdapter(R.layout.fragment_collection_item, mDataList);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                //TODO 去加载更多数据
                if (!NetUtil.isNetworkConnected(getActivity())) {
                    ToastUtil.shortBottonToast(getContext(), "请检查网络设置");
                } else {
                    requestData(false);
                }
            }
        });


        //设置列表动画
//        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        collectRecycler.setAdapter(mAdapter);


    }


    public void requestData(final boolean isRefresh) {

        if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
            llShow.setVisibility(View.GONE);
            List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
            for (int i = 0; i < users.size(); i++) {
                id = users.get(0).getId();
            }

            if (isRefresh) {
                page = 1;
                OkGo.<String>post(BiaoXunTongApi.URL_GETCOLLECTIONLIST)
                        .params("userid", id)
                        .params("page", page)
                        .params("size", 10)
                        .cacheKey("collect_cache" + id)
                        .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                        .cacheTime(3600 * 48000)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                final JSONObject object = JSON.parseObject(response.body());
                                final JSONObject data = object.getJSONObject("data");
                                final JSONArray array = data.getJSONArray("list");
                                final int count = data.getInteger("count");
                                final boolean nextPage = data.getBoolean("nextpage");

                                if (array.size() > 0) {
                                    page = 2;
                                    setData(isRefresh, array, nextPage);
                                    mainBarName.setText("我的收藏(" + "共" + count + "条)");
                                } else {
                                    //TODO 内容为空的处理
                                    loadingStatus.showEmpty();
                                    collectRefresh.setEnableRefresh(false);
                                    mainBarName.setText("我的收藏(" + "共" + count + "条)");
                                }
                            }

                            @Override
                            public void onCacheSuccess(Response<String> response) {

                                if (!isInitCache) {
                                    final JSONObject object = JSON.parseObject(response.body());
                                    final JSONObject data = object.getJSONObject("data");
                                    final JSONArray array = data.getJSONArray("list");
                                    final int count = data.getInteger("count");
                                    final boolean nextPage = data.getBoolean("nextpage");

                                    if (array.size() > 0) {
                                        setData(isRefresh, array, nextPage);
                                        mainBarName.setText("我的收藏(" + "共" + count + "条)");
                                    } else {
                                        //TODO 内容为空的处理
                                        loadingStatus.showEmpty();
                                        collectRefresh.setEnableRefresh(false);
                                        mainBarName.setText("我的收藏(" + "共" + count + "条)");
                                    }
                                    isInitCache = true;

                                }

                            }
                        });
            } else {
                OkGo.<String>post(BiaoXunTongApi.URL_GETCOLLECTIONLIST)
                        .params("userid", id)
                        .params("page", page)
                        .params("size", 10)
                        .cacheMode(CacheMode.NO_CACHE)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                final JSONObject object = JSON.parseObject(response.body());
                                final JSONObject data = object.getJSONObject("data");
                                final JSONArray array = data.getJSONArray("list");
                                final int count = data.getInteger("count");
                                final boolean nextPage = data.getBoolean("nextpage");

                                if (array.size() > 0) {
                                    setData(isRefresh, array, nextPage);
                                    mainBarName.setText("我的收藏(" + "共" + count + "条)");
                                } else {
                                    //TODO 内容为空的处理
                                    loadingStatus.showEmpty();
                                    collectRefresh.setEnableRefresh(false);
                                    mainBarName.setText("我的收藏(" + "共" + count + "条)");
                                }
                            }
                        });
            }

        } else {
            llShow.setVisibility(View.VISIBLE);
        }


    }

    private void setData(boolean isRefresh, JSONArray data, boolean nextPage) {
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            loadingStatus.showContent();
            mDataList.clear();
            for (int i = 0; i < data.size(); i++) {
                CollectionListBean bean = new CollectionListBean();
                JSONObject list = data.getJSONObject(i);
                bean.setEntryName(list.getString("entryName"));
                bean.setAddress(list.getString("address"));
                bean.setType(list.getString("type"));
                bean.setSysTime(list.getString("sysTime"));
                bean.setEntityId(list.getInteger("entityId"));
                bean.setEntity(list.getString("entity"));
                mDataList.add(bean);
            }

            collectRefresh.finishRefresh(0, true);
            mAdapter.setEnableLoadMore(true);
        } else {
            page++;
            loadingStatus.showContent();
            if (size > 0) {
                for (int i = 0; i < data.size(); i++) {
                    CollectionListBean bean = new CollectionListBean();
                    JSONObject list = data.getJSONObject(i);
                    bean.setEntryName(list.getString("entryName"));
                    bean.setAddress(list.getString("address"));
                    bean.setType(list.getString("type"));
                    bean.setSysTime(list.getString("sysTime"));
                    bean.setEntityId(list.getInteger("entityId"));
                    bean.setEntity(list.getString("entity"));
                    mDataList.add(bean);
                }

            }
            collectRefresh.finishLoadmore(0, true);

        }

        if (!nextPage) {
            //第一页如果不够一页就不显示没有更多数据布局
            mAdapter.loadMoreEnd();
        } else {
            mAdapter.loadMoreComplete();
        }

        mAdapter.notifyDataSetChanged();

    }
}
