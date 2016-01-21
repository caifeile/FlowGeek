package org.thanatos.flowgeek.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import nucleus.presenter.Presenter;
import nucleus.view.NucleusActivity;

public class BaseActivity<P extends Presenter> extends NucleusActivity<P> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
