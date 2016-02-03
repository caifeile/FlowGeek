package org.thanatos.base.model;

import android.content.Context;
import android.content.SharedPreferences;

import org.thanatos.base.R;

/**
 * Created by thanatos on 16/2/2.
 */
public class SharePreferenceManager {

    public static SharedPreferences getApplicationSetting(Context context){
        return context.getSharedPreferences(ApplicationSetting.FILE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 应用配置首选项, 保存主题...一些系统级别的配置
     */
    public static class ApplicationSetting{
        public static final String FILE_NAME = "application_setting";
        public static final String KEY_THEME = "key_theme";

        /**
         * 主题列举
         */
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

}
