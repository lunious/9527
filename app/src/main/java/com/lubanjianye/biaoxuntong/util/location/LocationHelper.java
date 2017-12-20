package com.lubanjianye.biaoxuntong.util.location;

import android.location.GpsStatus;
import android.location.Location;
import android.os.Bundle;

/**
 * 项目名:   Lunioussky
 * 包名:     com.lubanjianye.biaoxuntong.util.location
 * 文件名:   LocationHelper
 * 创建者:   lunious
 * 创建时间: 2017/11/23  13:40
 * 描述:     TODO
 */

public interface LocationHelper {

    void UpdateLocation(Location location);//位置信息发生改变

    void UpdateStatus(String provider, int status, Bundle extras);//位置状态发生改变

    void UpdateGPSStatus(GpsStatus pGpsStatus);//GPS状态发生改变

    void UpdateLastLocation(Location location);
}
