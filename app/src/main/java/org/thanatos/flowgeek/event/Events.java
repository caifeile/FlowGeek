package org.thanatos.flowgeek.event;

/**
 * @author thanatos
 * @create 2016-01-05
 **/
public class Events<T> {

    public enum Type{
        DELIVER_ARTICLE_ID, DELIVER_SEND_CMM, DELIVER_SELECT_EMOTION, GET_ARTICLE_ID
    }

    public Type what;
    public T object;

    public static <O> Events<O> just(O t){
        Events<O> events = new Events<>();
        events.object = t;
        return events;
    }

    public <T> T getObj() {
        return (T) object;
    }

}
