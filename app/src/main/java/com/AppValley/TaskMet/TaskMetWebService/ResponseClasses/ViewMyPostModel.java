package com.AppValley.TaskMet.TaskMetWebService.ResponseClasses;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class ViewMyPostModel {

    //Common parameters for all categories

    int id;
    String key;
    String title;
    String mainCategory;
    int isShowLocation;
    int isShowNumber;
    int isPremiumPost;
    int isApproved;
    int isRejected;
    int isClosed;
    int likes;
    int views;
    int isWaiting;
    int isComplete;
    String currentDate;
    String expireDate;
    double latitude;
    double longitude;


    //Data class of view post
    @SerializedName("data")
    PostDataModel postDataModel;


    //images
    @SerializedName("images")
    List<PostImagesModel> postImages = new ArrayList<>();

    //Data class of post publisher
    @SerializedName("customer")
    PostOwnerModel postOwnerModel;

    //------------------------------------ Getter & Setters ----------------------------------------

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public int isShowLocation() {
        return isShowLocation;
    }

    public void setShowLocation(int showLocation) {
        isShowLocation = showLocation;
    }

    public int isShowNumber() {
        return isShowNumber;
    }

    public void setShowNumber(int showNumber) {
        isShowNumber = showNumber;
    }

    public int isPremiumPost() {
        return isPremiumPost;
    }

    public void setPremiumPost(int premiumPost) {
        isPremiumPost = premiumPost;
    }

    public int isApproved() {
        return isApproved;
    }

    public void setApproved(int approved) {
        isApproved = approved;
    }

    public int isRejected() {
        return isRejected;
    }

    public void setRejected(int rejected) {
        isRejected = rejected;
    }

    public int isClosed() {
        return isClosed;
    }

    public void setClosed(int closed) {
        isClosed = closed;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getViews() {
        return views;
    }

    public int getIsShowLocation() {
        return isShowLocation;
    }

    public void setIsShowLocation(int isShowLocation) {
        this.isShowLocation = isShowLocation;
    }

    public int getIsShowNumber() {
        return isShowNumber;
    }

    public void setIsShowNumber(int isShowNumber) {
        this.isShowNumber = isShowNumber;
    }

    public int getIsPremiumPost() {
        return isPremiumPost;
    }

    public void setIsPremiumPost(int isPremiumPost) {
        this.isPremiumPost = isPremiumPost;
    }

    public int getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(int isApproved) {
        this.isApproved = isApproved;
    }

    public int getIsRejected() {
        return isRejected;
    }

    public void setIsRejected(int isRejected) {
        this.isRejected = isRejected;
    }

    public int getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(int isClosed) {
        this.isClosed = isClosed;
    }

    public int getIsWaiting() {
        return isWaiting;
    }

    public void setIsWaiting(int isWaiting) {
        this.isWaiting = isWaiting;
    }

    public int getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(int isComplete) {
        this.isComplete = isComplete;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int isWaiting() {
        return isWaiting;
    }

    public void setWaiting(int waiting) {
        isWaiting = waiting;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PostDataModel getPostDataModel() {
        return postDataModel;
    }

    public void setPostDataModel(PostDataModel postDataModel) {
        this.postDataModel = postDataModel;
    }

    public List<PostImagesModel> getPostImages() {
        return postImages;
    }

    public void setPostImages(List<PostImagesModel> postImages) {
        this.postImages = postImages;
    }

    public PostOwnerModel getPostOwnerModel() {
        return postOwnerModel;
    }

    public void setPostOwnerModel(PostOwnerModel postOwnerModel) {
        this.postOwnerModel = postOwnerModel;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
