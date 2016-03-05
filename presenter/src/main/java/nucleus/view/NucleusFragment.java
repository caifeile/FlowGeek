package nucleus.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.trello.rxlifecycle.components.support.RxFragment;

import nucleus.factory.PresenterFactory;
import nucleus.factory.ReflectionPresenterFactory;
import nucleus.presenter.Presenter;

/**
 * This view is an example of how a view should control it's presenter.
 * You can inherit from this class or copy/paste this class's code to
 * create your own view implementation.
 *
 * @param <P> a type of presenter to return with {@link #getPresenter}.
 */
public abstract class NucleusFragment<P extends Presenter> extends RxFragment implements ViewWithPresenter<P> {

    private static final String PRESENTER_STATE_KEY = "fragment_presenter_state";

    // 在ViewPager中,虽然Fragment被destroy了,再是实例似乎并没有被销毁,重新重新创建的时候并不会初始化这里的参数,而是
    // 仍然保留成员变量的值
    private PresenterLifecycleDelegate<P> presenterDelegate =
        new PresenterLifecycleDelegate<>(ReflectionPresenterFactory.<P>fromViewClass(getClass()));

    /**
     * Returns a current presenter factory.
     */
    public PresenterFactory<P> getPresenterFactory() {
        return presenterDelegate.getPresenterFactory();
    }

    /**
     * Sets a presenter factory.
     * Call this method before onCreate/onFinishInflate to override default {@link ReflectionPresenterFactory} presenter factory.
     * Use this method for presenter dependency injection.
     */
    @Override
    public void setPresenterFactory(PresenterFactory<P> presenterFactory) {
        presenterDelegate.setPresenterFactory(presenterFactory);
    }

    /**
     * Returns a current attached presenter.
     * This method is guaranteed to return a non-null value between
     * onResume/onPause and onAttachedToWindow/onDetachedFromWindow calls
     * if the presenter factory returns a non-null value.
     *
     * @return a currently attached presenter or null.
     */
    public P getPresenter() {
        return presenterDelegate.getPresenter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && getPresenter()==null)
            presenterDelegate.onRestoreInstanceState(savedInstanceState.getBundle(PRESENTER_STATE_KEY));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(PRESENTER_STATE_KEY, presenterDelegate.onSaveInstanceState());
    }

    @Override
    public void onResume() {
        super.onResume();
        presenterDelegate.onResume(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenterDelegate.onDestroy(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenterDelegate.onDestroy(getActivity().isFinishing());
    }
}
