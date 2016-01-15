package org.thanatos.flowgeek.presenter;

import nucleus.presenter.RxPresenter;
import rx.Subscription;
import rx.internal.util.SubscriptionList;

/**
 * Created by thanatos on 15/12/29.
 */
public class ThxPresenter<View> extends RxPresenter<View>{

    private SubscriptionList mSubscriptionListOnDropView = new SubscriptionList();

    @Override
    protected void onTakeView(View view) {
        super.onTakeView(view);
        if(mSubscriptionListOnDropView.isUnsubscribed())
            mSubscriptionListOnDropView = new SubscriptionList();
    }

    /**
     * 解除view时,这些订阅者将会解除订阅
     * @param subscription a subscription to add.
     */
    public void put(Subscription subscription) {
        mSubscriptionListOnDropView.add(subscription);
    }

    /**
     *
     * @param subscription a subscription to remove.
     */
    public void drop(Subscription subscription) {
        mSubscriptionListOnDropView.remove(subscription);
    }

    @Override
    protected void onDropView() {
        super.onDropView();
        mSubscriptionListOnDropView.unsubscribe();
    }
}
