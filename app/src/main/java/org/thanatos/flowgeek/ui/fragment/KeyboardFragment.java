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

    private Drawable mEmotionUnselected;
    private Drawable mEmotionSelected;

    // 真奇怪,判断软键盘是否显示都好麻烦,我只能简单粗暴了
    private boolean isShowSoftInput = false;
    // 回复的对象
    private Comment mReplyCmm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_keyboard, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        initSubscribers();

        mViewPager.setCurrentItem(0);

        mEmotionSelected = getResources().getDrawable(R.mipmap.icon_emotion_selected);
        mEmotionUnselected = getResources().getDrawable(R.mipmap.icon_emotion);

        mInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    hideEmoLayout();
                }else{
                    hideSoftKeyboard();
                }
            }
        });

    }

    private void initSubscribers() {
        // register a listener to receive a event that mean user selected a emotion
        RxBus.getInstance().toObservable()
                .compose(bindUntilEvent(FragmentEvent.DESTROY))
                .filter(events -> events.what == Events.EventEnum.DELIVER_SELECT_EMOTION)
                .subscribe(events -> {
                    EmotionRules emotion = events.<EmotionRules>getMessage();

                    if (mInput == null || emotion == null) {
                        return;
                    }
                    int start = mInput.getSelectionStart();
                    int end = mInput.getSelectionEnd();
                    if (start == end) {
                        // 没有多选时，直接在当前光标处添加
                        mInput.append(InputHelper.insertEtn(mContext, emotion));
                    } else {
                        // 将已选中的部分替换为表情(当长按文字时会多选刷中很多文字)
                        Spannable str = InputHelper.insertEtn(mContext, emotion);
                        mInput.getText().replace(Math.min(start, end), Math.max(start, end), str, 0, str.length());
                    }
                });

        // 接受返回事件,如果显示表情面板,隐藏!如果显示软键盘,隐藏!如果显示回复某某某,隐藏!
        RxBus.getInstance().toObservable()
                .compose(bindUntilEvent(FragmentEvent.DESTROY))
                .filter(events -> events.what == Events.EventEnum.DELIVER_GO_BACK)
                .subscribe(events -> {
                    if (mReplyCmm!=null){
                        mInput.setHint(getResources().getString(R.string.please_say_something));
                        mReplyCmm = null;
                        return;
                    }
                    if (isShowingEmoLayout()) {
                        hideEmoLayout();
                        return;
                    }
                    if (isShowSoftInput) {
                        hideSoftKeyboard();
                        return;
                    }
                    RxBus.getInstance().send(Events.EventEnum.WE_HIDE_ALL, null);
                });

        // 点击评论,显示回复某某某
        RxBus.getInstance().toObservable()
                .compose(bindUntilEvent(FragmentEvent.DESTROY))
                .filter(events -> events.what == Events.EventEnum.DELIVER_REPLY_SOMEONE)
                .subscribe(events -> {
                    mReplyCmm = events.getMessage();
                    mInput.setHint("回复 @" + mReplyCmm.getAuthor());
                });

        // 评论成功,清空
        RxBus.getInstance().toObservable()
                .compose(bindUntilEvent(FragmentEvent.DESTROY))
                .filter(events -> events.what == Events.EventEnum.DELIVER_CLEAR_IMPUT)
                .subscribe(events -> {
                    mInput.setHint(getResources().getString(R.string.please_say_something));
                    mInput.setText(null);
                });
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

    private boolean isShowingEmoLayout() {
        return mEmoLayout.getVisibility() == View.VISIBLE;
    }

    /**
     * 显示\关闭表情面板
     */
    @OnClick(R.id.iv_emotion)
    public void switchEmotionPanel(){
        if (mEmoLayout.getVisibility() == View.VISIBLE){
            hideEmoLayout();
        }else {
            mIvEmotion.setImageDrawable(mEmotionSelected);
            hideSoftKeyboard();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mEmoLayout.setVisibility(View.VISIBLE);
                }
            }, 300);
        }
    }

    /**
     * 删除表情或字符
     */
    @OnClick(R.id.iv_backspace)
    public void removeEmo() {
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

    /**
     * 显示软件盘的时候隐藏表情
     */
    @OnClick(R.id.et_input)
    public void hideEmoLayout() {
        if (mEmoLayout.getVisibility() == View.GONE) return;
        mIvEmotion.setImageDrawable(mEmotionUnselected);
        mEmoLayout.setVisibility(View.GONE);
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftKeyboard() {
        DeviceManager.getSoftInputManager(getContext()).hideSoftInputFromWindow(mInput.getWindowToken(), 0);
        isShowSoftInput = false;
    }

    /**
     * 显示软键盘
     */
    public void showSoftKeyboard() {
        mInput.requestFocus();
        DeviceManager.getSoftInputManager(getContext()).showSoftInput(mInput, InputMethodManager.SHOW_FORCED);
        isShowSoftInput = true;
    }
}
