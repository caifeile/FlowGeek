package org.thanatos.flowgeek.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.RxLifecycle;

import org.thanatos.base.manager.DeviceManager;
import org.thanatos.base.ui.fragment.BaseTabNavFragment;
import org.thanatos.flowgeek.R;
import org.thanatos.flowgeek.bean.EmotionRules;
import org.thanatos.flowgeek.event.Events;
import org.thanatos.flowgeek.event.RxBus;
import org.thanatos.flowgeek.utils.UIHelper;
import org.thanatos.flowgeek.utils.Utility;
import org.thanatos.flowgeek.widget.ThxEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;

/**
 * @author thanatos
 * @create 2016-01-05
 **/
public class KeyboardFragment extends BaseTabNavFragment implements ThxEditText.DrawableRightListener {

    @Bind(R.id.et_input) ThxEditText mInput;
    @Bind(R.id.emotion_layout) LinearLayout mEmoLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_keyboard, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mInput.setOnDrawableRightListener(this);

        // register a listener to receive a event that mean user selected a emotion
        RxBus.getInstance().toObservable()
                .compose(RxLifecycle.bindFragment(Observable.just(FragmentEvent.RESUME)))
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
                        mInput.append(UIHelper.display(mInput.getResources(), emotion));
                    } else {
                        // 将已选中的部分替换为表情(当长按文字时会多选刷中很多文字)
                        Spannable str = UIHelper.display(mInput.getResources(), emotion);
                        mInput.getText().replace(Math.min(start, end), Math.max(start, end), str, 0, str.length());
                    }
                });
    }

    @Override
    public View setupTabItemView(String title) {
        TextView view = new TextView(getContext());
        view.setBackgroundResource(R.drawable.selector_emotion_item);
        view.setTextColor(getResources().getColor(R.color.emotion_item));
        view.setPadding(
                UIHelper.dip2px(mContext, 5f), UIHelper.dip2px(mContext, 5f),
                UIHelper.dip2px(mContext, 5f), UIHelper.dip2px(mContext, 5f)
        );
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.CENTER;
        params.weight = 1;
        view.setLayoutParams(params);
        view.setText(title);
        return view;
    }

    @Override
    public void onSetupTabs() {
        addTab(resources.getString(R.string.emotion_qq), EmotionPanelFragment.class);
    }

    private boolean isShowingEmoLayout() {
        return mEmoLayout.getVisibility() == View.VISIBLE;
    }

    /**
     * 删除表情或字符
     */
    @OnClick(R.id.iv_backspace)
    public void rmEmo() {
        UIHelper.backspace(mInput);
    }

    /**
     * 发送信息
     */
    @OnClick(R.id.iv_send)
    public void send() {
        // send a event to the presenter of CmmActivity
        if (Utility.isEmpty(mInput.getText().toString())) return;
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
