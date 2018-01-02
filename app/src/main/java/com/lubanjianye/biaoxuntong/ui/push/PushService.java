package com.lubanjianye.biaoxuntong.ui.push;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.app.BiaoXunTong;
import com.lubanjianye.biaoxuntong.ui.browser.BrowserActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.IndexBxtgdjDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.IndexSggjyDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.IndexSggjycgrowDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.IndexSggjycgtableDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.index.detail.IndexXcgggDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.result.detail.ResultSggjyzbjgDetailActivity;
import com.lubanjianye.biaoxuntong.ui.main.result.detail.ResultXjgggDetailActivity;
import com.lubanjianye.biaoxuntong.util.notify.NotifyUtil;
import com.mixpush.client.core.MixPushIntentService;
import com.mixpush.client.core.MixPushMessage;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.push
 * 文件名:   PushService
 * 创建者:   lunious
 * 创建时间: 2017/12/13  11:36
 * 描述:     TODO
 */

public class PushService extends MixPushIntentService {

    String mTitle = null;
    String mContent = null;
    String mEntityId = null;
    String mEntity = null;
    String mType = null;
    String mUrl = null;

    @Override
    public void onReceivePassThroughMessage(MixPushMessage mixPushMessage) {

        if (mixPushMessage.getContent() != null) {

            JSONObject object = JSON.parseObject(mixPushMessage.getContent());

            mTitle = object.getString("body");
            mContent = object.getString("body");
            mEntityId = object.getString("entityId");
            mEntity = object.getString("entity");
            mType = object.getString("type");
            mUrl = object.getString("url");


            notify_normal_moreLine(mType, Integer.parseInt(mEntityId), mEntity, mTitle, mContent, mUrl);
        }


    }

    @Override
    public void onNotificationMessageClicked(MixPushMessage mixPushMessage) {

    }

    private int requestCode = (int) SystemClock.uptimeMillis();
    private NotifyUtil currentNotify;
    Intent intent = null;

    /**
     * 高仿网易新闻
     */
    private void notify_normal_moreLine(String mType, int mEntityId, String mEntity, String mTitle, String mContent, String mUrl) {

        //根据type去跳转页面

        if ("1".equals(mType)) {
            if ("xjggg".equals(mEntity) || "sjggg".equals(mEntity)) {
                intent = new Intent(BiaoXunTong.getApplicationContext(), ResultXjgggDetailActivity.class);
            } else if ("sggjyzbjg".equals(mEntity) || "sggjycgjgrow".equals(mEntity) || "sggjyjgcgtable".equals(mEntity)) {
                intent = new Intent(BiaoXunTong.getApplicationContext(), ResultSggjyzbjgDetailActivity.class);
            } else if ("sggjy".equals(mEntity)) {
                intent = new Intent(BiaoXunTong.getApplicationContext(), IndexSggjyDetailActivity.class);
            } else if ("xcggg".equals(mEntity)) {
                intent = new Intent(BiaoXunTong.getApplicationContext(), IndexXcgggDetailActivity.class);
            } else if ("bxtgdj".equals(mEntity)) {
                intent = new Intent(BiaoXunTong.getApplicationContext(), IndexBxtgdjDetailActivity.class);
            } else if ("sggjycgtable".equals(mEntity)) {
                intent = new Intent(BiaoXunTong.getApplicationContext(), IndexSggjycgtableDetailActivity.class);
            } else if ("sggjycgrow".equals(mEntity)) {
                intent = new Intent(BiaoXunTong.getApplicationContext(), IndexSggjycgrowDetailActivity.class);
            } else if ("scggg".equals(mEntity)) {

            }
            intent.putExtra("entityId", mEntityId);
            intent.putExtra("entity", mEntity);
        } else if ("2".equals(mType)) {

        } else if ("3".equals(mType)) {
            intent = new Intent(getApplicationContext(), BrowserActivity.class);
            intent.putExtra("url", mUrl);
            intent.putExtra("title", mTitle);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(),
                requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        int largeIcon = R.mipmap.log;
        int smallIcon = R.mipmap.log;
        String ticker = mTitle;
        String title = mTitle;
        String content = mContent;
        //实例化工具类，并且调用接口
        NotifyUtil notify = new NotifyUtil(getApplicationContext(), requestCode);
        notify.notify_biaoxuntong(pIntent, smallIcon, largeIcon, ticker,
                title, content, true, true, false);
        currentNotify = notify;
    }

}
