package com.lubanjianye.biaoxuntong.ui.main.user.company;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SearchView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment;

/**
 * 项目名:   9527
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.user.company
 * 文件名:   BindCompanyFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/21  11:47
 * 描述:     TODO
 */

public class BindCompanyFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout llIvBack = null;
    private AppCompatTextView mainBarName = null;
    private SearchView viewSearcher = null;

    @Override
    public Object setLayout() {
        return R.layout.fragment_bind_company;
    }

    @Override
    public void initView() {
        llIvBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);
        viewSearcher = getView().findViewById(R.id.view_bind);
        llIvBack.setOnClickListener(this);


    }

    @Override
    public void initData() {
        mainBarName.setText("绑定企业");
        llIvBack.setVisibility(View.VISIBLE);

        //根据id-search_src_text获取TextView
        SearchView.SearchAutoComplete searchText = (SearchView.SearchAutoComplete) viewSearcher.findViewById(R.id.search_src_text);
        //修改字体大小
        searchText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);

        //修改字体颜色
        searchText.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
        searchText.setHintTextColor(ContextCompat.getColor(getContext(), R.color.search_hint));

        //根据id-search_mag_icon获取ImageView
        ImageView searchButton = (ImageView) viewSearcher.findViewById(R.id.search_mag_icon);

        searchButton.setImageResource(R.mipmap.search);

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_iv_back:
                getActivity().onBackPressed();
                break;
            default:
                break;
        }
    }
}
