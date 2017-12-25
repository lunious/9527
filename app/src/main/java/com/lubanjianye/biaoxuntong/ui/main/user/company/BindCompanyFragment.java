package com.lubanjianye.biaoxuntong.ui.main.user.company;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.bean.BindCompanyBean;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.net.RestClient;
import com.lubanjianye.biaoxuntong.net.api.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.net.callback.ISuccess;
import com.lubanjianye.biaoxuntong.ui.main.user.avater.AvaterActivity;
import com.lubanjianye.biaoxuntong.util.netStatus.AppNetworkMgr;
import com.lubanjianye.biaoxuntong.util.tosaty.Toasty;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Headers;

/**
 * 项目名:   9527
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.user.company
 * 文件名:   BindCompanyFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/21  11:47
 * 描述:     TODO
 */

public class BindCompanyFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout llIvBack = null;
    private AppCompatTextView mainBarName = null;
    private SearchView viewSearcher = null;
    RecyclerView bindCompanyRecycler = null;


    private String mProvinceCode = "510000";
    private String mqyIds = "";


    private long userId = 0;
    private String token = "";
    private String mobile = "";
    private String nickName = "";
    private String comid = "";
    private String imageUrl = "";
    private String companyName = "";

    private PromptDialog promptDialog;
    String mKeyWord = "";
    private List<String> companyList = new ArrayList<>();

    private BindCompanyAdapter mAdapter;
    private ArrayList<BindCompanyBean> mDataList = new ArrayList<>();


    @Override
    public Object setLayout() {
        return R.layout.fragment_bind_company;
    }

    @Override
    public void initView() {
        llIvBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);
        viewSearcher = getView().findViewById(R.id.view_bind);
        bindCompanyRecycler = getView().findViewById(R.id.bind_company_recycler);

        llIvBack.setOnClickListener(this);

    }

    @Override
    public void initData() {
        mainBarName.setText("绑定企业");
        llIvBack.setVisibility(View.VISIBLE);
        promptDialog = new PromptDialog(getActivity());


        //根据id-search_src_text获取TextView
        SearchView.SearchAutoComplete searchText = (SearchView.SearchAutoComplete) viewSearcher.findViewById(R.id.search_src_text);
        //修改字体大小
        searchText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);

        //修改字体颜色
        searchText.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
        searchText.setHintTextColor(ContextCompat.getColor(getContext(), R.color.search_hint));

        //根据id-search_mag_icon获取ImageView
        ImageView searchButton = (ImageView) viewSearcher.findViewById(R.id.search_mag_icon);

        searchButton.setImageResource(R.mipmap.search);

    }

    @Override
    public void initEvent() {
        initRecyclerView();
        initAdapter();
        //搜索功能
        viewSearcher.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (TextUtils.isEmpty(query.trim())) {
                    if (mDataList != null) {
                        mDataList.clear();
                    }
                    mAdapter.notifyDataSetChanged();
                    return false;
                } else {
                    mKeyWord = query.trim();
                    requestCompanyData(true);

                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText.trim())) {
                    mKeyWord = "";
                    if (mDataList != null) {
                        mDataList.clear();
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    mKeyWord = newText.trim();
                    requestCompanyData(true);
                }
                return true;
            }
        });

    }

    public void requestCompanyData(final boolean isRefresh) {

        List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();

        for (int i = 0; i < users.size(); i++) {
            userId = users.get(0).getId();
            token = users.get(0).getToken();
        }

        if (isRefresh) {
            RestClient.builder()
                    .url(BiaoXunTongApi.URL_GETSUITCOMPANY)
                    .params("name", mKeyWord)
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(Headers headers, String response) {
                            final JSONObject object = JSON.parseObject(response);
                            String status = object.getString("status");

                            if ("200".equals(status)) {

                                JSONArray data = object.getJSONArray("data");

                                if (data.size() > 0) {
                                    mqyIds = data.toString();
                                    requestData(true);
                                } else {
                                    requestData(true);
                                }

                            } else if ("500".equals(status)) {
                                Toasty.info(getContext(), "请输入关键字！", Toast.LENGTH_SHORT, true).show();
                            } else {
                                Toasty.info(getContext(), "服务器错误！", Toast.LENGTH_SHORT, true).show();
                            }

                        }
                    })
                    .build()
                    .post();

        } else {
            requestData(false);
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

    private void initRecyclerView() {

        bindCompanyRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        bindCompanyRecycler.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

                final BindCompanyBean data = (BindCompanyBean) adapter.getData().get(position);
                final String sfId = data.getSfId();

                List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();

                for (int i = 0; i < users.size(); i++) {
                    userId = users.get(0).getId();
                    token = users.get(0).getToken();
                    mobile = users.get(0).getMobile();
                    nickName = users.get(0).getNickName();
                    comid = users.get(0).getComid();
                    imageUrl = users.get(0).getImageUrl();

                }
                companyName = companyList.get(position);

                final PromptButton cancel = new PromptButton("确定", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton button) {
                        promptDialog.showLoading("绑定中...");
                        RestClient.builder()
                                .url(BiaoXunTongApi.URL_USERBINDCOMPANY)
                                .params("userId", userId)
                                .params("sf_id", sfId)
                                .success(new ISuccess() {
                                    @Override
                                    public void onSuccess(Headers headers, String response) {

                                        final JSONObject object = JSON.parseObject(response);
                                        String status = object.getString("status");
                                        String message = object.getString("message");


                                        if ("200".equals(status)) {
                                            promptDialog.dismissImmediately();
                                            Toasty.success(getContext(), message, Toast.LENGTH_SHORT, true).show();
                                            final UserProfile profile = new UserProfile(userId, mobile, nickName, token, comid, imageUrl, companyName);
                                            DatabaseManager.getInstance().getDao().update(profile);
                                            EventBus.getDefault().post(new EventMessage(EventMessage.BIND_COMPANY_SUCCESS));
                                            getActivity().onBackPressed();
                                        } else if ("500".equals(status)) {
                                            promptDialog.dismissImmediately();
                                            Toasty.success(getContext(), message, Toast.LENGTH_SHORT, true).show();
                                        } else {
                                            promptDialog.dismissImmediately();
                                            Toasty.success(getContext(), message, Toast.LENGTH_SHORT, true).show();
                                        }

                                    }


                                })
                                .build()
                                .post();

                    }
                });
                cancel.setTextColor(Color.parseColor("#00bfdc"));
                cancel.setTextSize(16);

                final PromptButton toLogin = new PromptButton("再看看", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton button) {
                    }
                });
                toLogin.setTextColor(Color.parseColor("#cccc33"));
                toLogin.setTextSize(16);
                promptDialog.getAlertDefaultBuilder().withAnim(false).cancleAble(false).touchAble(false);
                promptDialog.showWarnAlert("确定要绑定该企业吗？", toLogin, cancel, false);


            }
        });

    }

    private void initAdapter() {
        mAdapter = new BindCompanyAdapter(R.layout.fragment_company_bind_item, mDataList);
        bindCompanyRecycler.setAdapter(mAdapter);

    }

    public void requestData(final boolean isRefresh) {

        if (!AppNetworkMgr.isNetworkConnected(getActivity())) {

        } else {
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
                            String status = object.getString("status");

                            if ("200".equals(status)) {

                                final JSONArray array = object.getJSONArray("data");

                                String cpn = "";

                                for (int i = 0; i < array.size(); i++) {
                                    final JSONObject data = array.getJSONObject(i);
                                    cpn = data.getString("qy");
                                    companyList.add(cpn);
                                }

                                if (array.size() > 0) {
                                    setData(isRefresh, array);
                                } else {
                                    if (mDataList != null) {
                                        mDataList.clear();
                                        mAdapter.notifyDataSetChanged();
                                    }
                                    //TODO 内容为空的处理
                                }

                            } else {

                            }

                        }
                    })
                    .build()
                    .post();
        }

    }

    private void setData(boolean isRefresh, JSONArray data) {

        if (isRefresh) {
            mDataList.clear();
            for (int i = 0; i < data.size(); i++) {
                BindCompanyBean bean = new BindCompanyBean();
                JSONObject list = data.getJSONObject(i);
                bean.setQy(list.getString("qy"));
                bean.setLxr(list.getString("lxr"));
                bean.setEntrySign(list.getString("entrySign"));
                bean.setSfId(list.getString("sfId"));
                mDataList.add(bean);
            }
            mAdapter.setEnableLoadMore(true);
            mAdapter.notifyDataSetChanged();
        }
    }

}

