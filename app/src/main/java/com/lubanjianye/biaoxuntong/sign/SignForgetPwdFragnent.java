package com.lubanjianye.biaoxuntong.sign;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.api.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.util.parser.RichTextParser;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;


/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.sign
 * 文件名:   SignForgetPwdFragnent
 * 创建者:   lunious
 * 创建时间: 2017/12/13  21:08
 * 描述:     TODO
 */

public class SignForgetPwdFragnent extends BaseFragment implements View.OnClickListener {

    private LinearLayout llIvBack = null;
    private AppCompatTextView mainBarName = null;
    private AppCompatEditText etNewUsername = null;
    private AppCompatEditText etNewCode = null;
    private AppCompatTextView tvNewSmsCall = null;
    private AppCompatEditText etNewPwd = null;
    private AppCompatTextView btNewSubmit = null;

    private boolean mMachPhoneNum = false;
    private CountDownTimer mTimer = null;

    @Override
    public Object setLayout() {
        return R.layout.fragment_forget_pwd;
    }

    @Override
    public void initView() {
        llIvBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);
        etNewUsername = getView().findViewById(R.id.et_new_username);
        etNewCode = getView().findViewById(R.id.et_new_code);
        tvNewSmsCall = getView().findViewById(R.id.tv_new_sms_call);
        etNewPwd = getView().findViewById(R.id.et_new_pwd);
        btNewSubmit = getView().findViewById(R.id.bt_new_submit);
        llIvBack.setOnClickListener(this);
        tvNewSmsCall.setOnClickListener(this);
        btNewSubmit.setOnClickListener(this);

    }

    @Override
    public void initData() {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("重置密码");

        etNewUsername.addTextChangedListener(
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
                            tvNewSmsCall.setAlpha(0.4f);
                        } else if (length == 11) {
                            if (mMachPhoneNum) {
                                if (tvNewSmsCall.getTag() == null) {
                                    tvNewSmsCall.setAlpha(1.0f);
                                } else {
                                    tvNewSmsCall.setAlpha(0.4f);
                                }
                            } else {
                                ToastUtil.shortBottonToast(getContext(), "请输入正确的手机号码");
                                tvNewSmsCall.setAlpha(0.4f);
                            }
                        } else if (length > 11) {
                            tvNewSmsCall.setAlpha(0.4f);
                        } else if (length <= 0) {
                            tvNewSmsCall.setAlpha(0.4f);
                        }


                    }
                }
        );


        etNewPwd.addTextChangedListener(new TextWatcher() {
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
                } else {

                }

                String username = etNewUsername.getText().toString().trim();
                String smsCode = etNewCode.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    ToastUtil.shortBottonToast(getContext(), "用户名未填写！");
                }
                if (TextUtils.isEmpty(smsCode)) {
                    ToastUtil.shortBottonToast(getContext(), "验证码未填写！");
                }
            }
        });

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_iv_back:
                if (mTimer != null) {
                    mTimer.onFinish();
                    mTimer.cancel();
                }
                getActivity().finish();
                getActivity().onBackPressed();
                break;
            case R.id.tv_new_sms_call:
                requestSmsCode();
                break;
            case R.id.bt_new_submit:
                requestRegister();
                break;
            default:
                break;
        }
    }

    //获取验证码
    private void requestSmsCode() {

        if (!mMachPhoneNum) {
            return;
        }
        if (tvNewSmsCall.getTag() == null) {
            tvNewSmsCall.setAlpha(0.6f);
            tvNewSmsCall.setTag(true);

            mTimer = new CountDownTimer(60 * 1000, 1000) {

                @SuppressLint("DefaultLocale")
                @Override
                public void onTick(long millisUntilFinished) {
                    tvNewSmsCall.setText(String.format("%s%s%d%s",
                            getResources().getString(R.string.register_sms_hint), "(", millisUntilFinished / 1000, ")"));
                }

                @Override
                public void onFinish() {
                    tvNewSmsCall.setTag(null);
                    tvNewSmsCall.setText(getResources().getString(R.string.register_sms_hint));
                    tvNewSmsCall.setAlpha(1.0f);
                }
            }.start();

            final String phone = etNewUsername.getText().toString().trim();

            OkGo.<String>post(BiaoXunTongApi.URL_GETCODE)
                    .params("phone", phone)
                    .params("type", "2")
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

    //修改密码
    private void requestRegister() {
        final String mobile = etNewUsername.getText().toString().trim();
        final String code = etNewCode.getText().toString().trim();
        final String pass = etNewPwd.getText().toString().trim();
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

        OkGo.<String>post(BiaoXunTongApi.URL_FORGETPWD)
                .params("code", mobile + "_" + code)
                .params("newPass", pass)
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
                            ToastUtil.shortBottonToast(getContext(), "重置密码成功，请登录！");
                            //跳到登陆页面
                            startActivity(new Intent(getActivity(), SignInActivity.class));
                            getActivity().onBackPressed();
                        } else {
                            ToastUtil.shortBottonToast(getContext(), message);
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        if (mTimer != null) {
                            mTimer.onFinish();
                            mTimer.cancel();
                        }
                        ToastUtil.shortBottonToast(getContext(), "修改失败！");
                        super.onError(response);
                    }
                });
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
