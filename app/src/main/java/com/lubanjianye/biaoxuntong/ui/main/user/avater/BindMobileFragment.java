package com.lubanjianye.biaoxuntong.ui.main.user.avater;

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
import com.lubanjianye.biaoxuntong.net.RestClient;
import com.lubanjianye.biaoxuntong.net.api.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.net.callback.IFailure;
import com.lubanjianye.biaoxuntong.net.callback.ISuccess;
import com.lubanjianye.biaoxuntong.util.parser.RichTextParser;
import com.lubanjianye.biaoxuntong.util.tosaty.Toasty;

import java.util.List;

import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Headers;

/**
 * 项目名:   LBBXT
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.fragment.user.avater
 * 文件名:   BindMobileFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/15  13:24
 * 描述:     TODO
 */

public class BindMobileFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout llIvBack = null;
    private AppCompatTextView mainBarName = null;

    private AppCompatEditText etBindTel = null;
    private AppCompatEditText etBindCode = null;
    private AppCompatTextView tvBindSmsCall = null;
    private AppCompatButton btnBindSubmit = null;

    private long id = 0;
    private String mobile = "";
    private String nickName = "";
    private String token = "";
    private String comid = "";
    private String imageUrl = "";
    private String companyName = "";


    private boolean mMachPhoneNum = false;
    private CountDownTimer mTimer = null;


    private PromptDialog promptDialog = null;

    @Override
    public Object setLayout() {
        return R.layout.fragment_bind_mobile;
    }

    @Override
    public void initView() {
        llIvBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);
        etBindTel = getView().findViewById(R.id.et_bind_tel);
        etBindCode = getView().findViewById(R.id.et_bind_code);
        tvBindSmsCall = getView().findViewById(R.id.tv_bind_sms_call);
        btnBindSubmit = getView().findViewById(R.id.btn_bind_submit);
        llIvBack.setOnClickListener(this);
        tvBindSmsCall.setOnClickListener(this);
        btnBindSubmit.setOnClickListener(this);


    }

    @Override
    public void initData() {
        mainBarName.setText("绑定手机号");
        llIvBack.setVisibility(View.VISIBLE);
        //创建对象
        promptDialog = new PromptDialog(getActivity());
        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);
    }

    @Override
    public void initEvent() {
        etBindTel.addTextChangedListener(new TextWatcher() {
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
                    String smsCode = etBindCode.getText().toString().trim();

                    if (!TextUtils.isEmpty(smsCode)) {
                        btnBindSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                        btnBindSubmit.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        btnBindSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                        btnBindSubmit.setTextColor(getResources().getColor(R.color.white));
                    }
                } else {
                    btnBindSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    btnBindSubmit.setTextColor(getResources().getColor(R.color.white));
                }

                if (length > 0 && length < 11) {

                    tvBindSmsCall.setAlpha(0.4f);

                } else if (length == 11) {
                    if (mMachPhoneNum) {

                        if (tvBindSmsCall.getTag() == null) {
                            tvBindSmsCall.setAlpha(1.0f);
                        } else {
                            tvBindSmsCall.setAlpha(0.4f);
                        }
                    } else {

                        Toast.makeText(getContext(), "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                        tvBindSmsCall.setAlpha(0.4f);
                    }
                } else if (length > 11) {
                    tvBindSmsCall.setAlpha(0.4f);

                } else if (length <= 0) {
                    tvBindSmsCall.setAlpha(0.4f);

                }

            }
        });

        etBindCode.addTextChangedListener(new TextWatcher() {
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
                    btnBindSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                    btnBindSubmit.setTextColor(getResources().getColor(R.color.white));
                } else {
                    btnBindSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    btnBindSubmit.setTextColor(getResources().getColor(R.color.white));
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_iv_back:
                getActivity().onBackPressed();
                break;
            case R.id.tv_bind_sms_call:
                requestSmsCode();
                break;
            case R.id.btn_bind_submit:
                promptDialog.showLoading("绑定中...");
                BindRequest();
                break;
            default:
                break;
        }
    }

    private void requestSmsCode() {

        if (!mMachPhoneNum) {
            return;
        }

        if (tvBindSmsCall.getTag() == null) {
            tvBindSmsCall.setAlpha(0.6f);
            tvBindSmsCall.setTag(true);

            mTimer = new CountDownTimer(60 * 1000, 1000) {

                @SuppressLint("DefaultLocale")
                @Override
                public void onTick(long millisUntilFinished) {
                    tvBindSmsCall.setText(String.format("%s%s%d%s",
                            getResources().getString(R.string.register_sms_hint), "(", millisUntilFinished / 1000, ")"));
                }

                @Override
                public void onFinish() {
                    tvBindSmsCall.setTag(null);
                    tvBindSmsCall.setText(getResources().getString(R.string.register_sms_hint));
                    tvBindSmsCall.setAlpha(1.0f);
                }
            }.start();


            List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
            for (int i = 0; i < users.size(); i++) {
                id = users.get(0).getId();
                nickName = users.get(0).getNickName();
                token = users.get(0).getToken();
                comid = users.get(0).getComid();
                imageUrl = users.get(0).getImageUrl();
                companyName = users.get(0).getCompanyName();
            }


            final String phone = etBindTel.getText().toString().trim();
            //获取验证码
            RestClient.builder()
                    .url(BiaoXunTongApi.URL_GETCODE)
                    .params("phone", phone)
                    .params("userId", id)
                    .params("type", "4")
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(Headers headers, String response) {
                            final JSONObject profileJson = JSON.parseObject(response);
                            final String status = profileJson.getString("status");
                            final String message = profileJson.getString("message");

                            if ("200".equals(status)) {
                                Toasty.success(getContext(), "验证码发送成功", Toast.LENGTH_SHORT, true).show();
                            } else {
                                if (mTimer != null) {
                                    mTimer.onFinish();
                                    mTimer.cancel();
                                }
                                Toasty.info(getContext(), message, Toast.LENGTH_SHORT, true).show();
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
            Toasty.info(getContext(), "别激动，休息一下吧...", Toast.LENGTH_SHORT, true).show();
        }

    }

    private void BindRequest() {

        final String username = etBindTel.getText().toString().trim();
        final String password = etBindCode.getText().toString().trim();

        List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
        for (int i = 0; i < users.size(); i++) {
            id = users.get(0).getId();
            nickName = users.get(0).getNickName();
            token = users.get(0).getToken();
            comid = users.get(0).getComid();
            imageUrl = users.get(0).getImageUrl();
            companyName = users.get(0).getCompanyName();
        }
        mobile = username;

        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(username)) {

            //绑定手机号
            RestClient.builder()
                    .url(BiaoXunTongApi.URL_BINDMOBILE)
                    .params("mobile", username)
                    .params("userId", id)
                    .params("code", id + "10000000_" + password)
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(Headers headers, String response) {

                            final JSONObject profileJson = JSON.parseObject(response);

                            final String status = profileJson.getString("status");
                            final String message = profileJson.getString("message");
                            if ("200".equals(status)) {
                                promptDialog.dismissImmediately();

                                if (mTimer != null) {
                                    mTimer.onFinish();
                                    mTimer.cancel();
                                }

                                final UserProfile profile = new UserProfile(id, mobile, nickName, token, comid, imageUrl, companyName);
                                DatabaseManager.getInstance().getDao().update(profile);
                                getActivity().onBackPressed();
                                Toasty.success(getContext(), "绑定成功", Toast.LENGTH_SHORT, true).show();
                            } else {
                                promptDialog.dismissImmediately();
                                if (mTimer != null) {
                                    mTimer.onFinish();
                                    mTimer.cancel();
                                }
                                Toasty.info(getContext(), message, Toast.LENGTH_SHORT, true).show();
                            }
                        }
                    })
                    .build()
                    .post();

        } else {
            Toasty.error(getContext(), "请输入正确的手机号!", Toast.LENGTH_SHORT, true).show();
        }


    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();

        if (promptDialog != null) {
            promptDialog.dismissImmediately();
        }
    }
}
