package org.thanatos.base.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.thanatos.base.R;


/**
 * 自定义导航元素的viewpager
 * 使用场景：表情选项卡，轮番
 *
 * @author thanatos
 * @create 2016-01-05
 **/
public abstract class BaseTabNavFragment extends BaseTabFragment implements ViewPager.OnPageChangeListener {

    protected LinearLayout mNavLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_dot_nav, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mNavLayout = (LinearLayout) view.findViewById(R.id.tab_nav);
        super.onViewCreated(view, savedInstanceState);
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void addTab(String title, Class<? extends Fragment> fragment, int catalog) {
        super.addTab(title, fragment, catalog);
        View view = setupTabItemView(title);
        mNavLayout.addView(view);
        mTabs.get(mTabs.size()-1).view = view;
    }

    @Override
    public void addTab(String title, Class<? extends Fragment> fragment) {
        super.addTab(title, fragment);
        View view = setupTabItemView(title);
        mNavLayout.addView(view);
        mTabs.get(mTabs.size()-1).view = view;
    }

    @Override
    public void addTab(String title, Fragment fragment) {
        super.addTab(title, fragment);
        View view = setupTabItemView(title);
        mNavLayout.addView(view);
        mTabs.get(mTabs.size()-1).view = view;
    }

    public void setCurrentItem(int index){
        mViewPager.setCurrentItem(index);
        mTabs.get(index).view.setSelected(true);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i=0; i<mTabs.size(); i++){
            if (position == i)
                mTabs.get(i).view.setSelected(true);
            else
                mTabs.get(i).view.setSelected(false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
