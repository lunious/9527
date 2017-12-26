package com.lubanjianye.biaoxuntong.ui.main.index;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.flyco.tablayout.SlidingTabLayout;
import com.igexin.sdk.PushManager;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.net.RestClient;
import com.lubanjianye.biaoxuntong.net.api.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.net.callback.ISuccess;
import com.lubanjianye.biaoxuntong.sign.SignInActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.search.IndexSearchActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.sortcolumn.SortColumnActivity;
import com.lubanjianye.biaoxuntong.util.netStatus.NetUtil;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Headers;

import static com.lubanjianye.biaoxuntong.app.BiaoXunTong.getApplicationContext;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.fragment
 * 文件名:   IndexTabFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/9  0:33
 * 描述:     TODO
 */

public class IndexTabFragment extends BaseFragment implements View.OnClickListener, BDLocationListener {

    private SlidingTabLayout indexStlTab = null;
    private ViewPager indexVp = null;
    private LinearLayout llSearch = null;
    private ImageView ivAdd = null;


    private PromptDialog promptDialog;

    public LocationClient mLocationClient = null;

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        String city = bdLocation.getCity();

        Log.d("CITY", "city==" + city);

    }


    private String clientID = PushManager.getInstance().getClientid(getApplicationContext());

    private IndexFragmentAdapter mAdapter = null;

    private final List<String> mList = new ArrayList<String>();


    private long userId = 0;
    private String mobile = null;
    private String nickName = null;
    private String token = null;
    private String comid = null;
    private String imageUrl = null;
    private String companyName = null;


    @Override
    public Object setLayout() {
        return R.layout.fragment_main_index;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //取消注册EventBus
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView() {

        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(IndexTabFragment.this);
        //注册监听函数
        LocationClientOption option = new LocationClientOption();

        option.setIsNeedAddress(true);
        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true

        mLocationClient.setLocOption(option);


        //注册EventBus
        EventBus.getDefault().register(this);

        indexStlTab = getView().findViewById(R.id.index_stl_tab);
        indexVp = getView().findViewById(R.id.index_vp);
        llSearch = getView().findViewById(R.id.ll_search);
        ivAdd = getView().findViewById(R.id.iv_add);

        llSearch.setOnClickListener(this);
        ivAdd.setOnClickListener(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXXXXX(EventMessage message) {

        if (EventMessage.LOGIN_SUCCSS.equals(message.getMessage()) || EventMessage.LOGIN_OUT.equals(message.getMessage())
                || EventMessage.TAB_CHANGE.equals(message.getMessage())) {
            //更新UI
            if (indexStlTab != null) {
                indexStlTab.setCurrentTab(0);
                indexStlTab.setViewPager(indexVp);

            }
            requestData();

        } else {
            //TODO
        }
    }

    @Override
    public void initData() {
        //创建对象
        promptDialog = new PromptDialog(getActivity());
    }

    @Override
    public void initEvent() {
        requestData();
    }

    public void requestData() {

        mLocationClient.start();

        if (!NetUtil.isNetworkConnected(getContext())) {
            ToastUtil.shortToast(getContext(), "网络出错，请检查网络设置！");

            if (mList.size() > 0) {
                mList.clear();
            }

            mList.add("最新标讯");
            mList.add("施工");
            mList.add("监理");
            mList.add("勘察");
            mList.add("设计");
            mList.add("政府采购");
            mList.add("行业资讯");

            mAdapter = new IndexFragmentAdapter(getContext(), getFragmentManager(), mList);
            mAdapter.notifyDataSetChanged();
            indexVp.setAdapter(mAdapter);
            indexStlTab.setViewPager(indexVp);
            mAdapter.notifyDataSetChanged();

        } else {

            if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
                //得到用个户userId
                List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
                for (int i = 0; i < users.size(); i++) {
                    userId = users.get(0).getId();
                    token = users.get(0).getToken();
                }

                RestClient.builder().url(BiaoXunTongApi.URL_CHECKTOKEN)
                        .params("userId", userId)
                        .params("token", token)
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(Headers headers, String response) {

                                if ("200".equals(response) || "400".equals(response)) {
                                    RestClient.builder()
                                            .url(BiaoXunTongApi.URL_INDEXTAB)
                                            .params("userId", userId)
                                            .params("clientId", clientID)
                                            .success(new ISuccess() {
                                                @Override
                                                public void onSuccess(Headers headers, String response) {

                                                    final JSONObject object = JSON.parseObject(response);
                                                    String status = object.getString("status");
                                                    String message = object.getString("message");

                                                    if ("200".equals(status)) {
                                                        final JSONArray ownerList = object.getJSONArray("data");

                                                        if (mList.size() > 0) {
                                                            mList.clear();
                                                        }

                                                        for (int i = 0; i < ownerList.size(); i++) {
                                                            final JSONObject list = ownerList.getJSONObject(i);
                                                            String name = list.getString("name");
                                                            mList.add(name);
                                                        }

                                                        mAdapter = new IndexFragmentAdapter(getContext(), getFragmentManager(), mList);
                                                        mAdapter.notifyDataSetChanged();
                                                        indexVp.setAdapter(mAdapter);
                                                        indexStlTab.setViewPager(indexVp);

                                                    } else {
                                                        ToastUtil.shortToast(getContext(), message);
                                                    }

                                                }
                                            })
                                            .build()
                                            .post();

                                } else {


                                    //后台退出登陆
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
                                                }
                                            })
                                            .build()
                                            .post();


                                    RestClient.builder()
                                            .url(BiaoXunTongApi.URL_INDEXTAB)
                                            .params("clientId", clientID)
                                            .success(new ISuccess() {
                                                @Override
                                                public void onSuccess(Headers headers, String response) {
                                                    final JSONObject object = JSON.parseObject(response);
                                                    String status = object.getString("status");
                                                    String message = object.getString("message");

                                                    if ("200".equals(status)) {
                                                        final JSONArray ownerList = object.getJSONArray("data");

                                                        if (mList.size() > 0) {
                                                            mList.clear();
                                                        }

                                                        for (int i = 0; i < ownerList.size(); i++) {
                                                            final JSONObject list = ownerList.getJSONObject(i);
                                                            String name = list.getString("name");
                                                            mList.add(name);
                                                        }


                                                        mAdapter = new IndexFragmentAdapter(getContext(), getFragmentManager(), mList);
                                                        mAdapter.notifyDataSetChanged();
                                                        indexVp.setAdapter(mAdapter);
                                                        indexStlTab.setViewPager(indexVp);

                                                    } else {
                                                        ToastUtil.shortToast(getContext(), message);
                                                    }

                                                }
                                            })
                                            .build()
                                            .post();


                                    final PromptButton cancel = new PromptButton("取      消", new PromptButtonListener() {
                                        @Override
                                        public void onClick(PromptButton button) {
                                            RestClient.builder()
                                                    .url(BiaoXunTongApi.URL_INDEXTAB)
                                                    .params("clientId", clientID)
                                                    .success(new ISuccess() {
                                                        @Override
                                                        public void onSuccess(Headers headers, String response) {
                                                            final JSONObject object = JSON.parseObject(response);
                                                            String status = object.getString("status");
                                                            String message = object.getString("message");

                                                            if ("200".equals(status)) {
                                                                final JSONArray ownerList = object.getJSONArray("data");

                                                                if (mList.size() > 0) {
                                                                    mList.clear();
                                                                }

                                                                for (int i = 0; i < ownerList.size(); i++) {
                                                                    final JSONObject list = ownerList.getJSONObject(i);
                                                                    String name = list.getString("name");
                                                                    mList.add(name);
                                                                }


                                                                mAdapter = new IndexFragmentAdapter(getContext(), getFragmentManager(), mList);
                                                                mAdapter.notifyDataSetChanged();
                                                                indexVp.setAdapter(mAdapter);
                                                                indexStlTab.setViewPager(indexVp);

                                                            } else {
                                                                ToastUtil.shortToast(getContext(), message);
                                                            }

                                                        }
                                                    })
                                                    .build()
                                                    .post();

                                        }
                                    });
                                    cancel.setTextColor(Color.parseColor("#cccc33"));
                                    cancel.setTextSize(16);

                                    final PromptButton toLogin = new PromptButton("重新登陆", new PromptButtonListener() {
                                        @Override
                                        public void onClick(PromptButton button) {
                                            startActivity(new Intent(getActivity(), SignInActivity.class));
                                        }
                                    });
                                    toLogin.setTextColor(Color.parseColor("#00bfdc"));
                                    toLogin.setTextSize(16);
                                    promptDialog.getAlertDefaultBuilder().withAnim(false).cancleAble(false).touchAble(false);
                                    promptDialog.showWarnAlert("账号登陆过期！", toLogin, cancel, false);

                                }
                            }
                        })
                        .build()
                        .post();

            } else {
                RestClient.builder()
                        .url(BiaoXunTongApi.URL_INDEXTAB)
                        .params("clientId", clientID)
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(Headers headers, String response) {
                                final JSONObject object = JSON.parseObject(response);
                                String status = object.getString("status");
                                String message = object.getString("message");

                                if ("200".equals(status)) {
                                    final JSONArray ownerList = object.getJSONArray("data");

                                    if (mList.size() > 0) {
                                        mList.clear();
                                    }

                                    for (int i = 0; i < ownerList.size(); i++) {
                                        final JSONObject list = ownerList.getJSONObject(i);
                                        String name = list.getString("name");
                                        mList.add(name);
                                    }

                                    mAdapter = new IndexFragmentAdapter(getContext(), getFragmentManager(), mList);
                                    mAdapter.notifyDataSetChanged();
                                    indexVp.setAdapter(mAdapter);
                                    indexStlTab.setViewPager(indexVp);
                                } else {
                                    ToastUtil.shortToast(getContext(), message);
                                }

                            }
                        })
                        .build()
                        .post();
            }

        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_search:
                startActivity(new Intent(getActivity(), IndexSearchActivity.class));
                break;
            case R.id.iv_add:
                startActivity(new Intent(getActivity(), SortColumnActivity.class));
                break;
            default:
                break;
        }
    }
}
