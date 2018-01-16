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
import com.lubanjianye.biaoxuntong.api.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.sign.SignInActivity;
import com.lubanjianye.biaoxuntong.ui.share.OpenBuilder;
import com.lubanjianye.biaoxuntong.ui.share.OpenConstant;
import com.lubanjianye.biaoxuntong.ui.share.Share;
import com.lubanjianye.biaoxuntong.util.aes.AesUtil;
import com.lubanjianye.biaoxuntong.util.dialog.PromptDialog;
import com.lubanjianye.biaoxuntong.util.netStatus.NetUtil;
import com.lubanjianye.biaoxuntong.util.netStatus.AppSysMgr;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 项目名:   Lunioussky
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.index.detail
 * 文件名:   IndexSggjycgtableDetailFragment
 * 创建者:   lunious
 * 创建时间: 2017/10/26  23:15
 * 描述:     TODO
 */

public class IndexSggjycgtableDetailFragment extends BaseFragment implements View.OnClickListener, OpenBuilder.Callback {

    LinearLayout llIvBack = null;
    AppCompatTextView mainBarName = null;
    MultipleStatusView sggjycgtableDetailStatusView = null;
    TextView tvMainTitle = null;
    TextView tvArea = null;
    TextView tvMainPubType = null;
    TextView tvMainCaigouType = null;
    TextView tvDataBeginTime = null;
    TextView tvMainDeadTime = null;
    TextView tvPuNum = null;
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
    TextView tvOwerDailiren = null;
    TextView tvOwerCaigouxiangmu = null;
    ImageView ivFav = null;
    LinearLayout llFav = null;
    LinearLayout llShare = null;
    NestedScrollView detailNsv = null;
    LinearLayout llBucai = null;
    private AppCompatTextView tvBucai = null;


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


    public static IndexSggjycgtableDetailFragment create(@NonNull int entityId, String entity, String ajaxlogtype) {
        final Bundle args = new Bundle();
        args.putInt(ARG_ENTITYID, entityId);
        args.putString(ARG_ENTITY, entity);
        args.putString(ARG_AJAXTYPE, ajaxlogtype);
        final IndexSggjycgtableDetailFragment fragment = new IndexSggjycgtableDetailFragment();
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
        return R.layout.fragment_index_sggjycgtable_detail;
    }

    @Override
    public void initView() {
        llIvBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);
        sggjycgtableDetailStatusView = getView().findViewById(R.id.sggjycgtable_detail_status_view);
        tvMainTitle = getView().findViewById(R.id.tv_main_title);
        tvArea = getView().findViewById(R.id.tv_area);
        tvMainPubType = getView().findViewById(R.id.tv_main_pub_type);
        tvMainCaigouType = getView().findViewById(R.id.tv_main_caigou_type);
        tvDataBeginTime = getView().findViewById(R.id.tv_data_begin_time);
        tvMainDeadTime = getView().findViewById(R.id.tv_main_dead_time);
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
        tvOwerDailiren = getView().findViewById(R.id.tv_ower_dailiren);
        tvOwerCaigouxiangmu = getView().findViewById(R.id.tv_ower_caigouxiangmu);
        ivFav = getView().findViewById(R.id.iv_fav);
        llFav = getView().findViewById(R.id.ll_fav);
        llShare = getView().findViewById(R.id.ll_share);
        detailNsv = getView().findViewById(R.id.detail_nsv);
        llBucai = getView().findViewById(R.id.ll_bucai);
        tvBucai = getView().findViewById(R.id.tv_bucai);

        llWeiBoShare = getView().findViewById(R.id.ll_weibo_share);
        llQQBoShare = getView().findViewById(R.id.ll_qq_share);
        llWeixinBoShare = getView().findViewById(R.id.ll_chat_share);
        llPyqShare = getView().findViewById(R.id.ll_pyq_share);


        llIvBack.setOnClickListener(this);
        llShare.setOnClickListener(this);
        llFav.setOnClickListener(this);

        llWeiBoShare.setOnClickListener(this);
        llQQBoShare.setOnClickListener(this);
        llWeixinBoShare.setOnClickListener(this);
        llPyqShare.setOnClickListener(this);


    }

    @Override
    public void initData() {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("标讯详情");
        sggjycgtableDetailStatusView.setOnRetryClickListener(mRetryClickListener);
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
            sggjycgtableDetailStatusView.showNoNetwork();
        } else {

            if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
                List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
                for (int i = 0; i < users.size(); i++) {
                    id = users.get(0).getId();
                    nickName = users.get(0).getNickName();
                    token = users.get(0).getToken();
                    comid = users.get(0).getComid();
                    imageUrl = users.get(0).getImageUrl();
                }

                OkGo.<String>post(BiaoXunTongApi.URL_GETCOLLECTIONLISTDETAIL)
                        .params("entityId", mEntityId)
                        .params("entity", mEntity)
                        .params("userid", id)
                        .params("deviceId", deviceId)
                        .params("ajaxlogtype", ajaxlogtype)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String jiemi = AesUtil.aesDecrypt(response.body(), BiaoXunTongApi.PAS_KEY);

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
                                    final JSONObject data = object.getJSONObject("data");
                                    String reportTitle = data.getString("reportTitle");
                                    shareUrl = data.getString("url");
                                    shareTitle = reportTitle;
                                    if (!TextUtils.isEmpty(reportTitle)) {
                                        tvMainTitle.setText(reportTitle);
                                    } else {
                                        tvMainTitle.setText("/");
                                    }
                                    String administrativeAddress = data.getString("administrativeAddress");
                                    if (!TextUtils.isEmpty(administrativeAddress)) {
                                        tvArea.setText(administrativeAddress);
                                    } else {
                                        tvArea.setText("/");
                                    }
                                    String noticeType = data.getString("noticeType");
                                    if (!TextUtils.isEmpty(noticeType)) {
                                        tvMainPubType.setText(noticeType);
                                    } else {
                                        tvMainPubType.setText("/");
                                    }
                                    String purchasingType = data.getString("purchasingType");
                                    shareContent = purchasingType;
                                    if (!TextUtils.isEmpty(purchasingType)) {
                                        tvMainCaigouType.setText(purchasingType);
                                    } else {
                                        tvMainCaigouType.setText("/");
                                    }
                                    String openingTime = data.getString("openingTime");
                                    if (!TextUtils.isEmpty(openingTime)) {
                                        tvDataBeginTime.setText(openingTime);
                                    } else {
                                        tvDataBeginTime.setText("/");
                                    }
                                    String deadline = data.getString("deadline");
                                    if (!TextUtils.isEmpty(deadline)) {
                                        tvMainDeadTime.setText(deadline);
                                    } else {
                                        tvMainDeadTime.setText("/");
                                    }
                                    String entryNum = data.getString("entryNum");
                                    if (!TextUtils.isEmpty(entryNum)) {
                                        tvPuNum.setText(entryNum);
                                    } else {
                                        tvPuNum.setText("/");
                                    }
                                    String entryName = data.getString("entryName");
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
                                    String prerequisites = data.getString("prerequisites");
                                    if (!TextUtils.isEmpty(prerequisites)) {
                                        tvOwerJine.setText(prerequisites);
                                    } else {
                                        tvOwerJine.setText("/");
                                    }
                                    String offeringType = data.getString("offeringType");
                                    if (!TextUtils.isEmpty(offeringType)) {
                                        tvOwerBaojia.setText(offeringType);
                                    } else {
                                        tvOwerBaojia.setText("/");
                                    }
                                    String startAndStopTime = data.getString("startAndStopTime");
                                    if (!TextUtils.isEmpty(startAndStopTime)) {
                                        tvOwerMingdan.setText(startAndStopTime);
                                    } else {
                                        tvOwerMingdan.setText("/");
                                    }
                                    String payMoney = data.getString("payMoney");
                                    if (!TextUtils.isEmpty(payMoney)) {
                                        tvOwerLianxi.setText(payMoney);
                                    } else {
                                        tvOwerLianxi.setText("/");
                                    }
                                    String documentAddress = data.getString("documentAddress");
                                    if (!TextUtils.isEmpty(documentAddress)) {
                                        tvOwerLianxi2.setText(documentAddress);
                                    } else {
                                        tvOwerLianxi2.setText("/");
                                    }
                                    String tenderLocation = data.getString("tenderLocation");
                                    if (!TextUtils.isEmpty(tenderLocation)) {
                                        tvOwerLianxiNumber.setText(tenderLocation);
                                    } else {
                                        tvOwerLianxiNumber.setText("/");
                                    }
                                    String openingAddress = data.getString("openingAddress");
                                    if (!TextUtils.isEmpty(openingAddress)) {
                                        tvOwerLianxiLink.setText(openingAddress);
                                    } else {
                                        tvOwerLianxiLink.setText("/");
                                    }
                                    String purchaserContact = data.getString("purchaserContact");
                                    if (!TextUtils.isEmpty(purchaserContact)) {
                                        tvOwerPinshen.setText(purchaserContact);
                                    } else {
                                        tvOwerPinshen.setText("/");
                                    }
                                    String purchasingAgentContact = data.getString("purchasingAgentContact");
                                    if (!TextUtils.isEmpty(purchasingAgentContact)) {
                                        tvOwerDailiren.setText(purchasingAgentContact);
                                    } else {
                                        tvOwerDailiren.setText("/");
                                    }
                                    String nameAndphone = data.getString("nameAndphone");
                                    if (!TextUtils.isEmpty(nameAndphone)) {
                                        tvOwerCaigouxiangmu.setText(nameAndphone);
                                    } else {
                                        tvOwerCaigouxiangmu.setText("/");
                                    }
                                    String specialFields = data.getString("specialFields");
                                    if (!TextUtils.isEmpty(specialFields)) {
                                        llBucai.setVisibility(View.VISIBLE);
                                        String s = specialFields.replace("*", "").replace("</", "\n").replace("<","\n\n");
                                        tvBucai.setText(s);
                                    } else {
                                        llBucai.setVisibility(View.GONE);
                                    }
                                    sggjycgtableDetailStatusView.showContent();
                                } else {
                                    sggjycgtableDetailStatusView.showError();
                                }
                            }
                        });

            } else {
                OkGo.<String>post(BiaoXunTongApi.URL_GETCOLLECTIONLISTDETAIL)
                        .params("entityId", mEntityId)
                        .params("entity", mEntity)
                        .params("deviceId", deviceId)
                        .params("ajaxlogtype", ajaxlogtype)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String jiemi = AesUtil.aesDecrypt(response.body(), BiaoXunTongApi.PAS_KEY);

                                final JSONObject object = JSON.parseObject(jiemi);
                                String status = object.getString("status");
                                final JSONObject data = object.getJSONObject("data");
                                if ("200".equals(status)) {
                                    sggjycgtableDetailStatusView.showContent();
                                    String reportTitle = data.getString("reportTitle");
                                    shareUrl = data.getString("url");
                                    shareTitle = reportTitle;
                                    if (!TextUtils.isEmpty(reportTitle)) {
                                        tvMainTitle.setText(reportTitle);
                                    } else {
                                        tvMainTitle.setText("/");
                                    }
                                    String administrativeAddress = data.getString("administrativeAddress");
                                    if (!TextUtils.isEmpty(administrativeAddress)) {
                                        tvArea.setText(administrativeAddress);
                                    } else {
                                        tvArea.setText("/");
                                    }
                                    String noticeType = data.getString("noticeType");
                                    if (!TextUtils.isEmpty(noticeType)) {
                                        tvMainPubType.setText(noticeType);
                                    } else {
                                        tvMainPubType.setText("/");
                                    }
                                    String purchasingType = data.getString("purchasingType");
                                    shareContent = purchasingType;
                                    if (!TextUtils.isEmpty(purchasingType)) {
                                        tvMainCaigouType.setText(purchasingType);
                                    } else {
                                        tvMainCaigouType.setText("/");
                                    }
                                    String openingTime = data.getString("openingTime");
                                    if (!TextUtils.isEmpty(openingTime)) {
                                        tvDataBeginTime.setText(openingTime);
                                    } else {
                                        tvDataBeginTime.setText("/");
                                    }
                                    String deadline = data.getString("deadline");
                                    if (!TextUtils.isEmpty(deadline)) {
                                        tvMainDeadTime.setText(deadline);
                                    } else {
                                        tvMainDeadTime.setText("/");
                                    }
                                    String entryNum = data.getString("entryNum");
                                    if (!TextUtils.isEmpty(entryNum)) {
                                        tvPuNum.setText(entryNum);
                                    } else {
                                        tvPuNum.setText("/");
                                    }
                                    String entryName = data.getString("entryName");
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
                                    String prerequisites = data.getString("prerequisites");
                                    if (!TextUtils.isEmpty(prerequisites)) {
                                        tvOwerJine.setText(prerequisites);
                                    } else {
                                        tvOwerJine.setText("/");
                                    }
                                    String offeringType = data.getString("offeringType");
                                    if (!TextUtils.isEmpty(offeringType)) {
                                        tvOwerBaojia.setText(offeringType);
                                    } else {
                                        tvOwerBaojia.setText("/");
                                    }
                                    String startAndStopTime = data.getString("startAndStopTime");
                                    if (!TextUtils.isEmpty(startAndStopTime)) {
                                        tvOwerMingdan.setText(startAndStopTime);
                                    } else {
                                        tvOwerMingdan.setText("/");
                                    }
                                    String payMoney = data.getString("payMoney");
                                    if (!TextUtils.isEmpty(payMoney)) {
                                        tvOwerLianxi.setText(payMoney);
                                    } else {
                                        tvOwerLianxi.setText("/");
                                    }
                                    String documentAddress = data.getString("documentAddress");
                                    if (!TextUtils.isEmpty(documentAddress)) {
                                        tvOwerLianxi2.setText(documentAddress);
                                    } else {
                                        tvOwerLianxi2.setText("/");
                                    }
                                    String tenderLocation = data.getString("tenderLocation");
                                    if (!TextUtils.isEmpty(tenderLocation)) {
                                        tvOwerLianxiNumber.setText(tenderLocation);
                                    } else {
                                        tvOwerLianxiNumber.setText("/");
                                    }
                                    String openingAddress = data.getString("openingAddress");
                                    if (!TextUtils.isEmpty(openingAddress)) {
                                        tvOwerLianxiLink.setText(openingAddress);
                                    } else {
                                        tvOwerLianxiLink.setText("/");
                                    }
                                    String purchaserContact = data.getString("purchaserContact");
                                    if (!TextUtils.isEmpty(purchaserContact)) {
                                        tvOwerPinshen.setText(purchaserContact);
                                    } else {
                                        tvOwerPinshen.setText("/");
                                    }
                                    String purchasingAgentContact = data.getString("purchasingAgentContact");
                                    if (!TextUtils.isEmpty(purchasingAgentContact)) {
                                        tvOwerDailiren.setText(purchasingAgentContact);
                                    } else {
                                        tvOwerDailiren.setText("/");
                                    }
                                    String nameAndphone = data.getString("nameAndphone");
                                    if (!TextUtils.isEmpty(nameAndphone)) {
                                        tvOwerCaigouxiangmu.setText(nameAndphone);
                                    } else {
                                        tvOwerCaigouxiangmu.setText("/");
                                    }
                                    String specialFields = data.getString("specialFields");
                                    if (!TextUtils.isEmpty(specialFields)) {
                                        llBucai.setVisibility(View.VISIBLE);
                                        String s = specialFields.replace("*", "").replace("</", "\n").replace("<","\n\n");
                                        tvBucai.setText(s);
                                    } else {
                                        llBucai.setVisibility(View.GONE);
                                    }

                                } else {
                                    sggjycgtableDetailStatusView.showError();
                                }
                            }
                        });

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
                        OkGo.<String>post(BiaoXunTongApi.URL_DELEFAV)
                                .params("entityid", mEntityId)
                                .params("entity", mEntity)
                                .params("userid", id)
                                .params("deviceId", deviceId)
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        final JSONObject object = JSON.parseObject(response.body());
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
                                });

                    } else if (myFav == 0) {
                        OkGo.<String>post(BiaoXunTongApi.URL_ADDFAV)
                                .params("entityid", mEntityId)
                                .params("entity", mEntity)
                                .params("userid", id)
                                .params("deviceId", deviceId)
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        final JSONObject object = JSON.parseObject(response.body());
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
                                });
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

    @Override
    public void onFailed() {

    }

    @Override
    public void onSuccess() {

    }
}
