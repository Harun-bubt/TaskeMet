package com.AppValley.TaskMet.TaskMetWebService.ResponseClasses;

public class FreePostLimitModel {

    int todayPostCount;
    int limit;

    public int getTodayPostCount() {
        return todayPostCount;
    }

    public void setTodayPostCount(int todayPostCount) {
        this.todayPostCount = todayPostCount;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

}
