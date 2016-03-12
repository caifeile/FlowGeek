package org.thanatos.flowgeek.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apmem.tools.layouts.FlowLayout;
import org.thanatos.base.model.SharePreferenceManager;
import org.thanatos.base.ui.activity.BaseHoldBackActivity;
import org.thanatos.base.utils.Utilities;
import org.thanatos.flowgeek.AppManager;
import org.thanatos.flowgeek.R;
import org.thanatos.flowgeek.bean.User;
import org.thanatos.flowgeek.presenter.UserHomePresenter;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import nucleus.factory.RequiresPresenter;

/**
 * Created by thanatos on 16/2/19.
 */
@RequiresPresenter(UserHomePresenter.class)
public class UserHomeActivity extends BaseHoldBackActivity<UserHomePresenter>{

    @Bind(R.id.iv_portrait) CircleImageView mPortrait;
    @Bind(R.id.tv_person_name) TextView mUserName;
    @Bind(R.id.tv_join_time) TextView mJoinTime;
    @Bind(R.id.tv_coordinate) TextView mCoordinate;
    @Bind(R.id.tv_integral) TextView mIntegral;
    @Bind(R.id.tv_collection) TextView mCollection;
    @Bind(R.id.tv_focus) TextView mFocus;
    @Bind(R.id.tv_fans) TextView mFans;
    @Bind(R.id.layout_platforms) FlowLayout mLayoutPlatforms;
    @Bind(R.id.layout_expertise) FlowLayout mLayoutExpertise;

    public static final String BUNDLE_USER_KEY = "BUNDLE_USER_KEY";

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        user = (User) getIntent().getSerializableExtra(BUNDLE_USER_KEY);
        if (user == null || user.getId() == null) finish();
        initUser();
    }

    @SuppressWarnings("all")
    private void initUser() {
        // user's portrait
        Picasso.with(this).load(user.getPortrait()).into(mPortrait);

        // user's nick
        mUserName.setText(user.getName());

        // user's join time
        String joinTime = "0000-00-00";
        if (!Utilities.isEmpty(user.getJoinTime())){
            joinTime = user.getJoinTime().split(" ")[0];
        }
        mJoinTime.setText("加入时间: " + joinTime);

        // user's location
        mCoordinate.setText(user.getFrom() == null ? "-- --" : user.getFrom());

        // user's statistic
        mIntegral.setText(getString(R.string.integral) + "\n" + user.getScore());
        mCollection.setText(getString(R.string.collection) + "\n" + user.getFavoriteCount());
        mFocus.setText(getString(R.string.follow) + "\n" + user.getFollowers());
        mFans.setText(getString(R.string.fans) + "\n" + user.getFans());

        // user's develop platform
        String platform = user.getDevPlatform();
        if (!Utilities.isEmpty(platform)){
            String[] plats = platform.split(",");
            for (String item : plats){
                TextView view = (TextView) LayoutInflater.from(this)
                        .inflate(R.layout.view_plarform_item, mLayoutPlatforms, false);
                view.setText(item);
                mLayoutPlatforms.addView(view);
            }

        }

        // user's expertise
        String expertise = user.getExpertise();
        if (!Utilities.isEmpty(expertise)){
            String[] plats = expertise.split(",");
            for (String item : plats){
                TextView view = (TextView) LayoutInflater.from(this)
                        .inflate(R.layout.view_plarform_item, mLayoutExpertise, false);
                view.setText(item);
                mLayoutExpertise.addView(view);
            }

        }

    }

    public void onRequestFinished(User user){
        this.user = user;
        if (isLocalUser()){
            AppManager.LOCAL_LOGINED_USER = user;
            SharedPreferences.Editor editor = SharePreferenceManager.getLocalUser(this).edit();
            editor.putString(SharePreferenceManager.LocalUser.KEY_GENDER, user.getGender());
            editor.putInt(SharePreferenceManager.LocalUser.KEY_SKILL_SCORE, user.getScore());
            editor.putString(SharePreferenceManager.LocalUser.KEY_NICK, user.getName());
            editor.putString(SharePreferenceManager.LocalUser.KEY_PORTRAIT, user.getPortrait());
            editor.apply();
        }
        initUser();
    }

    public void onRequestFailed() {
        Toast.makeText(this, "加载失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String onSetTitle() {
        return "";
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_user_home;
    }

    private boolean isLocalUser(){
        return AppManager.isLogined()
                && AppManager.LOCAL_LOGINED_USER.getUid() == user.getUid();
    }

    public long getUserIdentify(){
        return user.getUid();
    }

}
