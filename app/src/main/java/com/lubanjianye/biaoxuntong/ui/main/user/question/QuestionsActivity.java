package com.lubanjianye.biaoxuntong.ui.main.user.question;

import com.lubanjianye.biaoxuntong.base.BaseActivity;
import com.lubanjianye.biaoxuntong.base.BaseFragment;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.fragment.user.question
 * 文件名:   QuestionsActivity
 * 创建者:   lunious
 * 创建时间: 2017/12/11  22:34
 * 描述:     TODO
 */

public class QuestionsActivity extends BaseActivity {
    @Override
    public BaseFragment setRootFragment() {
        return new Questionsfragment();
    }

}
