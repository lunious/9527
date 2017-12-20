package com.lubanjianye.biaoxuntong.ui.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.IdRes;

/**
 * 项目名:   Lunioussky
 * 包名:     com.lubanjianye.biaoxuntong.ui.share
 * 文件名:   OpenUtils
 * 创建者:   lunious
 * 创建时间: 2017/11/2  17:12
 * 描述:     TODO
 */

public class OpenUtils {

    public static Bitmap getShareBitmap(Context context, @IdRes int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

    public static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System
                .currentTimeMillis();
    }
}

