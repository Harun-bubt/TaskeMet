package com.AppValley.TaskMet.TaskMetWebService.ResponseClasses;

public class JobAdModel {

    int id;
    String title;
    String mainCategory;
    String type;
    String jobCategory;
    String positionType;
    String salaryFrom;
    String salaryTo;
    String expireDate;
    String currentDate;
    int isPremiumPost;
    String link;
    String key;

    public JobAdModel(int id, String title, String mainCategory, String type, String jobCategory, String positionType, String salaryFrom, String salaryTo, String expireDate, String currentDate, int isPremiumPost, String link, String key) {
        this.id = id;
        this.title = title;
        this.mainCategory = mainCategory;
        this.type = type;
        this.jobCategory = jobCategory;
        this.positionType = positionType;
        this.salaryFrom = salaryFrom;
        this.salaryTo = salaryTo;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJobCategory() {
        return jobCategory;
    }

    public void setJobCategory(String jobCategory) {
        this.jobCategory = jobCategory;
    }

    public String getPositionType() {
        return positionType;
    }

    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }

    public String getSalaryFrom() {
        return salaryFrom;
    }

    public void setSalaryFrom(String salaryFrom) {
        this.salaryFrom = salaryFrom;
    }

    public String getSalaryTo() {
        return salaryTo;
    }

    public void setSalaryTo(String salaryTo) {
        this.salaryTo = salaryTo;
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
