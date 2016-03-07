package org.thanatos.flowgeek.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.KeyEvent;

import com.trello.rxlifecycle.ActivityEvent;

import org.thanatos.base.ui.activity.BaseHoldBackActivity;
import org.thanatos.flowgeek.R;
import org.thanatos.flowgeek.event.Events;
import org.thanatos.flowgeek.event.RxBus;
import org.thanatos.flowgeek.ui.fragment.CmmFragment;
import org.thanatos.flowgeek.ui.fragment.KeyboardFragment;

import butterknife.ButterKnife;

/**
 *  评论列表
 */
public class CmmActivity extends BaseHoldBackActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, Fragment.instantiate(this, CmmFragment.class.getName()))
                .replace(R.id.frame_keyboard, Fragment.instantiate(this, KeyboardFragment.class.getName()))
                .commit();

        ViewCompat.setElevation(mToolbar, 7);

        RxBus.getInstance().toObservable()
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .filter(events -> events.what == Events.EventEnum.WE_HIDE_ALL)
                .subscribe(events -> {
                    finish();
                });

    }

    @Override
    protected String onSetTitle() {
        return "评论";
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_cmm;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                RxBus.getInstance().send(Events.EventEnum.DELIVER_GO_BACK, null);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
