package com.edward.javaecho;

import android.app.Activity;
import android.widget.TextView;

/**
 * 功能描述：
 *
 * @author (作者) edward
 * 创建时间： 2018/12/17
 */
public class SystemOut {
    private static Activity mActivity;
    private static TextView mTextView;
    private static Thread mThread;


    public static void init(Activity activity, TextView textView) {
        mThread = Thread.currentThread();
        mActivity = activity;
        mTextView = textView;
    }

    public static void reset() {
        if (mTextView == null) return;
        mTextView.setText("");
    }

    public static void clear() {
        if (mTextView == null) return;
        mTextView = null;
    }

    public static void println(int i) {
        println(Integer.toString(i));
    }

    public static void println(final String text) {
        System.out.println(text);
        if (mThread == Thread.currentThread()) {
            if (mTextView == null) return;
            mTextView.append(text + "\r\n");
        } else {
            if (mActivity == null) return;
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mTextView == null) return;
                    mTextView.append(text + "\r\n");
                }
            });
        }
    }

    public static void println(final Throwable throwable) {
        throwable.printStackTrace();
        if (mThread == Thread.currentThread()) {
            if (mTextView == null) return;
            mTextView.append(throwable.getClass().getSimpleName() + throwable.getMessage() + "\r\n");
            for (StackTraceElement stackTraceElement : throwable.getStackTrace()) {
                mTextView.append(stackTraceElement.toString() + "\r\n");
            }
        } else {
            if (mActivity == null) return;
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mTextView == null) return;
                    mTextView.append(throwable.getClass().getSimpleName() + throwable.getMessage() + "\r\n");
                    for (StackTraceElement stackTraceElement : throwable.getStackTrace()) {
                        mTextView.append(stackTraceElement.toString() + "\r\n");
                    }
                }
            });
        }
    }

}
