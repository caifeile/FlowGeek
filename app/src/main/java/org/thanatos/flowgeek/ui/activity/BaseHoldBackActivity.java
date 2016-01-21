package org.thanatos.flowgeek.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import org.thanatos.flowgeek.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import nucleus.presenter.Presenter;
import nucleus.presenter.RxPresenter;

public abstract class BaseHoldBackActivity<P extends Presenter> extends BaseActivity<P> {

    @Bind(R.id.toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(onBindLayout());
        ButterKnife.bind(this);

        mToolbar.setTitle("");
        mToolbar.setSubtitle(onSetTitle());
        mToolbar.setSubtitleTextColor(getResources().getColor(R.color.toolbar_subtitle));
        mToolbar.setNavigationIcon(R.mipmap.icon_back);
        mToolbar.setNavigationOnClickListener((v) -> onBackPressed());
    }

    protected abstract String onSetTitle();

    protected abstract int onBindLayout();

}
