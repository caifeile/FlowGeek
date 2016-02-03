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

import com.trello.rxlifecycle.FragmentEvent;

import org.thanatos.base.manager.DeviceManager;
import org.thanatos.base.ui.fragment.BaseTabNavFragment;
import org.thanatos.flowgeek.R;
import org.thanatos.flowgeek.bean.EmotionRules;
import org.thanatos.flowgeek.event.Events;
import org.thanatos.flowgeek.event.RxBus;
import org.thanatos.base.utils.Utilities;
import org.thanatos.flowgeek.utils.InputHelper;
import org.thanatos.flowgeek.widget.EmotionPickerEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author thanatos
 * @create 2016-01-05
 **/
public class KeyboardFragment extends BaseTabNavFragment implements EmotionPickerEditText.DrawableRightListener {

    @Bind(R.id.et_input) EditText mInput;
    @Bind(R.id.emotion_layout) LinearLayout mEmoLayout;
    @Bind(R.id.iv_emotion) ImageView mIvEmotion;

    private Drawable mEmotionUnselected;
    private Drawable mEmotionSelected;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_keyboard, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

//        mInput.setOnDrawableRightListener(this);

        // register a listener to receive a event that mean user selected a emotion
        RxBus.getInstance().toObservable()
                .compose(bindUntilEvent(FragmentEvent.DESTROY))
                .filter(events -> events.what == Events.Type.DELIVER_SELECT_EMOTION)
                .subscribe(events -> {
                    EmotionRules emotion = events.<EmotionRules>getMessage();
                    if (mInput == null || emotion == null) {
                        return;
                    }
                    int start = mInput.getSelectionStart();
                    int end = mInput.getSelectionEnd();
                    if (start == end) {
                        // 没有多选时，直接在当前光标处添加
                        mInput.append(InputHelper.display(mInput.getResources(), emotion));
                    } else {
                        // 将已选中的部分替换为表情(当长按文字时会多选刷中很多文字)
                        Spannable str = InputHelper.display(mInput.getResources(), emotion);
                        mInput.getText().replace(Math.min(start, end), Math.max(start, end), str, 0, str.length());
                    }
                });
        mViewPager.setCurrentItem(0);

        mEmotionSelected = getResources().getDrawable(R.mipmap.icon_emotion_selected);
        mEmotionUnselected = getResources().getDrawable(R.mipmap.icon_emotion);
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

        /*TextView view = new TextView(getContext());
        view.setGravity(Gravity.CENTER);
        view.setBackgroundResource(R.drawable.selector_emotion_item);
        view.setTextColor(getResources().getColor(R.color.emotion_item));
        view.setPadding(
                UIHelper.dip2px(mContext, 5f), UIHelper.dip2px(mContext, 5f),
                UIHelper.dip2px(mContext, 5f), UIHelper.dip2px(mContext, 5f)
        );
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT
        );
        params.weight = 1;
        view.setLayoutParams(params);
        view.setText(title);*/
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
    public void showEmotionPanel(){
        if (mEmoLayout.getVisibility() == View.VISIBLE){
            mIvEmotion.setImageDrawable(mEmotionUnselected);
            mEmoLayout.setVisibility(View.GONE);
        }else {
            mIvEmotion.setImageDrawable(mEmotionSelected);
            mEmoLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 删除表情或字符
     */
    @OnClick(R.id.iv_backspace)
    public void rmEmo() {
        InputHelper.backspace(mInput);
    }

    /**
     * 发送信息
     */
    @OnClick(R.id.iv_send)
    public void send() {
        // send a event to the presenter of CmmActivity
        if (Utilities.isEmpty(mInput.getText().toString())) return;
        Events<String> events = new Events<>();
        events.what = Events.Type.DELIVER_SEND_CMM;
        events.message = mInput.getText().toString();
        RxBus.getInstance().send(events);
    }

    /**
     * 显示软件盘的时候隐藏表情
     */
    @OnClick(R.id.et_input)
    public void hideEmoLayout() {
        if (isShowingEmoLayout()) mEmoLayout.setVisibility(View.GONE);
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftKeyboard() {
        DeviceManager.getSoftInputManager(getContext()).hideSoftInputFromWindow(mInput.getWindowToken(), 0);
    }

    /**
     * 显示软键盘
     */
    public void showSoftKeyboard() {
        mInput.requestFocus();
        DeviceManager.getSoftInputManager(getContext()).showSoftInput(mInput, InputMethodManager.SHOW_FORCED);
    }

    @Override
    public void onDrawableRightClick() {
        if (isShowingEmoLayout()) {
            mEmoLayout.setVisibility(View.GONE);
            showSoftKeyboard();
        } else {
            hideSoftKeyboard();
            new Handler().postDelayed(() -> mEmoLayout.setVisibility(View.VISIBLE), 500);
        }
    }
}
