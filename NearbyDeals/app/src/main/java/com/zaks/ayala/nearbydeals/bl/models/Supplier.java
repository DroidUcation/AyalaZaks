package com.zaks.ayala.nearbydeals.bl.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.zaks.ayala.nearbydeals.data.datacontracts.CategoriesContract;
import com.zaks.ayala.nearbydeals.data.datacontracts.DealsContract;
import com.zaks.ayala.nearbydeals.data.datacontracts.SuppliersContract;

import java.io.Serializable;

/**
 * Created by אילה on 05-Jul-16.
 */
public class Supplier implements Serializable {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private Category Category;
    private double latitude;
    private double longitude;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public com.zaks.ayala.nearbydeals.bl.models.Category getCategory() {
        return Category;
    }

    public void setCategory(com.zaks.ayala.nearbydeals.bl.models.Category category) {
        Category = category;
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

    public static Supplier fromCursor(Cursor cursor) {
        Supplier supplier = new Supplier();
        if (cursor.getColumnIndex(SuppliersContract.SupplierEntry.Column_ID) != -1)
            supplier.setId(cursor.getInt(cursor.getColumnIndex(SuppliersContract.SupplierEntry.Column_ID)));
        if (cursor.getColumnIndex(SuppliersContract.SupplierEntry.Column_Name) != -1)
            supplier.setName(cursor.getString(cursor.getColumnIndex(SuppliersContract.SupplierEntry.Column_Name)));
        if (cursor.getColumnIndex(SuppliersContract.SupplierEntry.Column_Email) != -1)
            supplier.setEmail(cursor.getString(cursor.getColumnIndex(SuppliersContract.SupplierEntry.Column_Email)));
        if (cursor.getColumnIndex(SuppliersContract.SupplierEntry.Column_Phone) != -1)
            supplier.setPhone(cursor.getString(cursor.getColumnIndex(SuppliersContract.SupplierEntry.Column_Phone)));
        if (cursor.getColumnIndex(SuppliersContract.SupplierEntry.Column_Address) != -1)
            supplier.setAddress(cursor.getString(cursor.getColumnIndex(SuppliersContract.SupplierEntry.Column_Address)));
        if (cursor.getColumnIndex(SuppliersContract.SupplierEntry.Column_Latitude) != -1)
            supplier.setLatitude(cursor.getDouble(cursor.getColumnIndex(SuppliersContract.SupplierEntry.Column_Latitude)));
        if (cursor.getColumnIndex(SuppliersContract.SupplierEntry.Column_Longitude) != -1)
            supplier.setLongitude(cursor.getDouble(cursor.getColumnIndex(SuppliersContract.SupplierEntry.Column_Longitude)));
        int catID=0;
        String catDesc="", catColor="";
        if (cursor.getColumnIndex(CategoriesContract.CategoryEntry.Column_ID) != -1)
            catID = cursor.getInt(cursor.getColumnIndex(CategoriesContract.CategoryEntry.Column_ID));
        if (cursor.getColumnIndex(CategoriesContract.CategoryEntry.Column_Description) != -1)
            catDesc = cursor.getString(cursor.getColumnIndex(CategoriesContract.CategoryEntry.Column_Description));
        if (cursor.getColumnIndex(CategoriesContract.CategoryEntry.Column_Color) != -1)
            catColor = cursor.getString(cursor.getColumnIndex(CategoriesContract.CategoryEntry.Column_Color));

        com.zaks.ayala.nearbydeals.bl.models.Category supplierCategory = new Category(catID, catDesc, catColor);

        supplier.setCategory(supplierCategory);
        return supplier;
    }

    public static String[] getProjectionMap() {
        return new String[]{
                SuppliersContract.SupplierEntry.Column_ID,
                SuppliersContract.SupplierEntry.Column_Name,
                SuppliersContract.SupplierEntry.Column_Email,
                SuppliersContract.SupplierEntry.Column_Phone,
                SuppliersContract.SupplierEntry.Column_Address,
                SuppliersContract.SupplierEntry.Column_Longitude,
                SuppliersContract.SupplierEntry.Column_Latitude,
                CategoriesContract.CategoryEntry.Column_Description,
                CategoriesContract.CategoryEntry.Column_Color,
                CategoriesContract.CategoryEntry.Column_ID
        };
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(SuppliersContract.SupplierEntry.Column_Name, getName());
        values.put(SuppliersContract.SupplierEntry.Column_Email, getEmail());
        values.put(SuppliersContract.SupplierEntry.Column_Phone, getPhone());
        values.put(SuppliersContract.SupplierEntry.Column_Address, getAddress());
        values.put(SuppliersContract.SupplierEntry.Column_Latitude, getLongitude());
        values.put(SuppliersContract.SupplierEntry.Column_Longitude, getLongitude());
        values.put(SuppliersContract.SupplierEntry.Column_CategoryID, getCategory().getId());
        return values;
    }
}
