package com.lubanjianye.biaoxuntong.app;

import android.content.Context;
import android.os.Handler;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.app
 * 文件名:   BiaoXunTong
 * 创建者:   lunious
 * 创建时间: 2017/12/10  19:17
 * 描述:     TODO
 */

public final class BiaoXunTong {
    public static Configurator init(Context context) {
        Configurator.getInstance()
                .getLatteConfigs()
                .put(ConfigKeys.APPLICATION_CONTEXT, context.getApplicationContext());
        return Configurator.getInstance();
    }

    public static Configurator getConfigurator() {
        return Configurator.getInstance();
    }

    public static <T> T getConfiguration(Object key) {
        return getConfigurator().getConfiguration(key);
    }

    public static Context getApplicationContext() {
        return getConfiguration(ConfigKeys.APPLICATION_CONTEXT);
    }

    public static Handler getHandler() {
        return getConfiguration(ConfigKeys.HANDLER);
    }

}
