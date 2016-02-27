package org.thanatos.flowgeek.ui.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import org.thanatos.base.adapter.BaseListAdapter;
import org.thanatos.base.ui.fragment.BaseListFragment;
import org.thanatos.base.utils.UIHelper;
import org.thanatos.flowgeek.R;
import org.thanatos.flowgeek.adapter.CmmAdapter;
import org.thanatos.flowgeek.bean.Comment;
import org.thanatos.flowgeek.event.Events;
import org.thanatos.flowgeek.event.RxBus;
import org.thanatos.flowgeek.presenter.CmmPresenter;

import java.math.BigInteger;

import nucleus.factory.RequiresPresenter;


/**
 * A simple {@link Fragment} subclass.
 */
@RequiresPresenter(CmmPresenter.class)
public class CmmFragment extends BaseCmmFragment<CmmPresenter>{

    public static final String BUNDLE_KEY_CMM_ID = "bundle_key_cmm_id";

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView.setPadding(0, setDividerSize(), 0, 0);
    }

    @Override
    protected BaseListAdapter<Comment> onSetupAdapter() {
        return new CmmAdapter(getContext(), BaseListAdapter.ONLY_FOOTER);
    }

    @Override
    public int setDividerSize() {
        return getResources().getDimensionPixelSize(R.dimen.min_divider_height);
    }


}
