package org.thanatos.pay.api;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by thanatos on 16/2/1.
 */
public class ServerAPI {

    public static WeChatPayServer weChatPayServer;

    public static WeChatPayServer getWeChatPayServer(){
        if (weChatPayServer == null){
            weChatPayServer = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://api.mch.weixin.qq.com/pay")
                    .build()
                    .create(WeChatPayServer.class);
        }
        return weChatPayServer;
    }


}
