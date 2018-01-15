package com.lubanjianye.biaoxuntong.ui.main.user.avater;

import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.ui.main.user.company.BindCompanyActivity;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * 项目名:   LBBXT
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.fragment.user.avater
 * 文件名:   AvaterFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/15  13:05
 * 描述:     TODO
 */

public class AvaterFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout llIvBack = null;
    private AppCompatTextView mainBarName = null;
    private AppCompatTextView tvUserName = null;
    private AppCompatTextView tvUserCompany = null;
    private AppCompatTextView tvUserMobile = null;

    private long id = 0;
    private String mobile = "";
    private String nickName = "";
    private String token = "";
    private String comid = "";
    private String companyName = "";


    @Override
    public Object setLayout() {
        return R.layout.fragment_avater;
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

        llIvBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);
        tvUserName = getView().findViewById(R.id.tv_user_name);
        tvUserCompany = getView().findViewById(R.id.tv_user_company);
        tvUserMobile = getView().findViewById(R.id.tv_user_mobile);
        llIvBack.setOnClickListener(this);
        tvUserCompany.setOnClickListener(this);
        tvUserMobile.setOnClickListener(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXXXXX(EventMessage message) {

        if (EventMessage.BIND_MOBILE_SUCCESS.equals(message.getMessage())) {
            //绑定手机号成功后更新UI
            List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
            for (int i = 0; i < users.size(); i++) {
                mobile = users.get(0).getMobile();
            }
            if (!TextUtils.isEmpty(mobile)) {
                tvUserMobile.setText(mobile);
            } else {
                tvUserMobile.setText("点击绑定手机号");
            }

        }
        if (EventMessage.BIND_COMPANY_SUCCESS.equals(message.getMessage())) {
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
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("个人中心");
    }

    @Override
    public void initEvent() {
        requestData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_iv_back:
                getActivity().onBackPressed();
                break;
            case R.id.tv_user_company:

                if ("点击绑定企业".equals(tvUserCompany.getText().toString().trim())) {

                    if (!TextUtils.isEmpty(mobile)) {
                        //进入绑定企业界面
                        startActivity(new Intent(getActivity(), BindCompanyActivity.class));
                    } else {
                        ToastUtil.shortToast(getContext(), "请先绑定手机号！");
                    }

                } else {
                    //TODO
                }
                break;
            case R.id.tv_user_mobile:
                if ("点击绑定手机号".equals(tvUserMobile.getText().toString().trim())) {
                    //进入绑定手机号界面
                    startActivity(new Intent(getActivity(), BindMobileActivity.class));
                    getActivity().onBackPressed();
                } else {
                    //TODO
                }
                break;
            default:
                break;
        }
    }


    public void requestData() {

        List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();

        for (int i = 0; i < users.size(); i++) {
            id = users.get(0).getId();
            mobile = users.get(0).getMobile();
            nickName = users.get(0).getNickName();
            token = users.get(0).getToken();
            comid = users.get(0).getComid();
            companyName = users.get(0).getCompanyName();

        }

        if (!TextUtils.isEmpty(nickName)) {
            tvUserName.setText(nickName);
        } else {
            tvUserName.setText(mobile);
        }

        if (!TextUtils.isEmpty(mobile)) {
            tvUserMobile.setText(mobile);
        } else {
            tvUserMobile.setText("点击绑定手机号");
        }


        if (!TextUtils.isEmpty(companyName)) {
            tvUserCompany.setText(companyName);
        } else {
            tvUserCompany.setText("点击绑定企业");
        }


    }
}
