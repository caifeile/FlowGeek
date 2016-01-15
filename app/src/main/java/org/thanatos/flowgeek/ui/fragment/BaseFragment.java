package org.thanatos.flowgeek.ui.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by aoyolo on 15/12/21.
 */
public class BaseFragment extends Fragment{

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
