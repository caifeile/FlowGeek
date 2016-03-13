package org.thanatos.flowgeek.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.thanatos.component.FloatingActionsMenu;
import org.thanatos.flowgeek.R;
import org.thanatos.flowgeek.UIManager;
import org.thanatos.flowgeek.ui.activity.MainActivity;
import org.thanatos.flowgeek.ui.activity.TweetPublishActivity;

/**
 * Created by thanatos on 16/2/17.
 */
public class TabTweetFragment extends BaseTabMainFragment implements View.OnClickListener {

    private MainActivity mAttachActivity;
    private FloatingActionsMenu mFloatingActionMenu;
    private FrameLayout mFrameCover;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mAttachActivity = (MainActivity) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_tweet, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFrameCover = (FrameLayout) LayoutInflater
                .from(mContext)
                .inflate(R.layout.view_cover, mAttachActivity.getCoordinatorLayout(), false);

        mFloatingActionMenu = (FloatingActionsMenu) LayoutInflater
                .from(mContext)
                .inflate(R.layout.view_tweet_floating_action_button, mAttachActivity.getCoordinatorLayout(), false);

        mFrameCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFloatingActionMenu.toggle();

            }
        });

        mFrameCover.setVisibility(View.GONE);
        mFloatingActionMenu.setCoverView(mFrameCover);

        mFloatingActionMenu.findViewById(R.id.btn_publish_text).setOnClickListener(this);
        mFloatingActionMenu.findViewById(R.id.btn_publish_image).setOnClickListener(this);
        mFloatingActionMenu.findViewById(R.id.btn_publish_photograph).setOnClickListener(this);
        mFloatingActionMenu.findViewById(R.id.btn_publish_voice).setOnClickListener(this);

        mAttachActivity.addToCoordinatorLayout(mFrameCover);
        mAttachActivity.addToCoordinatorLayout(mFloatingActionMenu);

    }

    @Override
    public void onSetupTabs() {
        addTab("最新动弹", ListTweetFragment.class, ListTweetFragment.TWEET_TYPE_NEW);
        addTab("热门动弹", ListTweetFragment.class, ListTweetFragment.TWEET_TYPE_HOT);
        addTab("我的动弹", ListTweetFragment.class, ListTweetFragment.TWEET_TYPE_SELF);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAttachActivity.removeFormCoordinatorLayout(mFloatingActionMenu);
        mAttachActivity.removeFormCoordinatorLayout(mFrameCover);
    }

    @Override
    public void onClick(View v) {
        mFloatingActionMenu.toggle();
        switch (v.getId()){
            case R.id.btn_publish_text:
                UIManager.showTweetPublishUI(getContext(), TweetPublishActivity.TYPE_TEXT);
                break;

            case R.id.btn_publish_image:
                UIManager.showTweetPublishUI(getContext(), TweetPublishActivity.TYPE_IMAGE);
                break;

            case R.id.btn_publish_photograph:
                // TODO 拍照上传图片到动弹
                UIManager.showTweetPublishUI(getContext(), TweetPublishActivity.TYPE_PHOTOGRAPH);
                break;

            case R.id.btn_publish_voice:
                // TODO 录音上传语音到动弹
                UIManager.showTweetPublishUI(getContext(), TweetPublishActivity.TYPE_VOICE);
                break;

            case R.id.btn_scan:
                // TODO 扫一扫
                break;
        }
    }
}
