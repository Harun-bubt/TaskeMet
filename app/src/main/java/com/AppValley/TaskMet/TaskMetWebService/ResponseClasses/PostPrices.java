package com.AppValley.TaskMet.TaskMetWebService.ResponseClasses;

import com.google.gson.annotations.SerializedName;

public class PostPrices {

    @SerializedName("makePostPremium")
    PremiumPostPrice premiumPostPrice;

    @SerializedName("postNow")
    PostNowPrice postNowPrice;

    public PremiumPostPrice getPremiumPostPrice() {
        return premiumPostPrice;
    }

    public void setPremiumPostPrice(PremiumPostPrice premiumPostPrice) {
        this.premiumPostPrice = premiumPostPrice;
    }

    public PostNowPrice getPostNowPrice() {
        return postNowPrice;
    }

    public void setPostNowPrice(PostNowPrice postNowPrice) {
        this.postNowPrice = postNowPrice;
    }

}
