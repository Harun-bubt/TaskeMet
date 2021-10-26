package com.AppValley.TaskMet.TaskMetWebService.ResponseClasses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UserJobAds {

    @SerializedName("data")
    ArrayList<JobAdModel> jobAds = new ArrayList<>();

    String next_page_url;


    public ArrayList<JobAdModel> getJobAds() {
        return jobAds;
    }

    public void setJobAds(ArrayList<JobAdModel> jobAds) {
        this.jobAds = jobAds;
    }

    public String getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
        this.next_page_url = next_page_url;
    }

}
