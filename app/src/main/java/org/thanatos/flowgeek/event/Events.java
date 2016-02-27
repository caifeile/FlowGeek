package org.thanatos.flowgeek.event;

/**
 * @author thanatos
 * @create 2016-01-05
 **/
public class Events<T> {

    public enum EventEnum {
        DELIVER_ARTICLE_ID, DELIVER_SELECT_EMOTION, GET_ARTICLE_CATALOG, DELIVER_ARTICLE_ID_FROM_LIST, DELIVER_ARTICLE_CATALOG_FROM_LIST, GET_TWEET_ID, GET_TWEET, DELIVER_TWEET, DELIVER_LOGIN, DELIVER_GO_BACK, WE_HIDE_ALL, DELIVER_REPLY_SOMEONE, DELIVER_ARTICLE_ID_FROM_KEYBOARD, DELIVER_ARTICLE_CATALOG_FROM_KEYBOARD, DELIVER_CLEAR_IMPUT, DELIVER_SEND_COMMENT, GET_ARTICLE_OWNER_ID, DELIVER_ARTICLE_OWNER_ID, GET_ARTICLE_ID
    }

    public EventEnum what;
    public T message;

    public static <O> Events<O> just(O t) {
        Events<O> events = new Events<>();
        events.message = t;
        return events;
    }

    public <T> T getMessage() {
        return (T) message;
    }

}
