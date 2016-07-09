package com.zaks.ayala.nearbydeals.bl.models;

import java.io.Serializable;

/**
 * Created by אילה on 06-Jun-16.
 */
public class Category implements Serializable {

    public Category(){}

    public Category(int id ,String description, String color) {
        ID=id;
        Description = description;
        Color = color;
    }
    private int ID;
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

    public int getId() {
        return ID;
    }
    @Override
    public String toString() {
        return this.Description;            // What to display in the Spinner list.
    }
    public void setId(int id) {
        this.ID = id;
    }
}
