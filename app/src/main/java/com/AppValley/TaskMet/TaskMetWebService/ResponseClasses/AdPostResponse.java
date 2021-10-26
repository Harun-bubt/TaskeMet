package com.AppValley.TaskMet.TaskMetWebService.ResponseClasses;

public class AdPostResponse {

    String status;
    int id;
    String title;
    String expireDate;
    String mainCategory;
    boolean isWaiting;
    String link;
    String key;
    PostNowPrice postNow;

    public String getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public String getMainCategory() {
        return mainCategory;
    }

    public boolean isWaiting() {
        return isWaiting;
    }

    public String getLink() {
        return link;
    }

    public String getKey() {
        return key;
    }

    public PostNowPrice getPostNow() {
        return postNow;
    }
}