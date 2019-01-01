package com.edward.javaecho;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

/**
 * 功能描述：
 *
 * @author (作者) edward（冯丰枫）
 * @link http://www.jianshu.com/u/f7176d6d53d2
 * 创建时间： 2019/1/1
 */
public class StringUtils {
    /**
     * 复制文本到系统粘贴版
     */
    public static void copy(CharSequence text, Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard == null) return;
        clipboard.setPrimaryClip(ClipData.newPlainText("text", text));
        Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
    }

    /**
     * 从系统粘贴板粘贴文本
     */
    public static String paste(Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard == null) return "";
        ClipData clipData = clipboard.getPrimaryClip();
        if (clipData != null) {
            return clipData.getItemAt(0).getText().toString().trim();
        }
        return "";
    }
}
