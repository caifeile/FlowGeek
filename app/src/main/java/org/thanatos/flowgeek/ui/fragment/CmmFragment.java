package org.thanatos.flowgeek.ui.fragment;


import android.app.Fragment;

import org.thanatos.base.adapter.BaseListAdapter;
import org.thanatos.base.ui.fragment.BaseListFragment;
import org.thanatos.flowgeek.R;
import org.thanatos.flowgeek.adapter.CmmAdapter;
import org.thanatos.flowgeek.bean.Comment;
import org.thanatos.flowgeek.presenter.CmmPresenter;

import java.math.BigInteger;

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

    @Override
    public int setDividerSize() {
        return getResources().getDimensionPixelSize(R.dimen.min_divider_height);
    }
}
