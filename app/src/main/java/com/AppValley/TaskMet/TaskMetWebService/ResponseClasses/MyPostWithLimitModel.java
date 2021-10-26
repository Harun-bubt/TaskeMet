package com.AppValley.TaskMet.TaskMetWebService.ResponseClasses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MyPostWithLimitModel {

    @SerializedName("limit")
    NormalPostLimit postLimitModel;

    @SerializedName("premiumLimit")
    PremiumPostLimit premiumPostLimit;

    @SerializedName("prices")
    PostPrices postPrices;

    @SerializedName("wallet")
    MyWallet myWallet;

    @SerializedName("posts")
    MyPostModel posts;

    public NormalPostLimit getPostLimitModel() {
        return postLimitModel;
    }

    public void setPostLimitModel(NormalPostLimit postLimitModel) {
        this.postLimitModel = postLimitModel;
    }

    public MyPostModel getPosts() {
        return posts;
    }

    public void setPosts(MyPostModel posts) {
        this.posts = posts;
    }

    public PremiumPostLimit getPremiumPostLimit() {
        return premiumPostLimit;
    }

    public void setPremiumPostLimit(PremiumPostLimit premiumPostLimit) {
        this.premiumPostLimit = premiumPostLimit;
    }

    public PostPrices getPostPrices() {
        return postPrices;
    }

    public void setPostPrices(PostPrices postPrices) {
        this.postPrices = postPrices;
    }

    public MyWallet getMyWallet() {
        return myWallet;
    }

    public void setMyWallet(MyWallet myWallet) {
        this.myWallet = myWallet;
    }
}
