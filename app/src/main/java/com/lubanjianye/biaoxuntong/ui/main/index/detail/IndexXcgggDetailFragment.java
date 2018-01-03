package com.lubanjianye.biaoxuntong.ui.main.index.detail;

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
import com.lubanjianye.biaoxuntong.ui.browser.BrowserActivity;
import com.lubanjianye.biaoxuntong.ui.share.OpenBuilder;
import com.lubanjianye.biaoxuntong.ui.share.OpenConstant;
import com.lubanjianye.biaoxuntong.ui.share.Share;
import com.lubanjianye.biaoxuntong.util.AppConfig;
import com.lubanjianye.biaoxuntong.util.aes.AesUtil;
import com.lubanjianye.biaoxuntong.util.dialog.PromptDialog;
import com.lubanjianye.biaoxuntong.util.netStatus.NetUtil;
import com.lubanjianye.biaoxuntong.util.netStatus.AppSysMgr;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import okhttp3.Headers;

/**
 * 项目名:   Lunioussky
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.index.detail
 * 文件名:   IndexXcgggDetailFragment
 * 创建者:   lunious
 * 创建时间: 2017/10/26  22:09
 * 描述:     TODO
 */

public class IndexXcgggDetailFragment extends BaseFragment implements View.OnClickListener, OpenBuilder.Callback {

    LinearLayout llIvBack = null;
    AppCompatTextView mainBarName = null;
    TextView tvMianDingbiaoTime = null;
    MultipleStatusView xcgggDetailStatusView = null;
    TextView tvMainTitle = null;
    TextView tvMainArea = null;
    TextView tvMainCaigouMethod = null;
    TextView tvMainPubTime = null;
    TextView tvPubNum = null;
    TextView tvOwerCainame = null;
    TextView tvOwerName = null;
    TextView tvOwerDaili = null;
    TextView tvOwerBaoshu = null;
    TextView tvOwerJine = null;
    TextView tvOwerBaojia = null;
    TextView tvOwerMingdan = null;
    TextView tvOwerLianxi = null;
    TextView tvOwerLianxi2 = null;
    TextView tvOwerLianxiNumber = null;
    TextView tvOwerLianxiLink = null;
    TextView tvOwerPinshen = null;
    TextView tvOwerA = null;
    TextView tvOwerB = null;
    TextView tvOwerC = null;
    TextView tvOwerD = null;
    TextView tvOwerE = null;
    TextView tvOwerF = null;
    TextView tvOwerG = null;
    TextView tvOwerH = null;
    ImageView ivFav = null;
    LinearLayout llFav = null;
    LinearLayout llMainCaigouMethod = null;
    LinearLayout llMianDingbiaoTime = null;
    LinearLayout llMainPubTime = null;
    LinearLayout llPubNum = null;
    LinearLayout llOwerCainame = null;
    LinearLayout llOwerName = null;
    LinearLayout llOwerDaili = null;
    LinearLayout llOwerBaoshu = null;
    LinearLayout llOwerJine = null;
    LinearLayout llOwerBaojia = null;
    LinearLayout llOwerMingdan = null;
    LinearLayout llOwerLianxi = null;
    LinearLayout llOwerLianxi2 = null;
    LinearLayout llOwerLianxiNumber = null;
    LinearLayout llOwerLianxiLink = null;
    LinearLayout llOwerPinshen = null;
    LinearLayout llOwerA = null;
    LinearLayout llOwerB = null;
    LinearLayout llOwerC = null;
    LinearLayout llOwerD = null;
    LinearLayout llOwerE = null;
    LinearLayout llOwerF = null;
    LinearLayout llOwerG = null;
    LinearLayout llOwerH = null;
    NestedScrollView detailNsv = null;
    LinearLayout llShare = null;

    private LinearLayout llWeiBoShare = null;
    private LinearLayout llQQBoShare = null;
    private LinearLayout llWeixinBoShare = null;
    private LinearLayout llPyqShare = null;


    private static final String ARG_ENTITYID = "ARG_ENTITYID";
    private static final String ARG_ENTITY = "ARG_ENTITY";
    private static final String ARG_AJAXTYPE = "ARG_AJAXTYPE";


    private int myFav = -1;
    private int mEntityId = -1;
    private String mEntity = "";

    private String shareTitle = "";
    private String shareContent = "";
    private String shareUrl = "";

    private String deviceId = AppSysMgr.getPsuedoUniqueID();
    private String ajaxlogtype = "";


    public static IndexXcgggDetailFragment create(@NonNull int entityId, String entity, String ajaxlogtype) {
        final Bundle args = new Bundle();
        args.putInt(ARG_ENTITYID, entityId);
        args.putString(ARG_ENTITY, entity);
        args.putString(ARG_AJAXTYPE, ajaxlogtype);
        final IndexXcgggDetailFragment fragment = new IndexXcgggDetailFragment();
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
            ajaxlogtype = args.getString(ARG_AJAXTYPE);
        }
    }


    @Override
    public Object setLayout() {
        return R.layout.fragment_index_xcggg_detail;
    }

    @Override
    public void initView() {
        llIvBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);
        tvMianDingbiaoTime = getView().findViewById(R.id.tv_mian_dingbiao_time);
        xcgggDetailStatusView = getView().findViewById(R.id.xcggg_detail_status_view);
        tvMainTitle = getView().findViewById(R.id.tv_main_title);
        tvMainArea = getView().findViewById(R.id.tv_main_area);
        tvMainCaigouMethod = getView().findViewById(R.id.tv_main_caigou_method);
        tvMainPubTime = getView().findViewById(R.id.tv_main_pub_time);
        tvPubNum = getView().findViewById(R.id.tv_pub_num);
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
        tvOwerA = getView().findViewById(R.id.tv_ower_a);
        tvOwerB = getView().findViewById(R.id.tv_ower_b);
        tvOwerC = getView().findViewById(R.id.tv_ower_c);
        tvOwerD = getView().findViewById(R.id.tv_ower_d);
        tvOwerE = getView().findViewById(R.id.tv_ower_e);
        tvOwerF = getView().findViewById(R.id.tv_ower_f);
        tvOwerG = getView().findViewById(R.id.tv_ower_g);
        tvOwerH = getView().findViewById(R.id.tv_ower_h);
        ivFav = getView().findViewById(R.id.iv_fav);
        llFav = getView().findViewById(R.id.ll_fav);
        llMainCaigouMethod = getView().findViewById(R.id.ll_main_caigou_method);
        llMianDingbiaoTime = getView().findViewById(R.id.ll_mian_dingbiao_time);
        llMainPubTime = getView().findViewById(R.id.ll_main_pub_time);
        llPubNum = getView().findViewById(R.id.ll_pub_num);
        llOwerCainame = getView().findViewById(R.id.ll_ower_cainame);
        llOwerName = getView().findViewById(R.id.ll_ower_name);
        llOwerDaili = getView().findViewById(R.id.ll_ower_daili);
        llOwerBaoshu = getView().findViewById(R.id.ll_ower_baoshu);
        llOwerJine = getView().findViewById(R.id.ll_ower_jine);
        llOwerBaojia = getView().findViewById(R.id.ll_ower_baojia);
        llOwerMingdan = getView().findViewById(R.id.ll_ower_mingdan);
        llOwerLianxi = getView().findViewById(R.id.ll_ower_lianxi);
        llOwerLianxi2 = getView().findViewById(R.id.ll_ower_lianxi2);
        llOwerLianxiNumber = getView().findViewById(R.id.ll_ower_lianxi_number);
        llOwerLianxiLink = getView().findViewById(R.id.ll_ower_lianxi_link);
        llOwerPinshen = getView().findViewById(R.id.ll_ower_pinshen);
        llOwerA = getView().findViewById(R.id.ll_ower_a);
        llOwerB = getView().findViewById(R.id.ll_ower_b);
        llOwerC = getView().findViewById(R.id.ll_ower_c);
        llOwerD = getView().findViewById(R.id.ll_ower_d);
        llOwerE = getView().findViewById(R.id.ll_ower_e);
        llOwerF = getView().findViewById(R.id.ll_ower_f);
        llOwerG = getView().findViewById(R.id.ll_ower_g);
        llOwerH = getView().findViewById(R.id.ll_ower_h);
        detailNsv = getView().findViewById(R.id.detail_nsv);
        llShare = getView().findViewById(R.id.ll_share);

        llWeiBoShare = getView().findViewById(R.id.ll_weibo_share);
        llQQBoShare = getView().findViewById(R.id.ll_qq_share);
        llWeixinBoShare = getView().findViewById(R.id.ll_chat_share);
        llPyqShare = getView().findViewById(R.id.ll_pyq_share);


        llIvBack.setOnClickListener(this);
        llFav.setOnClickListener(this);
        llShare.setOnClickListener(this);

        llWeiBoShare.setOnClickListener(this);
        llQQBoShare.setOnClickListener(this);
        llWeixinBoShare.setOnClickListener(this);
        llPyqShare.setOnClickListener(this);


    }

    @Override
    public void initData() {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("标讯详情");
        xcgggDetailStatusView.setOnRetryClickListener(mRetryClickListener);
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
                    mainBarName.setText(shareTitle);
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
            xcgggDetailStatusView.showNoNetwork();
        } else {
            xcgggDetailStatusView.showLoading();

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
                        .params("ajaxlogtype", ajaxlogtype)
//                        .params("token", id + "_" + token)
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(Headers headers, String response) {
                                String jiemi = AesUtil.aesDecrypt(response, BiaoXunTongApi.PAS_KEY);
                                //判断是否收藏过
                                final JSONObject object = JSON.parseObject(jiemi);
                                String status = object.getString("status");
                                int favorite = object.getInteger("favorite");
                                if (favorite == 1) {
                                    myFav = 1;
                                    ivFav.setImageResource(R.mipmap.ic_faved_pressed);
                                } else if (favorite == 0) {
                                    myFav = 0;
                                    ivFav.setImageResource(R.mipmap.ic_fav_pressed);
                                }

                                if ("200".equals(status)) {
                                    xcgggDetailStatusView.showContent();
                                    final JSONObject data = object.getJSONObject("data");
                                    String reportTitle = data.getString("reportTitle");
                                    shareUrl = data.getString("url");
                                    shareTitle = reportTitle;
                                    if (!TextUtils.isEmpty(reportTitle)) {
                                        tvMainTitle.setText(reportTitle);
                                    } else {
                                        tvMainTitle.setText("/");
                                    }
                                    String administrativeDivision = data.getString("administrativeDivision");
                                    if (!TextUtils.isEmpty(administrativeDivision)) {
                                        tvMainArea.setText(administrativeDivision);
                                    } else {
                                        tvMainArea.setText("/");
                                    }
                                    String purchasingType = data.getString("purchasingType");
                                    shareContent = purchasingType;
                                    if (!TextUtils.isEmpty(purchasingType)) {
                                        tvMainCaigouMethod.setText(purchasingType);
                                    } else {
                                        tvMainCaigouMethod.setText("/");
                                        llMainCaigouMethod.setVisibility(View.GONE);
                                    }
                                    String noticeTime = data.getString("noticeTime");
                                    if (!TextUtils.isEmpty(noticeTime)) {
                                        tvMainPubTime.setText(noticeTime.substring(0, 10));
                                    } else {
                                        tvMainPubTime.setText("/");
                                        llMainPubTime.setVisibility(View.GONE);
                                    }
                                    String entryNum = data.getString("entryNum");
                                    if (!TextUtils.isEmpty(entryNum)) {
                                        tvPubNum.setText(entryNum);
                                    } else {
                                        tvPubNum.setText("/");
                                        llPubNum.setVisibility(View.GONE);
                                    }
                                    String entryName = data.getString("entryName");
                                    if (!TextUtils.isEmpty(entryName)) {
                                        tvOwerCainame.setText(entryName);
                                    } else {
                                        tvOwerCainame.setText("/");
                                        llOwerCainame.setVisibility(View.GONE);
                                    }
                                    String purchaser = data.getString("purchaser");
                                    if (!TextUtils.isEmpty(purchaser)) {
                                        tvOwerName.setText(purchaser);
                                    } else {
                                        tvOwerName.setText("/");
                                        llOwerName.setVisibility(View.GONE);
                                    }
                                    String purchasingAgent = data.getString("purchasingAgent");
                                    if (!TextUtils.isEmpty(purchasingAgent)) {
                                        tvOwerDaili.setText(purchasingAgent);
                                    } else {
                                        tvOwerDaili.setText("/");
                                        llOwerDaili.setVisibility(View.GONE);
                                    }
                                    String noticeCount = data.getString("noticeCount");
                                    if (!TextUtils.isEmpty(noticeCount)) {
                                        tvOwerBaoshu.setText(noticeCount);
                                    } else {
                                        tvOwerBaoshu.setText("/");
                                        llOwerBaoshu.setVisibility(View.GONE);
                                    }
                                    final String descriptionPackages = data.getString("descriptionPackages");
                                    if (!TextUtils.isEmpty(descriptionPackages)) {
                                        tvOwerJine.setText("点击查看或下载附件");

                                        llOwerJine.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                AppConfig.openExternalBrowser(getContext(), descriptionPackages);
                                            }
                                        });
                                    } else {
                                        tvOwerJine.setText("不详");
                                        llOwerJine.setVisibility(View.GONE);
                                    }
                                    String qualificationsMaterials = data.getString("qualificationsMaterials");
                                    if (!TextUtils.isEmpty(qualificationsMaterials)) {
                                        tvOwerBaojia.setText(qualificationsMaterials);
                                    } else {
                                        tvOwerBaojia.setText("/");
                                        llOwerBaojia.setVisibility(View.GONE);
                                    }
                                    String offeringMethod = data.getString("offeringMethod");
                                    if (!TextUtils.isEmpty(offeringMethod)) {
                                        tvOwerMingdan.setText(offeringMethod);
                                    } else {
                                        tvOwerMingdan.setText("/");
                                        llOwerMingdan.setVisibility(View.GONE);
                                    }
                                    String signTime = data.getString("signTime");
                                    if (!TextUtils.isEmpty(signTime)) {
                                        tvOwerLianxi.setText(signTime);
                                    } else {
                                        tvOwerLianxi.setText("/");
                                        llOwerLianxi.setVisibility(View.GONE);
                                    }
                                    String signAddress = data.getString("signAddress");
                                    if (!TextUtils.isEmpty(signAddress)) {
                                        tvOwerLianxi2.setText(signAddress);
                                    } else {
                                        tvOwerLianxi2.setText("/");
                                        llOwerLianxi2.setVisibility(View.GONE);
                                    }
                                    String price = data.getString("price");
                                    if (!TextUtils.isEmpty(price)) {
                                        tvOwerLianxiNumber.setText(price);
                                    } else {
                                        tvOwerLianxiNumber.setText("/");
                                        llOwerLianxiNumber.setVisibility(View.GONE);
                                    }
                                    String saleAddress = data.getString("saleAddress");
                                    if (!TextUtils.isEmpty(saleAddress)) {
                                        tvOwerLianxiLink.setText(saleAddress);
                                    } else {
                                        tvOwerLianxiLink.setText("/");
                                        llOwerLianxiLink.setVisibility(View.GONE);
                                    }
                                    String signType = data.getString("signType");
                                    if (!TextUtils.isEmpty(signType)) {
                                        tvOwerPinshen.setText(signType);
                                    } else {
                                        tvOwerPinshen.setText("/");
                                        llOwerPinshen.setVisibility(View.GONE);
                                    }
                                    String startStopTime = data.getString("startStopTime");
                                    if (!TextUtils.isEmpty(startStopTime)) {
                                        tvOwerA.setText(startStopTime);
                                    } else {
                                        tvOwerA.setText("/");
                                        llOwerA.setVisibility(View.GONE);
                                    }
                                    String fileAddress = data.getString("fileAddress");
                                    if (!TextUtils.isEmpty(fileAddress)) {
                                        tvOwerB.setText(fileAddress);
                                    } else {
                                        tvOwerB.setText("/");
                                        llOwerB.setVisibility(View.GONE);
                                    }
                                    String inquiryTime = data.getString("inquiryTime");
                                    if (!TextUtils.isEmpty(inquiryTime)) {
                                        tvOwerC.setText(inquiryTime);
                                    } else {
                                        tvOwerC.setText("/");
                                        llOwerC.setVisibility(View.GONE);
                                    }
                                    String payAndtype = data.getString("payAndtype");
                                    if (!TextUtils.isEmpty(payAndtype)) {
                                        tvOwerD.setText(payAndtype);
                                    } else {
                                        tvOwerD.setText("/");
                                        llOwerD.setVisibility(View.GONE);
                                    }
                                    String purchaserContact = data.getString("purchaserContact");
                                    if (!TextUtils.isEmpty(purchaserContact)) {
                                        tvOwerE.setText(purchaserContact);
                                    } else {
                                        tvOwerE.setText("/");
                                        llOwerE.setVisibility(View.GONE);
                                    }
                                    String purchasingAgentContact = data.getString("purchasingAgentContact");
                                    if (!TextUtils.isEmpty(purchasingAgentContact)) {
                                        tvOwerF.setText(purchasingAgentContact);
                                    } else {
                                        tvOwerF.setText("/");
                                        llOwerF.setVisibility(View.GONE);
                                    }
                                    String nameAndphone = data.getString("nameAndphone");
                                    if (!TextUtils.isEmpty(nameAndphone)) {
                                        tvOwerG.setText(nameAndphone);
                                    } else {
                                        tvOwerG.setText("/");
                                        llOwerG.setVisibility(View.GONE);
                                    }
                                    final String link = data.getString("link");
                                    if (!TextUtils.isEmpty(link)) {
                                        tvOwerH.setText("点击查看详情");
                                        llOwerH.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getActivity(), BrowserActivity.class);
                                                intent.putExtra("url", link);
                                                intent.putExtra("title", shareTitle);
                                                startActivity(intent);
                                            }
                                        });
                                    } else {
                                        tvOwerH.setText("/");
                                        llOwerH.setVisibility(View.GONE);
                                    }
                                } else {
                                    xcgggDetailStatusView.showError();
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
                        .params("ajaxlogtype", ajaxlogtype)
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(Headers headers, String response) {
                                String jiemi = AesUtil.aesDecrypt(response, BiaoXunTongApi.PAS_KEY);

                                final JSONObject object = JSON.parseObject(jiemi);
                                String status = object.getString("status");
                                if ("200".equals(status)) {
                                    xcgggDetailStatusView.showContent();
                                    final JSONObject data = object.getJSONObject("data");
                                    String reportTitle = data.getString("reportTitle");
                                    shareUrl = data.getString("url");
                                    shareTitle = reportTitle;
                                    if (!TextUtils.isEmpty(reportTitle)) {
                                        tvMainTitle.setText(reportTitle);
                                    } else {
                                        tvMainTitle.setText("/");
                                    }
                                    String administrativeDivision = data.getString("administrativeDivision");
                                    if (!TextUtils.isEmpty(administrativeDivision)) {
                                        tvMainArea.setText(administrativeDivision);
                                    } else {
                                        tvMainArea.setText("/");
                                    }
                                    String purchasingType = data.getString("purchasingType");
                                    shareContent = purchasingType;
                                    if (!TextUtils.isEmpty(purchasingType)) {
                                        tvMainCaigouMethod.setText(purchasingType);
                                    } else {
                                        tvMainCaigouMethod.setText("/");
                                        llMainCaigouMethod.setVisibility(View.GONE);
                                    }
                                    String noticeTime = data.getString("noticeTime");
                                    if (!TextUtils.isEmpty(noticeTime)) {
                                        tvMainPubTime.setText(noticeTime.substring(0, 10));
                                    } else {
                                        tvMainPubTime.setText("/");
                                        llMainPubTime.setVisibility(View.GONE);
                                    }
                                    String entryNum = data.getString("entryNum");
                                    if (!TextUtils.isEmpty(entryNum)) {
                                        tvPubNum.setText(entryNum);
                                    } else {
                                        tvPubNum.setText("/");
                                        llPubNum.setVisibility(View.GONE);
                                    }
                                    String entryName = data.getString("entryName");
                                    if (!TextUtils.isEmpty(entryName)) {
                                        tvOwerCainame.setText(entryName);
                                    } else {
                                        tvOwerCainame.setText("/");
                                        llOwerCainame.setVisibility(View.GONE);
                                    }
                                    String purchaser = data.getString("purchaser");
                                    if (!TextUtils.isEmpty(purchaser)) {
                                        tvOwerName.setText(purchaser);
                                    } else {
                                        tvOwerName.setText("/");
                                        llOwerName.setVisibility(View.GONE);
                                    }
                                    String purchasingAgent = data.getString("purchasingAgent");
                                    if (!TextUtils.isEmpty(purchasingAgent)) {
                                        tvOwerDaili.setText(purchasingAgent);
                                    } else {
                                        tvOwerDaili.setText("/");
                                        llOwerDaili.setVisibility(View.GONE);
                                    }
                                    String noticeCount = data.getString("noticeCount");
                                    if (!TextUtils.isEmpty(noticeCount)) {
                                        tvOwerBaoshu.setText(noticeCount);
                                    } else {
                                        tvOwerBaoshu.setText("/");
                                        llOwerBaoshu.setVisibility(View.GONE);
                                    }
                                    final String descriptionPackages = data.getString("descriptionPackages");
                                    if (!TextUtils.isEmpty(descriptionPackages)) {
                                        tvOwerJine.setText("点击查看或下载附件");

                                        llOwerJine.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                AppConfig.openExternalBrowser(getContext(), descriptionPackages);
                                            }
                                        });
                                    } else {
                                        tvOwerJine.setText("不详");
                                        llOwerJine.setVisibility(View.GONE);
                                    }
                                    String qualificationsMaterials = data.getString("qualificationsMaterials");
                                    if (!TextUtils.isEmpty(qualificationsMaterials)) {
                                        tvOwerBaojia.setText(qualificationsMaterials);
                                    } else {
                                        tvOwerBaojia.setText("/");
                                        llOwerBaojia.setVisibility(View.GONE);
                                    }
                                    String offeringMethod = data.getString("offeringMethod");
                                    if (!TextUtils.isEmpty(offeringMethod)) {
                                        tvOwerMingdan.setText(offeringMethod);
                                    } else {
                                        tvOwerMingdan.setText("/");
                                        llOwerMingdan.setVisibility(View.GONE);
                                    }
                                    String signTime = data.getString("signTime");
                                    if (!TextUtils.isEmpty(signTime)) {
                                        tvOwerLianxi.setText(signTime);
                                    } else {
                                        tvOwerLianxi.setText("/");
                                        llOwerLianxi.setVisibility(View.GONE);
                                    }
                                    String signAddress = data.getString("signAddress");
                                    if (!TextUtils.isEmpty(signAddress)) {
                                        tvOwerLianxi2.setText(signAddress);
                                    } else {
                                        tvOwerLianxi2.setText("/");
                                        llOwerLianxi2.setVisibility(View.GONE);
                                    }
                                    String price = data.getString("price");
                                    if (!TextUtils.isEmpty(price)) {
                                        tvOwerLianxiNumber.setText(price);
                                    } else {
                                        tvOwerLianxiNumber.setText("/");
                                        llOwerLianxiNumber.setVisibility(View.GONE);
                                    }
                                    String saleAddress = data.getString("saleAddress");
                                    if (!TextUtils.isEmpty(saleAddress)) {
                                        tvOwerLianxiLink.setText(saleAddress);
                                    } else {
                                        tvOwerLianxiLink.setText("/");
                                        llOwerLianxiLink.setVisibility(View.GONE);
                                    }
                                    String signType = data.getString("signType");
                                    if (!TextUtils.isEmpty(signType)) {
                                        tvOwerPinshen.setText(signType);
                                    } else {
                                        tvOwerPinshen.setText("/");
                                        llOwerPinshen.setVisibility(View.GONE);
                                    }
                                    String startStopTime = data.getString("startStopTime");
                                    if (!TextUtils.isEmpty(startStopTime)) {
                                        tvOwerA.setText(startStopTime);
                                    } else {
                                        tvOwerA.setText("/");
                                        llOwerA.setVisibility(View.GONE);
                                    }
                                    String fileAddress = data.getString("fileAddress");
                                    if (!TextUtils.isEmpty(fileAddress)) {
                                        tvOwerB.setText(fileAddress);
                                    } else {
                                        tvOwerB.setText("/");
                                        llOwerB.setVisibility(View.GONE);
                                    }
                                    String inquiryTime = data.getString("inquiryTime");
                                    if (!TextUtils.isEmpty(inquiryTime)) {
                                        tvOwerC.setText(inquiryTime);
                                    } else {
                                        tvOwerC.setText("/");
                                        llOwerC.setVisibility(View.GONE);
                                    }
                                    String payAndtype = data.getString("payAndtype");
                                    if (!TextUtils.isEmpty(payAndtype)) {
                                        tvOwerD.setText(payAndtype);
                                    } else {
                                        tvOwerD.setText("/");
                                        llOwerD.setVisibility(View.GONE);
                                    }
                                    String purchaserContact = data.getString("purchaserContact");
                                    if (!TextUtils.isEmpty(purchaserContact)) {
                                        tvOwerE.setText(purchaserContact);
                                    } else {
                                        tvOwerE.setText("/");
                                        llOwerE.setVisibility(View.GONE);
                                    }
                                    String purchasingAgentContact = data.getString("purchasingAgentContact");
                                    if (!TextUtils.isEmpty(purchasingAgentContact)) {
                                        tvOwerF.setText(purchasingAgentContact);
                                    } else {
                                        tvOwerF.setText("/");
                                        llOwerF.setVisibility(View.GONE);
                                    }
                                    String nameAndphone = data.getString("nameAndphone");
                                    if (!TextUtils.isEmpty(nameAndphone)) {
                                        tvOwerG.setText(nameAndphone);
                                    } else {
                                        tvOwerG.setText("/");
                                        llOwerG.setVisibility(View.GONE);
                                    }
                                    final String link = data.getString("link");
                                    if (!TextUtils.isEmpty(link)) {
                                        tvOwerH.setText("点击查看详情");
                                        llOwerH.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getActivity(), BrowserActivity.class);
                                                intent.putExtra("url", link);
                                                intent.putExtra("title", shareTitle);
                                                startActivity(intent);
                                            }
                                        });
                                    } else {
                                        tvOwerH.setText("/");
                                        llOwerH.setVisibility(View.GONE);
                                    }
                                } else {
                                    xcgggDetailStatusView.showError();
                                }
                            }
                        })
                        .build()
                        .post();
            }


        }

    }

    private Share mShare = new Share();
    private PromptDialog promptDialog = null;

    @Override
    public void onClick(View view) {
        mShare.setAppName("鲁班标讯通");
        mShare.setAppShareIcon(R.mipmap.ic_share);
        if (mShare.getBitmapResID() == 0) {
            mShare.setBitmapResID(R.mipmap.ic_share);
        }
        mShare.setTitle(shareTitle);
        mShare.setContent(shareContent);
        mShare.setSummary(shareContent);
        mShare.setDescription(shareContent);
        mShare.setImageUrl(null);
        mShare.setUrl(BiaoXunTongApi.SHARE_URL + shareUrl);
        switch (view.getId()) {
            case R.id.ll_weibo_share:
                OpenBuilder.with(getActivity())
                        .useWeibo(OpenConstant.WB_APP_KEY)
                        .share(mShare, new OpenBuilder.Callback() {
                            @Override
                            public void onFailed() {

                            }

                            @Override
                            public void onSuccess() {

                            }
                        });
                break;
            case R.id.ll_qq_share:
                OpenBuilder.with(getActivity())
                        .useTencent(OpenConstant.QQ_APP_ID)
                        .share(mShare, new IUiListener() {
                            @Override
                            public void onComplete(Object o) {
                                ToastUtil.shortToast(getContext(), "分享成功");
                            }

                            @Override
                            public void onError(UiError uiError) {
                                ToastUtil.shortToast(getContext(), "分享失败");
                            }

                            @Override
                            public void onCancel() {
                                ToastUtil.shortToast(getContext(), "分享取消");
                            }
                        },this);
                break;
            case R.id.ll_chat_share:
                OpenBuilder.with(getActivity())
                        .useWechat(OpenConstant.WECHAT_APP_ID)
                        .shareSession(mShare, new OpenBuilder.Callback() {
                            @Override
                            public void onFailed() {

                            }

                            @Override
                            public void onSuccess() {

                            }
                        });
                break;
            case R.id.ll_pyq_share:
                OpenBuilder.with(getActivity())
                        .useWechat(OpenConstant.WECHAT_APP_ID)
                        .shareTimeLine(mShare, new OpenBuilder.Callback() {
                            @Override
                            public void onFailed() {

                            }

                            @Override
                            public void onSuccess() {

                            }
                        });
                break;
            case R.id.ll_iv_back:
                getActivity().onBackPressed();
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
                                            ToastUtil.shortToast(getContext(), "取消收藏");
                                            EventBus.getDefault().post(new EventMessage(EventMessage.CLICK_FAV));
                                        } else if ("500".equals(status)) {

                                            ToastUtil.shortToast(getContext(), "服务器异常");
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
                                            ToastUtil.shortToast(getContext(), "收藏成功");
                                            EventBus.getDefault().post(new EventMessage(EventMessage.CLICK_FAV));
                                        } else if ("500".equals(status)) {
                                            ToastUtil.shortToast(getContext(), "服务器异常");
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
            case R.id.ll_share:
                toShare(mEntityId, shareTitle, shareContent, BiaoXunTongApi.SHARE_URL + shareUrl);
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailed() {

    }

    @Override
    public void onSuccess() {

    }
}
