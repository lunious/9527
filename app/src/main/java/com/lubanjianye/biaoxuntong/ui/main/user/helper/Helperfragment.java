package com.lubanjianye.biaoxuntong.ui.main.user.helper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.util.tosaty.Toasty;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.fragment.user.helper
 * 文件名:   Helperfragment
 * 创建者:   lunious
 * 创建时间: 2017/12/11  23:04
 * 描述:     TODO
 */

public class Helperfragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout llBack = null;
    private AppCompatTextView mainBarName = null;
    private AppCompatTextView tvTelphoneOne = null;
    private AppCompatTextView tvTelphoneTwo = null;
    private AppCompatTextView telQqOne = null;
    private AppCompatTextView telQqTwo = null;

    @Override
    public Object setLayout() {
        return R.layout.fragment_helper;
    }

    @Override
    public void initView() {
        llBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);
        tvTelphoneOne = getView().findViewById(R.id.tv_telphone_one);
        tvTelphoneTwo = getView().findViewById(R.id.tv_telphone_two);
        telQqOne = getView().findViewById(R.id.tel_qq_one);
        telQqTwo = getView().findViewById(R.id.tel_qq_two);
        llBack.setOnClickListener(this);
        tvTelphoneOne.setOnClickListener(this);
        tvTelphoneTwo.setOnClickListener(this);
        telQqOne.setOnClickListener(this);
        telQqTwo.setOnClickListener(this);


    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        llBack.setVisibility(View.VISIBLE);
        mainBarName.setText("官方客服");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_iv_back:
                getActivity().onBackPressed();
                break;
            case R.id.tv_telphone_one:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:400-028-9997"));
                startActivity(intent);
                break;
            case R.id.tv_telphone_two:
                Intent intent1 = new Intent(Intent.ACTION_DIAL);
                intent1.setData(Uri.parse("tel:028-86677999"));
                startActivity(intent1);
                break;
            case R.id.tel_qq_one:
                String qqNum = "1928667799";
                if (checkApkExist(getActivity(), "com.tencent.mobileqq")) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + qqNum + "&version=1")));
                } else {
                    Toasty.error(getContext(), "本机未安装QQ应用", Toast.LENGTH_SHORT, true).show();
                }
                break;
            case R.id.tel_qq_two:
                String qqNum2 = "1208667799";
                if (checkApkExist(getActivity(), "com.tencent.mobileqq")) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + qqNum2 + "&version=1")));
                } else {
                    Toasty.error(getContext(), "本机未安装QQ应用", Toast.LENGTH_SHORT, true).show();
                }
                break;

            default:
                break;
        }
    }

    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName)) {
            return false;
        }
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
