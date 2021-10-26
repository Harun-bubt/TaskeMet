package com.AppValley.TaskMet.TaskMetWebService.ResponseClasses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserShoppingAds {

    @SerializedName("data")
    ArrayList<HomeAdModel> commonAds = new ArrayList<>();

    String next_page_url;

    public ArrayList<HomeAdModel> getCommonAds() {
        return commonAds;
    }

    public void setCommonAds(ArrayList<HomeAdModel> commonAds) {
        this.commonAds = commonAds;
    }

    public String getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
        this.next_page_url = next_page_url;
    }
}
