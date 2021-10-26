package com.AppValley.TaskMet.TaskMetWebService.ResponseClasses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserPropertyAds {

    @SerializedName("data")
    ArrayList<PropertyAdModel> propertyAds = new ArrayList<>();

    String next_page_url;

    public ArrayList<PropertyAdModel> getPropertyAds() {
        return propertyAds;
    }

    public void setPropertyAds(ArrayList<PropertyAdModel> propertyAds) {
        this.propertyAds = propertyAds;
    }

    public String getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
        this.next_page_url = next_page_url;
    }

}
