package org.thanatos.flowgeek.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import org.thanatos.base.ui.activity.BaseHoldBackActivity;
import org.thanatos.flowgeek.R;
import org.thanatos.flowgeek.presenter.TweetPublishPresenter;
import org.thanatos.flowgeek.ui.fragment.EmotionPanelFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;

/**
 * Created by thanatos on 2/29/16.
 */
@RequiresPresenter(TweetPublishPresenter.class)
public class TweetPublishActivity extends BaseHoldBackActivity<TweetPublishPresenter>{

    @Bind(R.id.frame_emotion_panel) FrameLayout mEmotionFrame;
    @Bind(R.id.et_input) EditText mInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected String onSetTitle() {
        return getResources().getString(R.string.dan_yi_dan);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_publish_tweet;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.publish, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_publish:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.iv_emotion) void toggleEmotionPanel(){
        if (mEmotionFrame.getVisibility() == View.VISIBLE){
            mEmotionFrame.setVisibility(View.GONE);
        }else{
            mEmotionFrame.setVisibility(View.VISIBLE);
        }
    }

}
