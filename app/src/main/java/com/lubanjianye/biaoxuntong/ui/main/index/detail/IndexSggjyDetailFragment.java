package com.lubanjianye.biaoxuntong.ui.main.index.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.classic.common.MultipleStatusView;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.net.RestClient;
import com.lubanjianye.biaoxuntong.net.api.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.net.callback.ISuccess;
import com.lubanjianye.biaoxuntong.sign.SignInActivity;
import com.lubanjianye.biaoxuntong.util.aes.AesUtil;
import com.lubanjianye.biaoxuntong.util.netStatus.NetUtil;
import com.lubanjianye.biaoxuntong.util.netStatus.AppSysMgr;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import okhttp3.Headers;

/**
 * 项目名:   Lunioussky
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.collection.detail
 * 文件名:   IndexSggjyDetailFragment
 * 创建者:   lunious
 * 创建时间: 2017/10/23  23:57
 * 描述:     TODO
 */

public class IndexSggjyDetailFragment extends BaseFragment implements View.OnClickListener {


    LinearLayout llIvBack = null;
    AppCompatTextView mainBarName = null;
    MultipleStatusView indexSggjyDetailStatusView = null;
    TextView tvMainTitle = null;
    TextView tvMainArea = null;
    TextView tvPubTime = null;
    TextView tvDeadTime = null;
    TextView tv1 = null;
    TextView tv2 = null;
    TextView tv3 = null;
    TextView tv4 = null;
    TextView tv5 = null;
    TextView tv6 = null;
    TextView tv7 = null;
    ImageView ivFav = null;
    LinearLayout llFav = null;
    LinearLayout llShare = null;
    NestedScrollView detailNsv = null;

    private static final String ARG_ENTITYID = "ARG_ENTITYID";
    private static final String ARG_ENTITY = "ARG_ENTITY";


    private int myFav = -1;
    private int mEntityId = -1;
    private String mEntity = "";

    private String shareTitle = "";
    private String shareContent = "";
    private String shareUrl = "";

    private String deviceId = AppSysMgr.getPsuedoUniqueID();


    public static IndexSggjyDetailFragment create(@NonNull int entityId, String entity) {
        final Bundle args = new Bundle();
        args.putInt(ARG_ENTITYID, entityId);
        args.putString(ARG_ENTITY, entity);
        final IndexSggjyDetailFragment fragment = new IndexSggjyDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle args = getArguments();
        if (args != null) {
            mEntityId = args.getInt(ARG_ENTITYID);
            mEntity = args.getString(ARG_ENTITY);
        }
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_index_sggjy_detail;
    }

    @Override
    public void initView() {
        llIvBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);
        indexSggjyDetailStatusView = getView().findViewById(R.id.index_sggjy_detail_status_view);
        tvMainTitle = getView().findViewById(R.id.tv_main_title);
        tvMainArea = getView().findViewById(R.id.tv_main_area);
        tvPubTime = getView().findViewById(R.id.tv_pub_time);
        tvDeadTime = getView().findViewById(R.id.tv_dead_time);
        tv1 = getView().findViewById(R.id.tv1);
        tv2 = getView().findViewById(R.id.tv2);
        tv3 = getView().findViewById(R.id.tv3);
        tv4 = getView().findViewById(R.id.tv4);
        tv5 = getView().findViewById(R.id.tv5);
        tv6 = getView().findViewById(R.id.tv6);
        tv7 = getView().findViewById(R.id.tv7);
        ivFav = getView().findViewById(R.id.iv_fav);
        llFav = getView().findViewById(R.id.ll_fav);
        llShare = getView().findViewById(R.id.ll_share);
        detailNsv = getView().findViewById(R.id.detail_nsv);

        llIvBack.setOnClickListener(this);
        llShare.setOnClickListener(this);
        llFav.setOnClickListener(this);
    }

    @Override
    public void initData() {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("标讯详情");
        indexSggjyDetailStatusView.setOnRetryClickListener(mRetryClickListener);
    }

    @Override
    public void initEvent() {
        requestData();
        initNsv();
    }


    //点击重试
    final View.OnClickListener mRetryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            requestData();
        }
    };


    private void initNsv() {
        detailNsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    // 向下滑动
                    if (mainBarName != null) {
                        mainBarName.setText(shareTitle);
                    }

                }

                if (scrollY < oldScrollY) {
                    // 向上滑动
                }

                if (scrollY == 0) {
                    // 顶部
                    mainBarName.setText("标讯详情");
                }

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    // 底部
                    mainBarName.setText(shareTitle);
                }
            }
        });
    }


    private long id = 0;
    private String nickName = "";
    private String token = "";
    private String comid = "";
    private String imageUrl = "";

    private void requestData() {

        if (!NetUtil.isNetworkConnected(getActivity())) {
            indexSggjyDetailStatusView.showNoNetwork();
        } else {
            indexSggjyDetailStatusView.showLoading();


            if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
                List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();

                for (int i = 0; i < users.size(); i++) {
                    id = users.get(0).getId();
                    nickName = users.get(0).getNickName();
                    token = users.get(0).getToken();
                    comid = users.get(0).getComid();
                    imageUrl = users.get(0).getImageUrl();
                }

                RestClient.builder()
                        .url(BiaoXunTongApi.URL_GETCOLLECTIONLISTDETAIL)
                        .params("entityId", mEntityId)
                        .params("entity", mEntity)
                        .params("userid", id)
                        .params("deviceId", deviceId)
//                        .params("token", id + "_" + token)
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(Headers headers, String response) {

                                String jiemi = AesUtil.aesDecrypt(response, BiaoXunTongApi.PAS_KEY);

                                //判断是否收藏过
                                final JSONObject object = JSON.parseObject(jiemi);
                                String status = object.getString("status");
                                int favorite = object.getInteger("favorite");

                                if ("200".equals(status)) {
                                    if (favorite == 1) {
                                        myFav = 1;
                                        ivFav.setImageResource(R.mipmap.ic_faved_pressed);
                                    } else if (favorite == 0) {
                                        myFav = 0;
                                        ivFav.setImageResource(R.mipmap.ic_fav_pressed);
                                    }
                                    final JSONObject data = object.getJSONObject("data");
                                    String reportTitle = data.getString("reportTitle");
                                    shareUrl = data.getString("url");
                                    shareTitle = reportTitle;
                                    if (!TextUtils.isEmpty(reportTitle)) {
                                        tvMainTitle.setText(reportTitle);
                                    } else {
                                        tvMainTitle.setText("/");
                                    }
                                    String area = data.getString("area");
                                    if (!TextUtils.isEmpty(area)) {
                                        tvMainArea.setText(area);
                                    } else {
                                        tvMainArea.setText("/");
                                    }
                                    String sysTime = data.getString("sysTime");
                                    if (!TextUtils.isEmpty(sysTime)) {
                                        tvPubTime.setText(sysTime);
                                    } else {
                                        tvPubTime.setText("/");
                                    }
                                    String deadTime = data.getString("deadTime");
                                    if (!TextUtils.isEmpty(deadTime)) {
                                        tvDeadTime.setText(deadTime);
                                    } else {
                                        tvDeadTime.setText("/");
                                    }
                                    String squone = data.getString("squone");
                                    if (!TextUtils.isEmpty(squone)) {
                                        //将所有<*a标签替换成html标签
                                        squone = squone.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        squone = squone.replace("\n", "<br/>");
                                        tv1.setText(Html.fromHtml(squone));
                                        shareContent = squone;
                                    }
                                    String squtwo = data.getString("squtwo");
                                    if (!TextUtils.isEmpty(squtwo)) {
                                        //将所有<*a标签替换成html标签
                                        squtwo = squtwo.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        squtwo = squtwo.replace("\n", "<br/>");
                                        tv2.setText(Html.fromHtml(squtwo));
                                    }
                                    String squthree = data.getString("squthree");
                                    if (!TextUtils.isEmpty(squthree)) {
                                        //将所有<*a标签替换成html标签
                                        squthree = squthree.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        squthree = squthree.replace("\n", "<br/>");
                                        tv3.setText(Html.fromHtml(squthree));
                                    }
                                    String squfour = data.getString("squfour");
                                    if (!TextUtils.isEmpty(squfour)) {
                                        //将所有<*a标签替换成html标签
                                        squfour = squfour.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        squfour = squfour.replace("\n", "<br/>");
                                        tv4.setText(Html.fromHtml(squfour));
                                    }
                                    String squfive = data.getString("squfive");
                                    if (!TextUtils.isEmpty(squfive)) {
                                        //将所有<*a标签替换成html标签
                                        squfive = squfive.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        squfive = squfive.replace("\n", "<br/>");
                                        tv5.setText(Html.fromHtml(squfive));
                                    }
                                    String squsix = data.getString("squsix");
                                    if (!TextUtils.isEmpty(squsix)) {
                                        //将所有<*a标签替换成html标签
                                        squsix = squsix.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        squsix = squsix.replace("\n", "<br/>");
                                        tv6.setText(Html.fromHtml(squsix));
                                    }
                                    String squseven = data.getString("squseven");
                                    if (!TextUtils.isEmpty(squseven)) {
                                        //将所有<*a标签替换成html标签
                                        squseven = squseven.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        squseven = squseven.replace("\n", "<br/>");
                                        tv7.setText(Html.fromHtml(squseven));
                                    }
                                    indexSggjyDetailStatusView.showContent();
                                } else {
                                    indexSggjyDetailStatusView.showError();
                                }

                            }
                        })
                        .build()
                        .post();
            } else {
                RestClient.builder()
                        .url(BiaoXunTongApi.URL_GETCOLLECTIONLISTDETAIL)
                        .params("entityId", mEntityId)
                        .params("entity", mEntity)
                        .params("deviceId", deviceId)
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(Headers headers, String response) {
                                String jiemi = AesUtil.aesDecrypt(response, BiaoXunTongApi.PAS_KEY);

                                final JSONObject object = JSON.parseObject(jiemi);
                                String status = object.getString("status");

                                if ("200".equals(status)) {
                                    indexSggjyDetailStatusView.showContent();
                                    final JSONObject data = object.getJSONObject("data");
                                    String reportTitle = data.getString("reportTitle");
                                    shareUrl = data.getString("url");
                                    shareTitle = reportTitle;
                                    if (!TextUtils.isEmpty(reportTitle)) {
                                        tvMainTitle.setText(reportTitle);
                                    } else {
                                        tvMainTitle.setText("/");
                                    }
                                    String area = data.getString("area");
                                    if (!TextUtils.isEmpty(area)) {
                                        tvMainArea.setText(area);
                                    } else {
                                        tvMainArea.setText("/");
                                    }
                                    String sysTime = data.getString("sysTime");
                                    if (!TextUtils.isEmpty(sysTime)) {
                                        tvPubTime.setText(sysTime);
                                    } else {
                                        tvPubTime.setText("/");
                                    }
                                    String deadTime = data.getString("deadTime");
                                    if (!TextUtils.isEmpty(deadTime)) {
                                        tvDeadTime.setText(deadTime);
                                    } else {
                                        tvDeadTime.setText("/");
                                    }
                                    String squone = data.getString("squone");
                                    if (!TextUtils.isEmpty(squone)) {
                                        //将所有<*a标签替换成html标签
                                        squone = squone.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        squone = squone.replace("\n", "<br/>");
                                        tv1.setText(Html.fromHtml(squone));
                                        shareContent = squone;
                                    }
                                    String squtwo = data.getString("squtwo");
                                    if (!TextUtils.isEmpty(squtwo)) {
                                        //将所有<*a标签替换成html标签
                                        squtwo = squtwo.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        squtwo = squtwo.replace("\n", "<br/>");
                                        tv2.setText(Html.fromHtml(squtwo));
                                    }
                                    String squthree = data.getString("squthree");
                                    if (!TextUtils.isEmpty(squthree)) {
                                        //将所有<*a标签替换成html标签
                                        squthree = squthree.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        squthree = squthree.replace("\n", "<br/>");
                                        tv3.setText(Html.fromHtml(squthree));
                                    }
                                    String squfour = data.getString("squfour");
                                    if (!TextUtils.isEmpty(squfour)) {
                                        //将所有<*a标签替换成html标签
                                        squfour = squfour.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        squfour = squfour.replace("\n", "<br/>");
                                        tv4.setText(Html.fromHtml(squfour));
                                    }
                                    String squfive = data.getString("squfive");
                                    if (!TextUtils.isEmpty(squfive)) {
                                        //将所有<*a标签替换成html标签
                                        squfive = squfive.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        squfive = squfive.replace("\n", "<br/>");
                                        tv5.setText(Html.fromHtml(squfive));
                                    }
                                    String squsix = data.getString("squsix");
                                    if (!TextUtils.isEmpty(squsix)) {
                                        //将所有<*a标签替换成html标签
                                        squsix = squsix.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        squsix = squsix.replace("\n", "<br/>");
                                        tv6.setText(Html.fromHtml(squsix));
                                    }
                                    String squseven = data.getString("squseven");
                                    if (!TextUtils.isEmpty(squseven)) {
                                        //将所有<*a标签替换成html标签
                                        squseven = squseven.replace("<*", "<font color='black'><u>").replace("</*", "</u></font>");
                                        squseven = squseven.replace("\n", "<br/>");
                                        tv7.setText(Html.fromHtml(squseven));
                                    }

                                } else {
                                    indexSggjyDetailStatusView.showError();
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
            case R.id.ll_iv_back:
                getActivity().onBackPressed();
                break;
            case R.id.ll_share:
                toShare(mEntityId, shareTitle, shareContent, BiaoXunTongApi.SHARE_URL + shareUrl);
                break;
            case R.id.ll_fav:

                if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
                    //已登录处理事件
                    List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
                    long id = 0;
                    for (int i = 0; i < users.size(); i++) {
                        id = users.get(0).getId();
                    }

                    if (myFav == 1) {
                        RestClient.builder()
                                .url(BiaoXunTongApi.URL_DELEFAV)
                                .params("entityid", mEntityId)
                                .params("entity", mEntity)
                                .params("userid", id)
                                .params("deviceId", deviceId)
                                .success(new ISuccess() {
                                    @Override
                                    public void onSuccess(Headers headers, String response) {
                                        final JSONObject object = JSON.parseObject(response);
                                        String status = object.getString("status");
                                        if ("200".equals(status)) {
                                            myFav = 0;
                                            ivFav.setImageResource(R.mipmap.ic_fav_pressed);
                                            ToastUtil.shortToast(getContext(),"取消收藏");
                                            EventBus.getDefault().post(new EventMessage(EventMessage.CLICK_FAV));
                                        } else if ("500".equals(status)) {
                                            ToastUtil.shortToast(getContext(),"服务器异常");
                                        }
                                    }
                                })
                                .build()
                                .post();
                    } else if (myFav == 0) {
                        RestClient.builder()
                                .url(BiaoXunTongApi.URL_ADDFAV)
                                .params("entityid", mEntityId)
                                .params("entity", mEntity)
                                .params("userid", id)
                                .params("deviceId", deviceId)
                                .success(new ISuccess() {
                                    @Override
                                    public void onSuccess(Headers headers, String response) {
                                        final JSONObject object = JSON.parseObject(response);
                                        String status = object.getString("status");
                                        if ("200".equals(status)) {
                                            myFav = 1;
                                            ivFav.setImageResource(R.mipmap.ic_faved_pressed);
                                            ToastUtil.shortToast(getContext(),"收藏成功");
                                            EventBus.getDefault().post(new EventMessage(EventMessage.CLICK_FAV));
                                        } else if ("500".equals(status)) {
                                            ToastUtil.shortToast(getContext(),"服务器异常");
                                        }
                                    }
                                })
                                .build()
                                .post();
                    }
                } else {
                    //未登录去登陆
                    startActivity(new Intent(getActivity(), SignInActivity.class));
                }
                break;
            default:
                break;
        }
    }
}
