package com.lubanjianye.biaoxuntong.net.callback;

import okhttp3.Headers;

/**
 * 项目名:   LuBanBiaoXunTong
 * 包名:     com.lubanjianye.biaoxuntong.core.net.callback
 * 文件名:   ISuccess
 * 创建者:   lunious
 * 创建时间: 2017/10/13  9:25
 * 描述:     TODO
 */

public interface ISuccess {
    void onSuccess(Headers headers, String response);
}
