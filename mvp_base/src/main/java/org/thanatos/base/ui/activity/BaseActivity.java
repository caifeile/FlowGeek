package org.thanatos.base.ui.activity;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import org.thanatos.base.R;
import org.thanatos.base.manager.DeviceManager;
import org.thanatos.base.model.SharePreferenceManager;
import org.thanatos.base.model.SharePreferenceManager.ApplicationSetting;
import org.thanatos.base.model.SharePreferenceManager.ApplicationSetting.ApplicationTheme;
import org.thanatos.base.utils.UIHelper;

import nucleus.presenter.Presenter;
import nucleus.view.NucleusActivity;

public abstract class BaseActivity<P extends Presenter> extends NucleusActivity<P> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 主题选择
        SharedPreferences preferences = SharePreferenceManager.getApplicationSetting(this);

        int theme = preferences.getInt(ApplicationSetting.KEY_THEME, ApplicationTheme.LIGHT.getKey());

        if (theme == ApplicationTheme.LIGHT.getKey()){
            setTheme(ApplicationTheme.LIGHT.getResId());
        }else if(theme == ApplicationTheme.DARK.getKey()){
            setTheme(ApplicationTheme.DARK.getResId());
        }

        // 方向锁定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()){
            DeviceManager.hideSoftInput(this, getCurrentFocus());
        }
    }
}
