package com.lubanjianye.biaoxuntong.sign;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.api.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.util.dialog.PromptDialog;
import com.lubanjianye.biaoxuntong.util.parser.RichTextParser;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.sign
 * 文件名:   SignUpFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/13  12:18
 * 描述:     TODO
 */

public class SignUpFragment extends BaseFragment implements View.OnClickListener {

    private PromptDialog promptDialog = null;

    protected LinearLayout llIvBack = null;
    protected AppCompatTextView mainBarName = null;
    protected EditText etRegisterUsername = null;
    protected EditText etRegisterCode = null;
    protected TextView tvRegisterSmsCall = null;
    protected EditText etRegisterPwd = null;
    protected AppCompatTextView btRegisterSubmit = null;
    protected TextView tvZhucexieyi = null;


    private boolean mMachPhoneNum;
    private CountDownTimer mTimer;

    @Override
    public Object setLayout() {
        return R.layout.fragment_sign_up;
    }

    @Override
    public void initView() {
        llIvBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);
        etRegisterUsername = getView().findViewById(R.id.et_register_username);
        etRegisterCode = getView().findViewById(R.id.et_register_code);
        tvRegisterSmsCall = getView().findViewById(R.id.tv_register_sms_call);
        etRegisterPwd = getView().findViewById(R.id.et_register_pwd);
        btRegisterSubmit = getView().findViewById(R.id.bt_register_submit);
        tvZhucexieyi = getView().findViewById(R.id.tv_zhucexieyi);
        llIvBack.setOnClickListener(this);
        tvRegisterSmsCall.setOnClickListener(this);
        btRegisterSubmit.setOnClickListener(this);
        tvZhucexieyi.setOnClickListener(this);


    }

    @Override
    public void initData() {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("注册");

        //创建对象
        promptDialog = new PromptDialog(getActivity());
        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);
    }

    @Override
    public void initEvent() {
        etRegisterUsername.addTextChangedListener(
                new TextWatcher() {

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
                        String input = s.toString();
                        mMachPhoneNum = RichTextParser.machPhoneNum(input);


                        if (length > 0 && length < 11) {
                            tvRegisterSmsCall.setAlpha(0.4f);
                        } else if (length == 11) {
                            if (mMachPhoneNum) {
                                if (tvRegisterSmsCall.getTag() == null) {
                                    tvRegisterSmsCall.setAlpha(1.0f);
                                } else {
                                    tvRegisterSmsCall.setAlpha(0.4f);
                                }
                            } else {
                                ToastUtil.shortToast(getContext(), "请输入正确的手机号码");
                                tvRegisterSmsCall.setAlpha(0.4f);
                            }
                        } else if (length > 11) {
                            tvRegisterSmsCall.setAlpha(0.4f);
                        } else if (length <= 0) {
                            tvRegisterSmsCall.setAlpha(0.4f);
                        }


                    }
                }
        );

        etRegisterCode.addTextChangedListener(new TextWatcher() {
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
                String pwd = etRegisterPwd.getText().toString().trim();

            }
        });

        etRegisterPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if (length > 0) {
                    ;
                } else {

                }

                String username = etRegisterUsername.getText().toString().trim();
                String smsCode = etRegisterCode.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    ToastUtil.shortBottonToast(getContext(), "用户名未填写");
                }
                if (TextUtils.isEmpty(smsCode)) {
                    ToastUtil.shortBottonToast(getContext(), "验证码未填写");
                }
            }
        });
    }


    //获取验证码
    private void requestSmsCode() {

        if (!mMachPhoneNum) {
            return;
        }
        if (tvRegisterSmsCall.getTag() == null) {
            tvRegisterSmsCall.setAlpha(0.6f);
            tvRegisterSmsCall.setTag(true);

            mTimer = new CountDownTimer(60 * 1000, 1000) {

                @SuppressLint("DefaultLocale")
                @Override
                public void onTick(long millisUntilFinished) {
                    tvRegisterSmsCall.setText(String.format("%s%s%d%s",
                            getResources().getString(R.string.register_sms_hint), "(", millisUntilFinished / 1000, ")"));
                }

                @Override
                public void onFinish() {
                    tvRegisterSmsCall.setTag(null);
                    tvRegisterSmsCall.setText(getResources().getString(R.string.register_sms_hint));
                    tvRegisterSmsCall.setAlpha(1.0f);
                }
            }.start();

            final String phone = etRegisterUsername.getText().toString().trim();

            OkGo.<String>post(BiaoXunTongApi.URL_GETCODE)
                    .params("phone", phone)
                    .params("type", "1")
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            final JSONObject profileJson = JSON.parseObject(response.body());
                            final String status = profileJson.getString("status");
                            final String message = profileJson.getString("message");
                            if ("200".equals(status)) {
                                ToastUtil.shortBottonToast(getContext(), "验证码发送成功");

                            } else {
                                if (mTimer != null) {
                                    mTimer.onFinish();
                                    mTimer.cancel();
                                }
                                ToastUtil.shortBottonToast(getContext(), message);
                            }
                        }

                        @Override
                        public void onError(Response<String> response) {
                            if (mTimer != null) {
                                mTimer.onFinish();
                                mTimer.cancel();
                            }
                            super.onError(response);
                        }
                    });


        } else {
            ToastUtil.shortBottonToast(getContext(), "别激动，休息一下吧...");
        }
    }

    //请求注册
    private void requestRegister() {

        final String mobile = etRegisterUsername.getText().toString().trim();
        final String code = etRegisterCode.getText().toString().trim();
        final String pass = etRegisterPwd.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            ToastUtil.shortBottonToast(getContext(), "请输入手机号");

            return;
        }
        if (!mMachPhoneNum || TextUtils.isEmpty(code)) {
            ToastUtil.shortBottonToast(getContext(), "验证码不正确");

            return;
        }
        if (TextUtils.isEmpty(pass)) {
            ToastUtil.shortBottonToast(getContext(), "密码格式不对");
            return;
        }

        if (!TextUtils.isEmpty(mobile) && !TextUtils.isEmpty(code) && !TextUtils.isEmpty(pass)) {
            promptDialog.showLoading("正在注册...");

            OkGo.<String>post(BiaoXunTongApi.URL_REGISTER)
                    .params("mobile", mobile)
                    .params("code", mobile + "_" + code)
                    .params("pass", pass)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            final JSONObject profileJson = JSON.parseObject(response.body());
                            final String status = profileJson.getString("status");
                            final String message = profileJson.getString("message");
                            if ("200".equals(status)) {
                                if (mTimer != null) {
                                    mTimer.onFinish();
                                    mTimer.cancel();
                                }
                                promptDialog.dismissImmediately();
                                ToastUtil.shortBottonToast(getContext(), "注册成功，请登录！");
                                //跳到登陆页面
                                startActivity(new Intent(getActivity(), SignInActivity.class));
                                getActivity().onBackPressed();
                                holdAccount();
                            } else {
                                promptDialog.dismissImmediately();
                                ToastUtil.shortBottonToast(getContext(), message);
                            }
                        }

                        @Override
                        public void onError(Response<String> response) {
                            if (mTimer != null) {
                                mTimer.onFinish();
                                mTimer.cancel();
                            }
                            ToastUtil.shortBottonToast(getContext(), "注册失败！");
                            super.onError(response);
                        }
                    });
        }

    }

    //保存账号信息
    private void holdAccount() {
        String username = etRegisterUsername.getText().toString().trim();
        if (!TextUtils.isEmpty(username)) {
            if (AppSharePreferenceMgr.contains(getContext(), "username")) {
                AppSharePreferenceMgr.remove(getContext(), "username");
            }
            AppSharePreferenceMgr.put(getContext(), "username", username);
        }
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
            case R.id.tv_register_sms_call:
                //请求验证码
                requestSmsCode();
                break;
            case R.id.bt_register_submit:
                //注册
                requestRegister();
                break;
            case R.id.tv_zhucexieyi:
                startActivity(new Intent(getContext(), ZhuCeXYActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        if (mTimer != null) {
            mTimer.onFinish();
            mTimer.cancel();
        }
        super.onDestroyView();
    }
}
