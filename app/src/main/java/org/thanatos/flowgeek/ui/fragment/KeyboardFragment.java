package org.thanatos.flowgeek.ui.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.trello.rxlifecycle.FragmentEvent;

import org.thanatos.base.manager.DeviceManager;
import org.thanatos.base.ui.fragment.BaseTabNavFragment;
import org.thanatos.base.utils.Utilities;
import org.thanatos.flowgeek.AppManager;
import org.thanatos.flowgeek.R;
import org.thanatos.flowgeek.UIManager;
import org.thanatos.flowgeek.bean.Comment;
import org.thanatos.flowgeek.bean.EmotionRules;
import org.thanatos.flowgeek.event.Events;
import org.thanatos.flowgeek.event.RxBus;
import org.thanatos.flowgeek.listener.KeyboardActionDelegation;
import org.thanatos.flowgeek.utils.InputHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author thanatos
 * @create 2016-01-05
 **/
public class KeyboardFragment extends BaseTabNavFragment {

    @Bind(R.id.et_input) EditText mInput;
    @Bind(R.id.emotion_layout) LinearLayout mEmoLayout;
    @Bind(R.id.iv_emotion) ImageView mIvEmotion;

    // 回复的对象
    private Comment mReplyCmm;
    private KeyboardActionDelegation mDelegatioin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_keyboard, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mDelegatioin = new KeyboardActionDelegation(mContext, mInput, mIvEmotion, mEmoLayout);

        initSubscribers();

        mViewPager.setCurrentItem(0);

    }

    private void initSubscribers() {
        // register a listener to receive a event that mean user selected a emotion
        RxBus.with(this)
                .setEvent(Events.EventEnum.DELIVER_SELECT_EMOTION)
                .setEndEvent(FragmentEvent.DESTROY)
                .onNext((events -> {
                    EmotionRules emotion = events.<EmotionRules>getMessage();
                    mDelegatioin.onEmotionItemSelected(emotion);
                })).create();

        // 接受返回事件,如果显示表情面板,隐藏!如果显示软键盘,隐藏!如果显示回复某某某,隐藏!
        RxBus.with(this)
                .setEvent(Events.EventEnum.DELIVER_GO_BACK)
                .setEndEvent(FragmentEvent.DESTROY)
                .onNext((events -> {
                    if (mReplyCmm!=null){
                        mInput.setHint(getResources().getString(R.string.please_say_something));
                        mReplyCmm = null;
                        return;
                    }
                    if(!mDelegatioin.onTurnBack()) return;
                    RxBus.getInstance().send(Events.EventEnum.WE_HIDE_ALL, null);
                })).create();

        RxBus.with(this)
                .setEvent(Events.EventEnum.DELIVER_REPLY_SOMEONE)
                .setEndEvent(FragmentEvent.DESTROY)
                .onNext((events -> {
                    mReplyCmm = events.getMessage();
                    mInput.setHint("回复 @" + mReplyCmm.getAuthor());
                })).create();

        RxBus.with(this)
                .setEvent(Events.EventEnum.DELIVER_CLEAR_IMPUT)
                .setEndEvent(FragmentEvent.DESTROY)
                .onNext((events -> {
                    mInput.setHint(getResources().getString(R.string.please_say_something));
                    mInput.setText(null);
                })).create();
    }

    @Override
    public View setupTabItemView(String title) {

        ImageView view = new ImageView(mContext);
        view.setImageResource(R.mipmap.icon_emotion_color);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT
        );
        params.weight = 1;
        view.setLayoutParams(params);

        return view;
    }

    @Override
    public void onSetupTabs() {
        addTab(getResources().getString(R.string.emotion_qq), EmotionPanelFragment.class);
    }

    /**
     * 总在前面添加元素,跟默认的基类做法相悖,所以我们复写它
     * @param title
     * @param fragment
     */
    @Override
    public void addTab(String title, Class<? extends Fragment> fragment) {
        mTabs.add(0, new ViewPageInfo(title, Fragment.instantiate(getActivity(), fragment.getName())));
        View view = setupTabItemView(title);
        mNavLayout.addView(view, 0);
        mTabs.get(0).view = view;
    }

    /**
     * 删除表情或字符
     */
    @OnClick(R.id.iv_backspace)
    public void removeEmotion() {
        InputHelper.backspace(mInput);
    }

    /**
     * 发送信息
     */
    @OnClick(R.id.iv_send)
    public void sendComment() {
        // 是不是欠抽啊?
        if (Utilities.isEmpty(mInput.getText().toString())){
            Toast.makeText(mContext, "别闹,写点东西再发╭∩╮（︶︿︶）╭∩╮", Toast.LENGTH_SHORT).show();
            return;
        }
        // 登录有没有啊?
        if (AppManager.LOCAL_LOGINED_USER == null){
            UIManager.jump2login(mContext);
            return;
        }
        // 封装实体, 发送消息给相应的presenter
        Comment comment = new Comment();
        if (mReplyCmm==null){
            comment.setId(-1L);
        }else{
            comment.setId(mReplyCmm.getId());
            comment.setAuthorId(mReplyCmm.getAuthorId());
        }
        comment.setContent(mInput.getText().toString());
        Events<Comment> events = Events.just(comment);
        events.what = Events.EventEnum.DELIVER_SEND_COMMENT;
        RxBus.getInstance().send(events);
    }

}
