package com.lubanjianye.biaoxuntong.ui.main.index.sortcolumn;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.bean.SortColumnBean;

import java.util.List;

/**
 * 项目名:   LBBXT
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.index.sortcolumn
 * 文件名:   SortColumnAdapter
 * 创建者:   lunious
 * 创建时间: 2017/12/16  18:31
 * 描述:     TODO
 */

public class SortColumnAdapter extends BaseItemDraggableAdapter<SortColumnBean, BaseViewHolder> {


    public SortColumnAdapter(int layoutResId, @Nullable List<SortColumnBean> data) {
        super(layoutResId, data);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void convert(BaseViewHolder helper, SortColumnBean item) {

        helper.setText(R.id.test_tv, item.getName());
        if (item.isShowDele()) {
            helper.setVisible(R.id.test_dele, true);
        } else {
            helper.setVisible(R.id.test_dele, false);
        }
        if (item.isChangeColo()) {
//            helper.setTextColor(R.id.test_tv, android.graphics.Color.parseColor("#00bfdc"));
        }
    }
}
