package com.AppValley.TaskMet.TaskMetWebService.ResponseClasses;

public class MarriageAdModel {

    int id;
    String title;
    String mainCategory;
    String myAge;
    String lookingFor;
    String maritialStatus;
    String expireDate;
    String currentDate;
    int isPremiumPost;
    String link;
    String key;


    public MarriageAdModel(int id, String title, String mainCategory, String myAge, String lookingFor, String maritialStatus, String expireDate, String currentDate, int isPremiumPost, String link, String key) {
        this.id = id;
        this.title = title;
        this.mainCategory = mainCategory;
        this.myAge = myAge;
        this.lookingFor = lookingFor;
        this.maritialStatus = maritialStatus;
        this.expireDate = expireDate;
        this.currentDate = currentDate;
        this.isPremiumPost = isPremiumPost;
        this.link = link;
        this.key = key;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }

    public String getMyAge() {
        return myAge;
    }

    public void setMyAge(String myAge) {
        this.myAge = myAge;
    }

    public String getLookingFor() {
        return lookingFor;
    }

    public void setLookingFor(String lookingFor) {
        this.lookingFor = lookingFor;
    }

    public String getMaritialStatus() {
        return maritialStatus;
    }

    public void setMaritialStatus(String maritialStatus) {
        this.maritialStatus = maritialStatus;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public int getIsPremiumPost() {
        return isPremiumPost;
    }

    public void setIsPremiumPost(int isPremiumPost) {
        this.isPremiumPost = isPremiumPost;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
