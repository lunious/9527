package com.lubanjianye.biaoxuntong.ui.browser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.browser
 * 文件名:   BrowserFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/11  12:59
 * 描述:     TODO
 */

public class BrowserFragment extends BaseFragment implements View.OnClickListener {


    private LinearLayout mBack = null;
    private AppCompatTextView mainTitle = null;
    private WebView mWebView = null;
    private ProgressBar mProgress = null;
    private ImageView mImgBack = null;
    private ImageView mImgForward = null;
    private ImageView mImgRefresh = null;
    private ImageView mImgSystemBrowser = null;
    private LinearLayout mLayoutBottom = null;

    public String DEFAULT = "http://www.lubanjianye.com/";
    public String mTitle = "";

    // 双击事件需要
    private int TAG = 1;
    private Activity aty;
    private String mCurrentUrl;

    private Animation animBottomIn, animBottomOut;
    private GestureDetector mGestureDetector;
    private CookieManager cookie;

    private static final String ARG_URL = "ARG_URL";
    private static final String ARG_TITLE = "ARG_TITLE";


    @Override
    public Object setLayout() {
        return R.layout.fragment_browser;
    }

    public static BrowserFragment create(@NonNull String url, String title) {
        final Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        args.putString(ARG_TITLE, title);
        final BrowserFragment fragment = new BrowserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initView() {
        mBack = getView().findViewById(R.id.ll_iv_back);
        mainTitle = getView().findViewById(R.id.main_bar_name);
        mWebView = getView().findViewById(R.id.webview);
        mProgress = getView().findViewById(R.id.progress);
        mImgBack = getView().findViewById(R.id.browser_back);
        mImgForward = getView().findViewById(R.id.browser_forward);
        mImgRefresh = getView().findViewById(R.id.browser_refresh);
        mImgSystemBrowser = getView().findViewById(R.id.browser_system_browser);
        mLayoutBottom = getView().findViewById(R.id.browser_bottom);
        mBack.setOnClickListener(this);
        mImgBack.setOnClickListener(this);
        mImgForward.setOnClickListener(this);
        mImgRefresh.setOnClickListener(this);
        mImgSystemBrowser.setOnClickListener(this);
    }

    @Override
    public void initData() {
        aty = getActivity();
        final Bundle args = getArguments();
        if (args != null) {
            DEFAULT = args.getString(ARG_URL);
            mTitle = args.getString(ARG_TITLE);
            mCurrentUrl = DEFAULT;
        }
    }

    @Override
    public void initEvent() {
        mBack.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(mTitle)) {
            mainTitle.setText(mTitle);
        } else {
            mainTitle.setText("鲁班标讯通");
        }

        initWebView();
        initBarAnim();
        mGestureDetector = new GestureDetector(aty, new MyGestureListener());
        mWebView.loadUrl(mCurrentUrl);
        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_iv_back:
                getActivity().onBackPressed();
                break;
            case R.id.browser_back:
                mWebView.goBack();
                break;
            case R.id.browser_forward:
                mWebView.goForward();
                break;
            case R.id.browser_refresh:
                mWebView.loadUrl(mWebView.getUrl());
                break;
            case R.id.browser_system_browser:
                try {
                    // 启用外部浏览器
                    Uri uri = Uri.parse(mCurrentUrl);
                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
                    aty.startActivity(it);
                } catch (Exception e) {
                    ToastUtil.shortToast(getContext(), "网页地址错误");
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.destroy();
        }

    }

    /**
     * 初始化上下栏的动画并设置结束监听事件
     */
    private void initBarAnim() {
        animBottomIn = AnimationUtils.loadAnimation(aty, R.anim.anim_bottom_in);
        animBottomOut = AnimationUtils.loadAnimation(aty,
                R.anim.anim_bottom_out);
        animBottomIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLayoutBottom.setVisibility(View.VISIBLE);
            }
        });
        animBottomOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLayoutBottom.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 载入链接之前会被调用
     *
     * @param view WebView
     * @param url  链接地址
     */
    protected void onUrlLoading(WebView view, String url) {
        mProgress.setVisibility(View.VISIBLE);
        cookie.setCookie(url, null);
    }

    /**
     * 链接载入成功后会被调用
     *
     * @param view WebView
     * @param url  链接地址
     */
    protected void onUrlFinished(WebView view, String url) {
        mCurrentUrl = url;
        if (mProgress != null) {
            mProgress.setVisibility(View.GONE);
        }

    }

    /**
     * 当前WebView显示页面的图标
     *
     * @param view WebView
     * @param icon web页面图标
     */
    protected void onWebIcon(WebView view, Bitmap icon) {
    }

    /**
     * 初始化浏览器设置信息
     */
    private void initWebView() {
        cookie = CookieManager.getInstance();
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true); // 启用支持javascript
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// 优先使用缓存
        webSettings.setAllowFileAccess(true);// 可以访问文件
        webSettings.setBuiltInZoomControls(true);// 支持缩放
        //适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            webSettings.setPluginState(WebSettings.PluginState.ON);
            webSettings.setDisplayZoomControls(false);// 支持缩放
            //适应屏幕
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadWithOverviewMode(true);
        }
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.setWebViewClient(new MyWebViewClient());
    }


    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
            onWebIcon(view, icon);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) { // 进度
            super.onProgressChanged(view, newProgress);
            if (newProgress > 90) {
                if (mProgress != null) {
                    mProgress.setVisibility(View.GONE);
                }

            }
        }

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            onUrlLoading(view, url);
//            boolean flag = super.shouldOverrideUrlLoading(view, url);
//            mCurrentUrl = url;
//            return flag;
            if (url == null) {
                return false;
            }
            try {
                if (url.startsWith("weixin://") //微信
                        || url.startsWith("mailto://") //邮件
                        || url.startsWith("tel://")//电话
                        || url.startsWith("tel:")//电话
                        //其他自定义的scheme
                        || url.startsWith("mqqwpa://")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
            } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
            }

            //处理http和https开头的url
            if (url.startsWith("http://") || url.startsWith("https://")) {
                mWebView.loadUrl(url);
                return false;
            }
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            onUrlFinished(view, url);
        }

    }


    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent e) {// webview的双击事件
            if (TAG % 2 == 0) {
                TAG++;
                mLayoutBottom.startAnimation(animBottomIn);
            } else {
                TAG++;
                mLayoutBottom.startAnimation(animBottomOut);
            }
            return super.onDoubleTap(e);
        }
    }
}
