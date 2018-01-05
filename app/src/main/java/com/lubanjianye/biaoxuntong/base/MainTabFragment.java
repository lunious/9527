package com.lubanjianye.biaoxuntong.base;


import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;


/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.fragment.base
 * 文件名:   MainTabFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/9  0:35
 * 描述:     TODO
 */

public class MainTabFragment extends BaseFragment {


    @Override
    public Object setLayout() {
        return null;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }


    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 3000L;
    private long TOUCH_TIME = 0;

    /**
     * 处理回退事件
     *
     * @return
     */
    @Override
    public boolean onBackPressedSupport() {
        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            _mActivity.finish();
        } else {
            TOUCH_TIME = System.currentTimeMillis();
            ToastUtil.shortToast(getContext(), "再按一次退出鲁班标讯通");
        }
        return true;
    }
}
