package org.thanatos.base.model;

import android.content.Context;
import android.content.SharedPreferences;

import org.thanatos.base.R;

/**
 * SharePreference管理类, oop管理众多的首选项文件
 *
 * Created by thanatos on 16/2/2.
 */
public class SharePreferenceManager {

    public static SharedPreferences getApplicationSetting(Context context){
        return context.getSharedPreferences(ApplicationSetting.FILE_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getLocalUser(Context context) {
        return context.getSharedPreferences(LocalUser.FILE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 应用配置首选项, 保存主题...一些系统级别的配置
     */
    public static class ApplicationSetting{
        public static final String FILE_NAME = "APPLICATION_SETTING";
        public static final String KEY_THEME = "KEY_THEME";


         // ---主题列举---
        public enum ApplicationTheme{
            LIGHT(1, R.style.LightTheme),
            DARK(2, R.style.DarkTheme);

            private int key;
            private int resId;

            ApplicationTheme(int key, int resId){
                this.key = key;
                this.resId = resId;
            }

            public int getKey() {
                return key;
            }

            public int getResId() {
                return resId;
            }
        }
    }

    /**
     * 专门保存用户信息
     */
    public static class LocalUser{
        public static final String FILE_NAME = "LOCAL_USER";

        public static final String KEY_USERNAME = "KEY_USERNAME";
        public static final String KEY_PASSWORD = "KEY_PASSWORD";
        public static final String KEY_GENDER = "KEY_GENDER";
        public static final String KEY_NICK = "KEY_NICK";
        public static final String KEY_SKILL_SCORE = "KEY_SKILL_SCORE";
        public static final String KEY_UID = "KEY_UID";
        public static final String KEY_LAST_LOGIN_TIME = "KEY_LAST_LOGIN_TIME";
        public static final String KEY_LOGIN_STATE = "KEY_LOGIN_STATE";
        public static final String KEY_PORTRAIT = "KEY_PORTRAIT";
        public static final String KEY_COOKIES = "KEY_COOKIES";

    }

}
