package com.AppValley.TaskMet.TaskMetWebService.ResponseClasses;

import com.google.gson.annotations.SerializedName;

public class WalletModel {

    String id;
    String account_type;
    String RC;
    String GC;
    String freeAds;
    String freePremiumAds;

    @SerializedName("packagesGC")
    PackagesModel gcPackages;

    @SerializedName("packagesRC")
    PackagesModel rcPackages;

    @SerializedName("premiumAccount")
    PremiumAccountModel premiumAccountModel;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public String getRC() {
        return RC;
    }

    public void setRC(String RC) {
        this.RC = RC;
    }

    public String getGC() {
        return GC;
    }

    public void setGC(String GC) {
        this.GC = GC;
    }

    public String getFreeAds() {
        return freeAds;
    }

    public void setFreeAds(String freeAds) {
        this.freeAds = freeAds;
    }

    public String getFreePremiumAds() {
        return freePremiumAds;
    }

    public void setFreePremiumAds(String freePremiumAds) {
        this.freePremiumAds = freePremiumAds;
    }

    public PackagesModel getGcPackages() {
        return gcPackages;
    }

    public void setGcPackages(PackagesModel gcPackages) {
        this.gcPackages = gcPackages;
    }

    public PackagesModel getRcPackages() {
        return rcPackages;
    }

    public void setRcPackages(PackagesModel rcPackages) {
        this.rcPackages = rcPackages;
    }

    public PremiumAccountModel getPremiumAccountModel() {
        return premiumAccountModel;
    }

    public void setPremiumAccountModel(PremiumAccountModel premiumAccountModel) {
        this.premiumAccountModel = premiumAccountModel;
    }
}
