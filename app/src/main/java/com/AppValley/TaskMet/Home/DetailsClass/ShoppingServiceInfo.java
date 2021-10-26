package com.AppValley.TaskMet.Home.DetailsClass;

public class ShoppingServiceInfo {

    int id;
    int imageIcon;
    String name;

    public ShoppingServiceInfo(int id, int imageIcon, String name) {
        this.id = id;
        this.imageIcon = imageIcon;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(int imageIcon) {
        this.imageIcon = imageIcon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
