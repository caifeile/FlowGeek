package org.thanatos.flowgeek.ui.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.thanatos.flowgeek.R;
import org.thanatos.flowgeek.adapter.BaseListAdapter;
import org.thanatos.flowgeek.adapter.CmmAdapter;
import org.thanatos.flowgeek.bean.Comment;
import org.thanatos.flowgeek.presenter.CmmPresenter;

import nucleus.factory.RequiresPresenter;

/**
 * A simple {@link Fragment} subclass.
 */
@RequiresPresenter(CmmPresenter.class)
public class CmmFragment extends BaseListFragment<Comment, CmmPresenter> {

    public static final String BUNDLE_KEY_CMM_ID = "bundle_key_cmm_id";

    @Override
    protected BaseListAdapter<Comment> onSetupAdapter() {
        return new CmmAdapter(getContext(), BaseListAdapter.ONLY_FOOTER);
    }


}
