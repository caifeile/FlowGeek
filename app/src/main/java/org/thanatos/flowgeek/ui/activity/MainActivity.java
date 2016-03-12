package org.thanatos.flowgeek.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.trello.rxlifecycle.ActivityEvent;

import org.thanatos.base.model.SharePreferenceManager;
import org.thanatos.base.model.SharePreferenceManager.LocalUser;
import org.thanatos.base.model.SharePreferenceManager.ApplicationSetting;
import org.thanatos.base.model.SharePreferenceManager.ApplicationSetting.ApplicationTheme;
import org.thanatos.base.ui.activity.BaseActivity;
import org.thanatos.flowgeek.AppManager;
import org.thanatos.flowgeek.R;
import org.thanatos.flowgeek.ServerAPI;
import org.thanatos.flowgeek.UIManager;
import org.thanatos.flowgeek.bean.NewsList;
import org.thanatos.flowgeek.event.Events;
import org.thanatos.flowgeek.event.RxBus;
import org.thanatos.flowgeek.ui.fragment.BaseTabMainFragment;
import org.thanatos.flowgeek.ui.fragment.ListNewsFragment;
import org.thanatos.flowgeek.ui.fragment.TabTweetFragment;
import org.thanatos.flowgeek.utils.DialogFactory;
import org.thanatos.pay.ui.fragment.EntryFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observable;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String CHANGE_THEME = "CHANGE_THEME";

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.layout_drawer) DrawerLayout mDrawerLayout;
    @Bind(R.id.nav_view) NavigationView mDrawerNavView;
    @Bind(R.id.layout_coordinator) CoordinatorLayout mLayoutCoordinator;
    private CircleImageView ivPortrait;
    private TextView tvNick;
    private TextView tvScore;
    private ImageView ivExit;

    private MenuItem mCurrentMenuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        View mNavHeaderView = mDrawerNavView.getHeaderView(0);
        ivPortrait = (CircleImageView) mNavHeaderView.findViewById(R.id.iv_portrait);
        tvNick = (TextView) mNavHeaderView.findViewById(R.id.tv_nick);
        tvScore = (TextView) mNavHeaderView.findViewById(R.id.tv_score);
        ivExit = (ImageView) mNavHeaderView.findViewById(R.id.iv_exit);

        initView();
        initLogin();
        initSubscribers();
    }

    private void initSubscribers() {
        // 接受订阅, 无论在哪里登录, 都能够接收到这个事件, 并且更新侧滑抽屉的View
        RxBus.with(this)
                .setEvent(Events.EventEnum.DELIVER_LOGIN)
                .setEndEvent(ActivityEvent.DESTROY)
                .onNext((events)->{
                    initLogin();
                }).create();
    }

    @SuppressWarnings("all")
    private void initLogin() {

        // 如果未登录
        if (AppManager.LOCAL_LOGINED_USER == null) {
            ivPortrait.setImageResource(R.mipmap.icon_default_portrait);
            ivPortrait.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIManager.jump2login(MainActivity.this);
                }
            });
            tvNick.setText("未登录");
            tvNick.setCompoundDrawables(null, null, null, null);
            ivExit.setVisibility(View.GONE);
            tvScore.setText(null);
            return;
        }

        // 已登录
        // portrait
        Picasso.with(this).load(AppManager.LOCAL_LOGINED_USER.getPortrait()).into(ivPortrait);
        ivPortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIManager.toUserHome(MainActivity.this, AppManager.LOCAL_LOGINED_USER);
            }
        });

        // nick
        tvNick.setText(AppManager.LOCAL_LOGINED_USER.getName());

        // gender 我真是服了他们的接口了
        if (AppManager.LOCAL_LOGINED_USER.getGender().equals("1")
                || AppManager.LOCAL_LOGINED_USER.getGender().trim().equals("男")){ // --> 男
            tvNick.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    getResources().getDrawable(R.mipmap.icon_male), null);
        }else if (AppManager.LOCAL_LOGINED_USER.getGender().equals("0")
                || AppManager.LOCAL_LOGINED_USER.getGender().trim().equals("女")){
            tvNick.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    getResources().getDrawable(R.mipmap.icon_female), null);
        }else{
            tvNick.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    getResources().getDrawable(R.mipmap.icon_gender), null);
        }

        // score
        tvScore.setText("技能分 : " + AppManager.LOCAL_LOGINED_USER.getScore());

        // exit
        ivExit.setVisibility(View.VISIBLE);
        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this, DialogFactory.getFactory()
                        .getTheme(MainActivity.this))
                        .setTitle(getResources().getString(R.string.logout))
                        .setMessage(getResources().getString(R.string.are_you_sure_logout))
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor editor =
                                        SharePreferenceManager.getLocalUser(MainActivity.this).edit();
                                editor.putBoolean(LocalUser.KEY_LOGIN_STATE, false);
                                editor.apply();
                                AppManager.LOCAL_LOGINED_USER = null;
                                ServerAPI.clearCookies();
                                initLogin();
                                dialog.dismiss();
                                RxBus.getInstance().send(Events.EventEnum.DELIVER_LOGOUT, null);
                            }
                        }).create().show();
            }
        });

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
            /*case R.id.menu_explore: // 发现探索
                if (mCurrentMenuItem!=null && mCurrentMenuItem.getItemId()==R.id.menu_explore) break;

                break;*/
            case R.id.menu_blog: // 博客
                if (mCurrentMenuItem!=null && mCurrentMenuItem.getItemId()==R.id.menu_blog) break;

                break;
            case R.id.menu_tweets: // 动弹
                if (mCurrentMenuItem!=null && mCurrentMenuItem.getItemId()==R.id.menu_tweets) break;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, Fragment.instantiate(this, TabTweetFragment.class.getName()))
                        .commit();
                break;

            case R.id.menu_technology_question_answer: // 技术问答
                if (mCurrentMenuItem!=null && mCurrentMenuItem.getItemId()==R.id.menu_technology_question_answer) break;

                break;

            case R.id.menu_theme: // 更改主题
                if (mCurrentMenuItem!=null && mCurrentMenuItem.getItemId()==R.id.menu_theme) break;
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
                if (mCurrentMenuItem!=null && mCurrentMenuItem.getItemId()==R.id.menu_setting) break;

                break;
            /*case R.id.menu_donate: // 捐助我
                if (mCurrentMenuItem!=null && mCurrentMenuItem.getItemId()==R.id.menu_donate) break;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container,
                                Fragment.instantiate(this, EntryFragment.class.getName()))
                        .commit();
                break;*/

            case R.id.menu_new : // 资讯
                if (mCurrentMenuItem!=null && mCurrentMenuItem.getItemId()==R.id.menu_new) break;
                setDefaultMenuItem();
                break;
        }
        item.setChecked(true);
        mCurrentMenuItem = item;
        mDrawerLayout.closeDrawer(mDrawerNavView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_search:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case RESULT_OK:
                initLogin();
        }
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
            if (mCurrentMenuItem!=null && mCurrentMenuItem.getItemId()!=R.id.menu_new){
                setDefaultMenuItem();
                mDrawerNavView.setCheckedItem(R.id.menu_new);
                mCurrentMenuItem = mDrawerNavView.getMenu().getItem(0);
                return true;
            }
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
