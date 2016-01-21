package org.thanatos.flowgeek.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.thanatos.flowgeek.AppManager;
import org.thanatos.flowgeek.UIManager;
import org.thanatos.flowgeek.adapter.BaseListAdapter;
import org.thanatos.flowgeek.adapter.NewsAdapter;
import org.thanatos.flowgeek.bean.News;
import org.thanatos.flowgeek.bean.NewsList;
import org.thanatos.flowgeek.presenter.NewsPresenter;

import nucleus.factory.RequiresPresenter;


/**
 * Created by thanatos on 15/12/22.
 */
@RequiresPresenter(NewsPresenter.class)
public class ListNewsFragment extends BaseListFragment<News, NewsPresenter> implements BaseListAdapter.OnItemClickListener {

    public int mCatalog;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mCatalog = getArguments().getInt(AppManager.BUNDLE_TYPE, NewsList.CATALOG_ALL);
    }

    @Override
    protected BaseListAdapter<News> onSetupAdapter() {
        return new NewsAdapter(getActivity(), BaseListAdapter.ONLY_FOOTER);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(int position, long id, View view) {
        UIManager.redirectNewsActivity(getActivity(), mAdapter.getItem(position));
    }
}
