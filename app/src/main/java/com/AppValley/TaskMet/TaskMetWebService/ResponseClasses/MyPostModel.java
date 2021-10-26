package com.AppValley.TaskMet.TaskMetWebService.ResponseClasses;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class MyPostModel {

    @SerializedName("data")
    List<MyPostData> postData = new ArrayList<>();

    String next_page_url;

    public List<MyPostData> getPostData() {
        return postData;
    }

    public void setPostData(List<MyPostData> postData) {
        this.postData = postData;
    }

    public String getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
        this.next_page_url = next_page_url;
    }
}