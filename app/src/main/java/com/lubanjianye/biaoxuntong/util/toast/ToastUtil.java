package com.lubanjianye.biaoxuntong.util.toast;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lubanjianye.biaoxuntong.R;

/**
 * 项目名:   9527
 * 包名:     com.lubanjianye.biaoxuntong.util.toast
 * 文件名:   ToastUtil
 * 创建者:   lunious
 * 创建时间: 2017/12/26  11:02
 * 描述:     TODO
 */

public class ToastUtil {


    //对话框时长号(毫秒)
    private static int duration = 100;

    //自定义toast对象
    private static Toast toast;

    /**
     * 自定义短Toast调用
     *
     * @param context 上下文
     * @param message 显示文本
     * @return void
     */
    public static void shortToast(final Context context, final String message) {
        if (null == toast) {
            toast = new Toast(context);
            toast.setDuration(Toast.LENGTH_SHORT);
            View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.sys_show_toast, null);
            TextView textView = (TextView) view.findViewById(R.id.sys_show_toast_txt);
            textView.setText(message);
            toast.setView(view);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            TextView textView = (TextView) toast.getView().findViewById(R.id.sys_show_toast_txt);
            textView.setText(message);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    public static void shortBottonToast(final Context context, final String message) {
        if (null == toast) {
            toast = new Toast(context);
            toast.setDuration(Toast.LENGTH_SHORT);
            View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.sys_show_toast, null);
            TextView textView = (TextView) view.findViewById(R.id.sys_show_toast_txt);
            textView.setText(message);
            toast.setView(view);
            toast.show();
        } else {
            TextView textView = (TextView) toast.getView().findViewById(R.id.sys_show_toast_txt);
            textView.setText(message);
            toast.show();
        }
    }

    /**
     * 自定义长Toast调用
     *
     * @param context 上下文
     * @param message 显示文本
     * @return void
     */
    public static void longToast(final Context context, final String message) {
        if (null == toast) {
            toast = new Toast(context);
            toast.setDuration(Toast.LENGTH_LONG);
            View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.sys_show_toast, null);
            TextView textView = (TextView) view.findViewById(R.id.sys_show_toast_txt);
            textView.setText(message);
            toast.setView(view);
            toast.show();
        } else {
            TextView textView = (TextView) toast.getView().findViewById(R.id.sys_show_toast_txt);
            textView.setText(message);
            toast.show();
        }
    }

    /**
     * 取消显示Toast
     */
    public static void cancelToast() {
        if (null != toast) {
            toast.cancel();
        }
    }


}
