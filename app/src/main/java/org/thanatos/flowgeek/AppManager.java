package org.thanatos.flowgeek;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by thanatos on 15-9-22.
 */
public class AppManager extends Application{

    public static Context context;
    public static Resources resources;
    public static SharedPreferences preferences;

    public static final String BUNDLE_TYPE = "BUNDLE_TYPE";

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        resources = getResources();
        preferences = getSharedPreferences(context.getPackageName(), MODE_PRIVATE);
        LeakCanary.install(this);
    }

    public static PackageInfo getPackageInfo(){
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
