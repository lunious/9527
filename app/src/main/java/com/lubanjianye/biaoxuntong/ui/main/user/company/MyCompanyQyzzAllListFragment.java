package com.lubanjianye.biaoxuntong.ui.main.user.company;

import android.content.DialogInterface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.classic.common.MultipleStatusView;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.bean.MyCompanyQyzzAllListBean;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.loadmore.CustomLoadMoreView;
import com.lubanjianye.biaoxuntong.net.RestClient;
import com.lubanjianye.biaoxuntong.net.api.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.net.callback.ISuccess;
import com.lubanjianye.biaoxuntong.util.dialog.DialogHelper;
import com.lubanjianye.biaoxuntong.util.netStatus.AppNetworkMgr;

import java.util.ArrayList;
import java.util.List;

import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Headers;

/**
 * 项目名:   LBBXT
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.fragment.user.company
 * 文件名:   MyCompanyQyzzAllListFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/15  14:45
 * 描述:     TODO
 */

public class MyCompanyQyzzAllListFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout llIvBack = null;
    private AppCompatTextView mainBarName = null;

    private RecyclerView companyQyzzRecycler = null;
    private SwipeRefreshLayout companyQyzzRefresh = null;
    private MultipleStatusView loadingStatus = null;


    private List<String> zy_code = new ArrayList<String>();
    private String zy_id = "";

    PromptDialog promptDialog = null;


    private MyCompanyQyzzAllListAdapter mAdapter;
    private ArrayList<MyCompanyQyzzAllListBean> mDataList = new ArrayList<>();

    private int page = 1;

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
        loadingStatus = getView().findViewById(R.id.mycompany_all_qyzz_list_status_view);
        llIvBack.setOnClickListener(this);

    }

    @Override
    public void initData() {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("全部企业资质");
        //创建对象
        promptDialog = new PromptDialog(getActivity());
        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);

        initRecyclerView();
        initAdapter();
        initRefreshLayout();
        companyQyzzRefresh.setRefreshing(false);
        requestData(0);
    }

    @Override
    public void initEvent() {

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
        companyQyzzRefresh.setColorSchemeResources(
                R.color.blue,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        companyQyzzRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO 刷新数据
                mAdapter.setEnableLoadMore(false);
                requestData(1);

            }
        });

    }


    private void initRecyclerView() {

        companyQyzzRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        companyQyzzRecycler.addOnItemTouchListener(new OnItemLongClickListener() {
            @Override
            public void onSimpleItemLongClick(BaseQuickAdapter adapter, View view, int position) {

                List<String> operators = new ArrayList<>();
                operators.add("删除");

                final String[] os = new String[operators.size()];
                operators.toArray(os);

                zy_id = zy_code.get(position);

                DialogHelper.getSelectDialog(getContext(), os, getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DialogHelper.getConfirmDialog(getActivity(), "是否删除该条企业资质?",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                promptDialog.showLoading("正在删除……");
                                                RestClient.builder()
                                                        .url(BiaoXunTongApi.URL_DELEQYZZ)
                                                        .params("userId", id)
                                                        .params("zy_id", zy_id)
                                                        .success(new ISuccess() {
                                                            @Override
                                                            public void onSuccess(Headers headers, String response) {
                                                                final JSONObject object = JSON.parseObject(response);
                                                                final String status = object.getString("status");

                                                                if ("200".equals(status)) {
                                                                    promptDialog.showSuccess("删除成功！");

                                                                    if (mDataList != null) {
                                                                        mDataList.clear();
                                                                    }
                                                                    requestData(1);
                                                                } else {
                                                                    promptDialog.showError("删除失败！");
                                                                }

                                                            }
                                                        })
                                                        .build()
                                                        .post();

                                            }
                                        }).show();
                            }
                        }).show();
            }
        });

    }

    private void initAdapter() {
        mAdapter = new MyCompanyQyzzAllListAdapter(R.layout.fragment_company_qyzz, mDataList);

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                //TODO 去加载更多数据
                requestData(2);
            }
        });
        //设置列表动画
//        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        companyQyzzRecycler.setAdapter(mAdapter);


    }


    private long id = 0;

    public void requestData(final int isRefresh) {

        if (!AppNetworkMgr.isNetworkConnected(getActivity())) {
            loadingStatus.showNoNetwork();
            companyQyzzRefresh.setEnabled(false);
        } else {
            if (isRefresh == 0) {
                loadingStatus.showLoading();
            }
            if (isRefresh == 0 || isRefresh == 1) {
                page = 1;
            }

            List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
            long id = 0;
            for (int i = 0; i < users.size(); i++) {
                id = users.get(0).getId();
            }
            RestClient.builder()
                    .url(BiaoXunTongApi.URL_GETALLCOMPANYQYZZ)
                    .params("userId", id)
                    .params("type", 0)
                    .params("page", page)
                    .params("size", 10)
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(Headers headers, String response) {

                            final JSONObject object = JSON.parseObject(response);
                            String status = object.getString("status");


                            if ("200".equals(status)) {

                                final JSONObject dataObj = object.getJSONObject("data");
                                final JSONArray array = dataObj.getJSONArray("list");

                                final boolean nextPage = dataObj.getBoolean("nextpage");

                                for (int i = 0; i < array.size(); i++) {
                                    final JSONObject data = array.getJSONObject(i);
                                    zy_code.add(data.getString("zy_code"));
                                }

                                if (array.size() > 0) {
                                    setData(isRefresh, array, nextPage);
                                } else {
                                    if (mDataList != null) {
                                        mDataList.clear();
                                        mAdapter.notifyDataSetChanged();
                                    }
                                    //TODO 内容为空的处理
                                    loadingStatus.showEmpty();
                                    companyQyzzRefresh.setEnabled(false);
                                }

                            } else {
                                //TODO 请求数据失败的处理
                            }

                        }
                    })
                    .build()
                    .post();

        }


    }


    private void setData(int isRefresh, JSONArray data, boolean nextPage) {
        page++;
        final int size = data == null ? 0 : data.size();
        if (isRefresh == 0 || isRefresh == 1) {
            loadingStatus.showContent();
            mDataList.clear();
            for (int i = 0; i < data.size(); i++) {
                MyCompanyQyzzAllListBean bean = new MyCompanyQyzzAllListBean();
                JSONObject list = data.getJSONObject(i);
                bean.setLx_name(list.getString("lx_name"));
                bean.setDl_name(list.getString("dl_name"));
                bean.setXl_name(list.getString("xl_name"));
                bean.setZy_name(list.getString("zy_name"));
                bean.setDj(list.getString("dj"));
                bean.setDq(list.getString("dq"));
                mDataList.add(bean);
            }
            companyQyzzRefresh.setRefreshing(false);
            mAdapter.setEnableLoadMore(true);
            mAdapter.notifyDataSetChanged();


        } else {
            loadingStatus.showContent();
            if (size > 0) {
                for (int i = 0; i < data.size(); i++) {
                    MyCompanyQyzzAllListBean bean = new MyCompanyQyzzAllListBean();
                    JSONObject list = data.getJSONObject(i);
                    bean.setLx_name(list.getString("lx_name"));
                    bean.setDl_name(list.getString("dl_name"));
                    bean.setXl_name(list.getString("xl_name"));
                    bean.setZy_name(list.getString("zy_name"));
                    bean.setDj(list.getString("dj"));
                    bean.setDq(list.getString("dq"));
                    mDataList.add(bean);
                }
                mAdapter.notifyDataSetChanged();
            }

            if (!nextPage) {
                //第一页如果不够一页就不显示没有更多数据布局
                mAdapter.loadMoreEnd();
            } else {
                mAdapter.loadMoreComplete();
            }
        }

    }
}
