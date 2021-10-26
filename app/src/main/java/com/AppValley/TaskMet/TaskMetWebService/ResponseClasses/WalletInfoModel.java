package com.AppValley.TaskMet.TaskMetWebService.ResponseClasses;

import com.google.gson.annotations.SerializedName;

public class WalletInfoModel {

    @SerializedName("limit")
    NormalPostLimit postLimitModel;

    @SerializedName("premiumLimit")
    PremiumPostLimit premiumPostLimit;

    @SerializedName("wallet")
    MyWallet myWallet;

    public NormalPostLimit getPostLimitModel() {
        return postLimitModel;
    }

    public PremiumPostLimit getPremiumPostLimit() {
        return premiumPostLimit;
    }

    public MyWallet getMyWallet() {
        return myWallet;
    }
}
