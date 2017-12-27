package com.lubanjianye.biaoxuntong.sign;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.igexin.sdk.PushManager;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.app.BiaoXunTong;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.net.RestClient;
import com.lubanjianye.biaoxuntong.net.api.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.net.callback.ISuccess;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import me.leefeng.promptlibrary.PromptDialog;
import me.shaohui.shareutil.LoginUtil;
import me.shaohui.shareutil.login.LoginListener;
import me.shaohui.shareutil.login.LoginPlatform;
import me.shaohui.shareutil.login.LoginResult;
import me.shaohui.shareutil.login.result.QQToken;
import me.shaohui.shareutil.login.result.QQUser;
import me.shaohui.shareutil.login.result.WeiboToken;
import me.shaohui.shareutil.login.result.WeiboUser;
import me.shaohui.shareutil.login.result.WxToken;
import me.shaohui.shareutil.login.result.WxUser;
import okhttp3.Headers;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.sign
 * 文件名:   SignInFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/13  0:40
 * 描述:     TODO
 */

public class SignInFragment extends BaseFragment implements View.OnClickListener {

    private PromptDialog promptDialog = null;
    private LinearLayout llIvBack = null;
    private AppCompatTextView mainBarName = null;
    private AppCompatEditText etLoginUsername = null;
    private AppCompatEditText etLoginPwd = null;
    private AppCompatButton btLoginSubmit = null;
    private AppCompatTextView tvLoginForgetPwd = null;
    private AppCompatButton btLoginRegister = null;
    private AppCompatTextView tvLoginFast = null;
    private ImageView ivLoginWeibo = null;
    private ImageView ivLoginWx = null;
    private ImageView ivLoginQq = null;


    private LoginListener mLoginListener = null;

    private long id = 0;
    private String mobile = "";
    private String nickName = "";
    private String token = "";
    private String comid = "";
    private String imageUrl = "";
    private String companyName = "";

    private String clientID = PushManager.getInstance().getClientid(BiaoXunTong.getApplicationContext());


    @Override
    public Object setLayout() {
        return R.layout.fragment_sign_in;
    }

    @Override
    public void initView() {

        llIvBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);
        etLoginUsername = getView().findViewById(R.id.et_login_username);
        etLoginPwd = getView().findViewById(R.id.et_login_pwd);
        btLoginSubmit = getView().findViewById(R.id.bt_login_submit);
        tvLoginForgetPwd = getView().findViewById(R.id.tv_login_forget_pwd);
        btLoginRegister = getView().findViewById(R.id.bt_login_register);
        tvLoginFast = getView().findViewById(R.id.tv_login_fast);
        ivLoginWeibo = getView().findViewById(R.id.iv_login_weibo);
        ivLoginWx = getView().findViewById(R.id.iv_login_wx);
        ivLoginQq = getView().findViewById(R.id.iv_login_qq);

        llIvBack.setOnClickListener(this);
        btLoginSubmit.setOnClickListener(this);
        tvLoginForgetPwd.setOnClickListener(this);
        btLoginRegister.setOnClickListener(this);
        tvLoginFast.setOnClickListener(this);
        ivLoginWeibo.setOnClickListener(this);
        ivLoginWx.setOnClickListener(this);
        ivLoginQq.setOnClickListener(this);


    }

    @Override
    public void initData() {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("登陆");

        //初始化控件状态数据
        String holdUsername = (String) AppSharePreferenceMgr.get(getContext(), "username", "");
        etLoginUsername.setText(holdUsername);
        etLoginUsername.setSelection(holdUsername.length());

        //创建对象
        promptDialog = new PromptDialog(getActivity());
        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);

        etLoginUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressWarnings("deprecation")
            @Override
            public void afterTextChanged(Editable s) {
                String username = s.toString().trim();
                if (username.length() > 0) {

                } else {

                }

                String pwd = etLoginPwd.getText().toString().trim();
                if (!TextUtils.isEmpty(pwd)) {
                    btLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                    btLoginSubmit.setTextColor(getResources().getColor(R.color.white));
                } else {
                    btLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    btLoginSubmit.setTextColor(getResources().getColor(R.color.white));
                }

            }
        });

        etLoginPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressWarnings("deprecation")
            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if (length > 0) {

                } else {
                }

                String username = etLoginUsername.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    ToastUtil.shortBottonToast(getContext(), "用户名未填写!");
                }
                String pwd = etLoginPwd.getText().toString().trim();
                if (!TextUtils.isEmpty(pwd)) {
                    btLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                    btLoginSubmit.setTextColor(getResources().getColor(R.color.white));
                } else {
                    btLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    btLoginSubmit.setTextColor(getResources().getColor(R.color.white));
                }
            }
        });
    }

    @Override
    public void initEvent() {
        mLoginListener = new LoginListener() {

            @Override
            public void loginSuccess(LoginResult result) {
                promptDialog.dismissImmediately();
                promptDialog.showLoading("正在登录...");

                // 处理result
                switch (result.getPlatform()) {
                    case LoginPlatform.QQ:
                        QQUser user = (QQUser) result.getUserInfo();
                        QQToken mToken = (QQToken) result.getToken();
                        nickName = user.getNickname();
                        String openid = mToken.getOpenid();
                        imageUrl = user.getqZoneHeadImageLarge();

                        RestClient.builder()
                                .url(BiaoXunTongApi.URL_QQLOGIN)
                                .params("Source", "3")
                                .params("qq", openid)
                                .params("clientId", clientID)
                                .success(new ISuccess() {
                                    @Override
                                    public void onSuccess(Headers headers, String response) {
                                        final JSONObject profileJson = JSON.parseObject(response);
                                        final String status = profileJson.getString("status");
                                        final String message = profileJson.getString("message");

                                        if ("200".equals(status)) {
                                            promptDialog.dismissImmediately();
                                            final JSONObject userInfo = JSON.parseObject(response).getJSONObject("data");
                                            id = userInfo.getLong("id");
                                            token = headers.get("token");
                                            mobile = userInfo.getString("mobile");
                                            comid = userInfo.getString("comid");

                                            Log.d("IUGASUIDGUISADUIGYS", id + "");

                                            if (!"0".equals(comid)) {
                                                RestClient.builder()
                                                        .url(BiaoXunTongApi.URL_GETCOMPANYNAME)
                                                        .params("userId", id)
                                                        .params("comId", comid)
                                                        .success(new ISuccess() {
                                                            @Override
                                                            public void onSuccess(Headers headers, String response) {
                                                                final JSONObject profileCompany = JSON.parseObject(response);
                                                                final String status = profileCompany.getString("status");
                                                                final JSONObject data = profileCompany.getJSONObject("data");
                                                                if ("200".equals(status)) {
                                                                    companyName = data.getString("qy");
                                                                } else {
                                                                    companyName = null;
                                                                }
                                                                final UserProfile profile = new UserProfile(id, mobile, nickName, token, comid, imageUrl, companyName);
                                                                DatabaseManager.getInstance().getDao().insert(profile);
                                                                AppSharePreferenceMgr.put(getContext(), EventMessage.LOGIN_SUCCSS, true);
                                                                EventBus.getDefault().post(new EventMessage(EventMessage.LOGIN_SUCCSS));
                                                                getActivity().onBackPressed();
                                                                ToastUtil.shortBottonToast(getContext(), "登陆成功");

                                                            }
                                                        })
                                                        .build()
                                                        .post();

                                            } else {
                                                final UserProfile profile = new UserProfile(id, mobile, nickName, token, comid, imageUrl, companyName);
                                                DatabaseManager.getInstance().getDao().insert(profile);
                                                AppSharePreferenceMgr.put(getContext(), EventMessage.LOGIN_SUCCSS, true);
                                                EventBus.getDefault().post(new EventMessage(EventMessage.LOGIN_SUCCSS));
                                                getActivity().onBackPressed();
                                                ToastUtil.shortBottonToast(getContext(), "登陆成功");
                                            }


                                        } else {
                                            promptDialog.dismissImmediately();
                                            ToastUtil.shortToast(getContext(), message);
                                        }
                                    }
                                })
                                .build()
                                .post();
                        break;
                    case LoginPlatform.WEIBO:
                        WeiboUser weiboUser = (WeiboUser) result.getUserInfo();
                        WeiboToken weiboToken = (WeiboToken) result.getToken();
                        nickName = weiboUser.getNickname();
                        String Microblog = weiboUser.getOpenId();
                        imageUrl = weiboUser.getHeadimgurl_hd();

                        RestClient.builder()
                                .url(BiaoXunTongApi.URL_WEIBOLOGIN)
                                .params("Source", 2)
                                .params("Microblog", Microblog)
                                .params("clientId", clientID)
                                .success(new ISuccess() {
                                    @Override
                                    public void onSuccess(Headers headers, String response) {
                                        final JSONObject profileJson = JSON.parseObject(response);
                                        final String status = profileJson.getString("status");
                                        final String message = profileJson.getString("message");
                                        if ("200".equals(status)) {
                                            promptDialog.dismissImmediately();
                                            final JSONObject userInfo = JSON.parseObject(response).getJSONObject("data");
                                            id = userInfo.getLong("id");
                                            token = headers.get("token");
                                            mobile = userInfo.getString("mobile");
                                            comid = userInfo.getString("comid");
                                            companyName = null;

                                            if (!"0".equals(comid)) {
                                                RestClient.builder()
                                                        .url(BiaoXunTongApi.URL_GETCOMPANYNAME)
                                                        .params("userId", id)
                                                        .params("comId", comid)
                                                        .success(new ISuccess() {
                                                            @Override
                                                            public void onSuccess(Headers headers, String response) {
                                                                final JSONObject profileCompany = JSON.parseObject(response);
                                                                final String status = profileCompany.getString("status");
                                                                final JSONObject data = profileCompany.getJSONObject("data");

                                                                if ("200".equals(status)) {
                                                                    companyName = data.getString("qy");
                                                                } else {
                                                                    companyName = null;
                                                                }
                                                                final UserProfile profile = new UserProfile(id, mobile, nickName, token, comid, imageUrl, companyName);
                                                                DatabaseManager.getInstance().getDao().insert(profile);
                                                                AppSharePreferenceMgr.put(getContext(), EventMessage.LOGIN_SUCCSS, true);
                                                                EventBus.getDefault().post(new EventMessage(EventMessage.LOGIN_SUCCSS));
                                                                getActivity().onBackPressed();
                                                                ToastUtil.shortBottonToast(getContext(), "登陆成功");
                                                            }
                                                        })
                                                        .build()
                                                        .post();

                                            } else {
                                                final UserProfile profile = new UserProfile(id, mobile, nickName, token, comid, imageUrl, companyName);
                                                DatabaseManager.getInstance().getDao().insert(profile);
                                                AppSharePreferenceMgr.put(getContext(), EventMessage.LOGIN_SUCCSS, true);
                                                EventBus.getDefault().post(new EventMessage(EventMessage.LOGIN_SUCCSS));
                                                getActivity().onBackPressed();
                                                ToastUtil.shortBottonToast(getContext(), "登陆成功");

                                            }

                                        } else {
                                            promptDialog.dismissImmediately();
                                            ToastUtil.shortToast(getContext(), message);
                                        }
                                    }
                                })
                                .build()
                                .post();


                        break;
                    case LoginPlatform.WX:
                        WxUser wxUser = (WxUser) result.getUserInfo();
                        WxToken wxToken = (WxToken) result.getToken();

                        nickName = wxUser.getNickname();
                        String Oppenid = wxUser.getOpenId();
                        imageUrl = wxUser.getHeadImageUrl();

                        RestClient.builder()
                                .url(BiaoXunTongApi.URL_WEIXINLOGIN)
                                .params("Source", 1)
                                .params("Oppenid", Oppenid)
                                .params("clientId", clientID)
                                .success(new ISuccess() {
                                    @Override
                                    public void onSuccess(Headers headers, String response) {
                                        final JSONObject profileJson = JSON.parseObject(response);
                                        final String status = profileJson.getString("status");
                                        final String message = profileJson.getString("message");
                                        if ("200".equals(status)) {
                                            promptDialog.dismissImmediately();
                                            final JSONObject userInfo = JSON.parseObject(response).getJSONObject("data");
                                            id = userInfo.getLong("id");
                                            token = headers.get("token");
                                            comid = userInfo.getString("comid");
                                            mobile = userInfo.getString("mobile");
                                            companyName = null;

                                            if (!"0".equals(comid)) {
                                                RestClient.builder()
                                                        .url(BiaoXunTongApi.URL_GETCOMPANYNAME)
                                                        .params("userId", id)
                                                        .params("comId", comid)
                                                        .success(new ISuccess() {
                                                            @Override
                                                            public void onSuccess(Headers headers, String response) {
                                                                final JSONObject profileCompany = JSON.parseObject(response);
                                                                final String status = profileCompany.getString("status");
                                                                final JSONObject data = profileCompany.getJSONObject("data");

                                                                if ("200".equals(status)) {
                                                                    companyName = data.getString("qy");
                                                                } else {
                                                                    companyName = null;
                                                                }
                                                                final UserProfile profile = new UserProfile(id, mobile, nickName, token, comid, imageUrl, companyName);
                                                                DatabaseManager.getInstance().getDao().insert(profile);
                                                                AppSharePreferenceMgr.put(getContext(), EventMessage.LOGIN_SUCCSS, true);
                                                                EventBus.getDefault().post(new EventMessage(EventMessage.LOGIN_SUCCSS));
                                                                getActivity().onBackPressed();
                                                                ToastUtil.shortBottonToast(getContext(), "登陆成功");
                                                            }
                                                        })
                                                        .build()
                                                        .post();

                                            } else {
                                                final UserProfile profile = new UserProfile(id, mobile, nickName, token, comid, imageUrl, companyName);
                                                DatabaseManager.getInstance().getDao().insert(profile);
                                                AppSharePreferenceMgr.put(getContext(), EventMessage.LOGIN_SUCCSS, true);
                                                EventBus.getDefault().post(new EventMessage(EventMessage.LOGIN_SUCCSS));
                                                getActivity().onBackPressed();
                                                ToastUtil.shortBottonToast(getContext(), "登陆成功");

                                            }

                                        } else {
                                            promptDialog.dismissImmediately();
                                            ToastUtil.shortToast(getContext(), message);
                                        }
                                    }
                                })
                                .build()
                                .post();


                        break;
                    default:
                        break;
                }
            }

            @Override
            public void loginFailure(Exception e) {
                promptDialog.dismissImmediately();
                ToastUtil.shortBottonToast(getContext(), "登录失败");

            }

            @Override
            public void loginCancel() {
                promptDialog.dismissImmediately();
                ToastUtil.shortBottonToast(getContext(), "登录取消");
            }
        };
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_iv_back:
                if (promptDialog != null) {
                    promptDialog.dismissImmediately();
                }
                getActivity().onBackPressed();
                break;
            case R.id.bt_login_submit:
                //账号密码登录
                loginRequest();

                break;
            case R.id.bt_login_register:
                //用户注册
                startActivity(new Intent(getActivity(), SignUpActivity.class));
                break;
            case R.id.tv_login_fast:
                //快捷登陆
                startActivity(new Intent(getActivity(), SignFastActivity.class));
                getActivity().onBackPressed();
                break;
            case R.id.tv_login_forget_pwd:
                //重置密码
                startActivity(new Intent(getActivity(), SignForgetPwdActivity.class));
                break;
            case R.id.iv_login_weibo:
                //微博登陆
                promptDialog.showLoading("请稍后...");
                LoginUtil.login(getActivity(), LoginPlatform.WEIBO, mLoginListener);
                break;
            case R.id.iv_login_wx:
                //微信登陆
                promptDialog.showLoading("请稍后...");
                LoginUtil.login(getActivity(), LoginPlatform.WX, mLoginListener);
                break;
            case R.id.iv_login_qq:
                //QQ登陆
                promptDialog.showLoading("请稍后...");
                LoginUtil.login(getActivity(), LoginPlatform.QQ, mLoginListener);
                break;
            default:
                break;
        }

    }


    @SuppressWarnings("ConstantConditions")
    private void loginRequest() {

        final String username = etLoginUsername.getText().toString().trim();
        final String password = etLoginPwd.getText().toString().trim();

        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(username)) {


            promptDialog.showLoading("正在登陆");
            //登录
            RestClient.builder()
                    .url(BiaoXunTongApi.URL_LOGIN)
                    .params("username", username)
                    .params("password", password)
                    .params("clientId", clientID)
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(Headers headers, String response) {
                            final JSONObject profileJson = JSON.parseObject(response);
                            final String status = profileJson.getString("status");
                            final String message = profileJson.getString("message");


                            if ("200".equals(status)) {
                                promptDialog.dismissImmediately();
                                final JSONObject userInfo = JSON.parseObject(response).getJSONObject("data");
                                id = userInfo.getLong("id");
                                nickName = userInfo.getString("nickName");
                                token = userInfo.getString("token");
                                comid = userInfo.getString("comid");
                                mobile = userInfo.getString("mobile");
                                imageUrl = null;

                                Log.d("IUGASUIDGUISADUIGYS", id + "");

                                if (!"0".equals(comid)) {
                                    RestClient.builder()
                                            .url(BiaoXunTongApi.URL_GETCOMPANYNAME)
                                            .params("userId", id)
                                            .params("comId", comid)
                                            .success(new ISuccess() {
                                                @Override
                                                public void onSuccess(Headers headers, String response) {
                                                    final JSONObject profileCompany = JSON.parseObject(response);
                                                    final String status = profileCompany.getString("status");
                                                    final JSONObject data = profileCompany.getJSONObject("data");

                                                    if ("200".equals(status)) {
                                                        companyName = data.getString("qy");
                                                    } else {
                                                        companyName = null;
                                                    }

                                                    promptDialog.dismissImmediately();
                                                    final UserProfile profile = new UserProfile(id, mobile, nickName, token, comid, imageUrl, companyName);
                                                    DatabaseManager.getInstance().getDao().insert(profile);
                                                    holdAccount();
                                                    AppSharePreferenceMgr.put(getContext(), EventMessage.LOGIN_SUCCSS, true);
                                                    EventBus.getDefault().post(new EventMessage(EventMessage.LOGIN_SUCCSS));
                                                    getActivity().onBackPressed();
                                                    ToastUtil.shortBottonToast(getContext(), "登陆成功");


                                                }
                                            })
                                            .build()
                                            .post();

                                } else {

                                    promptDialog.dismissImmediately();
                                    final UserProfile profile = new UserProfile(id, mobile, nickName, token, comid, imageUrl, companyName);
                                    DatabaseManager.getInstance().getDao().insert(profile);
                                    holdAccount();
                                    AppSharePreferenceMgr.put(getContext(), EventMessage.LOGIN_SUCCSS, true);
                                    EventBus.getDefault().post(new EventMessage(EventMessage.LOGIN_SUCCSS));
                                    getActivity().onBackPressed();
                                    ToastUtil.shortBottonToast(getContext(), "登陆成功");
                                }


                            } else {
                                promptDialog.dismissImmediately();
                                ToastUtil.shortToast(getContext(), message);

                            }
                        }
                    })
                    .build()
                    .post();

        } else {
            ToastUtil.shortBottonToast(getContext(), "请输入正确的手机号!");
        }

    }

    //保存账号信息
    private void holdAccount() {
        String username = etLoginUsername.getText().toString().trim();
        if (!TextUtils.isEmpty(username)) {
            if (AppSharePreferenceMgr.contains(getContext(), "username")) {
                AppSharePreferenceMgr.remove(getContext(), "username");
            }
            AppSharePreferenceMgr.put(getContext(), "username", username);
        }
    }
}
