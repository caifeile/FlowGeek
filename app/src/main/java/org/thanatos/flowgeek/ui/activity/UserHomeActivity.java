package org.thanatos.flowgeek.ui.activity;

import org.thanatos.base.ui.activity.BaseHoldBackActivity;
import org.thanatos.flowgeek.R;

/**
 * Created by thanatos on 16/2/19.
 */
public class UserHomeActivity extends BaseHoldBackActivity{

    public static final String BUNDLE_USER_KEY = "BUNDLE_USER_KEY";

    @Override
    protected String onSetTitle() {
        return "";
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_user_home;
    }

}
