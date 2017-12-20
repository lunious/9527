package com.lubanjianye.biaoxuntong.ui.main.query;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.bean.CompanySearchResultListBean;

import java.util.List;

/**
 * 项目名:   LBBXT
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.fragment.query
 * 文件名:   CompanySearchResultListAdapter
 * 创建者:   lunious
 * 创建时间: 2017/12/15  23:11
 * 描述:     TODO
 */


public class CompanySearchResultListAdapter extends BaseQuickAdapter<CompanySearchResultListBean, BaseViewHolder> {
    public CompanySearchResultListAdapter(int layoutResId, @Nullable List<CompanySearchResultListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CompanySearchResultListBean item) {

        helper.setText(R.id.tv_company_name, item.getQy());

        String lxr = item.getLxr();
        if (lxr != null) {
            helper.setText(R.id.tv_lxr, lxr);
        } else {
            helper.setText(R.id.tv_lxr, "暂未添加");
        }
        String areaType = item.getEntrySign();
        if ("0".equals(areaType)) {
            helper.setText(R.id.tv_area_type, "川内");
        } else if ("1".equals(areaType)) {
            helper.setText(R.id.tv_area_type, "入川");
        } else {
            helper.setText(R.id.tv_area_type, "");
            helper.setVisible(R.id.tv_area_type, false);
        }
    }
}

