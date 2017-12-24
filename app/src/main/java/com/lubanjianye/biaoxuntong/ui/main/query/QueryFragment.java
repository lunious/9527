package com.lubanjianye.biaoxuntong.ui.main.query;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gongwen.marqueen.MarqueeFactory;
import com.gongwen.marqueen.SimpleMF;
import com.gongwen.marqueen.SimpleMarqueeView;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.bean.QueryBean;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.loadmore.CustomLoadMoreView;
import com.lubanjianye.biaoxuntong.net.RestClient;
import com.lubanjianye.biaoxuntong.net.api.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.net.callback.ISuccess;
import com.lubanjianye.biaoxuntong.sign.SignInActivity;
import com.lubanjianye.biaoxuntong.ui.browser.BrowserActivity;
import com.lubanjianye.biaoxuntong.ui.dropdown.SpinerPopWindow;
import com.lubanjianye.biaoxuntong.ui.main.user.avater.AvaterActivity;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.tosaty.Toasty;

import java.util.ArrayList;
import java.util.List;

import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Headers;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.fragment
 * 文件名:   IndexTabFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/9  0:33
 * 描述:     TODO
 */

public class QueryFragment extends BaseFragment implements View.OnClickListener {

    private AppCompatTextView mainBarName = null;
    private EditText etQuery = null;
    private TextView tvQuery = null;
    private TextView tvZzlx = null;
    private TextView tvDl = null;
    private TextView tvXl = null;
    private TextView tvZy = null;
    private TextView tvDj = null;
    private TextView tvQy = null;
    private Button btnAdd = null;
    private View view = null;
    private LinearLayout ll = null;
    private TextView btnCheckResult = null;
    private TextView tvCheckResult = null;
    private Button btnStartSearch = null;
    private SimpleMarqueeView scrollView = null;
    private AppCompatTextView vipDetail = null;
    private LinearLayout llSearch = null;
    private RecyclerView rlv_query = null;


    private PromptDialog promptDialog;
    String provinceCode = "510000";

    private long id = 0;
    private String nickName = "";
    private String token = "";
    private String comid = "";
    private String imageUrl = "";
    private String mobile = "";

    private SpinerPopWindow<String> mSpinerZzlx;
    private SpinerPopWindow<String> mSpinerDl;
    private SpinerPopWindow<String> mSpinerXl;
    private SpinerPopWindow<String> mSpinerZy;
    private SpinerPopWindow<String> mSpinerDj;
    private SpinerPopWindow<String> mSpinerQy;

    private List<String> Zzlxlist = new ArrayList<String>();
    private List<String> Dllist = new ArrayList<String>();
    private List<String> Xllist = new ArrayList<String>();
    private List<String> Zylist = new ArrayList<String>();
    private List<String> Djlist = new ArrayList<String>();
    private List<String> Qylist = new ArrayList<String>();


    String one;
    String two;
    String three;
    String four;
    String five;
    String six;


    List<Object> zyIds = new ArrayList<Object>();
    String djId = "";
    int entrySign = -1;
    String lxId = "";
    String zcd = "";

    String qyIds = "";

    List<Object> allids = new ArrayList<Object>();
    List<Object> ids = new ArrayList<Object>();

    int i = 0;

    private QueryAdapter mAdapter;
    private ArrayList<QueryBean> mDataList = new ArrayList<>();

    //跑马灯相关
    private List<String> datas = null;

    private void initMarqueeView() {
        SimpleMF<String> marqueeFactory = new SimpleMF(getContext());
        marqueeFactory.setData(datas);
        scrollView.setMarqueeFactory(marqueeFactory);
        scrollView.startFlipping();
        marqueeFactory.setOnItemClickListener(new MarqueeFactory.OnItemClickListener<TextView, String>() {
            @Override
            public void onItemClickListener(MarqueeFactory.ViewHolder<TextView, String> holder) {
                etQuery.setText(holder.data);
                etQuery.setSelection(holder.data.length());
                llSearch.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getScrollViewData() {
        datas = new ArrayList<>();

        RestClient.builder()
                .url("http://www.lubanjianye.com/query/vipNames")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(Headers headers, String response) {
                        final JSONObject object = JSON.parseObject(response);
                        JSONArray array = object.getJSONArray("data");

                        if (array != null) {
                            for (int j = 0; j < array.size(); j++) {
                                datas.add(array.get(j).toString());
                            }
                            initMarqueeView();
                        }

                    }
                })
                .build()
                .post();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_query;
    }

    @Override
    public void initView() {
        mainBarName = getView().findViewById(R.id.main_bar_name);
        etQuery = getView().findViewById(R.id.et_query);
        tvQuery = getView().findViewById(R.id.tv_query);
        tvZzlx = getView().findViewById(R.id.tv_zzlx);
        tvDl = getView().findViewById(R.id.tv_dl);
        tvXl = getView().findViewById(R.id.tv_xl);
        tvZy = getView().findViewById(R.id.tv_zy);
        tvDj = getView().findViewById(R.id.tv_dj);
        tvQy = getView().findViewById(R.id.tv_qy);
        btnAdd = getView().findViewById(R.id.btn_add);
        view = getView().findViewById(R.id.view);
        ll = getView().findViewById(R.id.ll);
        btnCheckResult = getView().findViewById(R.id.btn_check_result);
        tvCheckResult = getView().findViewById(R.id.tv_check_result);
        btnStartSearch = getView().findViewById(R.id.btn_start_search);
        scrollView = getView().findViewById(R.id.scroll_view);
        vipDetail = getView().findViewById(R.id.vip_detail);
        llSearch = getView().findViewById(R.id.ll_search);
        rlv_query = getView().findViewById(R.id.rlv_query);

        vipDetail.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnStartSearch.setOnClickListener(this);
        tvQuery.setOnClickListener(this);


    }

    @Override
    public void initData() {
        mainBarName.setVisibility(View.VISIBLE);
        mainBarName.setText("资质查询");

        scrollView.startFlipping();

        initRecyclerView();
        initAdapter();
    }

    @Override
    public void initEvent() {

        //创建对象
        promptDialog = new PromptDialog(getActivity());
        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);

        //跑马灯效果
        getScrollViewData();

        loadZZLX();

        Qylist.add("川内");
        Qylist.add("入川");
        Qylist.add("川内+入川");

        List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
        for (int i = 0; i < users.size(); i++) {
            id = users.get(0).getId();
            nickName = users.get(0).getNickName();
            token = users.get(0).getToken();
            comid = users.get(0).getComid();
            imageUrl = users.get(0).getImageUrl();
            mobile = users.get(0).getMobile();
        }

        if (TextUtils.isEmpty(etQuery.getText().toString().trim())) {
            llSearch.setVisibility(View.GONE);
        } else {
            llSearch.setVisibility(View.VISIBLE);
        }

        etQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() != 0) {
                    llSearch.setVisibility(View.VISIBLE);
                } else {
                    llSearch.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    llSearch.setVisibility(View.VISIBLE);
                } else {
                    llSearch.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    llSearch.setVisibility(View.VISIBLE);
                } else {
                    llSearch.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initRecyclerView() {
        rlv_query.setLayoutManager(new LinearLayoutManager(getContext()));

        rlv_query.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toasty.info(getContext(), position + "", Toast.LENGTH_SHORT, true).show();
                mDataList.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initAdapter() {
        mAdapter = new QueryAdapter(R.layout.query_item, mDataList);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        rlv_query.setAdapter(mAdapter);
    }

    public void getSuitIds() {

        if ("特级".equals(five) || "甲级".equals(five) || "不分等级".equals(five)) {
            djId = "1";
        } else if ("一级".equals(five) || "乙级".equals(five)) {
            djId = "2";
        } else if ("二级".equals(five) || "丙级".equals(five) || "暂定级".equals(five) || "预备级".equals(five)) {
            djId = "3";
        } else if ("三级".equals(five) || "未分级".equals(five)) {
            djId = "4";
        } else {
            djId = "4";
        }

        if ("川内+入川".equals(six)) {
            entrySign = 2;
        } else if ("川内".equals(six)) {
            entrySign = 0;
        } else if ("入川".equals(six)) {
            entrySign = 1;
        } else if ("全国".equals(six)) {
            entrySign = -1;
        } else {
            entrySign = 2;
        }


        if ("特级".equals(five) || "甲级".equals(five) || "不分等级".equals(five)) {
            djId = "1";
        } else if ("一级".equals(five) || "乙级".equals(five)) {
            djId = "2";
        } else if ("二级".equals(five) || "丙级".equals(five) || "暂定级".equals(five) || "预备级".equals(five)) {
            djId = "3";
        } else if ("三级".equals(five) || "未分级".equals(five)) {
            djId = "4";
        }

        if ("川内+入川".equals(six)) {
            entrySign = 2;
        } else if ("川内".equals(six)) {
            entrySign = 0;
        } else if ("入川".equals(six)) {
            entrySign = 1;
        } else if ("全国".equals(six)) {
            entrySign = -1;
        }


    }


    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();

        if (promptDialog != null) {
            promptDialog.dismissImmediately();
        }

        AppSharePreferenceMgr.remove(getContext(), "tianjian1");
        AppSharePreferenceMgr.remove(getContext(), "tianjian2");
        AppSharePreferenceMgr.remove(getContext(), "tianjian3");
        AppSharePreferenceMgr.remove(getContext(), "hasIds");
        AppSharePreferenceMgr.remove(getContext(), "hasIds1");
        AppSharePreferenceMgr.remove(getContext(), "hasIds2");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.vip_detail:
                String VipUrl = "http://m.lubanjianye.com/home/index/vipservice.html";
                Intent intent = new Intent(getActivity(), BrowserActivity.class);
                intent.putExtra("url", VipUrl);
                intent.putExtra("title", "");
                startActivity(intent);
                break;
            case R.id.btn_add:

                if (mDataList.size() > 2) {
                    Toasty.info(getContext(), "最多叠加三个条件!", Toast.LENGTH_SHORT, true).show();
                } else {
                    QueryBean bean = new QueryBean();
                    bean.setZzlx(one);
                    bean.setDl(two);
                    bean.setXl(three);
                    bean.setZy(four);
                    bean.setDj(five);
                    bean.setDq(six);
                    mDataList.add(bean);

                    mAdapter.notifyDataSetChanged();
                }

//                if (TextUtils.isEmpty(one)) {
//                    Toasty.info(getContext(), "请选择资质类型", Toast.LENGTH_SHORT, true).show();
//
//                } else {
//                    if (mDataList.size() > 2) {
//                        Toasty.info(getContext(), "最多叠加三个条件!", Toast.LENGTH_SHORT, true).show();
//                    } else {
//                        QueryBean bean = new QueryBean();
//                        bean.setZzlx(one);
//                        bean.setDl(two);
//                        bean.setXl(three);
//                        bean.setZy(four);
//                        bean.setDj(five);
//                        bean.setDq(six);
//                        mDataList.add(bean);
//
//                        mAdapter.notifyDataSetChanged();
//                    }
//                }


                //得到符合条件的id
//                getSuitIds();
                break;
            case R.id.btn_start_search:
                Toasty.success(getContext(), "开始查询", Toast.LENGTH_SHORT, true).show();

                break;
            case R.id.tv_query:
                if (TextUtils.isEmpty(etQuery.getText().toString().trim())) {
                    Toasty.error(getContext(), "请输入关键字！", Toast.LENGTH_SHORT, true).show();
                } else {

                    if (AppSharePreferenceMgr.contains(getContext(), EventMessage.LOGIN_SUCCSS)) {
                        List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
                        for (int i = 0; i < users.size(); i++) {
                            id = users.get(0).getId();
                            nickName = users.get(0).getNickName();
                            token = users.get(0).getToken();
                            comid = users.get(0).getComid();
                            imageUrl = users.get(0).getImageUrl();
                            mobile = users.get(0).getMobile();
                        }

                        if (!TextUtils.isEmpty(mobile)) {
                            promptDialog.showLoading("正在查询...");

                            final String name = etQuery.getText().toString().trim();

                            RestClient.builder()
                                    .url(BiaoXunTongApi.URL_GETSUITCOMPANY)
                                    .params("name", name)
                                    .params("userid", id)
//                            .params("token", id + "_" + token)
                                    .success(new ISuccess() {
                                        @Override
                                        public void onSuccess(Headers headers, String response) {

                                            final JSONArray data = JSON.parseObject(response).getJSONArray("data");
                                            if (data.size() > 0) {
                                                //根据返回的id去查询公司名称
                                                promptDialog.dismissImmediately();
                                                Intent intent = new Intent(getActivity(), CompanySearchResultActivity.class);
                                                intent.putExtra("provinceCode", provinceCode);
                                                intent.putExtra("qyIds", data.toString());
                                                startActivity(intent);
                                            } else {
                                                promptDialog.dismissImmediately();
                                                Toasty.info(getContext(), "查询结果为0", Toast.LENGTH_SHORT, true).show();
                                            }
                                        }
                                    })
                                    .build()
                                    .post();

                        } else {
                            Toasty.info(getContext(), "请先绑定手机号", Toast.LENGTH_SHORT, true).show();
                            startActivity(new Intent(getActivity(), AvaterActivity.class));
                        }
                    } else {
                        //未登录去登陆
                        startActivity(new Intent(getActivity(), SignInActivity.class));
                    }
                }
                break;
            default:
                break;
        }
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
            case R.id.tv_zzlx:
                tvZzlx.setCompoundDrawables(null, null, drawable, null);
                break;
            case R.id.tv_dl:
                tvDl.setCompoundDrawables(null, null, drawable, null);
                break;
            case R.id.tv_xl:
                tvXl.setCompoundDrawables(null, null, drawable, null);
                break;
            case R.id.tv_zy:
                tvZy.setCompoundDrawables(null, null, drawable, null);
                break;
            case R.id.tv_dj:
                tvDj.setCompoundDrawables(null, null, drawable, null);
                break;
            case R.id.tv_qy:
                tvQy.setCompoundDrawables(null, null, drawable, null);
                break;
            default:
                break;
        }

    }

    /**
     * 加载资质类型列表
     */

    public void loadZZLX() {
        RestClient.builder()
                .url(BiaoXunTongApi.URL_GETZZLXLIST)
                .params("lx_code", "")
                .params("dl_code", "")
                .params("xl_code", "")
                .params("zy_code", "")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(Headers headers, String response) {

                        final JSONObject object = JSON.parseObject(response);
                        String status = object.getString("status");

                        if ("200".equals(status)) {

                            final JSONArray array = JSON.parseObject(response).getJSONArray("data");

                            for (int i = 0; i < array.size(); i++) {

                                final JSONObject data = array.getJSONObject(i);
                                String name = data.getString("lx_name");
                                Zzlxlist.add(name);
                            }


                            mSpinerZzlx = new SpinerPopWindow<String>(getActivity(), Zzlxlist, new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    mSpinerZzlx.dismiss();
                                    tvZzlx.setText(Zzlxlist.get(position));
                                    one = Zzlxlist.get(position);
                                    tvDl.setVisibility(View.VISIBLE);
                                    tvXl.setVisibility(View.VISIBLE);
                                    tvZy.setVisibility(View.VISIBLE);

                                    //重置后面选项名称
                                    tvDl.setText("大类");
                                    tvXl.setText("小类");
                                    tvZy.setText("专业");
                                    tvDj.setText("等级");
                                    tvQy.setText("地区范围");


                                    if (Dllist.size() > 0) {
                                        Dllist.clear();
                                    }

                                    if (Xllist.size() > 0) {
                                        Xllist.clear();
                                    }

                                    if (Zylist.size() > 0) {
                                        Zylist.clear();
                                    }

                                    if (Djlist.size() > 0) {
                                        Djlist.clear();
                                    }


                                    two = null;
                                    three = null;
                                    four = null;
                                    five = null;
                                    six = null;


                                    String lx_code = array.getJSONObject(position).getString("lx_code");
                                    if (Dllist.size() > 0) {
                                        Dllist.clear();
                                    }
                                    lxId = lx_code;
                                    loadDL(lx_code);


                                }
                            });

                            tvZzlx.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mSpinerZzlx == null) {
                                        return;
                                    }
                                    mSpinerZzlx.setWidth(ll.getWidth());
                                    mSpinerZzlx.setHeight(ll.getHeight() / 2);
                                    mSpinerZzlx.showAsDropDown(view);
                                    setTextImage(R.id.tv_zzlx, R.mipmap.up);
                                }
                            });

                            mSpinerZzlx.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                @Override
                                public void onDismiss() {
                                    //TODO
                                    setTextImage(R.id.tv_zzlx, R.mipmap.down);
                                }
                            });
                        }

                    }
                })
                .build()
                .post();
    }

    /**
     * 加载大类列表
     */
    public void loadDL(final String lx_code) {

        RestClient.builder()
                .url(BiaoXunTongApi.URL_GETZZLXLIST)
                .params("lx_code", lx_code)
                .params("dl_code", "")
                .params("xl_code", "")
                .params("zy_code", "")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(Headers headers, String response) {
                        final JSONObject object = JSON.parseObject(response);
                        String status = object.getString("status");

                        if ("200".equals(status)) {
                            final JSONArray array = JSON.parseObject(response).getJSONArray("data");

                            if ("监理".equals(tvZzlx.getText().toString())) {
                                Dllist.add(array.getJSONObject(0).getString("dl_name"));
                            } else {
                                for (int i = 0; i < array.size(); i++) {

                                    final JSONObject data = array.getJSONObject(i);
                                    String name = data.getString("dl_name");
                                    if (TextUtils.isEmpty(name)) {
                                        Dllist.add("不分大类");
                                        tvDl.setVisibility(View.GONE);
                                        tvXl.setVisibility(View.GONE);
                                        tvZy.setVisibility(View.GONE);

                                        //TODO 得到listZy_ids
                                        RestClient.builder()
                                                .url(BiaoXunTongApi.URL_GETZZLXLIST)
                                                .params("lx_code", lx_code)
                                                .params("dl_code", "")
                                                .params("xl_code", "")
                                                .params("zy_code", "")
                                                .success(new ISuccess() {
                                                    @Override
                                                    public void onSuccess(Headers headers, String response) {
                                                        final JSONObject object = JSON.parseObject(response);
                                                        String status = object.getString("status");

                                                        if ("200".equals(status)) {
                                                            final JSONArray array = JSON.parseObject(response).getJSONArray("data");
                                                            //得到listzy_ids
                                                            if (zyIds.size() > 0) {
                                                                zyIds.clear();
                                                            }
                                                            for (int i = 0; i < array.size(); i++) {
                                                                final JSONObject data = array.getJSONObject(i);
                                                                JSONArray listzy_ids = data.getJSONArray("listzy_ids");
                                                                if (listzy_ids != null) {
                                                                    zyIds = listzy_ids;
                                                                }
                                                            }

                                                        }

                                                    }
                                                })
                                                .build()
                                                .post();
                                    } else {
                                        Dllist.add(name);
                                    }

                                    JSONArray djArray = data.getJSONArray("listdj");
                                    if (djArray != null) {

                                        if (Djlist.size() > 0) {
                                            Djlist.clear();
                                        }

                                        for (int j = 0; j < djArray.size(); j++) {
                                            final JSONObject dj = djArray.getJSONObject(j);
                                            String djName = dj.getString("name");
                                            Djlist.add(djName);

                                        }
                                        loadDJ();
                                    }

                                    JSONArray qyArray = data.getJSONArray("listdq");
                                    if (qyArray != null) {
                                        loadQY();
                                    }


                                }
                            }


                            mSpinerDl = new SpinerPopWindow<String>(getActivity(), Dllist, new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    mSpinerDl.dismiss();

                                    String DlName = Dllist.get(position);
                                    tvDl.setText(Dllist.get(position));

                                    two = Dllist.get(position);

                                    tvXl.setVisibility(View.VISIBLE);
                                    tvZy.setVisibility(View.VISIBLE);

                                    //重置后面选项名称
                                    tvXl.setText("小类");
                                    tvZy.setText("专业");
                                    tvDj.setText("等级");
                                    tvQy.setText("地区范围");

                                    if (Xllist.size() > 0) {
                                        Xllist.clear();
                                    }

                                    if (Zylist.size() > 0) {
                                        Zylist.clear();
                                    }

                                    if (Djlist.size() > 0) {
                                        Djlist.clear();
                                    }


                                    three = null;
                                    four = null;
                                    five = null;
                                    six = null;

                                    String dl_code = array.getJSONObject(position).getString("dl_code");

                                    if (Xllist.size() > 0) {
                                        Xllist.clear();
                                    }

                                    loadXL(dl_code);


                                }
                            });

                            tvDl.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mSpinerDl == null) {
                                        return;
                                    }
                                    mSpinerDl.setWidth(ll.getWidth());
                                    mSpinerDl.setHeight(ll.getHeight() / 2);
                                    mSpinerDl.showAsDropDown(view);
                                    setTextImage(R.id.tv_dl, R.mipmap.up);
                                }
                            });

                            mSpinerDl.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                @Override
                                public void onDismiss() {
                                    //TODO
                                    setTextImage(R.id.tv_dl, R.mipmap.down);
                                }
                            });
                        }


                    }
                })
                .build()
                .post();
    }

    /**
     * 加载小类列表
     */
    public void loadXL(final String dl_code) {
        RestClient.builder()
                .url(BiaoXunTongApi.URL_GETZZLXLIST)
                .params("lx_code", "")
                .params("dl_code", dl_code)
                .params("xl_code", "")
                .params("zy_code", "")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(Headers headers, String response) {
                        final JSONObject object = JSON.parseObject(response);
                        String status = object.getString("status");

                        if ("200".equals(status)) {
                            final JSONArray array = JSON.parseObject(response).getJSONArray("data");

                            for (int i = 0; i < array.size(); i++) {

                                final JSONObject data = array.getJSONObject(i);
                                String name = data.getString("xl_name");
                                if (TextUtils.isEmpty(name)) {
                                    Xllist.add("不分小类");
                                    tvXl.setVisibility(View.GONE);
                                    tvZy.setVisibility(View.GONE);

                                    //TODO 得到listZy_ids
                                    RestClient.builder()
                                            .url(BiaoXunTongApi.URL_GETZZLXLIST)
                                            .params("lx_code", "")
                                            .params("dl_code", dl_code)
                                            .params("xl_code", "")
                                            .params("zy_code", "")
                                            .success(new ISuccess() {
                                                @Override
                                                public void onSuccess(Headers headers, String response) {
                                                    final JSONObject object = JSON.parseObject(response);
                                                    String status = object.getString("status");

                                                    if ("200".equals(status)) {
                                                        final JSONArray array = JSON.parseObject(response).getJSONArray("data");
                                                        //得到listzy_ids
                                                        if (zyIds.size() > 0) {
                                                            zyIds.clear();
                                                        }
                                                        for (int i = 0; i < array.size(); i++) {
                                                            final JSONObject data = array.getJSONObject(i);
                                                            JSONArray listzy_ids = data.getJSONArray("listzy_ids");
                                                            if (listzy_ids != null) {
                                                                zyIds = listzy_ids;
                                                            }
                                                        }

                                                    }

                                                }
                                            })
                                            .build()
                                            .post();


                                } else {
                                    Xllist.add(name);
                                }

                                JSONArray djArray = data.getJSONArray("listdj");
                                if (djArray != null) {

                                    if (Djlist.size() > 0) {
                                        Djlist.clear();
                                    }
                                    for (int j = 0; j < djArray.size(); j++) {
                                        final JSONObject dj = djArray.getJSONObject(j);

                                        String djName = dj.getString("name");
                                        Djlist.add(djName);
                                    }

                                    loadDJ();
                                }

                                JSONArray qyArray = data.getJSONArray("listdq");
                                if (qyArray != null) {
                                    loadQY();
                                }

                            }
                            mSpinerXl = new SpinerPopWindow<String>(getActivity(), Xllist, new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    mSpinerXl.dismiss();

                                    tvXl.setText(Xllist.get(position));
                                    tvZy.setVisibility(View.VISIBLE);

                                    three = Xllist.get(position);

                                    //重置后面选项名称
                                    tvZy.setText("专业");
                                    tvDj.setText("等级");
                                    tvQy.setText("地区范围");

                                    four = null;
                                    five = null;
                                    six = null;


                                    if (Zylist.size() > 0) {
                                        Zylist.clear();
                                    }

                                    if (Djlist.size() > 0) {
                                        Djlist.clear();
                                    }


                                    String xl_code = array.getJSONObject(position).getString("xl_code");
                                    loadZY(xl_code);

                                }
                            });

                            tvXl.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mSpinerXl == null) {
                                        return;
                                    }
                                    mSpinerXl.setWidth(ll.getWidth());
                                    mSpinerXl.setHeight(ll.getHeight() / 2);
                                    mSpinerXl.showAsDropDown(view);
                                    setTextImage(R.id.tv_xl, R.mipmap.up);
                                }
                            });

                            mSpinerXl.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                @Override
                                public void onDismiss() {
                                    //TODO
                                    setTextImage(R.id.tv_xl, R.mipmap.down);
                                }
                            });
                        }


                    }
                })
                .build()
                .post();
    }

    /**
     * 加载专业列表
     */
    public void loadZY(final String xl_code) {

        RestClient.builder()
                .url(BiaoXunTongApi.URL_GETZZLXLIST)
                .params("lx_code", "")
                .params("dl_code", "")
                .params("xl_code", xl_code)
                .params("zy_code", "")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(Headers headers, String response) {
                        final JSONObject object = JSON.parseObject(response);
                        String status = object.getString("status");

                        if ("200".equals(status)) {
                            final JSONArray array = JSON.parseObject(response).getJSONArray("data");

                            for (int i = 0; i < array.size(); i++) {

                                final JSONObject data = array.getJSONObject(i);
                                String name = data.getString("zy_name");
                                if (TextUtils.isEmpty(name)) {
                                    Zylist.add("不分专业");
                                    tvZy.setVisibility(View.GONE);

                                    //TODO 得到listZy_ids
                                    RestClient.builder()
                                            .url(BiaoXunTongApi.URL_GETZZLXLIST)
                                            .params("lx_code", "")
                                            .params("dl_code", "")
                                            .params("xl_code", xl_code)
                                            .params("zy_code", "")
                                            .success(new ISuccess() {
                                                @Override
                                                public void onSuccess(Headers headers, String response) {
                                                    final JSONObject object = JSON.parseObject(response);
                                                    String status = object.getString("status");

                                                    if ("200".equals(status)) {
                                                        final JSONArray array = JSON.parseObject(response).getJSONArray("data");
                                                        //得到listzy_ids
                                                        if (zyIds.size() > 0) {
                                                            zyIds.clear();
                                                        }
                                                        for (int i = 0; i < array.size(); i++) {
                                                            final JSONObject data = array.getJSONObject(i);
                                                            JSONArray listzy_ids = data.getJSONArray("listzy_ids");
                                                            if (listzy_ids != null) {
                                                                zyIds = listzy_ids;
                                                            }
                                                        }

                                                    }

                                                }
                                            })
                                            .build()
                                            .post();


                                } else {
                                    Zylist.add(name);
                                }

                                JSONArray djArray = data.getJSONArray("listdj");
                                if (djArray != null) {

                                    if (Djlist.size() > 0) {
                                        Djlist.clear();
                                    }
                                    for (int j = 0; j < djArray.size(); j++) {
                                        final JSONObject dj = djArray.getJSONObject(j);

                                        String djName = dj.getString("name");
                                        Djlist.add(djName);
                                    }

                                    loadDJ();
                                }

                                JSONArray qyArray = data.getJSONArray("listdq");
                                if (qyArray != null) {
                                    loadQY();
                                }

                            }
                            mSpinerZy = new SpinerPopWindow<String>(getActivity(), Zylist, new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    mSpinerZy.dismiss();

                                    tvZy.setText(Zylist.get(position));

                                    four = Zylist.get(position);

                                    //重置后面选项名称
                                    tvDj.setText("等级");
                                    tvQy.setText("地区范围");

                                    five = null;
                                    six = null;

                                    String zy_code = array.getJSONObject(position).getString("zy_code");

                                    //TODO 得到listZy_ids
                                    RestClient.builder()
                                            .url(BiaoXunTongApi.URL_GETZZLXLIST)
                                            .params("lx_code", "")
                                            .params("dl_code", "")
                                            .params("xl_code", "")
                                            .params("zy_code", zy_code)
                                            .success(new ISuccess() {
                                                @Override
                                                public void onSuccess(Headers headers, String response) {
                                                    final JSONObject object = JSON.parseObject(response);
                                                    String status = object.getString("status");

                                                    if ("200".equals(status)) {
                                                        final JSONArray array = JSON.parseObject(response).getJSONArray("data");
                                                        //得到listzy_ids
                                                        if (zyIds.size() > 0) {
                                                            zyIds.clear();
                                                        }
                                                        for (int i = 0; i < array.size(); i++) {
                                                            final JSONObject data = array.getJSONObject(i);
                                                            JSONArray listzy_ids = data.getJSONArray("listzy_ids");
                                                            if (listzy_ids != null) {
                                                                zyIds = listzy_ids;
                                                            }
                                                        }

                                                    }

                                                }
                                            })
                                            .build()
                                            .post();


                                }
                            });

                            tvZy.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mSpinerZy == null) {
                                        return;
                                    }
                                    mSpinerZy.setWidth(ll.getWidth());
                                    mSpinerZy.setHeight(ll.getHeight() / 2);
                                    mSpinerZy.showAsDropDown(view);
                                    setTextImage(R.id.tv_zy, R.mipmap.up);
                                }
                            });

                            mSpinerZy.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                @Override
                                public void onDismiss() {
                                    //TODO
                                    setTextImage(R.id.tv_zy, R.mipmap.down);
                                }
                            });
                        }


                    }
                })
                .build()
                .post();
    }

    /**
     * 加载等级列表
     */
    public void loadDJ() {

        mSpinerDj = new SpinerPopWindow<String>(getActivity(), Djlist, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSpinerDj.dismiss();
                tvDj.setText(Djlist.get(position));

                five = Djlist.get(position);

                //重置后面选项名称
                tvQy.setText("地区范围");
            }
        });

        tvDj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSpinerDj == null) {
                    return;
                }
                mSpinerDj.setWidth(ll.getWidth());
                mSpinerDj.setHeight(ll.getHeight() / 2);
                mSpinerDj.showAsDropDown(view);
                setTextImage(R.id.tv_dj, R.mipmap.up);
            }
        });

        mSpinerDj.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //TODO
                setTextImage(R.id.tv_dj, R.mipmap.down);
            }
        });
    }

    /**
     * 加载区域列表
     */
    public void loadQY() {

        mSpinerQy = new SpinerPopWindow<String>(getActivity(), Qylist, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSpinerQy.dismiss();
                tvQy.setText(Qylist.get(position));

                six = Qylist.get(position);

            }
        });

        tvQy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSpinerQy == null) {
                    return;
                }
                mSpinerQy.setWidth(ll.getWidth());
                mSpinerQy.setHeight(ll.getHeight() / 2);
                mSpinerQy.showAsDropDown(view);
                setTextImage(R.id.tv_qy, R.mipmap.up);
            }
        });

        mSpinerQy.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setTextImage(R.id.tv_qy, R.mipmap.down);
            }
        });
    }
}