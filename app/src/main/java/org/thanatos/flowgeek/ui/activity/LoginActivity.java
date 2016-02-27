package org.thanatos.flowgeek.ui.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.widget.Toast;

import org.thanatos.base.model.SharePreferenceManager;
import org.thanatos.base.model.SharePreferenceManager.LocalUser;
import org.thanatos.base.ui.activity.BaseHoldBackActivity;
import org.thanatos.base.utils.Utilities;
import org.thanatos.flowgeek.AppManager;
import org.thanatos.flowgeek.R;
import org.thanatos.flowgeek.bean.User;
import org.thanatos.flowgeek.event.Events;
import org.thanatos.flowgeek.event.RxBus;
import org.thanatos.flowgeek.presenter.LoginPresenter;
import org.thanatos.flowgeek.utils.DialogFactory;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;

/**
 * Created by thanatos on 16/2/22.
 */
@RequiresPresenter(LoginPresenter.class)
public class LoginActivity extends BaseHoldBackActivity<LoginPresenter>{

    public static final String KEY_RESULT_USER = "KEY_RESULT_USER";

    @Bind(R.id.et_username) TextInputLayout mInputUserName;
    @Bind(R.id.et_password) TextInputLayout mInputPassword;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected String onSetTitle() {
        return "登录";
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_login;
    }

    @SuppressWarnings("all")
    @OnClick(R.id.btn_submit) void onSubmit(){
        dialog = DialogFactory.getFactory().getLoadingDialog(this);
        dialog.show();
        // 校验
        String username = mInputUserName.getEditText().getText().toString();
        String password = mInputPassword.getEditText().getText().toString();
        if (Utilities.isEmpty(username) || Utilities.isEmpty(password)){
            Toast.makeText(this, "账号密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //
        getPresenter().login(username, password);
    }

    @SuppressWarnings("all")
    public void onLoadSuccessful(User user) {
        if (dialog!=null) dialog.dismiss();

        SharedPreferences.Editor editor = SharePreferenceManager.getLocalUser(this).edit();
        editor.putString(LocalUser.KEY_USERNAME, mInputUserName.getEditText().getText().toString());
        editor.putString(LocalUser.KEY_PASSWORD, mInputPassword.getEditText().getText().toString());
        editor.putString(LocalUser.KEY_GENDER, user.getGender());
        editor.putInt(LocalUser.KEY_SKILL_SCORE, user.getScore());
        editor.putString(LocalUser.KEY_NICK, user.getName());
        editor.putLong(LocalUser.KEY_UID, user.getUid());
        editor.putLong(LocalUser.KEY_LAST_LOGIN_TIME, new Date().getTime());
        editor.putBoolean(LocalUser.KEY_LOGIN_STATE, true);
        editor.putString(LocalUser.KEY_PORTRAIT, user.getPortrait());
        editor.apply();

        AppManager.LOCAL_LOGINED_USER = user;
        RxBus.getInstance().send(Events.EventEnum.DELIVER_LOGIN, null);

        finish();
    }

    public void onLoadFailed(String message) {
        if (dialog!=null) dialog.dismiss();
        if (message == null){
            Toast.makeText(this, "未知原因", Toast.LENGTH_SHORT).show();
            return;
        }
        mInputUserName.setError(message);
    }
}
