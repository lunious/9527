package com.lubanjianye.biaoxuntong.ui.main.query;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.bean.QueryBean;

import java.util.List;

/**
 * 项目名:   9527
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.query
 * 文件名:   QueryAdapter
 * 创建者:   lunious
 * 创建时间: 2017/12/24  21:58
 * 描述:     TODO
 */

public class QueryAdapter extends BaseItemDraggableAdapter<QueryBean, BaseViewHolder> {

    public QueryAdapter(int layoutResId, @Nullable List<QueryBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, QueryBean item) {
        String zzlx = item.getZzlx();
        String dl = item.getDl();
        String xl = item.getXl();
        String zy = item.getZy();
        String dj = item.getDj();
        String dq = item.getDq();
        if (TextUtils.isEmpty(zzlx)) {
            helper.setVisible(R.id.tv_query_item, false);
        } else {
            helper.setText(R.id.tv_query_item, zzlx + (dl == null ? "" : "__" + dl) + (xl == null ? "" : "__" + xl) + (zy == null ? "" : "__" + zy)
                    + (dj == null ? "" : "__" + dj) + (dq == null ? "" : "__" + dq));
        }

    }
}
