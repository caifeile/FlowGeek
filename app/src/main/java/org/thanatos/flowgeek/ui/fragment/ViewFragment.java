package org.thanatos.flowgeek.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 一个简单的Fragment，专门显示一个View，特别服务于轮番、ViewPager
 *
 * Created by thanatos on 15-7-27.
 */
public class ViewFragment extends BaseFragment {

    private View view;

    public ViewFragment() {

    }

    public ViewFragment(View view) {
        this.view = view;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return view;
    }
}
