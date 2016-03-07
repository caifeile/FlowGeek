package org.thanatos.flowgeek.event;

import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.ActivityLifecycleProvider;
import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.FragmentLifecycleProvider;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * 使用RxJava代替EventBus的方案
 * @author thanatos
 * @create 2016-01-05
 **/
public class RxBus {

    private static RxBus rxBus;
    private final Subject<Events<?>, Events<?>> _bus = new SerializedSubject<>(PublishSubject.create());

    private RxBus(){}

    public static RxBus getInstance(){
        if (rxBus == null){
            synchronized (RxBus.class){
                if (rxBus == null){
                    rxBus = new RxBus();
                }
            }
        }
        return rxBus;
    }

    public void send(Events<?> o) {
        _bus.onNext(o);
    }

    public void send(Events.EventEnum sendType, Events.EventEnum receiveType){
        Events<Object> event = new Events<>();
        event.what = sendType;
        event.message = receiveType;
        send(event);
    }

    public Observable<Events<?>> toObservable() {
        return _bus;
    }

    public static SubscriberBuilder with(FragmentLifecycleProvider provider){
        return new SubscriberBuilder(provider);
    }

    public static SubscriberBuilder with(ActivityLifecycleProvider provider){
        return new SubscriberBuilder(provider);
    }


    public static class SubscriberBuilder{

        private FragmentLifecycleProvider mFragLifecycleProvider;
        private ActivityLifecycleProvider mActLifecycleProvider;
        private FragmentEvent mFragmentEndEvent;
        private ActivityEvent mActivityEndEvent;
        private Events.EventEnum event;
        private Action1<? super Events<?>> onNext;
        private Action1<Throwable> onError;

        public SubscriberBuilder(FragmentLifecycleProvider provider) {
            this.mFragLifecycleProvider = provider;
        }

        public SubscriberBuilder(ActivityLifecycleProvider provider){
            this.mActLifecycleProvider = provider;
        }

        public SubscriberBuilder setEvent(Events.EventEnum event){
            this.event = event;
            return this;
        }

        public SubscriberBuilder setEndEvent(FragmentEvent event){
            this.mFragmentEndEvent = event;
            return this;
        }

        public SubscriberBuilder setEndEvent(ActivityEvent event){
            this.mActivityEndEvent = event;
            return this;
        }

        public SubscriberBuilder onNext(Action1<? super Events<?>> action){
            this.onNext = action;
            return this;
        }

        public SubscriberBuilder onError(Action1<Throwable> action){
            this.onError = action;
            return this;
        }

        public Observable observable(){
            if (mFragLifecycleProvider!=null){
                return RxBus.getInstance().toObservable()
                        .compose(mFragLifecycleProvider.bindUntilEvent(mFragmentEndEvent))
                        .filter(events -> events.what == event);
            }
            if (mActLifecycleProvider!=null){
                return RxBus.getInstance().toObservable()
                        .compose(mActLifecycleProvider.bindUntilEvent(mActivityEndEvent))
                        .filter(events -> events.what == event);
            }
            return null;
        }

        public void create(){
            _create();
        }

        public Subscription _create(){
            if (mFragLifecycleProvider!=null){
                return RxBus.getInstance().toObservable()
                        .compose(mFragLifecycleProvider.bindUntilEvent(mFragmentEndEvent))
                        .filter(events -> events.what == event)
                        .subscribe(onNext, onError == null ? Throwable::printStackTrace : onError);
            }
            if (mActLifecycleProvider!=null){
                return RxBus.getInstance().toObservable()
                        .compose(mActLifecycleProvider.bindUntilEvent(mActivityEndEvent))
                        .filter(events -> events.what == event)
                        .subscribe(onNext, onError == null ? Throwable::printStackTrace : onError);
            }
            return null;
        }
    }

}
