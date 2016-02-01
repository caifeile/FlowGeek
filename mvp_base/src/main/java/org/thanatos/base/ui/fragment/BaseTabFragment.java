package org.thanatos.base.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import org.thanatos.base.R;

import java.util.ArrayList;

/**
 *
 * Created by thanatos on 15-7-21.
 */
public abstract class BaseTabFragment extends BaseFragment{

    protected ViewPager mViewPager;

    protected FragmentStatePagerAdapter mAdapter;
    protected ArrayList<ViewPageInfo> mTabs;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);

        if (mAdapter==null){
            mTabs = new ArrayList<>();
            onSetupTabs();

            mViewPager.setAdapter(new FragmentStatePagerAdapter(getGenuineFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    return mTabs.get(position).fragment;
                }
                @Override
                public int getCount() {
                    return mTabs.size();
                }
                @Override
                public CharSequence getPageTitle(int position) {
                    return mTabs.get(position).tag;
                }
            });

        }else{
            mViewPager.setAdapter(mAdapter);
        }
    }

    /**
     * 导航元素
     * @param title
     * @return
     */
    public abstract View setupTabItemView(String title);

    /**
     * 设置Fragment
     */
    public abstract void onSetupTabs();

    public FragmentManager getGenuineFragmentManager(){
        return getFragmentManager();
    }

    /**
     * 添加Fragment对象到ViewPager
     */
    public void addTab(String tag, Class<? extends Fragment> fragment, int catalog){
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_TYPE, catalog);
        mTabs.add(new ViewPageInfo(tag, Fragment.instantiate(getActivity(), fragment.getName(), bundle)));
    }

    public void addTab(String tag, Class<? extends Fragment> fragment){
        mTabs.add(new ViewPageInfo(tag, Fragment.instantiate(getActivity(), fragment.getName())));
    }

    public void addTab(String tag, Fragment fragment){
        mTabs.add(new ViewPageInfo(tag, fragment));
    }

    public void setCurrentItem(int index){
        mViewPager.setCurrentItem(index);
    }

    public int getCurrentItem(){
        return mViewPager.getCurrentItem();
    }

    public int getPageCount(){
        return mTabs.size();
    }

    /**
     * ViewPageInformation
     */
    public static class ViewPageInfo {
        public String tag;
        public View view;
        public Fragment fragment;

        public ViewPageInfo(String tag, Fragment fragment){
            this.tag = tag;
            this.fragment = fragment;
        }
    }
}
