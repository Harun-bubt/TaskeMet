package com.AppValley.TaskMet.chatNotification;

public class NotificationSender {
    public Data data;
    public String to;

    public NotificationSender(Data data, String to) {
        this.data = data;
        this.to = to;
    }

    public NotificationSender() {
    }

    public Data getData2() {
        return data;
    }

    public void setData2(Data data) {
        this.data = data;
    }
}
