package com.lubanjianye.biaoxuntong.ui.main.user.setting;

        import com.lubanjianye.biaoxuntong.base.BaseActivity;
        import com.lubanjianye.biaoxuntong.base.BaseFragment;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.fragment.user.setting
 * 文件名:   SettingActivity
 * 创建者:   lunious
 * 创建时间: 2017/12/11  23:13
 * 描述:     TODO
 */

public class SettingActivity extends BaseActivity {
    @Override
    public BaseFragment setRootFragment() {
        return new Settingfragment();
    }

}
