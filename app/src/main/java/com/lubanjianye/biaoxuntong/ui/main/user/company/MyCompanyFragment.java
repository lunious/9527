package com.lubanjianye.biaoxuntong.ui.main.user.company;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.app.BiaoXunTong;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.bean.MyCompanyQyzzAllListBean;
import com.lubanjianye.biaoxuntong.bean.MyCompanyRyzzAllListBean;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.net.RestClient;
import com.lubanjianye.biaoxuntong.net.api.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.net.callback.ISuccess;

import java.util.ArrayList;
import java.util.List;

import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Headers;

/**
 * 项目名:   LBBXT
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.fragment.user.company
 * 文件名:   MyCompanyFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/15  14:22
 * 描述:     TODO
 */

public class MyCompanyFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout llIvBack = null;
    private AppCompatTextView mainBarName = null;
    private LinearLayout llIbAdd = null;

    private AppCompatTextView tvMyCompany = null;
    private LinearLayout llCompanyName = null;
    private RecyclerView rlvQyzz = null;
    private RecyclerView rlvRyzz = null;
    private AppCompatTextView tvRyzzTip = null;
    private AppCompatTextView tvQyzzTip = null;
    private LinearLayout llMoreQyzz = null;
    private LinearLayout llMoreRyzz = null;
    private AppCompatTextView qyzzCount = null;
    private AppCompatTextView ryzzCount = null;


    private PromptDialog promptDialog = null;

    private MyCompanyQyzzAllListAdapter mQyzzAdapter = null;
    private ArrayList<MyCompanyQyzzAllListBean> mQyzzDataList = new ArrayList<>();

    private MyCompanyRyzzAllListAdapter mRyzzAdapter = null;
    private ArrayList<MyCompanyRyzzAllListBean> mRyzzDataList = new ArrayList<>();


    private int page = 1;


    private long id = 0;
    private String nickName = "";
    private String mobile = "";
    private String token = "";
    private String comid = "";
    private String imageUrl = "";
    private String companyName = "";


    private List<String> zzbm = new ArrayList<String>();
    private List<String> zgzy_code = new ArrayList<String>();
    private List<String> ryname = new ArrayList<String>();
    private String zzbm_id = "";
    private String zgzy_id = "";
    private String name = "";


    private List<String> zy_code = new ArrayList<String>();
    private String zy_id = "";

    @Override
    public Object setLayout() {
        return R.layout.fragment_my_company;
    }

    @Override
    public void initView() {
        llIvBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);
        llIbAdd = getView().findViewById(R.id.ll_ib_add);
        tvMyCompany = getView().findViewById(R.id.tv_my_company);
        llCompanyName = getView().findViewById(R.id.ll_company_name);
        rlvQyzz = getView().findViewById(R.id.rlv_qyzz);
        rlvRyzz = getView().findViewById(R.id.rlv_ryzz);
        tvRyzzTip = getView().findViewById(R.id.tv_ryzz_tip);
        tvQyzzTip = getView().findViewById(R.id.tv_qyzz_tip);
        llMoreQyzz = getView().findViewById(R.id.ll_more_qyzz);
        llMoreRyzz = getView().findViewById(R.id.ll_more_ryzz);
        qyzzCount = getView().findViewById(R.id.qyzz_count);
        ryzzCount = getView().findViewById(R.id.ryzz_count);
        llIvBack.setOnClickListener(this);
        llMoreQyzz.setOnClickListener(this);
        llMoreRyzz.setOnClickListener(this);
        llIbAdd.setOnClickListener(this);

    }

    @Override
    public void initData() {
        llIbAdd.setVisibility(View.VISIBLE);
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("我的资质");
        //创建对象
        promptDialog = new PromptDialog(getActivity());
        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);
        List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();

        for (int i = 0; i < users.size(); i++) {
            id = users.get(0).getId();
            nickName = users.get(0).getNickName();
            mobile = users.get(0).getMobile();
            token = users.get(0).getToken();
            comid = users.get(0).getComid();
            imageUrl = users.get(0).getImageUrl();
            companyName = users.get(0).getCompanyName();
        }


    }

    @Override
    public void initEvent() {
        initQyzzRecyclerView();
        initRyzzRecyclerView();
        initAdapter();
        getCompanyName();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_iv_back:
                getActivity().onBackPressed();
                break;
            case R.id.ll_ib_add:
                PromptButton cancle = new PromptButton("取消", null);
                cancle.setTextColor(Color.parseColor("#00bfdc"));
                cancle.setTextSize(16);

                PromptButton xzryzz = new PromptButton("新增人员资质", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton promptButton) {
//                promptButton.setTextColor(Color.RED);
                        //新增人员资质界面
                        startActivity(new Intent(getActivity(), AddRyzzActivity.class));
                    }
                });
                xzryzz.setTextColor(Color.parseColor("#6d6d6d"));
                xzryzz.setTextSize(16);
                PromptButton xzqyzz = new PromptButton("新增企业资质", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton promptButton) {
//                promptButton.setTextColor(Color.RED);
                        //新增企业资质界面
                        startActivity(new Intent(getActivity(), AddQyzzActivity.class));
                    }
                });
                xzqyzz.setTextColor(Color.parseColor("#6d6d6d"));
                xzqyzz.setTextSize(16);
                //跳转到选择页面
                promptDialog.showAlertSheet("", true, cancle, xzryzz, xzqyzz);
                break;
            case R.id.ll_more_qyzz:
                startActivity(new Intent(getActivity(), MyCompanyQyzzAllListActivity.class));
                break;
            case R.id.ll_more_ryzz:
                startActivity(new Intent(getActivity(), MyCompanyRyzzAllListActivity.class));
                break;
            default:
                break;

        }
    }

    private void getCompanyName() {


        if (!TextUtils.isEmpty(companyName)) {
            tvMyCompany.setText(companyName);
        } else {
            tvMyCompany.setText("");
        }

        requestQyzzData();
        requestRyzzData();

    }

    private void initAdapter() {
        mQyzzAdapter = new MyCompanyQyzzAllListAdapter(R.layout.fragment_company_qyzz, mQyzzDataList);
        rlvQyzz.setAdapter(mQyzzAdapter);

        mRyzzAdapter = new MyCompanyRyzzAllListAdapter(R.layout.fragment_company_ryzz, mRyzzDataList);
        rlvRyzz.setAdapter(mRyzzAdapter);

    }


    private void initQyzzRecyclerView() {

        rlvQyzz.setLayoutManager(new GridLayoutManager(getContext(), 1));


    }

    private void initRyzzRecyclerView() {

        rlvRyzz.setLayoutManager(new GridLayoutManager(getContext(), 1));


    }


    public void requestQyzzData() {

        List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
        long id = 0;
        for (int i = 0; i < users.size(); i++) {
            id = users.get(0).getId();
        }
        RestClient.builder()
                .url(BiaoXunTongApi.URL_GETALLCOMPANYQYZZ)
                .params("userId", id)
                .params("type", 0)
                .params("size", 5)
                .params("page", page)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(Headers headers, String response) {

                        final JSONObject object = JSON.parseObject(response);
                        String status = object.getString("status");

                        if ("200".equals(status)) {

                            final JSONArray array = object.getJSONObject("data").getJSONArray("list");
                            final int count = object.getJSONObject("data").getInteger("count");
                            for (int i = 0; i < array.size(); i++) {
                                final JSONObject data = array.getJSONObject(i);
                                zy_code.add(data.getString("zy_code"));
                            }

                            if (array.size() >= 5) {
                                if (llMoreQyzz != null) {
                                    llMoreQyzz.setVisibility(View.VISIBLE);
                                }
                                qyzzCount.setText(count + "");
                            } else {
                                if (llMoreQyzz != null) {
                                    llMoreQyzz.setVisibility(View.GONE);
                                }

                            }

                            if (array.size() > 0) {
                                if (tvQyzzTip != null) {
                                    tvQyzzTip.setVisibility(View.GONE);
                                }

                                for (int i = 0; i < array.size(); i++) {
                                    MyCompanyQyzzAllListBean bean = new MyCompanyQyzzAllListBean();
                                    JSONObject list = array.getJSONObject(i);
                                    bean.setLx_name(list.getString("lx_name"));
                                    bean.setDl_name(list.getString("dl_name"));
                                    bean.setXl_name(list.getString("xl_name"));
                                    bean.setZy_name(list.getString("zy_name"));
                                    bean.setDj(list.getString("dj"));
                                    bean.setDq(list.getString("dq"));
                                    mQyzzDataList.add(bean);
                                }

                                mQyzzAdapter.notifyDataSetChanged();

                            } else {
                                if (tvQyzzTip != null) {
                                    tvQyzzTip.setVisibility(View.VISIBLE);
                                }

                            }

                        } else {
                        }

                    }
                })
                .build()
                .post();


    }

    public void requestRyzzData() {

        List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
        long id = 0;
        String token = "";
        for (int i = 0; i < users.size(); i++) {
            id = users.get(0).getId();
            token = users.get(0).getToken();
        }

        RestClient.builder()
                .url(BiaoXunTongApi.URL_GETALLCOMPANYRYZZ)
                .params("userId", id)
                .params("type", 0)
                .params("size", 5)
                .params("page", page)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(Headers headers, String response) {

                        Log.d("DASUDGUAISBDASD", response);

                        final JSONObject object = JSON.parseObject(response);
                        String status = object.getString("status");

                        if ("200".equals(status)) {

                            final JSONArray array = object.getJSONObject("data").getJSONArray("list");
                            final int count = object.getJSONObject("data").getInteger("count");
                            for (int i = 0; i < array.size(); i++) {
                                final JSONObject data = array.getJSONObject(i);
                                zzbm.add(data.getString("zzbm"));
                                zgzy_code.add(data.getString("zgzy_code"));
                                ryname.add(data.getString("ryname"));
                            }


                            if (array.size() >= 5) {
                                if (llMoreRyzz != null) {
                                    llMoreRyzz.setVisibility(View.VISIBLE);
                                }

                                ryzzCount.setText(count + "");

                            } else {
                                if (llMoreRyzz != null) {
                                    llMoreRyzz.setVisibility(View.GONE);
                                }

                            }


                            if (array.size() > 0) {


                                if (tvRyzzTip != null) {
                                    tvRyzzTip.setVisibility(View.GONE);
                                }

                                for (int i = 0; i < array.size(); i++) {
                                    MyCompanyRyzzAllListBean bean = new MyCompanyRyzzAllListBean();
                                    JSONObject list = array.getJSONObject(i);
                                    bean.setLx_name(list.getString("lx_name"));
                                    bean.setRyname(list.getString("ryname"));
                                    bean.setZg_mcdj(list.getString("zg_mcdj"));
                                    bean.setZg_name(list.getString("zg_name"));
                                    bean.setZgzy(list.getString("zgzy"));
                                    mRyzzDataList.add(bean);
                                }

                                mRyzzAdapter.notifyDataSetChanged();
                            } else {
                                if (tvRyzzTip != null) {
                                    tvRyzzTip.setVisibility(View.VISIBLE);
                                }

                            }
                        } else {
                        }

                    }
                })
                .build()
                .post();
    }


    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        if (promptDialog != null) {
            promptDialog.dismissImmediately();
        }

    }

}
