package com.lubanjianye.biaoxuntong.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.ContentFrameLayout;

import com.lubanjianye.biaoxuntong.R;

import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.fragment.base
 * 文件名:   BaseActivity
 * 创建者:   lunious
 * 创建时间: 2017/12/9  21:14
 * 描述:     TODO
 */

public abstract class BaseActivity extends SwipeBackActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContainer(savedInstanceState);
    }

    //用来容纳Fragment的容器
    private void initContainer(@Nullable Bundle savedInstanceState) {
        final ContentFrameLayout container = new ContentFrameLayout(this);
        container.setId(R.id.fragment_container);
        setContentView(container);
        if (savedInstanceState == null) {
            loadRootFragment(R.id.fragment_container, setRootFragment());
        }
    }

    public abstract BaseFragment setRootFragment();


    @Override
    protected void onDestroy() {
        //垃圾回收
        System.gc();
        System.runFinalization();
        super.onDestroy();
    }

    @Override
    public void onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        super.onBackPressedSupport();
    }


}
