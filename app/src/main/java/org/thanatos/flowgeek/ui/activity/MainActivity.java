package org.thanatos.flowgeek.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.internal.ScrimInsetsFrameLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.thanatos.base.model.SharePreferenceManager;
import org.thanatos.base.model.SharePreferenceManager.ApplicationSetting;
import org.thanatos.base.model.SharePreferenceManager.ApplicationSetting.ApplicationTheme;
import org.thanatos.base.ui.activity.BaseActivity;
import org.thanatos.flowgeek.R;
import org.thanatos.flowgeek.bean.NewsList;
import org.thanatos.flowgeek.ui.fragment.BaseTabMainFragment;
import org.thanatos.flowgeek.ui.fragment.ListNewsFragment;
import org.thanatos.flowgeek.ui.fragment.ListTweetFragment;
import org.thanatos.flowgeek.ui.fragment.TabTweetFragment;
import org.thanatos.pay.ui.fragment.EntryFragment;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String CHANGE_THEME = "change_theme";
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.layout_drawer) DrawerLayout mDrawerLayout;
    @Bind(R.id.nav_view) NavigationView mDrawerNavView;
    @Bind(R.id.layout_coordinator) CoordinatorLayout mLayoutCoordinator;

    private MenuItem mPreMenuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.transparent));
        }

        mToolbar.setTitle("");
        mToolbar.setSubtitle(getResources().getString(R.string.app_name));

        setSupportActionBar(mToolbar);

        mDrawerNavView.setItemIconTintList(null);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open_drawer, R.string.close_drawer);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        if (getIntent().getBooleanExtra(CHANGE_THEME, false)){
            mDrawerLayout.openDrawer(mDrawerNavView);
        }

        mDrawerNavView.setNavigationItemSelectedListener(this);

        setDefaultMenuItem();
        mDrawerNavView.setCheckedItem(R.id.menu_new);
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
            case R.id.menu_explore: // 发现探索
                if (mPreMenuItem!=null && mPreMenuItem.getItemId()==R.id.menu_explore) break;
                CoordinatorLayout layout;

                break;
            case R.id.menu_blog: // 博客
                if (mPreMenuItem!=null && mPreMenuItem.getItemId()==R.id.menu_blog) break;

                break;
            case R.id.menu_tweets: // 动弹
                if (mPreMenuItem!=null && mPreMenuItem.getItemId()==R.id.menu_tweets) break;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, Fragment.instantiate(this, TabTweetFragment.class.getName()))
                        .commit();
                break;

            case R.id.menu_technology_question_answer: // 技术问答
                if (mPreMenuItem!=null && mPreMenuItem.getItemId()==R.id.menu_technology_question_answer) break;

                break;

            case R.id.menu_theme: // 更改主题
                if (mPreMenuItem!=null && mPreMenuItem.getItemId()==R.id.menu_theme) break;
                SharedPreferences preferences = SharePreferenceManager.getApplicationSetting(this);
                int theme = preferences.getInt(ApplicationSetting.KEY_THEME, ApplicationTheme.LIGHT.getKey());
                SharedPreferences.Editor editor = preferences.edit();
                if (theme == ApplicationTheme.LIGHT.getKey()){
                    editor.putInt(ApplicationSetting.KEY_THEME, ApplicationTheme.DARK.getKey());
                }else{
                    editor.putInt(ApplicationSetting.KEY_THEME, ApplicationTheme.LIGHT.getKey());
                }
                editor.apply();
                finish();
                Intent intent = getIntent();
                intent.putExtra(CHANGE_THEME, true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                return true;
            case R.id.menu_setting: // 设置
                if (mPreMenuItem!=null && mPreMenuItem.getItemId()==R.id.menu_setting) break;

                break;
            case R.id.menu_donate: // 捐助我
                if (mPreMenuItem!=null && mPreMenuItem.getItemId()==R.id.menu_donate) break;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container,
                                Fragment.instantiate(this, EntryFragment.class.getName()))
                        .commit();
                break;

            case R.id.menu_new : // 资讯
                if (mPreMenuItem!=null && mPreMenuItem.getItemId()==R.id.menu_new) break;
                setDefaultMenuItem();
                break;
        }
        item.setChecked(true);
        if (mPreMenuItem!=null) mPreMenuItem.setChecked(false);
        mPreMenuItem = item;
        mDrawerLayout.closeDrawer(mDrawerNavView);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void addToCoordinatorLayout(View view){
        mLayoutCoordinator.addView(view);
    }

    public void removeFormCoordinatorLayout(View view){
        mLayoutCoordinator.removeView(view);
    }

    public CoordinatorLayout getCoordinatorLayout(){
        return mLayoutCoordinator;
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
