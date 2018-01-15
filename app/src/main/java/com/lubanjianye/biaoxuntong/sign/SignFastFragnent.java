package com.lubanjianye.biaoxuntong.sign;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.net.RestClient;
import com.lubanjianye.biaoxuntong.net.api.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.net.callback.IFailure;
import com.lubanjianye.biaoxuntong.net.callback.ISuccess;
import com.lubanjianye.biaoxuntong.util.dialog.PromptDialog;
import com.lubanjianye.biaoxuntong.util.parser.RichTextParser;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import okhttp3.Headers;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.sign
 * 文件名:   SignFastFragnent
 * 创建者:   lunious
 * 创建时间: 2017/12/13  21:52
 * 描述:     TODO
 */

public class SignFastFragnent extends BaseFragment implements View.OnClickListener {

    private LinearLayout llIvBack = null;
    private AppCompatTextView mainBarName = null;
    private AppCompatEditText etRetrieveTel = null;
    private AppCompatEditText etRetrieveCodeInput = null;
    private AppCompatTextView retrieveSmsCall = null;
    private AppCompatButton btRetrieveSubmit = null;

    private PromptDialog promptDialog = null;

    private long id = 0;
    private String mobile = "";
    private String nickName = "";
    private String token = "";
    private String comid = "";
    private String imageUrl = "";
    private String companyName = "";

    private boolean mMachPhoneNum = false;
    private CountDownTimer mTimer = null;


    @Override
    public Object setLayout() {
        return R.layout.fragment_sign_fast;
    }

    @Override
    public void initView() {
        llIvBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);
        etRetrieveTel = getView().findViewById(R.id.et_retrieve_tel);
        etRetrieveCodeInput = getView().findViewById(R.id.et_retrieve_code_input);
        retrieveSmsCall = getView().findViewById(R.id.retrieve_sms_call);
        btRetrieveSubmit = getView().findViewById(R.id.bt_retrieve_submit);
        llIvBack.setOnClickListener(this);
        retrieveSmsCall.setOnClickListener(this);
        btRetrieveSubmit.setOnClickListener(this);


    }

    @Override
    public void initData() {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("验证码登陆");
        //创建对象
        promptDialog = new PromptDialog(getActivity());
        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);
    }

    @Override
    public void initEvent() {

        etRetrieveTel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                if (length > 0) {

                } else {

                }

            }

            @SuppressWarnings("deprecation")
            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                String input = s.toString();
                mMachPhoneNum = RichTextParser.machPhoneNum(input);

                //对提交控件的状态判定
                if (mMachPhoneNum) {
                    String smsCode = etRetrieveCodeInput.getText().toString().trim();

                    if (!TextUtils.isEmpty(smsCode)) {
                        btRetrieveSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                        btRetrieveSubmit.setTextColor(getResources().getColor(R.color.main_status_white));
                    } else {
                        btRetrieveSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                        btRetrieveSubmit.setTextColor(getResources().getColor(R.color.main_status_white));
                    }
                } else {
                    btRetrieveSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    btRetrieveSubmit.setTextColor(getResources().getColor(R.color.main_status_white));
                }

                if (length > 0 && length < 11) {

                    retrieveSmsCall.setAlpha(0.4f);

                } else if (length == 11) {
                    if (mMachPhoneNum) {

                        if (retrieveSmsCall.getTag() == null) {
                            retrieveSmsCall.setAlpha(1.0f);
                        } else {
                            retrieveSmsCall.setAlpha(0.4f);
                        }
                    } else {

                        Toast.makeText(getContext(), "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                        retrieveSmsCall.setAlpha(0.4f);
                    }
                } else if (length > 11) {
                    retrieveSmsCall.setAlpha(0.4f);

                } else if (length <= 0) {
                    retrieveSmsCall.setAlpha(0.4f);

                }

            }
        });

        etRetrieveCodeInput.addTextChangedListener(new TextWatcher() {
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
                if (length > 0 && mMachPhoneNum) {
                    btRetrieveSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                    btRetrieveSubmit.setTextColor(getResources().getColor(R.color.main_status_white));
                } else {
                    btRetrieveSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    btRetrieveSubmit.setTextColor(getResources().getColor(R.color.main_status_white));
                }

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_iv_back:
                if (mTimer != null) {
                    mTimer.onFinish();
                    mTimer.cancel();
                }
                getActivity().onBackPressed();
                break;
            case R.id.retrieve_sms_call:
                requestSmsCode();
                break;
            case R.id.bt_retrieve_submit:
                loginRequest();
                break;
            default:
                break;
        }
    }

    private void requestSmsCode() {

        if (!mMachPhoneNum) {
            return;
        }

        if (retrieveSmsCall.getTag() == null) {
            retrieveSmsCall.setAlpha(0.6f);
            retrieveSmsCall.setTag(true);

            mTimer = new CountDownTimer(60 * 1000, 1000) {

                @SuppressLint("DefaultLocale")
                @Override
                public void onTick(long millisUntilFinished) {
                    retrieveSmsCall.setText(String.format("%s%s%d%s",
                            getResources().getString(R.string.register_sms_hint), "(", millisUntilFinished / 1000, ")"));
                }

                @Override
                public void onFinish() {
                    retrieveSmsCall.setTag(null);
                    retrieveSmsCall.setText(getResources().getString(R.string.register_sms_hint));
                    retrieveSmsCall.setAlpha(1.0f);
                }
            }.start();

            final String phone = etRetrieveTel.getText().toString().trim();
            //获取验证码
            RestClient.builder()
                    .url(BiaoXunTongApi.URL_GETCODE)
                    .params("phone", phone)
                    .params("type", "3")
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(Headers headers, String response) {
                            final JSONObject profileJson = JSON.parseObject(response);
                            final String status = profileJson.getString("status");
                            final String message = profileJson.getString("message");
                            if ("200".equals(status)) {
                                ToastUtil.shortToast(getContext(), "验证码发送成功");
                            } else {
                                if (mTimer != null) {
                                    mTimer.onFinish();
                                    mTimer.cancel();
                                }
                                ToastUtil.shortToast(getContext(), message);
                            }
                        }
                    })
                    .failure(new IFailure() {
                        @Override
                        public void onFailure() {
                            if (mTimer != null) {
                                mTimer.onFinish();
                                mTimer.cancel();
                            }

                        }
                    })
                    .build()
                    .post();

        } else {
            ToastUtil.shortToast(getContext(), "别激动，休息一下吧...");
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void loginRequest() {

        final String username = etRetrieveTel.getText().toString().trim();
        final String password = etRetrieveCodeInput.getText().toString().trim();


        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(username)) {
            //登录
            RestClient.builder()
                    .url(BiaoXunTongApi.URL_FASTLOGIN)
                    .params("mobile", username)
                    .params("code", username + "_" + password)
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(Headers headers, String response) {
                            final JSONObject profileJson = JSON.parseObject(response);
                            final String status = profileJson.getString("status");
                            final String message = profileJson.getString("message");
                            if ("200".equals(status)) {
                                if (mTimer != null) {
                                    mTimer.onFinish();
                                    mTimer.cancel();
                                }
                                final JSONObject userInfo = JSON.parseObject(response).getJSONObject("data");
                                id = userInfo.getLong("id");
                                mobile = userInfo.getString("mobile");
                                token = headers.get("token");
                                comid = userInfo.getString("comid");
                                nickName = userInfo.getString("nickName");
                                imageUrl = null;

                                if (comid != null) {
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

                                                }
                                            })
                                            .build()
                                            .post();

                                }

                                final UserProfile profile = new UserProfile(id, mobile, nickName, token, comid, imageUrl, companyName);
                                DatabaseManager.getInstance().getDao().insert(profile);
                                AppSharePreferenceMgr.put(getContext(), EventMessage.LOGIN_SUCCSS, true);
                                EventBus.getDefault().post(new EventMessage(EventMessage.LOGIN_SUCCSS));
                                promptDialog.dismissImmediately();
                                getActivity().onBackPressed();
                            } else {
                                ToastUtil.shortToast(getContext(), message);

                            }
                        }
                    })
                    .failure(new IFailure() {
                        @Override
                        public void onFailure() {
                            promptDialog.showError("服务器繁忙，登陆失败");
                        }
                    })
                    .build()
                    .post();

        } else {
            ToastUtil.shortToast(getContext(), "手机号或验证码有误");
        }

    }
}
