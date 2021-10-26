package com.AppValley.TaskMet.TaskMetWebService.ResponseClasses;

public class CommonAdModel {

    int id;
    String title;
    String mainCategory;
    String expireDate;
    String currentDate;
    int isPremiumPost;
    String link;
    String key;
    String price;
    String cost;

    public CommonAdModel(int id, String title, String mainCategory, String cost, String expireDate, String currentDate, int isPremiumPost, String link, String key) {
        this.id = id;
        this.title = title;
        this.mainCategory = mainCategory;
        this.cost = cost;
        this.expireDate = expireDate;
        this.currentDate = currentDate;
        this.isPremiumPost = isPremiumPost;
        this.link = link;
        this.key = key;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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
