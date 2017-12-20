package com.lubanjianye.biaoxuntong.searchview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 项目名:   LBBXT
 * 包名:     com.lubanjianye.biaoxuntong.searchview
 * 文件名:   SearchListView
 * 创建者:   lunious
 * 创建时间: 2017/12/19  11:18
 * 描述:     TODO
 */

public class SearchListView extends ListView {
    public SearchListView(Context context) {
        super(context);
    }

    public SearchListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //通过复写其onMeasure方法、达到对ScrollView适配的效果
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
