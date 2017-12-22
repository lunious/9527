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
import com.lubanjianye.biaoxuntong.util.aes.AesUtil;
import com.lubanjianye.biaoxuntong.util.netStatus.AppSysMgr;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.tosaty.Toasty;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import okhttp3.Headers;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.fragment.result.detail
 * 文件名:   ResultSggjyzbjgDetailFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/12  21:29
 * 描述:     TODO
 */

public class ResultSggjyzbjgDetailFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout llIvBack = null;
    private AppCompatTextView mainBarName = null;
    private AppCompatTextView tvFavCount = null;
    private LinearLayout llShare = null;
    private AppCompatTextView tvMainTitle = null;
    private AppCompatTextView tvDataTime = null;
    private AppCompatTextView tvDataDetail = null;
    private AppCompatTextView tvOwerBiaoduanname = null;
    private AppCompatTextView tvOwerXiangmuyezhu = null;
    private AppCompatTextView tvOwerXiangmuyezhuNumb = null;
    private AppCompatTextView tvOwerZhaobiaoren = null;
    private AppCompatTextView tvOwerZhaobiaorenNumb = null;
    private AppCompatTextView tvOwerZhaobiaodaili = null;
    private AppCompatTextView tvOwerZhaobiaodailiNumb = null;
    private AppCompatTextView tvOwerKaibiaodidian = null;
    private AppCompatTextView tvOwerKaibiaoshijian = null;
    private AppCompatTextView tvOwerGongshiqi = null;
    private AppCompatTextView tvOwerToubiaoxianjia = null;
    private AppCompatTextView tvOwerDiyi = null;
    private AppCompatTextView tvOwerDier = null;
    private AppCompatTextView tvOwerDisan = null;
    private AppCompatTextView tvOwerCompanyName = null;
    private AppCompatTextView tvOwerCompanyBaojia = null;
    private AppCompatTextView tvOwerCompanyToubiaojia = null;
    private AppCompatTextView tvOwerCompanyPinjia = null;
    private ImageView ivFav = null;
    private LinearLayout llFav = null;
    private MultipleStatusView sggjyDetailStatusView = null;
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
        return R.layout.fragment_result_sggjyzbjg_detail;
    }

    public static ResultSggjyzbjgDetailFragment create(@NonNull int entityId, String entity) {
        final Bundle args = new Bundle();
        args.putInt(ARG_ENTITYID, entityId);
        args.putString(ARG_ENTITY, entity);
        final ResultSggjyzbjgDetailFragment fragment = new ResultSggjyzbjgDetailFragment();
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

        tvFavCount = getView().findViewById(R.id.tv_fav_count);
        llShare = getView().findViewById(R.id.ll_share);
        tvMainTitle = getView().findViewById(R.id.tv_main_title);
        tvDataTime = getView().findViewById(R.id.tv_data_time);
        tvDataDetail = getView().findViewById(R.id.tv_data_detail);
        tvOwerBiaoduanname = getView().findViewById(R.id.tv_ower_biaoduanname);
        tvOwerXiangmuyezhu = getView().findViewById(R.id.tv_ower_xiangmuyezhu);
        tvOwerXiangmuyezhuNumb = getView().findViewById(R.id.tv_ower_xiangmuyezhu_numb);
        tvOwerZhaobiaoren = getView().findViewById(R.id.tv_ower_zhaobiaoren);
        tvOwerZhaobiaorenNumb = getView().findViewById(R.id.tv_ower_zhaobiaoren_numb);
        tvOwerZhaobiaodaili = getView().findViewById(R.id.tv_ower_zhaobiaodaili);
        tvOwerZhaobiaodailiNumb = getView().findViewById(R.id.tv_ower_zhaobiaodaili_numb);
        tvOwerKaibiaodidian = getView().findViewById(R.id.tv_ower_kaibiaodidian);
        tvOwerKaibiaoshijian = getView().findViewById(R.id.tv_ower_kaibiaoshijian);
        tvOwerGongshiqi = getView().findViewById(R.id.tv_ower_gongshiqi);
        tvOwerToubiaoxianjia = getView().findViewById(R.id.tv_ower_toubiaoxianjia);
        tvOwerDiyi = getView().findViewById(R.id.tv_ower_diyi);
        tvOwerDier = getView().findViewById(R.id.tv_ower_dier);
        tvOwerDisan = getView().findViewById(R.id.tv_ower_disan);
        tvOwerCompanyName = getView().findViewById(R.id.tv_ower_company_name);
        tvOwerCompanyBaojia = getView().findViewById(R.id.tv_ower_company_baojia);
        tvOwerCompanyToubiaojia = getView().findViewById(R.id.tv_ower_company_toubiaojia);
        tvOwerCompanyPinjia = getView().findViewById(R.id.tv_ower_company_pinjia);
        ivFav = getView().findViewById(R.id.iv_fav);
        llFav = getView().findViewById(R.id.ll_fav);
        sggjyDetailStatusView = getView().findViewById(R.id.sggjy_detail_status_view);
        detailNsv = getView().findViewById(R.id.detail_nsv);

        llIvBack.setOnClickListener(this);
        llShare.setOnClickListener(this);
        llFav.setOnClickListener(this);


    }

    @Override
    public void initData() {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("工程招标中标公示详情");
        sggjyDetailStatusView.setOnRetryClickListener(mRetryClickListener);

    }

    //点击重试
    final View.OnClickListener mRetryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            requestData();
        }
    };

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
                    mainBarName.setText("工程招标中标公示详情");
                }

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    // 底部
                    mainBarName.setText(shareTitle);
                }
            }
        });
    }

    private long id = 0;

    private void requestData() {


        if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
            //已登陆时的数据请求

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
                                ivFav.setImageResource(R.drawable.ic_faved);
                            } else if (favorite == 0) {
                                myFav = 0;
                                ivFav.setImageResource(R.drawable.ic_fav);
                            }
                            if ("200".equals(status)) {
                                final JSONObject data = object.getJSONObject("data");

                                final String url = data.getString("url");
                                shareUrl = url;

                                tvDataDetail.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getActivity(), BrowserActivity.class);
                                        intent.putExtra("url", url);
                                        intent.putExtra("title", shareTitle);
                                        startActivity(intent);
                                    }
                                });


                                String reportTitle = data.getString("reportTitle");
                                shareTitle = reportTitle;
                                if (!TextUtils.isEmpty(reportTitle)) {
                                    tvMainTitle.setText(reportTitle);
                                } else {
                                    tvMainTitle.setText("/");
                                }
                                String sysTime = data.getString("sysTime");
                                if (!TextUtils.isEmpty(sysTime)) {
                                    tvDataTime.setText(sysTime.substring(0, 10));
                                } else {
                                    tvDataTime.setText("/");
                                }
                                String entryName = data.getString("entryName");
                                shareContent = entryName;
                                if (!TextUtils.isEmpty(entryName)) {
                                    tvOwerBiaoduanname.setText(entryName);
                                } else {
                                    tvOwerBiaoduanname.setText("/");
                                }
                                String entryOwner = data.getString("entryOwner");
                                if (!TextUtils.isEmpty(entryOwner)) {
                                    tvOwerXiangmuyezhu.setText(entryOwner);
                                } else {
                                    tvOwerXiangmuyezhu.setText("/");
                                }
                                String ownerTel = data.getString("ownerTel");
                                if (!TextUtils.isEmpty(ownerTel)) {
                                    tvOwerXiangmuyezhuNumb.setText(ownerTel);
                                } else {
                                    tvOwerXiangmuyezhuNumb.setText("/");
                                }
                                String tenderee = data.getString("tenderee");
                                if (!TextUtils.isEmpty(tenderee)) {
                                    tvOwerZhaobiaoren.setText(tenderee);
                                } else {
                                    tvOwerZhaobiaoren.setText("/");
                                }
                                String tendereeTel = data.getString("tendereeTel");
                                if (!TextUtils.isEmpty(tendereeTel)) {
                                    tvOwerZhaobiaorenNumb.setText(tendereeTel);
                                } else {
                                    tvOwerZhaobiaorenNumb.setText("/");
                                }
                                String biddingAgency = data.getString("biddingAgency");
                                if (!TextUtils.isEmpty(biddingAgency)) {
                                    tvOwerZhaobiaodaili.setText(biddingAgency);
                                } else {
                                    tvOwerZhaobiaodaili.setText("/");
                                }
                                String biddingAgencTel = data.getString("biddingAgencTel");
                                if (!TextUtils.isEmpty(biddingAgencTel)) {
                                    tvOwerZhaobiaodailiNumb.setText(biddingAgencTel);
                                } else {
                                    tvOwerZhaobiaodailiNumb.setText("/");
                                }
                                String placeAddress = data.getString("placeAddress");
                                if (!TextUtils.isEmpty(placeAddress)) {
                                    tvOwerKaibiaodidian.setText(placeAddress);
                                } else {
                                    tvOwerKaibiaodidian.setText("/");
                                }
                                String placeTime = data.getString("placeTime");
                                if (!TextUtils.isEmpty(placeTime)) {
                                    tvOwerKaibiaoshijian.setText(placeTime);
                                } else {
                                    tvOwerKaibiaoshijian.setText("/");
                                }
                                String publicityPeriod = data.getString("publicityPeriod");
                                if (!TextUtils.isEmpty(publicityPeriod)) {
                                    tvOwerGongshiqi.setText(publicityPeriod);
                                } else {
                                    tvOwerGongshiqi.setText("/");
                                }
                                String bigPrice = data.getString("bigPrice");
                                if (!TextUtils.isEmpty(bigPrice)) {
                                    tvOwerToubiaoxianjia.setText(bigPrice);
                                } else {
                                    tvOwerToubiaoxianjia.setText("/");
                                }
                                String oneTree = data.getString("oneTree");
                                if (!TextUtils.isEmpty(oneTree)) {
                                    tvOwerDiyi.setText(oneTree.substring(0, oneTree.indexOf("_")));
                                } else {
                                    tvOwerDiyi.setText("/");
                                }
                                String twoTree = data.getString("twoTree");
                                if (!TextUtils.isEmpty(twoTree)) {
                                    tvOwerDier.setText(twoTree.substring(0, twoTree.indexOf("_")));
                                } else {
                                    tvOwerDier.setText("/");
                                }
                                String threeTree = data.getString("threeTree");
                                if (!TextUtils.isEmpty(threeTree)) {
                                    tvOwerDisan.setText(threeTree.substring(0, threeTree.indexOf("_")));
                                } else {
                                    tvOwerDisan.setText("/");
                                }
                                String oneCompany = data.getString("oneCompany");
                                if (!TextUtils.isEmpty(oneCompany)) {
                                    tvOwerCompanyName.setText(oneCompany);
                                } else {
                                    tvOwerCompanyName.setText("/");
                                }
                                String onePrice = data.getString("onePrice");
                                if (!TextUtils.isEmpty(onePrice)) {
                                    tvOwerCompanyBaojia.setText(onePrice);
                                } else {
                                    tvOwerCompanyBaojia.setText("/");
                                }
                                String oneReviewPrice = data.getString("oneReviewPrice");
                                if (!TextUtils.isEmpty(oneReviewPrice)) {
                                    tvOwerCompanyToubiaojia.setText(oneReviewPrice);
                                } else {
                                    tvOwerCompanyToubiaojia.setText("/");
                                }
                                String oneScore = data.getString("oneScore");
                                if (!TextUtils.isEmpty(oneScore)) {
                                    tvOwerCompanyPinjia.setText(oneScore);
                                } else {
                                    tvOwerCompanyPinjia.setText("/");
                                }
                                sggjyDetailStatusView.showContent();
                            } else {
                                sggjyDetailStatusView.showError();
                            }

                        }
                    })
                    .build()
                    .post();
        } else {
            //未登陆时的数据请求
            RestClient.builder()
                    .url(BiaoXunTongApi.URL_GETRESULTLISTDETAIL)
                    .params("entityId", mEntityId)
                    .params("entity", mEntity)
                    .params("deviceId", deviceId)
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(Headers headers, String response) {
                            String jiemi = AesUtil.aesDecrypt(response, BiaoXunTongApi.PAS_KEY);

                            final JSONObject object = JSON.parseObject(jiemi);
                            String status = object.getString("status");
                            final JSONObject data = object.getJSONObject("data");

                            if ("200".equals(status)) {
                                final String url = data.getString("url");
                                shareUrl = url;

                                tvDataDetail.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getActivity(), BrowserActivity.class);
                                        intent.putExtra("url", url);
                                        intent.putExtra("title", shareTitle);
                                        startActivity(intent);

                                    }
                                });
                                String reportTitle = data.getString("reportTitle");
                                shareTitle = reportTitle;
                                if (!TextUtils.isEmpty(reportTitle)) {
                                    tvMainTitle.setText(reportTitle);
                                } else {
                                    tvMainTitle.setText("/");
                                }
                                String sysTime = data.getString("sysTime");
                                if (!TextUtils.isEmpty(sysTime)) {
                                    tvDataTime.setText(sysTime.substring(0, 10));
                                } else {
                                    tvDataTime.setText("/");
                                }
                                String entryName = data.getString("entryName");
                                shareContent = entryName;
                                if (!TextUtils.isEmpty(entryName)) {
                                    tvOwerBiaoduanname.setText(entryName);
                                } else {
                                    tvOwerBiaoduanname.setText("/");
                                }
                                String entryOwner = data.getString("entryOwner");
                                if (!TextUtils.isEmpty(entryOwner)) {
                                    tvOwerXiangmuyezhu.setText(entryOwner);
                                } else {
                                    tvOwerXiangmuyezhu.setText("/");
                                }
                                String ownerTel = data.getString("ownerTel");
                                if (!TextUtils.isEmpty(ownerTel)) {
                                    tvOwerXiangmuyezhuNumb.setText(ownerTel);
                                } else {
                                    tvOwerXiangmuyezhuNumb.setText("/");
                                }
                                String tenderee = data.getString("tenderee");
                                if (!TextUtils.isEmpty(tenderee)) {
                                    tvOwerZhaobiaoren.setText(tenderee);
                                } else {
                                    tvOwerZhaobiaoren.setText("/");
                                }
                                String tendereeTel = data.getString("tendereeTel");
                                if (!TextUtils.isEmpty(tendereeTel)) {
                                    tvOwerZhaobiaorenNumb.setText(tendereeTel);
                                } else {
                                    tvOwerZhaobiaorenNumb.setText("/");
                                }
                                String biddingAgency = data.getString("biddingAgency");
                                if (!TextUtils.isEmpty(biddingAgency)) {
                                    tvOwerZhaobiaodaili.setText(biddingAgency);
                                } else {
                                    tvOwerZhaobiaodaili.setText("/");
                                }
                                String biddingAgencTel = data.getString("biddingAgencTel");
                                if (!TextUtils.isEmpty(biddingAgencTel)) {
                                    tvOwerZhaobiaodailiNumb.setText(biddingAgencTel);
                                } else {
                                    tvOwerZhaobiaodailiNumb.setText("/");
                                }
                                String placeAddress = data.getString("placeAddress");
                                if (!TextUtils.isEmpty(placeAddress)) {
                                    tvOwerKaibiaodidian.setText(placeAddress);
                                } else {
                                    tvOwerKaibiaodidian.setText("/");
                                }
                                String placeTime = data.getString("placeTime");
                                if (!TextUtils.isEmpty(placeTime)) {
                                    tvOwerKaibiaoshijian.setText(placeTime);
                                } else {
                                    tvOwerKaibiaoshijian.setText("/");
                                }
                                String publicityPeriod = data.getString("publicityPeriod");
                                if (!TextUtils.isEmpty(publicityPeriod)) {
                                    tvOwerGongshiqi.setText(publicityPeriod);
                                } else {
                                    tvOwerGongshiqi.setText("/");
                                }
                                String bigPrice = data.getString("bigPrice");
                                if (!TextUtils.isEmpty(bigPrice)) {
                                    tvOwerToubiaoxianjia.setText(bigPrice);
                                } else {
                                    tvOwerToubiaoxianjia.setText("/");
                                }
                                String oneTree = data.getString("oneTree");
                                if (!TextUtils.isEmpty(oneTree)) {
                                    tvOwerDiyi.setText(oneTree.substring(0, oneTree.indexOf("_")));
                                } else {
                                    tvOwerDiyi.setText("/");
                                }
                                String twoTree = data.getString("twoTree");
                                if (!TextUtils.isEmpty(twoTree)) {
                                    tvOwerDier.setText(twoTree.substring(0, twoTree.indexOf("_")));
                                } else {
                                    tvOwerDier.setText("/");
                                }
                                String threeTree = data.getString("threeTree");
                                if (!TextUtils.isEmpty(threeTree)) {
                                    tvOwerDisan.setText(threeTree.substring(0, threeTree.indexOf("_")));
                                } else {
                                    tvOwerDisan.setText("/");
                                }
                                String oneCompany = data.getString("oneCompany");
                                if (!TextUtils.isEmpty(oneCompany)) {
                                    tvOwerCompanyName.setText(oneCompany);
                                } else {
                                    tvOwerCompanyName.setText("/");
                                }
                                String onePrice = data.getString("onePrice");
                                if (!TextUtils.isEmpty(onePrice)) {
                                    tvOwerCompanyBaojia.setText(onePrice);
                                } else {
                                    tvOwerCompanyBaojia.setText("/");
                                }
                                String oneReviewPrice = data.getString("oneReviewPrice");
                                if (!TextUtils.isEmpty(oneReviewPrice)) {
                                    tvOwerCompanyToubiaojia.setText(oneReviewPrice);
                                } else {
                                    tvOwerCompanyToubiaojia.setText("/");
                                }
                                String oneScore = data.getString("oneScore");
                                if (!TextUtils.isEmpty(oneScore)) {
                                    tvOwerCompanyPinjia.setText(oneScore);
                                } else {
                                    tvOwerCompanyPinjia.setText("/");
                                }
                                if (sggjyDetailStatusView != null) {
                                    sggjyDetailStatusView.showContent();
                                }

                            } else {
                                sggjyDetailStatusView.showError();
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
