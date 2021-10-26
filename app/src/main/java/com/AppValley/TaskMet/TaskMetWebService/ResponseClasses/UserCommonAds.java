package com.AppValley.TaskMet.TaskMetWebService.ResponseClasses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserCommonAds {

    @SerializedName("data")
    ArrayList<CommonAdModel> commonAds = new ArrayList<>();

    String next_page_url;

    public ArrayList<CommonAdModel> getCommonAds() {
        return commonAds;
    }

    public void setCommonAds(ArrayList<CommonAdModel> commonAds) {
        this.commonAds = commonAds;
    }

    public String getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
        this.next_page_url = next_page_url;
    }

}
