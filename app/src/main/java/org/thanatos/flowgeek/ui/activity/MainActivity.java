package org.thanatos.flowgeek.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import org.thanatos.base.ui.activity.BaseActivity;
import org.thanatos.flowgeek.R;
import org.thanatos.flowgeek.bean.NewsList;
import org.thanatos.flowgeek.ui.fragment.BaseTabMainFragment;
import org.thanatos.flowgeek.ui.fragment.ListNewsFragment;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.layout_drawer) DrawerLayout mDrawerLayout;
    @Bind(R.id.nav_view) NavigationView mDrawerNav;

    private MenuItem mPreMenuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {

        mToolbar.setTitle("");
        mToolbar.setSubtitle(getResources().getString(R.string.app_name));

        setSupportActionBar(mToolbar);

        mDrawerNav.setItemIconTintList(null);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open_drawer, R.string.close_drawer);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mDrawerNav.setNavigationItemSelectedListener(this);

        setDefaultMenuItem();
        mDrawerNav.setCheckedItem(R.id.menu_new);
    }

    /**
     * 设置默认的页面
     */
    private void setDefaultMenuItem(){
        Fragment mTab = new BaseTabMainFragment() {
            @Override
            public void onSetupTabs() {
                addTab(getResources().getString(R.string.new_news), ListNewsFragment.class, NewsList.CATALOG_ALL);
                addTab(getResources().getString(R.string.week_news), ListNewsFragment.class, NewsList.CATALOG_WEEK);
                addTab(getResources().getString(R.string.month_news), ListNewsFragment.class, NewsList.CATALOG_MONTH);
            }
        };
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, mTab)
                .commit();
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_explore:
                if (mPreMenuItem!=null && mPreMenuItem.getItemId()==R.id.menu_explore) break;

                break;
            case R.id.menu_blog:
                if (mPreMenuItem!=null && mPreMenuItem.getItemId()==R.id.menu_blog) break;

                break;
            case R.id.menu_tweets:
                if (mPreMenuItem!=null && mPreMenuItem.getItemId()==R.id.menu_tweets) break;

                break;
            case R.id.menu_technology_question_answer:
                if (mPreMenuItem!=null && mPreMenuItem.getItemId()==R.id.menu_technology_question_answer) break;

                break;
            case R.id.menu_theme:
                if (mPreMenuItem!=null && mPreMenuItem.getItemId()==R.id.menu_theme) break;

                break;
            case R.id.menu_setting:
                if (mPreMenuItem!=null && mPreMenuItem.getItemId()==R.id.menu_setting) break;

                break;
            case R.id.menu_donate:
                if (mPreMenuItem!=null && mPreMenuItem.getItemId()==R.id.menu_donate) break;

                break;
            case R.id.menu_new : // 资讯
                if (mPreMenuItem!=null && mPreMenuItem.getItemId()==R.id.menu_new) break;
                setDefaultMenuItem();
                break;
        }
        item.setChecked(true);
        if (mPreMenuItem!=null) mPreMenuItem.setChecked(false);
        mPreMenuItem = item;
        mDrawerLayout.closeDrawer(mDrawerNav);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private boolean isBacking = false;
    private Toast mBackToast;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isBacking) {
                if (mBackToast != null)
                    mBackToast.cancel();
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            } else {
                isBacking = true;
                mBackToast = Toast.makeText(this, "再按一次退出" + getResources().getString(R.string.app_name), Toast.LENGTH_LONG);
                mBackToast.show();
                new Handler().postDelayed(() -> {
                    isBacking = false;
                    if (mBackToast != null)
                        mBackToast.cancel();
                }, 2000);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
