package org.thanatos.flowgeek.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import org.thanatos.base.model.SharePreferenceManager;
import org.thanatos.base.model.SharePreferenceManager.ApplicationSetting;
import org.thanatos.base.model.SharePreferenceManager.ApplicationSetting.ApplicationTheme;
import org.thanatos.flowgeek.R;

/**
 * Created by thanatos on 16/2/23.
 */
public class DialogFactory {

    private static DialogFactory factory;

    private DialogFactory(){}

    public static DialogFactory getFactory(){
        return new DialogFactory();
    }

    public int getTheme(Context context){
        SharedPreferences preferences = SharePreferenceManager.getApplicationSetting(context);
        int theme = preferences.getInt(ApplicationSetting.KEY_THEME, ApplicationTheme.LIGHT.getKey());
        if (theme == ApplicationTheme.LIGHT.getKey()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return android.R.style.Theme_Material_Light_Dialog_Alert;
            }else return AlertDialog.THEME_HOLO_LIGHT;
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return android.R.style.Theme_Material_Dialog_Alert;
            }else return AlertDialog.THEME_HOLO_DARK;
        }
    }

    @Deprecated
    public ProgressDialog getLoadingDialog(Context context){
        ProgressDialog dialog = new ProgressDialog(context, getTheme(context));
        dialog.setMessage(context.getResources().getString(R.string.loading));
        return dialog;
    }

    public ProgressDialog getLoadingDialog(Context context, String message){
        ProgressDialog dialog = new ProgressDialog(context, getTheme(context));
        dialog.setMessage(message);
        return dialog;
    }



}
