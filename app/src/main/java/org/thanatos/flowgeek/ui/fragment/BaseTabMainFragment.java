package org.thanatos.flowgeek.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.thanatos.flowgeek.R;

import butterknife.Bind;

/**
 * @author thanatos
 * @create 2016-01-05
 **/
public abstract class BaseTabMainFragment extends BaseTabFragment{

    @Bind(R.id.tab_nav) TabLayout mTabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_universal_tab, container, false);
    }

    @SuppressWarnings("all")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mAdapter==null){
            mTabLayout.setupWithViewPager(viewPager);

            for (int i=0; i<mTabLayout.getTabCount(); i++){
                TabLayout.Tab tab = mTabLayout.getTabAt(i);
                if (tab!=null) tab.setCustomView(setupTabItemView(mTabs.get(i).tag));
            }

            if (mTabs.size()>0){
                setCurrentItem(0);
                mTabLayout.getTabAt(0).getCustomView().setSelected(true);
            }
        }
    }

    @Override
    public TextView setupTabItemView(String tag) {
        RelativeLayout layout = (RelativeLayout) View.inflate(mContext, R.layout.view_tab_item, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        layout.setLayoutParams(params);
        TextView tabItemView = (TextView) layout.findViewById(R.id.pager_nav_item);
        tabItemView.setText(tag);
        return tabItemView;
    }
}
