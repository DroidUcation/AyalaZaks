package com.zaks.ayala.nearbydeals.bl.models;

/**
 * Created by אילה on 06-Jun-16.
 */
public class Category {

    public Category(){}

    public Category(String description, String color) {
        Description = description;
        Color = color;
    }

    String Description;
    String Color;

    public void setColor(String color) {
        Color = color;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getColor() {
        return Color;
    }

    public String getDescription() {
        return Description;
    }
}
