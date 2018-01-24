package com.lubanjianye.biaoxuntong.ui.main.index;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.lubanjianye.biaoxuntong.bean.IndexListBean;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.api.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.loadmore.CustomLoadMoreView;
import com.lubanjianye.biaoxuntong.ui.browser.BrowserActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.IndexBxtgdjDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.IndexScgggDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.IndexSggjyDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.IndexSggjycgrowDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.IndexSggjycgtableDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.IndexXcgggDetailActivity;
import com.lubanjianye.biaoxuntong.util.aes.AesUtil;
import com.lubanjianye.biaoxuntong.util.loader.GlideImageLoader;
import com.lubanjianye.biaoxuntong.util.netStatus.NetUtil;
import com.lubanjianye.biaoxuntong.util.netStatus.AppSysMgr;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名:   LBBXT
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.index
 * 文件名:   IndexListFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/16  10:06
 * 描述:     TODO
 */

public class IndexListFragment extends BaseFragment {

    private RecyclerView indexRecycler = null;
    private SmartRefreshLayout indexRefresh = null;
    private MultipleStatusView loadingStatus = null;


    Banner indexItemBanner = null;
    private String mTitle = null;
    private String deviceId = AppSysMgr.getPsuedoUniqueID();


    private IndexListAdapter mAdapter = null;
    private ArrayList<IndexListBean> mDataList = new ArrayList<>();

    private int page = 1;
    private boolean isInitCache = false;

    public String getTitle() {
        return mTitle;
    }

    //轮播图
    private void initBanner() {
        final List<String> urls = new ArrayList<>();

        urls.add(url_1);
        urls.add(url_2);
        urls.add(url_3);

        if (indexItemBanner != null) {
            indexItemBanner.setIndicatorGravity(BannerConfig.RIGHT);
        }
        indexItemBanner.setImages(urls).setImageLoader(new GlideImageLoader()).start();
        indexItemBanner.setDelayTime(4000);


    }

    private void initRefreshLayout() {


        indexRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                if (!NetUtil.isNetworkConnected(getActivity())) {
                    ToastUtil.shortBottonToast(getContext(), "请检查网络设置");
                    indexRefresh.finishRefresh(2000, false);
                    mAdapter.setEnableLoadMore(false);
                } else {
                    requestData(true);
                }
            }
        });


//        indexRefresh.autoRefresh();


    }


    private void initRecyclerView() {

        indexRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        indexRecycler.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                final IndexListBean data = (IndexListBean) adapter.getData().get(position);
                final int entityId = data.getEntityId();
                final String entity = data.getEntity();

                Log.d("BASJHDHJSADASDA", entity + entityId);

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
                } else if ("sggjycgrow".equals(entity)) {
                    intent = new Intent(BiaoXunTong.getApplicationContext(), IndexSggjycgrowDetailActivity.class);
                    intent.putExtra("entityId", entityId);
                    intent.putExtra("entity", entity);
                    intent.putExtra("ajaxlogtype", "0");
                    intent.putExtra("mId", "");
                    startActivity(intent);
                } else if ("scggg".equals(entity)) {
                    intent = new Intent(BiaoXunTong.getApplicationContext(), IndexScgggDetailActivity.class);
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

        mTitle = getArguments().getString("title");

        if ("最新标讯".equals(mTitle)) {
            mAdapter = new IndexListAdapter(R.layout.fragment_index_zxbx_item, mDataList);
            mAdapter.addHeaderView(getHeaderView());
            getImage();
            indexItemBanner.setVisibility(View.VISIBLE);
        } else {
            mAdapter = new IndexListAdapter(R.layout.fragment_index_item, mDataList);
        }


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
        indexRecycler.setAdapter(mAdapter);


    }


    @Override
    public Object setLayout() {
        return R.layout.fragment_index_list;
    }

    @Override
    public void initView() {
        indexRecycler = getView().findViewById(R.id.index_recycler);
        indexRefresh = getView().findViewById(R.id.index_refresh);
        loadingStatus = getView().findViewById(R.id.index_list_status_view);

    }

    @Override
    public void initData() {
        initRecyclerView();
        initAdapter();
        initRefreshLayout();

    }

    @Override
    public void initEvent() {

        if (!NetUtil.isNetworkConnected(getActivity())) {
            ToastUtil.shortBottonToast(getContext(), "请检查网络设置");
            requestData(true);
            mAdapter.setEnableLoadMore(false);
        } else {
            requestData(true);
        }

    }

    private long id = 0;

    String url_1 = "";
    String url_2 = "";
    String url_3 = "";

    String detail_1 = "";
    String detail_2 = "";
    String detail_3 = "";

    public View getHeaderView() {
        View view = View.inflate(getContext(), R.layout.index_header_item, null);
        indexItemBanner = (Banner) view.findViewById(R.id.index_item_banner);
        return view;
    }

    public void getImage() {

        OkGo.<String>post(BiaoXunTongApi.URL_GETINDEXBANNER)
                .params("imgType", 1)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final JSONObject object = JSON.parseObject(response.body());

                        String status = object.getString("status");
                        String message = object.getString("message");

                        if ("200".equals(status)) {
                            final JSONObject data = object.getJSONObject("data");
                            url_1 = data.getString("imgOnePath");
                            url_2 = data.getString("imgTwoPath");
                            url_3 = data.getString("imgThreePath");
                            detail_1 = data.getString("imgOneUrl");
                            detail_2 = data.getString("imgTwoUrl");
                            detail_3 = data.getString("imgThreeUrl");

                        } else {
                            ToastUtil.shortToast(getContext(), message);
                        }
                        initBanner();
                    }
                });

        indexItemBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

                Intent intent = null;

                if (position == 0) {
                    toShare(0, "我正在使用【鲁班标讯通】,推荐给你", "企业资质、人员资格、业绩、信用奖惩、经营风险、法律诉讼一键查询！", detail_1);
                } else if (position == 1) {
                    intent = new Intent(getActivity(), BrowserActivity.class);
                    intent.putExtra("url", detail_2);
                    intent.putExtra("title", "鲁班建业通-招投标神器");
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), BrowserActivity.class);
                    intent.putExtra("url", detail_3);
                    intent.putExtra("title", "鲁班建业通-招投标神器");
                    startActivity(intent);
                }
            }
        });
    }

    public void requestData(final boolean isRefresh) {

        if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
            //已登录的数据请求
            List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
            for (int i = 0; i < users.size(); i++) {
                id = users.get(0).getId();
            }

            if (isRefresh) {
                page = 1;
                OkGo.<String>post(BiaoXunTongApi.URL_GETINDEXLIST)
                        .params("type", mTitle)
                        .params("userid", id)
                        .params("page", page)
                        .params("size", 10)
                        .params("deviceId", deviceId)
                        .cacheKey("index_list_login_cache" + mTitle)
                        .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                        .cacheTime(3600 * 72000)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {

                                String jiemi = AesUtil.aesDecrypt(response.body(), BiaoXunTongApi.PAS_KEY);

                                final JSONObject object = JSON.parseObject(jiemi);
                                final JSONObject data = object.getJSONObject("data");
                                final String status = object.getString("status");
                                final String message = object.getString("message");
                                final JSONArray array = data.getJSONArray("list");
                                final boolean nextPage = data.getBoolean("nextpage");

                                if ("200".equals(status)) {
                                    if (array.size() > 0) {
                                        page = 2;
                                        setData(isRefresh, array, nextPage);
                                    } else {
                                        if (mDataList != null) {
                                            mDataList.clear();
                                            mAdapter.notifyDataSetChanged();
                                        }
                                        //TODO 内容为空的处理
                                        loadingStatus.showEmpty();
                                        indexRefresh.setEnableRefresh(false);
                                    }
                                } else {
                                    ToastUtil.shortToast(getContext(), message);
                                }
                            }

                            @Override
                            public void onCacheSuccess(Response<String> response) {
                                if (!isInitCache) {
                                    String jiemi = AesUtil.aesDecrypt(response.body(), BiaoXunTongApi.PAS_KEY);

                                    final JSONObject object = JSON.parseObject(jiemi);
                                    final JSONObject data = object.getJSONObject("data");
                                    final String status = object.getString("status");
                                    final String message = object.getString("message");
                                    final JSONArray array = data.getJSONArray("list");
                                    final boolean nextPage = data.getBoolean("nextpage");

                                    if ("200".equals(status)) {
                                        if (array.size() > 0) {
                                            setData(isRefresh, array, nextPage);
                                        } else {
                                            if (mDataList != null) {
                                                mDataList.clear();
                                                mAdapter.notifyDataSetChanged();
                                            }
                                            //TODO 内容为空的处理
                                            loadingStatus.showEmpty();
                                            indexRefresh.setEnableRefresh(false);
                                        }
                                    } else {
                                        ToastUtil.shortToast(getContext(), message);
                                    }
                                    isInitCache = true;
                                }
                            }
                        });
            } else {
                OkGo.<String>post(BiaoXunTongApi.URL_GETINDEXLIST)
                        .params("type", mTitle)
                        .params("userid", id)
                        .params("page", page)
                        .params("size", 10)
                        .params("deviceId", deviceId)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {

                                String jiemi = AesUtil.aesDecrypt(response.body(), BiaoXunTongApi.PAS_KEY);

                                final JSONObject object = JSON.parseObject(jiemi);
                                final JSONObject data = object.getJSONObject("data");
                                final String status = object.getString("status");
                                final String message = object.getString("message");
                                final JSONArray array = data.getJSONArray("list");
                                final boolean nextPage = data.getBoolean("nextpage");

                                if ("200".equals(status)) {
                                    if (array.size() > 0) {
                                        setData(isRefresh, array, nextPage);
                                    } else {
                                        if (mDataList != null) {
                                            mDataList.clear();
                                            mAdapter.notifyDataSetChanged();
                                        }
                                        //TODO 内容为空的处理
                                        loadingStatus.showEmpty();
                                        indexRefresh.setEnableRefresh(false);
                                    }
                                } else {
                                    ToastUtil.shortToast(getContext(), message);
                                }
                            }
                        });
            }


        } else {
            //未登录的数据请求

            if (isRefresh) {
                page = 1;
                OkGo.<String>post(BiaoXunTongApi.URL_GETINDEXLIST)
                        .params("type", mTitle)
                        .params("page", page)
                        .params("size", 10)
                        .params("deviceId", deviceId)
                        .cacheKey("index_list_no_login_cache" + mTitle)
                        .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                        .cacheTime(3600 * 72000)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String jiemi = AesUtil.aesDecrypt(response.body(), BiaoXunTongApi.PAS_KEY);

                                final JSONObject object = JSON.parseObject(jiemi);
                                final JSONObject data = object.getJSONObject("data");
                                final String status = object.getString("status");
                                final String message = object.getString("message");
                                final JSONArray array = data.getJSONArray("list");
                                final boolean nextPage = data.getBoolean("nextpage");

                                if ("200".equals(status)) {
                                    if (array.size() > 0) {
                                        page = 2;
                                        setData(isRefresh, array, nextPage);
                                    } else {
                                        if (mDataList != null) {
                                            mDataList.clear();
                                            mAdapter.notifyDataSetChanged();
                                        }
                                        //TODO 内容为空的处理
                                        loadingStatus.showEmpty();
                                        indexRefresh.setEnableRefresh(false);
                                    }
                                } else {
                                    ToastUtil.shortToast(getContext(), message);
                                }

                            }

                            @Override
                            public void onCacheSuccess(Response<String> response) {
                                if (!isInitCache) {
                                    String jiemi = AesUtil.aesDecrypt(response.body(), BiaoXunTongApi.PAS_KEY);

                                    final JSONObject object = JSON.parseObject(jiemi);
                                    final JSONObject data = object.getJSONObject("data");
                                    final String status = object.getString("status");
                                    final String message = object.getString("message");
                                    final JSONArray array = data.getJSONArray("list");
                                    final boolean nextPage = data.getBoolean("nextpage");

                                    if ("200".equals(status)) {
                                        if (array.size() > 0) {
                                            page = 2;
                                            setData(isRefresh, array, nextPage);
                                        } else {
                                            if (mDataList != null) {
                                                mDataList.clear();
                                                mAdapter.notifyDataSetChanged();
                                            }
                                            //TODO 内容为空的处理
                                            loadingStatus.showEmpty();
                                            indexRefresh.setEnableRefresh(false);
                                        }
                                    } else {
                                        ToastUtil.shortToast(getContext(), message);
                                    }
                                    isInitCache = true;
                                }
                            }
                        });
            } else {
                OkGo.<String>post(BiaoXunTongApi.URL_GETINDEXLIST)
                        .params("type", mTitle)
                        .params("page", page)
                        .params("size", 10)
                        .params("deviceId", deviceId)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String jiemi = AesUtil.aesDecrypt(response.body(), BiaoXunTongApi.PAS_KEY);

                                final JSONObject object = JSON.parseObject(jiemi);
                                final JSONObject data = object.getJSONObject("data");
                                final String status = object.getString("status");
                                final String message = object.getString("message");
                                final JSONArray array = data.getJSONArray("list");
                                final boolean nextPage = data.getBoolean("nextpage");

                                if ("200".equals(status)) {
                                    if (array.size() > 0) {
                                        setData(isRefresh, array, nextPage);
                                    } else {
                                        if (mDataList != null) {
                                            mDataList.clear();
                                            mAdapter.notifyDataSetChanged();
                                        }
                                        //TODO 内容为空的处理
                                        loadingStatus.showEmpty();
                                        indexRefresh.setEnableRefresh(false);
                                    }
                                } else {
                                    ToastUtil.shortToast(getContext(), message);
                                }
                            }
                        });
            }


        }


    }

    private void setData(boolean isRefresh, JSONArray data, boolean nextPage) {
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            loadingStatus.showContent();
            mDataList.clear();
            for (int i = 0; i < data.size(); i++) {
                IndexListBean bean = new IndexListBean();
                JSONObject list = data.getJSONObject(i);
                bean.setEntryName(list.getString("entryName"));
                bean.setSysTime(list.getString("sysTime"));
                bean.setEntityId(list.getInteger("entityId"));
                bean.setEntity(list.getString("entity"));
                bean.setDeadTime(list.getString("deadTime"));
                bean.setAddress(list.getString("address"));
                if ("最新标讯".equals(mTitle)) {
                    bean.setType(list.getString("type"));
                }
                bean.setSignstauts(list.getString("signstauts"));
                mDataList.add(bean);
            }
            indexRefresh.finishRefresh(0, true);
            mAdapter.setEnableLoadMore(true);

        } else {
            page++;
            loadingStatus.showContent();
            if (size > 0) {
                for (int i = 0; i < data.size(); i++) {
                    IndexListBean bean = new IndexListBean();
                    JSONObject list = data.getJSONObject(i);
                    bean.setEntryName(list.getString("entryName"));
                    bean.setSysTime(list.getString("sysTime"));
                    bean.setEntityId(list.getInteger("entityId"));
                    bean.setEntity(list.getString("entity"));
                    bean.setDeadTime(list.getString("deadTime"));
                    bean.setAddress(list.getString("address"));
                    if ("最新标讯".equals(mTitle)) {
                        bean.setType(list.getString("type"));
                    }
                    bean.setSignstauts(list.getString("signstauts"));
                    mDataList.add(bean);
                }

            }
            indexRefresh.finishLoadmore(0, true);
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
