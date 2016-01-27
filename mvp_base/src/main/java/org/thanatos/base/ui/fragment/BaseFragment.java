package org.thanatos.base.ui.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import nucleus.presenter.Presenter;
import nucleus.view.NucleusFragment;

/**
 * Created by thanatos on 15/12/21.
 */
public abstract class BaseFragment<P extends Presenter> extends NucleusFragment<P>{

    public Context mContext;
    public Resources resources;

    public static final String BUNDLE_TYPE = "BUNDLE_TYPE";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        resources = mContext.getResources();
    }

}
