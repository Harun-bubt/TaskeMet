package com.AppValley.TaskMet.TaskMetWebService.ResponseClasses;

import com.google.gson.annotations.SerializedName;

public class MyWallet {

    @SerializedName("RC")
    int rc_balance;

    @SerializedName("GC")
    int gc_balance;

    @SerializedName("account_type")
    String accountType;

    public int getRc_balance() {
        return rc_balance;
    }

    public void setRc_balance(int rc_balance) {
        this.rc_balance = rc_balance;
    }

    public int getGc_balance() {
        return gc_balance;
    }

    public void setGc_balance(int gc_balance) {
        this.gc_balance = gc_balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
