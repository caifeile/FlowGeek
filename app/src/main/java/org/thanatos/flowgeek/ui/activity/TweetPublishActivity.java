package org.thanatos.flowgeek.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.trello.rxlifecycle.ActivityEvent;

import org.thanatos.base.ui.activity.BaseHoldBackActivity;
import org.thanatos.base.utils.Utilities;
import org.thanatos.flowgeek.AppManager;
import org.thanatos.flowgeek.R;
import org.thanatos.flowgeek.UIManager;
import org.thanatos.flowgeek.bean.EmotionRules;
import org.thanatos.flowgeek.event.Events;
import org.thanatos.flowgeek.event.RxBus;
import org.thanatos.flowgeek.listener.KeyboardActionDelegation;
import org.thanatos.flowgeek.presenter.TweetPublishPresenter;
import org.thanatos.flowgeek.ui.fragment.EmotionPanelFragment;
import org.thanatos.flowgeek.utils.ImageUtils;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import nucleus.factory.RequiresPresenter;
import rx.functions.Action1;

/**
 * Created by thanatos on 2/29/16.
 */
@RequiresPresenter(TweetPublishPresenter.class)
public class TweetPublishActivity extends BaseHoldBackActivity<TweetPublishPresenter>{

    @Bind(R.id.frame_emotion_panel) FrameLayout mEmotionFrame;
    @Bind(R.id.et_input) EditText mInput;
    @Bind(R.id.iv_emotion) ImageView mEmotionBtn;
    @Bind(R.id.iv_tweet_image) ImageView mTweetImage;

    public static final int TYPE_TEXT = 1;
    public static final int TYPE_IMAGE = 2;
    public static final int TYPE_VOICE = 3;
    public static final int TYPE_PHOTOGRAPH = 4;

    public static final String TYPE_KEY = "TWEET_PUBLISH_TYPE_KEY";

    private static final int REQUEST_CODE_PICK_IMAGE = 1;

    public int mCatalog = TYPE_TEXT;

    private File mImageFile;
    private File mVoiceFile;

    private KeyboardActionDelegation mDelegation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mCatalog = getIntent().getIntExtra(TYPE_KEY, TYPE_TEXT);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_emotion_panel, Fragment.instantiate(this, EmotionPanelFragment.class.getName()))
                .commit();

        mDelegation = new KeyboardActionDelegation(this, mInput, mEmotionBtn, mEmotionFrame);

        switch (mCatalog){
            case TYPE_IMAGE:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                if (intent.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(Intent.createChooser(intent, "选择图片"), REQUEST_CODE_PICK_IMAGE);
                }
                break;
        }

        RxBus.with(this)
                .setEvent(Events.EventEnum.DELIVER_SELECT_EMOTION)
                .setEndEvent(ActivityEvent.DESTROY)
                .onNext((events -> {
                    EmotionRules emotion = events.<EmotionRules>getMessage();
                    mDelegation.onEmotionItemSelected(emotion);
                })).create();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        switch (requestCode){
            case REQUEST_CODE_PICK_IMAGE:
                Uri imageURI = data.getData();
                try {
                    Bitmap image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageURI);
                    ImageUtils.getImageAbsolutePath(this, imageURI, new Action1<String>() {
                        @Override
                        public void call(String path) {
                            mImageFile = new File(path);
                            if (!mImageFile.exists()) {
                                mImageFile = null;
                                return;
                            }
                            if (mImageFile.length() > 1024 * 1024 * 2) {
                                Toast.makeText(TweetPublishActivity.this, "请选择1M以下的图片", Toast.LENGTH_SHORT).show();
                                mImageFile = null;
                                return;
                            }
                            mTweetImage.setImageBitmap(image);
                            mTweetImage.setVisibility(View.VISIBLE);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "读取图片异常", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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
                if (!AppManager.isLogined()){
                    UIManager.jump2login(this);
                    return true;
                }
                String message = mInput.getText().toString();
                if (Utilities.isEmpty(message)){
                    Toast.makeText(this, "写点什么吧", Toast.LENGTH_SHORT).show();
                }else{
                    getPresenter().publishTweet(AppManager.LOCAL_LOGINED_USER.getUid(),
                            message, mImageFile, mVoiceFile);
                    Toast.makeText(this, "发射中...", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                if (!mDelegation.onTurnBack()) return true;
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
