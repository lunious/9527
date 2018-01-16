package com.lubanjianye.biaoxuntong.ui.main.query;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.gongwen.marqueen.MarqueeFactory;
import com.gongwen.marqueen.SimpleMF;
import com.gongwen.marqueen.SimpleMarqueeView;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.bean.JsonString;
import com.lubanjianye.biaoxuntong.bean.QueryBean;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.api.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.sign.SignInActivity;
import com.lubanjianye.biaoxuntong.ui.browser.BrowserActivity;
import com.lubanjianye.biaoxuntong.ui.dropdown.SpinerPopWindow;
import com.lubanjianye.biaoxuntong.ui.main.user.avater.AvaterActivity;
import com.lubanjianye.biaoxuntong.util.dialog.PromptButton;
import com.lubanjianye.biaoxuntong.util.dialog.PromptButtonListener;
import com.lubanjianye.biaoxuntong.util.dialog.PromptDialog;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

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
    private AppCompatTextView btnStartSearch = null;
    private SimpleMarqueeView scrollView = null;
    private AppCompatTextView vipDetail = null;
    private LinearLayout llSearch = null;
    private RecyclerView rlv_query = null;

    private ItemDragAndSwipeCallback mItemDragAndSwipeCallback;
    private ItemTouchHelper mItemTouchHelper;

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
    List<Object> ids_1 = new ArrayList<Object>();
    List<Object> ids_2 = new ArrayList<Object>();
    List<Object> ids_3 = new ArrayList<Object>();


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

        OkGo.<String>post("http://www.lubanjianye.com/query/vipNames")
                .cacheMode(CacheMode.IF_NONE_CACHE_REQUEST)
                .cacheTime(3600 * 48000)
                .execute(new StringCallback() {

                    @Override
                    public void onCacheSuccess(Response<String> response) {
                        final JSONObject object = JSON.parseObject(response.body());
                        JSONArray array = object.getJSONArray("data");

                        if (array != null) {
                            for (int j = 0; j < array.size(); j++) {
                                datas.add(array.get(j).toString());
                            }
                            initMarqueeView();
                        }
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        final JSONObject object = JSON.parseObject(response.body());
                        JSONArray array = object.getJSONArray("data");

                        if (array != null) {
                            for (int j = 0; j < array.size(); j++) {
                                datas.add(array.get(j).toString());
                            }
                            initMarqueeView();
                        }
                    }
                });

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


    }

    private void initAdapter() {


        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(40);
        paint.setColor(Color.WHITE);


        OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                switch (pos) {
                    case 0:
                        if (ids_1.size() > 0) {
                            ids_1.clear();
                        } else {
                            if (ids_2.size() > 0) {
                                ids_2.clear();
                            } else {
                                ids_3.clear();
                            }
                        }
                        break;
                    case 1:
                        if (ids_2.size() > 0) {
                            ids_2.clear();
                        } else {
                            ids_3.clear();
                        }
                        break;
                    case 2:
                        ids_3.clear();
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
                canvas.drawColor(ContextCompat.getColor(getContext(), R.color.dark_view));
                canvas.drawText("继续滑动即可删除", 90, 90, paint);
            }
        };
        mAdapter = new QueryAdapter(R.layout.query_item, mDataList);

        mItemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(mItemDragAndSwipeCallback);
        mItemTouchHelper.attachToRecyclerView(rlv_query);

        mItemDragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.END | ItemTouchHelper.START);
        mAdapter.enableSwipeItem();
        mAdapter.setOnItemSwipeListener(onItemSwipeListener);
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

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mDataList.size() > 0) {
            mDataList.clear();
        }

        if (allids.size() > 0) {
            allids.clear();
        }
        if (ids_1.size() > 0) {
            ids_1.clear();
        }
        if (ids_2.size() > 0) {
            ids_2.clear();
        }
        if (ids_3.size() > 0) {
            ids_3.clear();
        }
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
                if (TextUtils.isEmpty(one)) {
                    ToastUtil.shortToast(getContext(), "请选择资质类型");

                } else {
                    if (mDataList.size() <= 2) {
                        QueryBean bean = new QueryBean();
                        bean.setZzlx(one);
                        bean.setDl(two);
                        bean.setXl(three);
                        bean.setZy(four);
                        bean.setDj(five);
                        bean.setDq(six);
                        mDataList.add(bean);
                        mAdapter.notifyDataSetChanged();

                        //得到符合条件的id
                        getSuitIds();
                        getSuitCompany();

                    } else {
                        ToastUtil.shortToast(getContext(), "最多叠加三个条件!");
                    }

                }

                break;
            case R.id.btn_start_search:

                if (mDataList.size() > 0) {

                    if (ids_1.size() > 0 && ids_2.size() == 0 && ids_3.size() == 0) {
                        if (ids.size() > 0) {
                            ids.clear();
                        }
                        ids.addAll(ids_1);
                        allids = ids;
                        i = allids.size();
                        qyIds = allids.toString();
                    } else if (ids_1.size() > 0 && ids_2.size() > 0 && ids_3.size() == 0) {
                        if (ids.size() > 0) {
                            ids.clear();
                        }
                        ids.addAll(ids_1);
                        ids.retainAll(ids_2);
                        allids = ids;
                        i = allids.size();
                        qyIds = allids.toString();
                    } else if (ids_1.size() > 0 && ids_2.size() == 0 && ids_3.size() > 0) {
                        if (ids.size() > 0) {
                            ids.clear();
                        }
                        ids.addAll(ids_1);
                        ids.retainAll(ids_3);
                        allids = ids;
                        i = allids.size();
                        qyIds = allids.toString();
                    } else if (ids_1.size() == 0 && ids_2.size() > 0 && ids_3.size() == 0) {
                        if (ids.size() > 0) {
                            ids.clear();
                        }
                        ids.addAll(ids_2);
                        allids = ids;
                        i = allids.size();
                        qyIds = allids.toString();
                    } else if (ids_1.size() == 0 && ids_2.size() > 0 && ids_3.size() > 0) {
                        if (ids.size() > 0) {
                            ids.clear();
                        }
                        ids.addAll(ids_2);
                        ids.retainAll(ids_3);
                        allids = ids;
                        i = allids.size();
                        qyIds = allids.toString();
                    } else if (ids_1.size() == 0 && ids_2.size() == 0 && ids_3.size() > 0) {
                        if (ids.size() > 0) {
                            ids.clear();
                        }
                        ids.addAll(ids_3);
                        allids = ids;
                        i = allids.size();
                        qyIds = allids.toString();
                    } else if (ids_1.size() > 0 && ids_2.size() > 0 && ids_3.size() > 0) {
                        if (ids.size() > 0) {
                            ids.clear();
                        }
                        ids.addAll(ids_3);
                        ids.retainAll(ids_1);
                        ids.retainAll(ids_2);
                        allids = ids;
                        i = allids.size();
                        qyIds = allids.toString();
                    }


                    if (i != 0) {
                        final PromptButton cancel = new PromptButton("取      消", new PromptButtonListener() {
                            @Override
                            public void onClick(PromptButton button) {
                            }
                        });
                        cancel.setTextColor(Color.parseColor("#cccc33"));
                        cancel.setTextSize(15);

                        final PromptButton toLogin = new PromptButton("查看详情", new PromptButtonListener() {
                            @Override
                            public void onClick(PromptButton button) {
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
                                        //根据返回的id去查询公司名称
                                        Intent intent = new Intent(getActivity(), CompanySearchResultActivity.class);
                                        intent.putExtra("provinceCode", provinceCode);
                                        intent.putExtra("qyIds", qyIds);
                                        startActivity(intent);
                                    } else {
                                        ToastUtil.shortToast(getContext(), "请先绑定手机号");
                                        Intent intent = new Intent(getActivity(), AvaterActivity.class);
                                        startActivity(intent);
                                    }

                                } else {
                                    //未登录去登陆
                                    startActivity(new Intent(getActivity(), SignInActivity.class));
                                }

                            }
                        });
                        toLogin.setTextColor(Color.parseColor("#00bfdc"));
                        toLogin.setTextSize(15);
                        promptDialog.getAlertDefaultBuilder().withAnim(false).cancleAble(false).touchAble(false);

                        promptDialog.showWarnAlert("共为你查询到" + i + "家企业!", cancel, toLogin);
                    } else {
                        final PromptButton cancel = new PromptButton("重新筛选", new PromptButtonListener() {
                            @Override
                            public void onClick(PromptButton button) {
                            }
                        });
                        cancel.setTextColor(Color.parseColor("#21a9ff"));
                        cancel.setTextSize(15);

                        promptDialog.getAlertDefaultBuilder().withAnim(false).cancleAble(false).touchAble(false);

                        promptDialog.showWarnAlert("共为你查询到" + i + "家企业!", cancel, false);
                    }
                } else {
                    ToastUtil.shortToast(getContext(), "请添加条件");
                }

                break;
            case R.id.tv_query:
                if (TextUtils.isEmpty(etQuery.getText().toString().trim())) {
                    ToastUtil.shortToast(getContext(), "请输入关键字！");
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

                            OkGo.<String>post(BiaoXunTongApi.URL_GETSUITCOMPANY)
                                    .params("name", name)
                                    .params("userid", id)
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(Response<String> response) {
                                            final JSONArray data = JSON.parseObject(response.body()).getJSONArray("data");
                                            if (data.size() > 0) {
                                                //根据返回的id去查询公司名称
                                                promptDialog.dismissImmediately();
                                                Intent intent = new Intent(getActivity(), CompanySearchResultActivity.class);
                                                intent.putExtra("provinceCode", provinceCode);
                                                intent.putExtra("qyIds", data.toString());
                                                startActivity(intent);
                                            } else {
                                                promptDialog.dismissImmediately();
                                                ToastUtil.shortToast(getContext(), "查询结果为0");
                                            }
                                        }
                                    });

                        } else {
                            ToastUtil.shortToast(getContext(), "请先绑定手机号");
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

    public void getSuitCompany() {
        JsonString jsonString = new JsonString();
        jsonString.setZyIds(zyIds);
        jsonString.setLxId(lxId);
        jsonString.setZcd(zcd);
        jsonString.setDjId(djId);
        jsonString.setProvinceCode(provinceCode);
        jsonString.setEntrySign(entrySign);
        String userJson = JSON.toJSONString(jsonString);

        OkGo.<String>post(BiaoXunTongApi.URL_GETSUITIDS)
                .params("JSONString", userJson)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final JSONObject jsonObject = JSONObject.parseObject(response.body());
                        String status = jsonObject.getString("status");

                        if ("200".equals(status)) {

                            final JSONArray dataArray = jsonObject.getJSONArray("data");

                            if (ids_1.size() > 0) {
                                if (ids_2.size() > 0) {
                                    ids_3 = dataArray;
                                } else {
                                    ids_2 = dataArray;
                                }
                            } else {
                                ids_1 = dataArray;
                            }

                        } else {
                            ToastUtil.shortToast(getContext(), "服务器错误");
                        }
                    }
                });

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

        OkGo.<String>post(BiaoXunTongApi.URL_GETZZLXLIST)
                .params("lx_code", "")
                .params("dl_code", "")
                .params("xl_code", "")
                .params("zy_code", "")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        final JSONObject object = JSON.parseObject(response.body());
                        String status = object.getString("status");

                        if ("200".equals(status)) {

                            final JSONArray array = JSON.parseObject(response.body()).getJSONArray("data");

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
                });
    }

    /**
     * 加载大类列表
     */
    public void loadDL(final String lx_code) {

        OkGo.<String>post(BiaoXunTongApi.URL_GETZZLXLIST)
                .params("lx_code", lx_code)
                .params("dl_code", "")
                .params("xl_code", "")
                .params("zy_code", "")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final JSONObject object = JSON.parseObject(response.body());
                        String status = object.getString("status");

                        if ("200".equals(status)) {
                            final JSONArray array = JSON.parseObject(response.body()).getJSONArray("data");

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

                                        OkGo.<String>post(BiaoXunTongApi.URL_GETZZLXLIST)
                                                .params("lx_code", lx_code)
                                                .params("dl_code", "")
                                                .params("xl_code", "")
                                                .params("zy_code", "")
                                                .execute(new StringCallback() {
                                                    @Override
                                                    public void onSuccess(Response<String> response) {
                                                        final JSONObject object = JSON.parseObject(response.body());
                                                        String status = object.getString("status");

                                                        if ("200".equals(status)) {
                                                            final JSONArray array = JSON.parseObject(response.body()).getJSONArray("data");
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
                                                });

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
                });

    }

    /**
     * 加载小类列表
     */
    public void loadXL(final String dl_code) {


        OkGo.<String>post(BiaoXunTongApi.URL_GETZZLXLIST)
                .params("lx_code", "")
                .params("dl_code", dl_code)
                .params("xl_code", "")
                .params("zy_code", "")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final JSONObject object = JSON.parseObject(response.body());
                        String status = object.getString("status");

                        if ("200".equals(status)) {
                            final JSONArray array = JSON.parseObject(response.body()).getJSONArray("data");

                            for (int i = 0; i < array.size(); i++) {

                                final JSONObject data = array.getJSONObject(i);
                                String name = data.getString("xl_name");
                                if (TextUtils.isEmpty(name)) {
                                    Xllist.add("不分小类");
                                    tvXl.setVisibility(View.GONE);
                                    tvZy.setVisibility(View.GONE);

                                    //TODO 得到listZy_ids
                                    OkGo.<String>post(BiaoXunTongApi.URL_GETZZLXLIST)
                                            .params("lx_code", "")
                                            .params("dl_code", dl_code)
                                            .params("xl_code", "")
                                            .params("zy_code", "")
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onSuccess(Response<String> response) {
                                                    final JSONObject object = JSON.parseObject(response.body());
                                                    String status = object.getString("status");

                                                    if ("200".equals(status)) {
                                                        final JSONArray array = JSON.parseObject(response.body()).getJSONArray("data");
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
                                            });


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
                });

    }

    /**
     * 加载专业列表
     */
    public void loadZY(final String xl_code) {

        OkGo.<String>post(BiaoXunTongApi.URL_GETZZLXLIST)
                .params("lx_code", "")
                .params("dl_code", "")
                .params("xl_code", xl_code)
                .params("zy_code", "")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final JSONObject object = JSON.parseObject(response.body());
                        String status = object.getString("status");

                        if ("200".equals(status)) {
                            final JSONArray array = JSON.parseObject(response.body()).getJSONArray("data");

                            for (int i = 0; i < array.size(); i++) {

                                final JSONObject data = array.getJSONObject(i);
                                String name = data.getString("zy_name");
                                if (TextUtils.isEmpty(name)) {
                                    Zylist.add("不分专业");
                                    tvZy.setVisibility(View.GONE);

                                    //TODO 得到listZy_ids
                                    OkGo.<String>post(BiaoXunTongApi.URL_GETZZLXLIST)
                                            .params("lx_code", "")
                                            .params("dl_code", "")
                                            .params("xl_code", xl_code)
                                            .params("zy_code", "")
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onSuccess(Response<String> response) {
                                                    final JSONObject object = JSON.parseObject(response.body());
                                                    String status = object.getString("status");

                                                    if ("200".equals(status)) {
                                                        final JSONArray array = JSON.parseObject(response.body()).getJSONArray("data");
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
                                            });

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

                                    OkGo.<String>post(BiaoXunTongApi.URL_GETZZLXLIST)
                                            .params("lx_code", "")
                                            .params("dl_code", "")
                                            .params("xl_code", "")
                                            .params("zy_code", zy_code)
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onSuccess(Response<String> response) {
                                                    final JSONObject object = JSON.parseObject(response.body());
                                                    String status = object.getString("status");

                                                    if ("200".equals(status)) {
                                                        final JSONArray array = JSON.parseObject(response.body()).getJSONArray("data");
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
                                            });

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
                });

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
