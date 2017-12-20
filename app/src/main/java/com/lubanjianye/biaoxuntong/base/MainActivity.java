package com.lubanjianye.biaoxuntong.base;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import com.lubanjianye.biaoxuntong.util.location.EasyPermissionsEx;
import com.lubanjianye.biaoxuntong.util.location.LocationHelper;
import com.lubanjianye.biaoxuntong.util.location.LocationUtils;

public class MainActivity extends BaseActivity {

    @Override
    public BaseFragment setRootFragment() {

        return new MainFragment();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 使用了 EasyPermissionsEx 类来管理动态权限配置
        if (EasyPermissionsEx.hasPermissions(MainActivity.this, mNeedPermissionsList)) {
            initLocation();
        } else {
            EasyPermissionsEx.requestPermissions(MainActivity.this, "需要定位权限来获取当地天气信息", 1, mNeedPermissionsList);
        }

        // 在页面销毁时取消定位监听
        LocationUtils.getInstance(MainActivity.this).removeLocationUpdatesListener();

    }

    @Override
    protected void onDestroy() {
        // 在页面销毁时取消定位监听
        LocationUtils.getInstance(MainActivity.this).removeLocationUpdatesListener();
        super.onDestroy();
    }

    private String[] mNeedPermissionsList = new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};


    private void initLocation() {
        LocationUtils.getInstance(MainActivity.this).initLocation(new LocationHelper() {
            @Override
            public void UpdateLocation(Location location) {
                Log.e("MoLin", "location.getLatitude():" + location.getLatitude());
            }

            @Override
            public void UpdateStatus(String provider, int status, Bundle extras) {
            }

            @Override
            public void UpdateGPSStatus(GpsStatus pGpsStatus) {

            }

            @Override
            public void UpdateLastLocation(Location location) {
                Log.e("MoLin", "UpdateLastLocation_location.getLatitude():" + location.getLatitude());

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("MoLin", "已获取权限!");
                    initLocation();
                } else {
                    if (EasyPermissionsEx.somePermissionPermanentlyDenied(this, mNeedPermissionsList)) {
                        EasyPermissionsEx.goSettings2Permissions(this, "需要定位权限来获取当地天气信息,但是该权限被禁止,你可以到设置中更改"
                                , "去设置", 1);
                    }
                }
            }
            break;
            default:
                break;

        }
    }

}
