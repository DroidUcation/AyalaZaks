package com.zaks.ayala.nearbydeals.bl.models;

import android.database.Cursor;

import com.zaks.ayala.nearbydeals.common.Utilities;
import com.zaks.ayala.nearbydeals.data.datacontracts.CategoriesContract;
import com.zaks.ayala.nearbydeals.data.datacontracts.DealsContract;
import com.zaks.ayala.nearbydeals.data.datacontracts.SuppliersContract;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by אילה on 16-May-16.
 */

public class Deal implements Serializable {

    private int id;
    private String supplierName;
    private String supplierEmail;
    private String supplierPhone;
    private String address;
    private String description;
    private Category category;
    private double latitude;
    private double longitude;
    private Date fromDate;
    private Date toDate;


    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category Category) {
        category = category;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public static Deal fromCursor(Cursor cursor) {
        Deal deal = new Deal();

        deal.setId(cursor.getInt(cursor.getColumnIndex(DealsContract.DealEntry.addPrefix(DealsContract.DealEntry.Column_ID))));
        deal.setDescription(cursor.getString(cursor.getColumnIndex(DealsContract.DealEntry.addPrefix(DealsContract.DealEntry.Column_Description))));
        deal.setAddress(cursor.getString(cursor.getColumnIndex(DealsContract.DealEntry.addPrefix(DealsContract.DealEntry.Column_Address))));
        deal.setFromDate(Utilities.getDateFromString(cursor.getString(cursor.getColumnIndex(DealsContract.DealEntry.addPrefix(DealsContract.DealEntry.Column_FromDate)))));
        deal.setToDate(Utilities.getDateFromString(cursor.getString(cursor.getColumnIndex(DealsContract.DealEntry.addPrefix(DealsContract.DealEntry.Column_ToDate)))));
        deal.setLatitude(cursor.getDouble(cursor.getColumnIndex(DealsContract.DealEntry.addPrefix(DealsContract.DealEntry.Column_Latitude))));
        deal.setLongitude(cursor.getDouble(cursor.getColumnIndex(DealsContract.DealEntry.addPrefix(DealsContract.DealEntry.Column_Longitude))));
        deal.setSupplierName(cursor.getString(cursor.getColumnIndex(SuppliersContract.SupplierEntry.addPrefix(SuppliersContract.SupplierEntry.Column_Name))));
        deal.setSupplierEmail(cursor.getString(cursor.getColumnIndex(SuppliersContract.SupplierEntry.addPrefix(SuppliersContract.SupplierEntry.Column_Email))));
        deal.setSupplierPhone(cursor.getString(cursor.getColumnIndex(SuppliersContract.SupplierEntry.addPrefix(SuppliersContract.SupplierEntry.Column_Phone))));
        Category category = new Category(cursor.getString(cursor.getColumnIndex(CategoriesContract.CategoryEntry.addPrefix(CategoriesContract.CategoryEntry.Column_Description))),
                cursor.getString(cursor.getColumnIndex(CategoriesContract.CategoryEntry.addPrefix(CategoriesContract.CategoryEntry.Column_Color))));
        deal.setCategory(category);

        return deal;
    }

    public static String[] getProjectionMap() {
        return new String[]{
                DealsContract.DealEntry.addPrefix(DealsContract.DealEntry.Column_ID),
                DealsContract.DealEntry.addPrefix(DealsContract.DealEntry.Column_Description),
                DealsContract.DealEntry.addPrefix(DealsContract.DealEntry.Column_Address),
                DealsContract.DealEntry.addPrefix(DealsContract.DealEntry.Column_FromDate),
                DealsContract.DealEntry.addPrefix(DealsContract.DealEntry.Column_ToDate),
                DealsContract.DealEntry.addPrefix(DealsContract.DealEntry.Column_Latitude),
                DealsContract.DealEntry.addPrefix(DealsContract.DealEntry.Column_Longitude),
                SuppliersContract.SupplierEntry.addPrefix(SuppliersContract.SupplierEntry.Column_Name),
                SuppliersContract.SupplierEntry.addPrefix(SuppliersContract.SupplierEntry.Column_Email),
                SuppliersContract.SupplierEntry.addPrefix(SuppliersContract.SupplierEntry.Column_Phone),
                CategoriesContract.CategoryEntry.addPrefix(CategoriesContract.CategoryEntry.Column_Description),
                CategoriesContract.CategoryEntry.addPrefix(CategoriesContract.CategoryEntry.Column_Color)
        };
    }

    public String getSupplierEmail() {
        return supplierEmail;
    }

    public void setSupplierEmail(String supplierEmail) {
        this.supplierEmail = supplierEmail;
    }

    public String getSupplierPhone() {
        return supplierPhone;
    }

    public void setSupplierPhone(String supplierPhone) {
        this.supplierPhone = supplierPhone;
    }
}
