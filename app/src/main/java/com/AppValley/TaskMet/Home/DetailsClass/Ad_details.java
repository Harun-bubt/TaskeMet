package com.AppValley.TaskMet.Home.DetailsClass;

public class Ad_details {
    String image;
    String title;
    String price;
    String mainCategory;
    String expireDate;
    int isPremiumPost;

    public Ad_details(String image, String title, String price, String mainCategory, String expireDate,int isPremiumPost) {
        this.image = image;
        this.title = title;
        this.price = price;
        this.mainCategory = mainCategory;
        this.expireDate = expireDate;
        this.isPremiumPost = isPremiumPost;
    }

    public int getIsPremiumPost() {
        return isPremiumPost;
    }

    public void setIsPremiumPost(int isPremiumPost) {
        this.isPremiumPost = isPremiumPost;
    }

    public String  getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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
}
