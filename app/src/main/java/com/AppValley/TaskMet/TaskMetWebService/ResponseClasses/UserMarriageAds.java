package com.AppValley.TaskMet.TaskMetWebService.ResponseClasses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserMarriageAds {

    @SerializedName("data")
    ArrayList<MarriageAdModel> marriageAds = new ArrayList<>();

    String next_page_url;

    public ArrayList<MarriageAdModel> getMarriageAds() {
        return marriageAds;
    }

    public void setMarriageAds(ArrayList<MarriageAdModel> marriageAds) {
        this.marriageAds = marriageAds;
    }

    public String getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
        this.next_page_url = next_page_url;
    }

}
