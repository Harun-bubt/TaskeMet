package com.AppValley.TaskMet.TaskMetWebService.ResponseClasses;

public class PremiumPostLimit {

    int todayPremiumPostCount;
    int limit;

    public int getTodayPremiumPostCount() {
        return todayPremiumPostCount;
    }

    public void setTodayPremiumPostCount(int todayPremiumPostCount) {
        this.todayPremiumPostCount = todayPremiumPostCount;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
