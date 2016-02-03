package org.thanatos.base.ui.activity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import org.thanatos.base.R;
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
        SharedPreferences preferences = SharePreferenceManager.getApplicationSetting(this);

        int theme = preferences.getInt(ApplicationSetting.KEY_THEME, ApplicationTheme.LIGHT.getKey());

        if (theme == ApplicationTheme.LIGHT.getKey()){

            setTheme(ApplicationTheme.LIGHT.getResId());

        }else if(theme == ApplicationTheme.DARK.getKey()){

            setTheme(ApplicationTheme.DARK.getResId());

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(UIHelper.getAttrValueFromTheme(R.attr.principle, getTheme()));
        }

    }

}
