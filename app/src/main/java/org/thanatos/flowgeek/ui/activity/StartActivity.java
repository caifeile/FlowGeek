package org.thanatos.flowgeek.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import org.thanatos.base.model.SharePreferenceManager;
import org.thanatos.base.model.SharePreferenceManager.LocalUser;
import org.thanatos.flowgeek.AppManager;
import org.thanatos.flowgeek.R;
import org.thanatos.flowgeek.bean.User;

import java.util.Date;

public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        loadLocalUser();

        new Handler().postDelayed(()->{
            startActivity(new Intent(StartActivity.this, MainActivity.class));
            finish();
        },2000);
    }

    /**
     * 从SharePreference登录用户
     */
    private void loadLocalUser() {
        SharedPreferences preferences = SharePreferenceManager.getLocalUser(this);
        // 首先是否有登录记录
        boolean isLogin = preferences.getBoolean(LocalUser.KEY_LOGIN_STATE, false);
        if (!isLogin) return;
        // 其次,登录记录是否过期
        long lastLoginTime = preferences.getLong(LocalUser.KEY_LAST_LOGIN_TIME, -1);
        if (lastLoginTime == -1) return;
        if (new Date().getTime() - lastLoginTime > 30L * 24 * 60 * 60 * 1000) return;
        // 验证信息是否完善
        User user = new User();
        user.setUid(preferences.getLong(LocalUser.KEY_UID, -1));
        if (user.getUid() == -1) return;
        user.setName(preferences.getString(LocalUser.KEY_NICK, null));
        if (user.getName() == null) return;
        user.setPortrait(preferences.getString(LocalUser.KEY_PORTRAIT, null));
        if (user.getPortrait() == null) return;
        user.setAccount(preferences.getString(LocalUser.KEY_USERNAME, null));
        if (user.getAccount() == null) return;
        user.setPwd(preferences.getString(LocalUser.KEY_PASSWORD, null));
        if (user.getPwd() == null) return;
        user.setGender(preferences.getString(LocalUser.KEY_GENDER, null));

        user.setScore(preferences.getInt(LocalUser.KEY_SKILL_SCORE, 0));

        AppManager.LOCAL_LOGINED_USER = user;
    }


}
