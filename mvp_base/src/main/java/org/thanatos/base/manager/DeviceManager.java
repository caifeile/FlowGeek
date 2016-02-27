package org.thanatos.base.manager;

import android.content.ClipboardManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by thanatos on 15-9-22.
 */
public class DeviceManager {

    private static ConnectivityManager mCnnManager;
    private static InputMethodManager mSoftInputManager;

    public static ConnectivityManager getCnnManager(Context context){
        if (mCnnManager==null)
            mCnnManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return mCnnManager;
    }

    public static InputMethodManager getSoftInputManager(Context context){
        if (mSoftInputManager==null)
            mSoftInputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return mSoftInputManager;
    }

    /**
     * 检测是否有网络
     * @return
     */
    public static boolean hasInternet(Context context) {
        return getCnnManager(context).getActiveNetworkInfo()!=null && getCnnManager(context).getActiveNetworkInfo().isAvailable();
    }

    /**
     * 网络类型
     * @return
     */
    public static int getInternetType(Context context){
        return getCnnManager(context).getActiveNetworkInfo().getType();
    }

    /**
     * 关闭软键盘
     * @param context
     * @param view
     */
    public static void hideSoftInput(Context context, View view){
        if (view == null) return;
        getSoftInputManager(context).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public static ClipboardManager getClipboardManager(Context context) {
        return (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }
}
