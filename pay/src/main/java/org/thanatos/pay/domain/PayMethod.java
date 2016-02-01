package org.thanatos.pay.domain;

import org.thanatos.pay.R;

/**
 * Created by thanatos on 16/1/29.
 */
public enum PayMethod {

    WECHAT(R.mipmap.icon_logo_wechat, R.string.wechat_pay, R.string.wechat_pay_subscribe);

    private int logo;
    private int payName;
    private int payDescribe;

    PayMethod(int logo, int payName, int payDescribe) {
        this.logo = logo;
        this.payName = payName;
        this.payDescribe = payDescribe;
    }


    public int getLogo() {
        return logo;
    }

    public int getPayName() {
        return payName;
    }

    public int getPayDescribe() {
        return payDescribe;
    }
}
