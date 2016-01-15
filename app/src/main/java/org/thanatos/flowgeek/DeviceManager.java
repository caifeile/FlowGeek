package org.thanatos.flowgeek;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by thanatos on 15-9-22.
 */
public class DeviceManager {

    private static ConnectivityManager mCnnManager;
    private static InputMethodManager mSoftInputManager;

    public static ConnectivityManager getCnnManager(){
        if (mCnnManager==null)
            mCnnManager = (ConnectivityManager) AppManager.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return mCnnManager;
    }

    public static InputMethodManager getSoftInputManager(){
        if (mSoftInputManager==null)
            mSoftInputManager = (InputMethodManager) AppManager.context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return mSoftInputManager;
    }

    /**
     * 检测是否有网络
     * @return
     */
    public static boolean hasInternet() {
        return getCnnManager().getActiveNetworkInfo()!=null && getCnnManager().getActiveNetworkInfo().isAvailable();
    }

    /**
     * 网络类型
     * @return
     */
    public static int getInternetType(){
        return getCnnManager().getActiveNetworkInfo().getType();
    }


}
