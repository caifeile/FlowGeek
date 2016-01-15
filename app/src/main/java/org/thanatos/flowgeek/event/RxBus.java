package org.thanatos.flowgeek.event;

import rx.Observable;
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

    public void send(Events.Type type){
        Events<Object> event = new Events<>();
        event.what = type;
        send(event);
    }

    public Observable<Events<?>> toObservable() {
        return _bus;
    }

}
