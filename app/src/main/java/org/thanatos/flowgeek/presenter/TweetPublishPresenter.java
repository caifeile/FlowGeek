package org.thanatos.flowgeek.presenter;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.RequestBody;

import org.thanatos.flowgeek.AppManager;
import org.thanatos.flowgeek.ServerAPI;
import org.thanatos.flowgeek.bean.RespResult;
import org.thanatos.flowgeek.ui.activity.TweetPublishActivity;

import java.io.File;

import nucleus.presenter.RxPresenter;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by thanatos on 2/29/16.
 */
public class TweetPublishPresenter extends RxPresenter<TweetPublishActivity>{


    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
    }

    public void publishTweet(long uid, String message, File image, File voice){
        RequestBody _uid = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(uid));
        RequestBody _message = RequestBody.create(MediaType.parse("multipart/form-data"), message);
        RequestBody _image = image == null
                ? null
                : RequestBody.create(MediaType.parse("multipart/form-data"), image);
        RequestBody _voice = voice == null
                ? null
                : RequestBody.create(MediaType.parse("multipart/form-data"), voice);

        Log.d("thanatosx", String.format("uid=%d, message=%s, image=%s, voice=%s", uid, message, image, voice));

        Call<RespResult> Call = ServerAPI.getOSChinaAPI().publicTweet(_uid, _message, _image, _voice);
        Call.enqueue(new Callback<RespResult>() {
            @Override
            public void onResponse(Response<RespResult> response, Retrofit retrofit) {
                RespResult result = response.body();
                if (result.getResult().getErrorCode() == 1){
                    Toast.makeText(AppManager.context, "发表成功", Toast.LENGTH_SHORT)
                            .show();
                }else{
                    Toast.makeText(AppManager.context,
                            result.getResult().getErrorMessage(), Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                Toast.makeText(AppManager.context, "发送失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
