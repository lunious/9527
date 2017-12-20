package com.lubanjianye.biaoxuntong.ui.main.result.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.lubanjianye.biaoxuntong.ui.browser.BrowserActivity;
import com.lubanjianye.biaoxuntong.util.AppConfig;
import com.lubanjianye.biaoxuntong.util.netStatus.AppSysMgr;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.tosaty.Toasty;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import okhttp3.Headers;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.fragment.result.detail
 * 文件名:   ResultXjgggDetailFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/12  20:49
 * 描述:     TODO
 */

public class ResultXjgggDetailFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout llIvBack = null;
    private AppCompatTextView mainBarName = null;
    private MultipleStatusView xjgggDetailStatusView = null;
    private AppCompatTextView tvMainTitle = null;
    private AppCompatTextView tvMainArea = null;
    private AppCompatTextView tvMainPubType = null;
    private AppCompatTextView tvMainPubMethod = null;
    private AppCompatTextView tvMainPubData = null;
    private AppCompatTextView tvMainPubTime = null;
    private AppCompatTextView tvPuNum = null;
    private AppCompatTextView tvOwerCainame = null;
    private AppCompatTextView tvOwerName = null;
    private AppCompatTextView tvOwerDaili = null;
    private AppCompatTextView tvOwerBaoshu = null;
    private AppCompatTextView tvOwerJine = null;
    private AppCompatTextView tvOwerBaojia = null;
    private AppCompatTextView tvOwerMingdan = null;
    private AppCompatTextView tvOwerLianxi = null;
    private AppCompatTextView tvOwerLianxi2 = null;
    private AppCompatTextView tvOwerLianxiNumber = null;
    private AppCompatTextView tvOwerLianxiLink = null;
    private AppCompatTextView tvOwerPinshen = null;
    private ImageView ivFav = null;
    private LinearLayout llFav = null;
    private LinearLayout llShare = null;
    private NestedScrollView detailNsv = null;


    private static final String ARG_ENTITYID = "ARG_ENTITYID";
    private static final String ARG_ENTITY = "ARG_ENTITY";


    private int myFav = -1;
    private int mEntityId = -1;
    private String mEntity = "";
    private String shareTitle = "";
    private String shareContent = "";
    private String shareUrl = "";
    private String deviceId = AppSysMgr.getPsuedoUniqueID();


    @Override
    public Object setLayout() {
        return R.layout.fragment_result_xjggg_detail;
    }


    public static ResultXjgggDetailFragment create(@NonNull int entityId, String entity) {
        final Bundle args = new Bundle();
        args.putInt(ARG_ENTITYID, entityId);
        args.putString(ARG_ENTITY, entity);
        final ResultXjgggDetailFragment fragment = new ResultXjgggDetailFragment();
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
    public void initView() {
        llIvBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);
        xjgggDetailStatusView = getView().findViewById(R.id.xjggg_detail_status_view);
        tvMainTitle = getView().findViewById(R.id.tv_main_title);
        tvMainArea = getView().findViewById(R.id.tv_main_area);
        tvMainPubType = getView().findViewById(R.id.tv_main_pub_type);
        tvMainPubMethod = getView().findViewById(R.id.tv_main_pub_method);
        tvMainPubData = getView().findViewById(R.id.tv_main_pub_data);
        tvMainPubTime = getView().findViewById(R.id.tv_main_pub_time);
        tvPuNum = getView().findViewById(R.id.tv_pu_num);
        tvOwerCainame = getView().findViewById(R.id.tv_ower_cainame);
        tvOwerName = getView().findViewById(R.id.tv_ower_name);
        tvOwerDaili = getView().findViewById(R.id.tv_ower_daili);
        tvOwerBaoshu = getView().findViewById(R.id.tv_ower_baoshu);
        tvOwerJine = getView().findViewById(R.id.tv_ower_jine);
        tvOwerBaojia = getView().findViewById(R.id.tv_ower_baojia);
        tvOwerMingdan = getView().findViewById(R.id.tv_ower_mingdan);
        tvOwerLianxi = getView().findViewById(R.id.tv_ower_lianxi);
        tvOwerLianxi2 = getView().findViewById(R.id.tv_ower_lianxi2);
        tvOwerLianxiNumber = getView().findViewById(R.id.tv_ower_lianxi_number);
        tvOwerLianxiLink = getView().findViewById(R.id.tv_ower_lianxi_link);
        tvOwerPinshen = getView().findViewById(R.id.tv_ower_pinshen);
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
        mainBarName.setText("政府采购结果公告详情");
        xjgggDetailStatusView.setOnRetryClickListener(mRetryClickListener);
    }

    @Override
    public void initEvent() {
        requestData();
        initNsv();
    }

    private void initNsv() {
        detailNsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    // 向下滑动
                    mainBarName.setText(shareTitle);
                }

                if (scrollY < oldScrollY) {
                    // 向上滑动
                }

                if (scrollY == 0) {
                    // 顶部
                    mainBarName.setText("政府采购结果公告详情");
                }

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    // 底部
                    mainBarName.setText(shareTitle);
                }
            }
        });
    }

    //点击重试
    final View.OnClickListener mRetryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            requestData();
        }
    };

    private void requestData() {

        if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
            long id = 0;
            //已登录时的数据请求
            List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
            for (int i = 0; i < users.size(); i++) {
                id = users.get(0).getId();
            }
            RestClient.builder()
                    .url(BiaoXunTongApi.URL_GETRESULTLISTDETAIL)
                    .params("entityId", mEntityId)
                    .params("entity", mEntity)
                    .params("userid", id)
                    .params("deviceId", deviceId)
//                        .params("token", id + "_" + token)
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(Headers headers, String response) {
                            //判断是否收藏过
                            final JSONObject object = JSON.parseObject(response);
                            String status = object.getString("status");
                            int favorite = object.getInteger("favorite");
                            if (favorite == 1) {
                                myFav = 1;
                                ivFav.setImageResource(R.drawable.ic_faved);
                            } else if (favorite == 0) {
                                myFav = 0;
                                ivFav.setImageResource(R.drawable.ic_fav);
                            }

                            if ("200".equals(status)) {
                                final JSONObject data = object.getJSONObject("data");
                                String reportTitle = data.getString("reportTitle");
                                shareTitle = reportTitle;
                                if (!TextUtils.isEmpty(reportTitle)) {
                                    tvMainTitle.setText(reportTitle);
//                                            mainBarName.setText(reportTitle);
                                } else {
                                    tvMainTitle.setText("/");
                                    mainBarName.setText("政府采购结果公告详情");
                                }
                                String area = data.getString("administrativeDivision");
                                if (!TextUtils.isEmpty(area)) {
                                    tvMainArea.setVisibility(View.VISIBLE);
                                    tvMainArea.setText(area);
                                } else {
                                    tvMainArea.setText("/");
                                }
                                String resource = data.getString("resource");
                                if (!TextUtils.isEmpty(resource)) {
                                    tvMainPubType.setText(resource);
                                } else {
                                    tvMainPubType.setText("/");
                                }
                                String purchasingType = data.getString("purchasingType");
                                if (!TextUtils.isEmpty(purchasingType)) {
                                    tvMainPubMethod.setText(purchasingType);
                                } else {
                                    tvMainPubMethod.setText("/");
                                }
                                String calibrationTime = data.getString("calibrationTime");
                                if (!TextUtils.isEmpty(calibrationTime)) {
                                    tvMainPubData.setText(calibrationTime.substring(0, 10));
                                } else {
                                    tvMainPubData.setText("/");
                                }
                                String noticeTime = data.getString("noticeTime");
                                if (!TextUtils.isEmpty(noticeTime)) {
                                    tvMainPubTime.setText(noticeTime.substring(0, 10));
                                } else {
                                    tvMainPubTime.setText("/");
                                }
                                String entryNum = data.getString("entryNum");
                                if (!TextUtils.isEmpty(entryNum)) {
                                    tvPuNum.setText(entryNum);
                                } else {
                                    tvPuNum.setText("/");
                                }
                                String entryName = data.getString("entryName");
                                shareContent = entryName;
                                if (!TextUtils.isEmpty(entryName)) {
                                    tvOwerCainame.setText(entryName);
                                } else {
                                    tvOwerCainame.setText("/");
                                }
                                String purchaser = data.getString("purchaser");
                                if (!TextUtils.isEmpty(purchaser)) {
                                    tvOwerName.setText(purchaser);
                                } else {
                                    tvOwerName.setText("/");
                                }
                                String purchasingAgent = data.getString("purchasingAgent");
                                if (!TextUtils.isEmpty(purchasingAgent)) {
                                    tvOwerDaili.setText(purchasingAgent);
                                } else {
                                    tvOwerDaili.setText("/");
                                }
                                String noticeCount = data.getString("noticeCount");
                                if (!TextUtils.isEmpty(noticeCount)) {
                                    tvOwerBaoshu.setText(noticeCount);
                                } else {
                                    tvOwerBaoshu.setText("/");
                                }
                                String allTotal = data.getString("allTotal");
                                if (!TextUtils.isEmpty(allTotal)) {
                                    tvOwerJine.setText(allTotal);
                                } else {
                                    tvOwerJine.setText("/");
                                }
                                String eachPackage = data.getString("eachPackage");
                                if (!TextUtils.isEmpty(eachPackage)) {
                                    tvOwerBaojia.setText(eachPackage);
                                } else {
                                    tvOwerBaojia.setText("/");
                                }
                                String memberList = data.getString("memberList");
                                if (!TextUtils.isEmpty(memberList)) {
                                    tvOwerMingdan.setText(memberList);
                                } else {
                                    tvOwerMingdan.setText("/");
                                }
                                String purchaserContact = data.getString("purchaserContact");
                                if (!TextUtils.isEmpty(purchaserContact)) {
                                    tvOwerLianxi.setText(purchaserContact);
                                } else {
                                    tvOwerLianxi.setText("/");
                                }
                                String purchasingAgentContact = data.getString("purchasingAgentContact");
                                if (!TextUtils.isEmpty(purchasingAgentContact)) {
                                    tvOwerLianxi2.setText(purchasingAgentContact);
                                } else {
                                    tvOwerLianxi2.setText("/");
                                }
                                String nameAndphone = data.getString("nameAndphone");
                                if (!TextUtils.isEmpty(nameAndphone)) {
                                    tvOwerLianxiNumber.setText(nameAndphone);
                                } else {
                                    tvOwerLianxiNumber.setText("/");
                                }
                                final String link = data.getString("link");

                                shareUrl = link;
                                if (!TextUtils.isEmpty(link)) {
                                    tvOwerLianxiLink.setText("点击查看详情");
                                    tvOwerLianxiLink.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getActivity(), BrowserActivity.class);
                                            intent.putExtra("url", link);
                                            intent.putExtra("title", shareTitle);
                                            startActivity(intent);
                                        }
                                    });
                                } else {
                                    tvOwerLianxiLink.setText("/");
                                }
                                final String reviewSituation = data.getString("reviewSituation");

                                if (!TextUtils.isEmpty(reviewSituation)) {
                                    tvOwerPinshen.setText("点击查看或下载附件");
                                    tvOwerPinshen.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AppConfig.openExternalBrowser(getContext(), reviewSituation);
                                        }
                                    });

                                } else {
                                    tvOwerPinshen.setText("/");
                                }
                                xjgggDetailStatusView.showContent();
                            } else {
                                xjgggDetailStatusView.showError();
                            }
                        }
                    })
                    .build()
                    .post();
        } else {
            //未登录时的数据请求
            RestClient.builder()
                    .url(BiaoXunTongApi.URL_GETRESULTLISTDETAIL)
                    .params("entityId", mEntityId)
                    .params("entity", mEntity)
                    .params("deviceId", deviceId)
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(Headers headers, String response) {
                            final JSONObject object = JSON.parseObject(response);
                            String status = object.getString("status");
                            final JSONObject data = object.getJSONObject("data");
                            if ("200".equals(status)) {
                                xjgggDetailStatusView.showContent();
                                String reportTitle = data.getString("reportTitle");
                                shareTitle = reportTitle;
                                if (!TextUtils.isEmpty(reportTitle)) {
                                    tvMainTitle.setText(reportTitle);
//                                            mainBarName.setText(reportTitle);
                                } else {
                                    tvMainTitle.setText("/");
                                    mainBarName.setText("政府采购结果公告详情");
                                }
                                String area = data.getString("administrativeDivision");
                                if (!TextUtils.isEmpty(area)) {
                                    tvMainArea.setVisibility(View.VISIBLE);
                                    tvMainArea.setText(area);
                                } else {
                                    tvMainArea.setText("/");
                                }
                                String resource = data.getString("resource");
                                if (!TextUtils.isEmpty(resource)) {
                                    tvMainPubType.setText(resource);
                                } else {
                                    tvMainPubType.setText("/");
                                }
                                String purchasingType = data.getString("purchasingType");
                                if (!TextUtils.isEmpty(purchasingType)) {
                                    tvMainPubMethod.setText(purchasingType);
                                } else {
                                    tvMainPubMethod.setText("/");
                                }
                                String calibrationTime = data.getString("calibrationTime");
                                if (!TextUtils.isEmpty(calibrationTime)) {
                                    tvMainPubData.setText(calibrationTime.substring(0, 10));
                                } else {
                                    tvMainPubData.setText("/");
                                }
                                String noticeTime = data.getString("noticeTime");
                                if (!TextUtils.isEmpty(noticeTime)) {
                                    tvMainPubTime.setText(noticeTime.substring(0, 10));
                                } else {
                                    tvMainPubTime.setText("/");
                                }
                                String entryNum = data.getString("entryNum");
                                if (!TextUtils.isEmpty(entryNum)) {
                                    tvPuNum.setText(entryNum);
                                } else {
                                    tvPuNum.setText("/");
                                }
                                String entryName = data.getString("entryName");
                                shareContent = entryName;
                                if (!TextUtils.isEmpty(entryName)) {
                                    tvOwerCainame.setText(entryName);
                                } else {
                                    tvOwerCainame.setText("/");
                                }
                                String purchaser = data.getString("purchaser");
                                if (!TextUtils.isEmpty(purchaser)) {
                                    tvOwerName.setText(purchaser);
                                } else {
                                    tvOwerName.setText("/");
                                }
                                String purchasingAgent = data.getString("purchasingAgent");
                                if (!TextUtils.isEmpty(purchasingAgent)) {
                                    tvOwerDaili.setText(purchasingAgent);
                                } else {
                                    tvOwerDaili.setText("/");
                                }
                                String noticeCount = data.getString("noticeCount");
                                if (!TextUtils.isEmpty(noticeCount)) {
                                    tvOwerBaoshu.setText(noticeCount);
                                } else {
                                    tvOwerBaoshu.setText("/");
                                }
                                String allTotal = data.getString("allTotal");
                                if (!TextUtils.isEmpty(allTotal)) {
                                    tvOwerJine.setText(allTotal);
                                } else {
                                    tvOwerJine.setText("/");
                                }
                                String eachPackage = data.getString("eachPackage");
                                if (!TextUtils.isEmpty(eachPackage)) {
                                    tvOwerBaojia.setText(eachPackage);
                                } else {
                                    tvOwerBaojia.setText("/");
                                }
                                String memberList = data.getString("memberList");
                                if (!TextUtils.isEmpty(memberList)) {
                                    tvOwerMingdan.setText(memberList);
                                } else {
                                    tvOwerMingdan.setText("/");
                                }
                                String purchaserContact = data.getString("purchaserContact");
                                if (!TextUtils.isEmpty(purchaserContact)) {
                                    tvOwerLianxi.setText(purchaserContact);
                                } else {
                                    tvOwerLianxi.setText("/");
                                }
                                String purchasingAgentContact = data.getString("purchasingAgentContact");
                                if (!TextUtils.isEmpty(purchasingAgentContact)) {
                                    tvOwerLianxi2.setText(purchasingAgentContact);
                                } else {
                                    tvOwerLianxi2.setText("/");
                                }
                                String nameAndphone = data.getString("nameAndphone");
                                if (!TextUtils.isEmpty(nameAndphone)) {
                                    tvOwerLianxiNumber.setText(nameAndphone);
                                } else {
                                    tvOwerLianxiNumber.setText("/");
                                }
                                final String link = data.getString("link");

                                tvOwerLianxiLink.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getActivity(), BrowserActivity.class);
                                        intent.putExtra("url", link);
                                        intent.putExtra("title", shareTitle);
                                        startActivity(intent);
                                    }
                                });
                                shareUrl = link;
                                if (!TextUtils.isEmpty(link)) {
                                    tvOwerLianxiLink.setText(link);
                                } else {
                                    tvOwerLianxiLink.setText("/");
                                }
                                final String reviewSituation = data.getString("reviewSituation");

                                tvOwerPinshen.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AppConfig.openExternalBrowser(getContext(), reviewSituation);
                                    }
                                });

                                if (!TextUtils.isEmpty(reviewSituation)) {
                                    tvOwerPinshen.setText(reviewSituation);
                                } else {
                                    tvOwerPinshen.setText("/");
                                }
                            } else {
                                xjgggDetailStatusView.showError();
                            }

                        }
                    })
                    .build()
                    .post();
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_iv_back:
                getActivity().finish();
                break;
            case R.id.ll_share:
                toShare(mEntityId, shareTitle, shareContent, shareUrl);
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
                                .success(new ISuccess() {
                                    @Override
                                    public void onSuccess(Headers headers, String response) {
                                        final JSONObject object = JSON.parseObject(response);
                                        String status = object.getString("status");
                                        if ("200".equals(status)) {
                                            myFav = 0;
                                            ivFav.setImageResource(R.drawable.ic_fav);
                                            Toasty.success(getContext(), "取消收藏", Toast.LENGTH_SHORT, true).show();
                                            EventBus.getDefault().post(new EventMessage(EventMessage.CLICK_FAV));
                                        } else if ("500".equals(status)) {
                                            Toasty.error(getContext(), "服务器异常", Toast.LENGTH_SHORT, true).show();
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
                                .success(new ISuccess() {
                                    @Override
                                    public void onSuccess(Headers headers, String response) {
                                        final JSONObject object = JSON.parseObject(response);
                                        String status = object.getString("status");
                                        if ("200".equals(status)) {
                                            myFav = 1;
                                            ivFav.setImageResource(R.drawable.ic_faved);
                                            Toasty.success(getContext(), "收藏成功", Toast.LENGTH_SHORT, true).show();
                                            EventBus.getDefault().post(new EventMessage(EventMessage.CLICK_FAV));
                                        } else if ("500".equals(status)) {
                                            Toasty.error(getContext(), "服务器异常", Toast.LENGTH_SHORT, true).show();
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
