package com.lubanjianye.biaoxuntong.ui.main.index.search;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.app.BiaoXunTong;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.bean.IndexListBean;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.api.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.loadmore.CustomLoadMoreView;
import com.lubanjianye.biaoxuntong.ui.dropdown.SpinerPopWindow;
import com.lubanjianye.biaoxuntong.ui.main.index.IndexListAdapter;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.IndexBxtgdjDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.IndexScgggDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.IndexSggjyDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.IndexSggjycgtableDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.IndexXcgggDetailActivity;
import com.lubanjianye.biaoxuntong.util.aes.AesUtil;
import com.lubanjianye.biaoxuntong.util.netStatus.NetUtil;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名:   LBBXT
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.index.search
 * 文件名:   IndexSearchFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/16  18:06
 * 描述:     TODO
 */

public class IndexSearchFragment extends BaseFragment implements View.OnClickListener {

    private TextView tvArea = null;
    private TextView tvType = null;
    private View view = null;
    private SearchView viewSearcher = null;
    private TextView tvSearch = null;
    private LinearLayout llIvBack = null;
    private RecyclerView searchRecycler = null;
    private SmartRefreshLayout searchRefresh = null;

    private com.lubanjianye.biaoxuntong.ui.main.index.IndexListAdapter mAdapter = null;
    private ArrayList<IndexListBean> mDataList = new ArrayList<>();
    private View noDataView = null;

    private int page = 1;

    private long id = 0;
    private String nickName = "";
    private String token = "";
    private String comid = "";
    private String imageUrl = "";


    String mArea = "";
    String mType = "";
    String mKeyWord = "";


    private SpinerPopWindow<String> mSpinerArea = null;
    private SpinerPopWindow<String> mSpinerType = null;
    private List<String> Arealist = new ArrayList<String>();
    private List<String> Typelist = new ArrayList<String>();

    @Override
    public Object setLayout() {
        return R.layout.fragment_index_search;
    }

    @Override
    public void initView() {
        tvArea = getView().findViewById(R.id.tv_area);
        tvType = getView().findViewById(R.id.tv_type);
        view = getView().findViewById(R.id.view);
        viewSearcher = getView().findViewById(R.id.view_searcher);
        tvSearch = getView().findViewById(R.id.tv_search);
        llIvBack = getView().findViewById(R.id.ll_iv_back);
        searchRecycler = getView().findViewById(R.id.search_recycler);
        searchRefresh = getView().findViewById(R.id.search_refresh);

        llIvBack.setOnClickListener(this);
    }

    @Override
    public void initData() {
        Typelist.add("全部");
        Arealist.add("全部");
        loadArea();
        loadType();

        //根据id-search_src_text获取TextView
        SearchView.SearchAutoComplete searchText = (SearchView.SearchAutoComplete) viewSearcher.findViewById(R.id.search_src_text);
        //修改字体大小
        searchText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        //重新布局，使其居中

        //修改字体颜色
        searchText.setTextColor(ContextCompat.getColor(getContext(), R.color.main_theme_color));
        searchText.setHintTextColor(ContextCompat.getColor(getContext(), R.color.search_hint));

        //根据id-search_mag_icon获取ImageView
        ImageView searchButton = (ImageView) viewSearcher.findViewById(R.id.search_mag_icon);

        searchButton.setImageResource(R.mipmap.search);


        //搜索功能
        viewSearcher.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (TextUtils.isEmpty(query.trim())) {
                    ToastUtil.shortToast(getContext(), "请输入要查询的内容！");
                    return false;
                } else {
                    mKeyWord = query.trim();
                    requestData(true);

                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText.trim())) {
                    mKeyWord = "";
                    requestData(true);
                } else {
                    mKeyWord = newText.trim();
                    requestData(true);
                }
                return true;
            }
        });
    }

    @Override
    public void initEvent() {
        initRecyclerView();
        initAdapter();
        initRefreshLayout();

        if (!NetUtil.isNetworkConnected(getActivity())) {
            ToastUtil.shortBottonToast(getContext(), "请检查网络设置");
            mAdapter.setEnableLoadMore(false);
            requestData(true);
        } else {
            requestData(true);
        }
    }

    private void initRefreshLayout() {


        searchRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                if (!NetUtil.isNetworkConnected(getActivity())) {
                    ToastUtil.shortBottonToast(getContext(), "请检查网络设置");
                    searchRefresh.finishRefresh(2000, false);
                    mAdapter.setEnableLoadMore(false);
                    requestData(true);
                } else {
                    requestData(true);
                }
            }
        });


//        indexRefresh.autoRefresh();

    }

    private void initRecyclerView() {

        searchRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        searchRecycler.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

                final IndexListBean data = (IndexListBean) adapter.getData().get(position);
                final int entityId = data.getEntityId();
                final String entity = data.getEntity();

                Log.d("BASUHDUSADASDAS", entity + "_____" + entityId);

                Intent intent = null;
                if ("sggjy".equals(entity)) {
                    intent = new Intent(BiaoXunTong.getApplicationContext(), IndexSggjyDetailActivity.class);
                    intent.putExtra("entityId", entityId);
                    intent.putExtra("entity", entity);
                    intent.putExtra("ajaxlogtype", "0");
                    intent.putExtra("mId", "");
                    startActivity(intent);

                } else if ("xcggg".equals(entity)) {
                    intent = new Intent(BiaoXunTong.getApplicationContext(), IndexXcgggDetailActivity.class);
                    intent.putExtra("entityId", entityId);
                    intent.putExtra("entity", entity);
                    intent.putExtra("ajaxlogtype", "0");
                    intent.putExtra("mId", "");
                    startActivity(intent);
                } else if ("bxtgdj".equals(entity)) {
                    intent = new Intent(BiaoXunTong.getApplicationContext(), IndexBxtgdjDetailActivity.class);
                    intent.putExtra("entityId", entityId);
                    intent.putExtra("entity", entity);
                    intent.putExtra("ajaxlogtype", "0");
                    intent.putExtra("mId", "");
                    startActivity(intent);
                } else if ("sggjycgtable".equals(entity)) {
                    intent = new Intent(BiaoXunTong.getApplicationContext(), IndexSggjycgtableDetailActivity.class);
                    intent.putExtra("entityId", entityId);
                    intent.putExtra("entity", entity);
                    intent.putExtra("ajaxlogtype", "0");
                    intent.putExtra("mId", "");
                    startActivity(intent);

                } else if ("scggg".equals(entity)) {
                    intent = new Intent(BiaoXunTong.getApplicationContext(), IndexScgggDetailActivity.class);
                    intent.putExtra("entityId", entityId);
                    intent.putExtra("entity", entity);
                    intent.putExtra("ajaxlogtype", "0");
                    intent.putExtra("mId", "");
                    startActivity(intent);
                }
            }
        });


        noDataView = getActivity().getLayoutInflater().inflate(R.layout.custom_empty_view, (ViewGroup) searchRecycler.getParent(), false);
        noDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestData(true);
            }
        });
    }

    private void initAdapter() {

        mAdapter = new IndexListAdapter(R.layout.fragment_index_item, mDataList);

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                //TODO 去加载更多数据
                requestData(false);
            }
        });
        //设置列表动画
//        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        searchRecycler.setAdapter(mAdapter);


    }


    public void requestData(final boolean isRefresh) {


        if ("地区".equals(tvArea.getText().toString().trim()) || "全部".equals(tvArea.getText().toString().trim())) {
            mArea = "";
        } else {
            mArea = tvArea.getText().toString().trim();
        }
        if ("类型".equals(tvType.getText().toString()) || "全部".equals(tvType.getText().toString().trim())) {
            mType = "";
        } else {
            mType = tvType.getText().toString().trim();
        }


        if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
            //已登录的数据请求
            List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
            for (int i = 0; i < users.size(); i++) {
                id = users.get(0).getId();
                nickName = users.get(0).getNickName();
                token = users.get(0).getToken();
                comid = users.get(0).getComid();
                imageUrl = users.get(0).getImageUrl();
            }

            if (isRefresh) {
                page = 1;
                OkGo.<String>post(BiaoXunTongApi.URL_GETINDEXLIST)
                        .params("userid", id)
                        .params("area", mArea)
                        .params("type", mType)
                        .params("page", page)
                        .params("keyWord", mKeyWord)
                        .params("size", 10)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {

                                String jiemi = AesUtil.aesDecrypt(response.body(), BiaoXunTongApi.PAS_KEY);

                                final JSONObject object = JSON.parseObject(jiemi);
                                final JSONObject data = object.getJSONObject("data");
                                final String status = object.getString("status");
                                final String message = object.getString("message");
                                final JSONArray array = data.getJSONArray("list");
                                final boolean nextPage = data.getBoolean("nextpage");

                                if ("200".equals(status)) {
                                    if (array.size() > 0) {
                                        page = 2;
                                        setData(isRefresh, array, nextPage);
                                    } else {
                                        if (mDataList != null) {
                                            mDataList.clear();
                                            mAdapter.notifyDataSetChanged();
                                        }
                                        //TODO 内容为空的处理
                                        mAdapter.setEmptyView(noDataView);
                                        if (searchRefresh != null) {
                                            searchRefresh.setEnableRefresh(false);
                                        }
                                    }
                                } else {
                                    ToastUtil.shortToast(getContext(), message);

                                }
                            }
                        });
            } else {
                OkGo.<String>post(BiaoXunTongApi.URL_GETINDEXLIST)
                        .params("userid", id)
                        .params("area", mArea)
                        .params("type", mType)
                        .params("page", page)
                        .params("keyWord", mKeyWord)
                        .params("size", 10)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {

                                String jiemi = AesUtil.aesDecrypt(response.body(), BiaoXunTongApi.PAS_KEY);

                                final JSONObject object = JSON.parseObject(jiemi);
                                final JSONObject data = object.getJSONObject("data");
                                final String status = object.getString("status");
                                final String message = object.getString("message");
                                final JSONArray array = data.getJSONArray("list");
                                final boolean nextPage = data.getBoolean("nextpage");

                                if ("200".equals(status)) {
                                    if (array.size() > 0) {
                                        setData(isRefresh, array, nextPage);
                                    } else {
                                        if (mDataList != null) {
                                            mDataList.clear();
                                            mAdapter.notifyDataSetChanged();
                                        }
                                        //TODO 内容为空的处理
                                        mAdapter.setEmptyView(noDataView);
                                        if (searchRefresh != null) {
                                            searchRefresh.setEnableRefresh(false);
                                        }
                                    }
                                } else {
                                    ToastUtil.shortToast(getContext(), message);

                                }
                            }
                        });
            }


        } else {
            //未登录的数据请求

            if (isRefresh) {
                page = 1;
                OkGo.<String>post(BiaoXunTongApi.URL_GETINDEXLIST)
                        .params("page", page)
                        .params("area", mArea)
                        .params("type", mType)
                        .params("keyWord", mKeyWord)
                        .params("size", 10)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String jiemi = AesUtil.aesDecrypt(response.body(), BiaoXunTongApi.PAS_KEY);

                                final JSONObject object = JSON.parseObject(jiemi);
                                final JSONObject data = object.getJSONObject("data");
                                final String status = object.getString("status");
                                final String message = object.getString("message");
                                final JSONArray array = data.getJSONArray("list");
                                final boolean nextPage = data.getBoolean("nextpage");

                                if ("200".equals(status)) {
                                    if (array.size() > 0) {
                                        page = 2;
                                        setData(isRefresh, array, nextPage);
                                    } else {
                                        if (mDataList != null) {
                                            mDataList.clear();
                                            mAdapter.notifyDataSetChanged();
                                        }
                                        //TODO 内容为空的处理
                                        mAdapter.setEmptyView(noDataView);
                                        if (searchRefresh != null) {
                                            searchRefresh.setEnableRefresh(false);
                                        }
                                    }
                                } else {
                                    ToastUtil.shortToast(getContext(), message);
                                }

                            }
                        });
            } else {
                OkGo.<String>post(BiaoXunTongApi.URL_GETINDEXLIST)
                        .params("page", page)
                        .params("area", mArea)
                        .params("type", mType)
                        .params("keyWord", mKeyWord)
                        .params("size", 10)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String jiemi = AesUtil.aesDecrypt(response.body(), BiaoXunTongApi.PAS_KEY);

                                final JSONObject object = JSON.parseObject(jiemi);
                                final JSONObject data = object.getJSONObject("data");
                                final String status = object.getString("status");
                                final String message = object.getString("message");
                                final JSONArray array = data.getJSONArray("list");
                                final boolean nextPage = data.getBoolean("nextpage");

                                if ("200".equals(status)) {
                                    if (array.size() > 0) {
                                        setData(isRefresh, array, nextPage);
                                    } else {
                                        if (mDataList != null) {
                                            mDataList.clear();
                                            mAdapter.notifyDataSetChanged();
                                        }
                                        //TODO 内容为空的处理
                                        mAdapter.setEmptyView(noDataView);
                                        if (searchRefresh != null) {
                                            searchRefresh.setEnableRefresh(false);
                                        }
                                    }
                                } else {
                                    ToastUtil.shortToast(getContext(), message);
                                }

                            }
                        });
            }


        }


    }

    private void setData(boolean isRefresh, JSONArray data, boolean nextPage) {
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            mDataList.clear();
            for (int i = 0; i < data.size(); i++) {
                IndexListBean bean = new IndexListBean();
                JSONObject list = data.getJSONObject(i);
                bean.setEntryName(list.getString("entryName"));
                bean.setSysTime(list.getString("sysTime"));
                bean.setEntityId(list.getInteger("entityId"));
                bean.setEntity(list.getString("entity"));
                bean.setDeadTime(list.getString("deadTime"));
                bean.setAddress(list.getString("address"));
                bean.setSignstauts(list.getString("signstauts"));
                mDataList.add(bean);
            }
            searchRefresh.finishRefresh(0, true);
            mAdapter.setEnableLoadMore(true);
        } else {
            page++;
            if (size > 0) {
                for (int i = 0; i < data.size(); i++) {
                    IndexListBean bean = new IndexListBean();
                    JSONObject list = data.getJSONObject(i);
                    bean.setEntryName(list.getString("entryName"));
                    bean.setSysTime(list.getString("sysTime"));
                    bean.setEntityId(list.getInteger("entityId"));
                    bean.setEntity(list.getString("entity"));
                    bean.setDeadTime(list.getString("deadTime"));
                    bean.setAddress(list.getString("address"));
                    bean.setSignstauts(list.getString("signstauts"));
                    mDataList.add(bean);
                }
            }
            searchRefresh.finishLoadmore(0, true);
        }

        if (!nextPage) {
            //第一页如果不够一页就不显示没有更多数据布局
            mAdapter.loadMoreEnd();
        } else {
            mAdapter.loadMoreComplete();
        }

        mAdapter.notifyDataSetChanged();


    }

    /**
     * 给TextView右边设置图片
     *
     * @param resId
     */
    public void setTextImage(int id, int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        // 必须设置图片大小，否则不显示
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        switch (id) {
            case R.id.tv_area:
                tvArea.setCompoundDrawables(null, null, drawable, null);
                break;
            case R.id.tv_type:
                tvType.setCompoundDrawables(null, null, drawable, null);
                break;
            default:
                break;
        }

    }

    public void loadArea() {

        OkGo.<String>post(BiaoXunTongApi.URL_GETALLTAB)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final JSONObject object = JSON.parseObject(response.body());
                        final JSONObject data = object.getJSONObject("data");
                        final JSONArray areaList = data.getJSONArray("areaList");

                        for (int i = 0; i < areaList.size(); i++) {
                            final JSONObject list = areaList.getJSONObject(i);
                            String name = list.getString("name");
                            Arealist.add(name);
                        }


                        mSpinerArea = new SpinerPopWindow<String>(getActivity(), Arealist, new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                mSpinerArea.dismiss();
                                tvArea.setText(Arealist.get(position));
                                mArea = tvArea.getText().toString();
                                mType = tvType.getText().toString();

                                requestData(true);

                            }
                        });

                        tvArea.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mSpinerArea == null) {
                                    return;
                                }
                                mSpinerArea.setWidth(searchRefresh.getWidth());
                                mSpinerArea.setHeight(searchRefresh.getHeight() / 2);
                                mSpinerArea.showAsDropDown(view);
                                setTextImage(R.id.tv_area, R.drawable.icon_up);
                            }
                        });

                        mSpinerArea.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                //TODO
                                setTextImage(R.id.tv_area, R.drawable.icon_down);
                            }
                        });
                    }
                });


    }

    public void loadType() {

        OkGo.<String>post(BiaoXunTongApi.URL_GETALLTAB)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final JSONObject object = JSON.parseObject(response.body());
                        final JSONObject data = object.getJSONObject("data");
                        final JSONArray ownerList = data.getJSONArray("ownerList");
                        final JSONArray typeList = data.getJSONArray("typeList");

                        for (int i = 0; i < ownerList.size(); i++) {
                            final JSONObject list = ownerList.getJSONObject(i);
                            String name = list.getString("name");
                            Typelist.add(name);
                        }

                        for (int i = 0; i < typeList.size(); i++) {
                            final JSONObject list = typeList.getJSONObject(i);
                            String name = list.getString("name");
                            Typelist.add(name);
                        }

                        mSpinerType = new SpinerPopWindow<String>(getActivity(), Typelist, new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                mSpinerType.dismiss();

                                tvType.setText(Typelist.get(position));
                                mArea = tvArea.getText().toString();
                                mType = tvType.getText().toString();

                                requestData(true);

                            }
                        });

                        tvType.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mSpinerType == null) {
                                    return;
                                }
                                mSpinerType.setWidth(searchRefresh.getWidth());
                                mSpinerType.setHeight(searchRefresh.getHeight() / 2);
                                mSpinerType.showAsDropDown(view);
                                setTextImage(R.id.tv_type, R.drawable.icon_up);
                            }
                        });

                        mSpinerType.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                //TODO
                                setTextImage(R.id.tv_type, R.drawable.icon_down);
                            }
                        });
                    }
                });

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
