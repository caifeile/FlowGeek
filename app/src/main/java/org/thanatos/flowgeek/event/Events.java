package org.thanatos.flowgeek.event;

/**
 * @author thanatos
 * @create 2016-01-05
 **/
public class Events<T> {

    public enum Type{
        DELIVER_ARTICLE_ID, DELIVER_SEND_CMM, DELIVER_SELECT_EMOTION, GET_ARTICLE_CATALOG, DELIVER_ARTICLE_ID_FROM_LIST, DELIVER_ARTICLE_CATALOG_FROM_LIST, GET_TWEET_ID, GET_TWEET, DELIVER_TWEET, GET_ARTICLE_ID
    }

    public Type what;
    public T message;

    public static <O> Events<O> just(O t){
        Events<O> events = new Events<>();
        events.message = t;
        return events;
    }

    public <T> T getMessage() {
        return (T) message;
    }

}
