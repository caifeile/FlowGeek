package org.thanatos.base.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import org.thanatos.base.R;
import nucleus.presenter.Presenter;

public abstract class BaseHoldBackActivity<P extends Presenter> extends BaseActivity<P> {

    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view;

        setContentView(view = getLayoutInflater().inflate(onBindLayout(), null));

        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);

        mToolbar.setTitle("");
        mToolbar.setSubtitle(onSetTitle());
        mToolbar.setSubtitleTextColor(getResources().getColor(R.color.toolbar_subtitle));
        mToolbar.setNavigationIcon(R.mipmap.icon_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    protected abstract String onSetTitle();

    protected abstract int onBindLayout();

}
