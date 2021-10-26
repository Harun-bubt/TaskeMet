package com.AppValley.TaskMet.TaskMetWebService.ResponseClasses;

public class PropertyAdModel {

    int id;
    String title;
    String mainCategory;
    String cost;
    String area;
    String propertyPostType;
    String propertyType;
    String areaType;
    String expireDate;
    String currentDate;
    int isPremiumPost;
    String link;
    String key;

    public PropertyAdModel(int id, String title, String mainCategory, String cost, String area, String propertyPostType, String propertyType, String areaType, String expireDate, String currentDate, int isPremiumPost, String link, String key) {
        this.id = id;
        this.title = title;
        this.mainCategory = mainCategory;
        this.cost = cost;
        this.area = area;
        this.propertyPostType = propertyPostType;
        this.propertyType = propertyType;
        this.areaType = areaType;
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

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPropertyPostType() {
        return propertyPostType;
    }

    public void setPropertyPostType(String propertyPostType) {
        this.propertyPostType = propertyPostType;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
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
