package com.lubanjianye.biaoxuntong.event;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.event
 * 文件名:   StartBrotherEvent
 * 创建者:   lunious
 * 创建时间: 2017/12/9  0:52
 * 描述:     TODO
 */

public class StartBrotherEvent {
    public SupportFragment targetFragment;

    public StartBrotherEvent(SupportFragment targetFragment) {
        this.targetFragment = targetFragment;
    }
}
