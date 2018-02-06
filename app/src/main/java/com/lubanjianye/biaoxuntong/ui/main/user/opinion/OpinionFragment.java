package com.lubanjianye.biaoxuntong.ui.main.user.opinion;

import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.ui.media.SelectImageActivity;
import com.lubanjianye.biaoxuntong.ui.media.SelectOptions;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;

/**
 * Created by 11645 on 2018/1/25.
 */

public class OpinionFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout llBack = null;
    private AppCompatTextView mainBarName = null;

    private RadioButton rbError = null;
    private EditText editText = null;
    private ImageView ivAdd = null;
    private ImageView ivClear = null;
    private AppCompatTextView atvCommit = null;


    private String mFilePath = "";


    @Override
    public Object setLayout() {
        return R.layout.fragment_opinion;
    }

    @Override
    public void initView() {
        llBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);

        rbError = getView().findViewById(R.id.rb_error);
        editText = getView().findViewById(R.id.et_feed_back);
        ivAdd = getView().findViewById(R.id.iv_add);
        ivClear = getView().findViewById(R.id.iv_clear_img);
        atvCommit = getView().findViewById(R.id.tv_commit);

        llBack.setOnClickListener(this);

        ivAdd.setOnClickListener(this);
        ivClear.setOnClickListener(this);
        atvCommit.setOnClickListener(this);


    }

    @Override
    public void initData() {
        llBack.setVisibility(View.VISIBLE);
        mainBarName.setText("意见反馈");
    }

    @Override
    public void initEvent() {
        rbError.setChecked(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_iv_back:
                getActivity().onBackPressed();
                break;
            case R.id.iv_add:
                openImageSelector();
                break;
            case R.id.iv_clear_img:
                ivAdd.setImageResource(R.mipmap.ic_tweet_add);
                ivClear.setVisibility(View.GONE);
                mFilePath = "";
                break;
            case R.id.tv_commit:
                String content = editText.getText().toString().trim();
                if (TextUtils.isEmpty(content) && TextUtils.isEmpty(mFilePath)) {
                    ToastUtil.shortToast(getContext(), "内容为空");
                    return;
                }
                ToastUtil.shortToast(getContext(), "感谢您的反馈！");
                getActivity().onBackPressed();
                break;
            default:
                break;
        }
    }

    protected RequestManager mImageLoader;

    public synchronized RequestManager getImageLoader() {
        if (mImageLoader == null) {
            mImageLoader = Glide.with(this);
        }
        return mImageLoader;
    }

    public void openImageSelector() {
        SelectImageActivity.show(getContext(), new SelectOptions.Builder()
                .setHasCam(false)
                .setSelectCount(1)
                .setCallback(new SelectOptions.Callback() {
                    @Override
                    public void doSelected(String[] images) {
                        mFilePath = images[0];
                        getImageLoader().load(mFilePath).into(ivAdd);
                        ivClear.setVisibility(View.VISIBLE);
                    }
                }).build());
    }

}
