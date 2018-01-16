package com.lubanjianye.biaoxuntong.ui.main.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.api.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.sign.AboutActivity;
import com.lubanjianye.biaoxuntong.sign.SignInActivity;
import com.lubanjianye.biaoxuntong.ui.browser.BrowserActivity;
import com.lubanjianye.biaoxuntong.ui.main.user.avater.AvaterActivity;
import com.lubanjianye.biaoxuntong.ui.main.user.company.MyCompanyActivity;
import com.lubanjianye.biaoxuntong.ui.main.user.setting.SettingActivity;
import com.lubanjianye.biaoxuntong.util.dialog.PromptButton;
import com.lubanjianye.biaoxuntong.util.dialog.PromptButtonListener;
import com.lubanjianye.biaoxuntong.util.dialog.PromptDialog;
import com.lubanjianye.biaoxuntong.util.loader.GlideImageLoader;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.fragment
 * 文件名:   IndexTabFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/9  0:33
 * 描述:     TODO
 */

public class UserTabFragment extends BaseFragment implements View.OnClickListener {


    private CircleImageView imgUserAvatar = null;
    private CircleImageView imgDefaultAvatar = null;
    private LinearLayout llCompany = null;
    private LinearLayout llHelper = null;
    private LinearLayout llQuestion = null;
    private LinearLayout llSetting = null;
    private RelativeLayout rlLogin = null;
    private RelativeLayout rlNoLogin = null;
    private AppCompatTextView tvUserCompany = null;
    private AppCompatTextView tvUserName = null;


    long id = 0;
    String mobile = "";
    String nickName = "";
    String token = "";
    String comid = "";
    String imageUrl = "";
    String companyName = "";


    private Banner banner = null;
    private String url_1 = "";
    private String url_2 = "";
    private String url_3 = "";
    private String detail_1 = "";
    private String detail_2 = "";
    private String detail_3 = "";

    private PromptDialog promptDialog = null;


    @Override
    public Object setLayout() {
        return R.layout.fragment_main_user;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //取消注册EventBus
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView() {
        //注册EventBus
        EventBus.getDefault().register(this);

        imgUserAvatar = getView().findViewById(R.id.img_user_avatar);
        imgDefaultAvatar = getView().findViewById(R.id.img_default_avatar);
        banner = getView().findViewById(R.id.banner);
        llCompany = getView().findViewById(R.id.ll_company);
        llHelper = getView().findViewById(R.id.ll_helper);
        llQuestion = getView().findViewById(R.id.ll_questions);
        llSetting = getView().findViewById(R.id.ll_setting);
        tvUserCompany = getView().findViewById(R.id.tv_user_company);
        tvUserName = getView().findViewById(R.id.tv_user_name);
        rlLogin = getView().findViewById(R.id.rl_login);
        rlNoLogin = getView().findViewById(R.id.rl_no_login);
        imgUserAvatar.setOnClickListener(this);
        imgDefaultAvatar.setOnClickListener(this);
        llCompany.setOnClickListener(this);
        llHelper.setOnClickListener(this);
        llQuestion.setOnClickListener(this);
        llSetting.setOnClickListener(this);

        //开启轮播图
        initBanner();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXXXXX(EventMessage message) {

        if (EventMessage.LOGIN_SUCCSS.equals(message.getMessage())) {
            //登陆成功后更新UI

            List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
            for (int i = 0; i < users.size(); i++) {
                id = users.get(0).getId();
                mobile = users.get(0).getMobile();
                nickName = users.get(0).getNickName();
                comid = users.get(0).getComid();
                imageUrl = users.get(0).getImageUrl();
                companyName = users.get(0).getCompanyName();

            }

            if (TextUtils.isEmpty(imageUrl)) {
                imgUserAvatar.setImageResource(R.mipmap.moren_touxiang);
            } else {
                Glide.with(getActivity()).load(imageUrl).into(imgUserAvatar);
            }

            rlNoLogin.setVisibility(View.GONE);
            tvUserCompany.setVisibility(View.VISIBLE);
            rlLogin.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(nickName)) {
                tvUserName.setText(nickName);
            } else {
                tvUserName.setText(mobile);
            }

            if (!TextUtils.isEmpty(companyName)) {
                tvUserCompany.setText(companyName);
            } else {
                tvUserCompany.setText("未绑定企业");
            }

        } else if (EventMessage.LOGIN_OUT.equals(message.getMessage())) {
            if (imgUserAvatar != null) {
                imgUserAvatar.setImageResource(R.mipmap.widget_default_face);
            }

            rlNoLogin.setVisibility(View.VISIBLE);
            tvUserCompany.setVisibility(View.GONE);
            rlLogin.setVisibility(View.GONE);
        } else if (EventMessage.BIND_COMPANY_SUCCESS.equals(message.getMessage())) {
            //绑定企业成功后更新UI
            List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
            for (int i = 0; i < users.size(); i++) {
                companyName = users.get(0).getCompanyName();
            }

            if (!TextUtils.isEmpty(companyName)) {
                tvUserCompany.setText(companyName);
            } else {
                tvUserCompany.setText("未绑定企业");
            }

        }
    }

    @Override
    public void initData() {
        //创建对象
        promptDialog = new PromptDialog(getActivity());
        if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
            long id = 0;
            String mobile = "";
            String nickName = "";
            String comid = "";
            String imageUrl = "";
            String companyName = "";

            List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
            for (int i = 0; i < users.size(); i++) {
                id = users.get(0).getId();
                nickName = users.get(0).getNickName();
                mobile = users.get(0).getMobile();
                comid = users.get(0).getComid();
                imageUrl = users.get(0).getImageUrl();
                companyName = users.get(0).getCompanyName();

            }

            if (TextUtils.isEmpty(imageUrl)) {
                imgUserAvatar.setImageResource(R.mipmap.moren_touxiang);
            } else {
                Glide.with(getActivity()).load(imageUrl).into(imgUserAvatar);
            }

            rlNoLogin.setVisibility(View.GONE);
            tvUserCompany.setVisibility(View.VISIBLE);
            rlLogin.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(nickName)) {
                tvUserName.setText(nickName);
            } else {
                tvUserName.setText(mobile);
            }

            if (!TextUtils.isEmpty(companyName)) {
                tvUserCompany.setText(companyName);
            } else {
                tvUserCompany.setText("未绑定企业");
            }
        } else {
            imgUserAvatar.setImageResource(R.mipmap.widget_default_face);
            tvUserCompany.setVisibility(View.GONE);
            rlLogin.setVisibility(View.GONE);
            rlNoLogin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initEvent() {


    }

    public void initBanner() {

        OkGo.<String>post(BiaoXunTongApi.URL_GETINDEXBANNER)
                .params("imgType", 2)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final JSONObject object = JSON.parseObject(response.body());

                        String status = object.getString("status");

                        if ("200".equals(status)) {
                            final JSONObject data = object.getJSONObject("data");
                            url_1 = data.getString("imgOnePath");
                            url_2 = data.getString("imgTwoPath");
                            url_3 = data.getString("imgThreePath");
                            detail_1 = data.getString("imgOneUrl");
                            detail_2 = data.getString("imgTwoUrl");
                            detail_3 = data.getString("imgThreeUrl");
                        }

                        final List<String> urls = new ArrayList<>();
                        urls.add(url_1);
                        urls.add(url_2);
                        urls.add(url_3);
                        banner.setImages(urls).setImageLoader(new GlideImageLoader()).start();
                        banner.setDelayTime(4000);
                    }
                });
        banner.setOnBannerListener(new OnBannerListener() {
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_user_avatar:
                //跳到个人中心
                startActivity(new Intent(getContext(), AvaterActivity.class));
                break;
            case R.id.img_default_avatar:
                //跳到登陆界面
                startActivity(new Intent(getContext(), SignInActivity.class));
                break;
            case R.id.ll_company:
                if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
                    List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
                    for (int i = 0; i < users.size(); i++) {
                        id = users.get(0).getId();
                        mobile = users.get(0).getNickName();
                        token = users.get(0).getToken();
                        comid = users.get(0).getComid();
                        companyName = users.get(0).getCompanyName();
                    }
                    if (!TextUtils.isEmpty(companyName)) {
                        //进入我的资质界面
                        startActivity(new Intent(getContext(), MyCompanyActivity.class));

                    } else {
                        //进入个人中心界面
                        ToastUtil.shortToast(getContext(), "请先绑定企业");
                        Intent intent = new Intent(getContext(), AvaterActivity.class);
                        startActivity(intent);

                    }
                } else {
                    //未登录去登陆
                    startActivity(new Intent(getContext(), SignInActivity.class));
                }
                break;
            case R.id.ll_helper:
                //客服界面
                final PromptButton cancel = new PromptButton("取      消", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton button) {

                    }
                });
                cancel.setTextColor(getResources().getColor(R.color.main_status_yellow));
                cancel.setTextSize(16);

                final PromptButton sure = new PromptButton("拨      打", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton button) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:400-028-9997"));
                        startActivity(intent);
                    }
                });
                sure.setTextColor(getResources().getColor(R.color.main_status_blue));
                sure.setTextSize(16);
                promptDialog.getAlertDefaultBuilder().withAnim(true).cancleAble(false).touchAble(false)
                        .round(8).loadingDuration(200);
                promptDialog.showWarnAlert("是否拨打:400-028-9997？", cancel, sure, true);

                break;
            case R.id.ll_questions:
                //关于我们界面
                startActivity(new Intent(getContext(), AboutActivity.class));
                break;
            case R.id.ll_setting:
                //设置界面
                startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            default:
                break;
        }
    }
}
