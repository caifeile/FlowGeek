package org.thanatos.base.ui.activity;

import android.os.Bundle;

import nucleus.presenter.Presenter;
import nucleus.view.NucleusActivity;

public abstract class BaseActivity<P extends Presenter> extends NucleusActivity<P> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
