package com.lubanjianye.biaoxuntong.ui.update;

import android.app.Activity;
import android.content.Intent;

import com.lubanjianye.biaoxuntong.base.BaseActivity;
import com.lubanjianye.biaoxuntong.base.BaseFragment;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.update
 * 文件名:   UpdateActivity
 * 创建者:   lunious
 * 创建时间: 2017/12/14  20:32
 * 描述:     TODO
 */

public class UpdateActivity extends BaseActivity {


    static String versionName = "";
    static String mContent = "";
    static String mUrl = "";


    public static void show(Activity activity, String name, String content,String url) {
        Intent intent = new Intent(activity, UpdateActivity.class);
        intent.putExtra("version", name);
        intent.putExtra("content", content);
        intent.putExtra("downloadUrl",url);
        versionName = name;
        mContent = content;
        mUrl = url;
        activity.startActivityForResult(intent, 0x01);
    }

    @Override
    public BaseFragment setRootFragment() {

        return UpdateFragment.create(versionName, mContent,mUrl);
    }
}
