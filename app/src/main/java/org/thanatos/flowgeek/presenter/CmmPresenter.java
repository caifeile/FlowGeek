package org.thanatos.flowgeek.presenter;

import android.os.Bundle;
import android.util.Log;

import org.thanatos.flowgeek.event.Events;
import org.thanatos.flowgeek.event.RxBus;
import org.thanatos.flowgeek.ui.fragment.CmmFragment;

/**
 * @author thanatos
 * @create 2016-01-05
 **/
public class CmmPresenter extends BaseListPresenter<CmmFragment>{

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        RxBus.getInstance().toObservable()
                .filter(events -> events.what == Events.Type.DELIVER_ARTICLE_ID)
                .subscribe((events) -> {
                    Log.d("thanatos", "CmmPresenter receive a event");
                    Log.d("thanatos", "id is " + events.<Long>getMessage());
                });
        RxBus.getInstance().send(Events.Type.GET_ARTICLE_ID);

    }


    @Override
    public void requestData(int mode, int pageNum) {

    }
}
