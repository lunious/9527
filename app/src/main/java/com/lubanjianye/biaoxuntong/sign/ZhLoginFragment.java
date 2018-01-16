package com.lubanjianye.biaoxuntong.sign;

import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.igexin.sdk.PushManager;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.app.BiaoXunTong;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.api.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.util.dialog.PromptDialog;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by 11645 on 2018/1/15.
 */

public class ZhLoginFragment extends BaseFragment implements View.OnClickListener {

    private AppCompatEditText etLoginUsername = null;
    private AppCompatEditText etLoginPwd = null;
    private AppCompatTextView btLoginSubmit = null;

    private PromptDialog promptDialog = null;

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
        return R.layout.zh_login;
    }

    @Override
    public void initView() {
        etLoginUsername = getView().findViewById(R.id.et_login_username);
        etLoginPwd = getView().findViewById(R.id.et_login_pwd);
        btLoginSubmit = getView().findViewById(R.id.bt_login_submit);
        btLoginSubmit.setOnClickListener(this);


    }

    @Override
    public void initData() {
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

            }
        });
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login_submit:
                //账号密码登录
                loginRequest();
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
            OkGo.<String>post(BiaoXunTongApi.URL_LOGIN)
                    .params("username", username)
                    .params("password", password)
                    .params("clientId", clientID)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            final JSONObject profileJson = JSON.parseObject(response.body());
                            final String status = profileJson.getString("status");
                            final String message = profileJson.getString("message");


                            Log.d("JBAJHBSDASDAS", response.body());

                            if ("200".equals(status)) {
                                promptDialog.dismissImmediately();
                                final JSONObject userInfo = JSON.parseObject(response.body()).getJSONObject("data");
                                id = userInfo.getLong("id");
                                nickName = userInfo.getString("nickName");
                                token = userInfo.getString("token");
                                comid = userInfo.getString("comid");
                                mobile = userInfo.getString("mobile");
                                imageUrl = null;

                                Log.d("IUGASUIDGUISADUIGYS", id + "");

                                if (!"0".equals(comid)) {

                                    OkGo.<String>post(BiaoXunTongApi.URL_GETCOMPANYNAME)
                                            .params("userId", id)
                                            .params("comId", comid)
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onSuccess(Response<String> response) {
                                                    final JSONObject profileCompany = JSON.parseObject(response.body());
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
                                            });

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
                                ToastUtil.shortBottonToast(getContext(), message);

                            }
                        }
                    });


        } else {
            ToastUtil.shortBottonToast(getContext(), "账号或密码错误");
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
