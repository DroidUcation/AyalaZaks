package com.zaks.ayala.nearbydeals.bl.models;

import android.content.ContentValues;
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
    private int supplierID;
    private String supplierName;
    private String supplierEmail;
    private String supplierPhone;
    private String address;
    private String description;
    private Category supplierCategory;
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
        return supplierCategory;
    }

    public void setCategory(Category category) {
        supplierCategory = category;
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

    public static Deal fromCursor(Cursor cursor) {
        Deal deal = new Deal();

        deal.setId(cursor.getInt(cursor.getColumnIndex(DealsContract.DealEntry.Column_ID)));
        deal.setDescription(cursor.getString(cursor.getColumnIndex(DealsContract.DealEntry.Column_Description)));
        deal.setAddress(cursor.getString(cursor.getColumnIndex(DealsContract.DealEntry.Column_Address)));
        deal.setFromDate(Utilities.getDateFromString(cursor.getString(cursor.getColumnIndex(DealsContract.DealEntry.Column_FromDate))));
        deal.setToDate(Utilities.getDateFromString(cursor.getString(cursor.getColumnIndex(DealsContract.DealEntry.Column_ToDate))));
        deal.setLatitude(cursor.getDouble(cursor.getColumnIndex(DealsContract.DealEntry.Column_Latitude)));
        deal.setLongitude(cursor.getDouble(cursor.getColumnIndex(DealsContract.DealEntry.Column_Longitude)));
        deal.setSupplierName(cursor.getString(cursor.getColumnIndex(SuppliersContract.SupplierEntry.Column_Name)));
        deal.setSupplierEmail(cursor.getString(cursor.getColumnIndex(SuppliersContract.SupplierEntry.Column_Email)));
        deal.setSupplierPhone(cursor.getString(cursor.getColumnIndex(SuppliersContract.SupplierEntry.Column_Phone)));
        Category category = new Category(
                cursor.getInt(cursor.getColumnIndex(CategoriesContract.CategoryEntry.Column_ID)),
                cursor.getString(cursor.getColumnIndex(CategoriesContract.CategoryEntry.Column_Description)),
                cursor.getString(cursor.getColumnIndex(CategoriesContract.CategoryEntry.Column_Color)));
        deal.setCategory(category);

        return deal;
    }

    public static String[] getProjectionMap() {
        return new String[]{
                DealsContract.DealEntry.Column_ID,
                DealsContract.DealEntry.Column_Description,
                DealsContract.DealEntry.Column_Address,
                DealsContract.DealEntry.Column_FromDate,
                DealsContract.DealEntry.Column_ToDate,
                DealsContract.DealEntry.Column_Latitude,
                DealsContract.DealEntry.Column_Longitude,
                SuppliersContract.SupplierEntry.Column_Name,
                SuppliersContract.SupplierEntry.Column_Email,
                SuppliersContract.SupplierEntry.Column_Phone,
                CategoriesContract.CategoryEntry.Column_Description,
                CategoriesContract.CategoryEntry.Column_Color,
                CategoriesContract.CategoryEntry.Column_ID
        };
    }

    public ContentValues getContentValues(boolean isNew) {
        ContentValues values = new ContentValues();
        values.put(DealsContract.DealEntry.Column_Description,getDescription());
        values.put(DealsContract.DealEntry.Column_Address,getAddress());
        values.put(DealsContract.DealEntry.Column_FromDate,Utilities.getDateForDB(getFromDate()));
        values.put(DealsContract.DealEntry.Column_ToDate,Utilities.getDateForDB(getToDate()));
        values.put(DealsContract.DealEntry.Column_Latitude,getLatitude());
        values.put(DealsContract.DealEntry.Column_Longitude,getLongitude());
        if(isNew) {
            values.put(DealsContract.DealEntry.Column_SupplierID, getSupplierID());
            values.put(DealsContract.DealEntry.Column_CategoryID, getCategory().getId());
        }
        return values;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }
}
