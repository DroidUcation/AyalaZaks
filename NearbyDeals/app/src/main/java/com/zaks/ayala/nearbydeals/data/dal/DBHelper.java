package com.zaks.ayala.nearbydeals.data.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;

import com.zaks.ayala.nearbydeals.common.Utilities;
import com.zaks.ayala.nearbydeals.data.datacontracts.CategoriesContract;
import com.zaks.ayala.nearbydeals.data.datacontracts.DealsContract;
import com.zaks.ayala.nearbydeals.data.datacontracts.SuppliersContract;

/**
 * Created by אילה on 18-May-16.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DBName = "NearbyDeals.db";
    private static final int DBVersion = 6;

    private static final String CreateDealsTable =
            "Create Table " + DealsContract.DealEntry.TableName + " (" +
                    DealsContract.DealEntry.Column_ID + " Integer Primary Key, " +
                    DealsContract.DealEntry.Column_SupplierID + " Integer, " +
                    DealsContract.DealEntry.Column_CategoryID + " Integer, " +
                    DealsContract.DealEntry.Column_Description + " Text, " +
                    DealsContract.DealEntry.Column_Image + " Text, " +
                    DealsContract.DealEntry.Column_Address + " Text, " +
                    DealsContract.DealEntry.Column_FromDate + " Text, " +
                    DealsContract.DealEntry.Column_ToDate + " Text, " +
                    DealsContract.DealEntry.Column_Latitude + " Real, " +
                    DealsContract.DealEntry.Column_Longitude + " Real" + ")";

    private static final String DropDealsTable = "Drop Table If Exists " + DealsContract.DealEntry.TableName;

    private static final String CreateCategoryTable =
            "Create Table " + CategoriesContract.CategoryEntry.TableName + " (" +
                    CategoriesContract.CategoryEntry.Column_ID + " Integer Primary Key, " +
                    CategoriesContract.CategoryEntry.Column_Description + " Text, " +
                    CategoriesContract.CategoryEntry.Column_Color + " Text" + ")";

    private static final String DropCategoriesTable = "Drop Table If Exists " + CategoriesContract.CategoryEntry.TableName;

    private static final String CreateSuppliersTable =
            "Create Table " + SuppliersContract.SupplierEntry.TableName + " (" +
                    SuppliersContract.SupplierEntry.Column_ID + " Integer Primary Key, " +
                    SuppliersContract.SupplierEntry.Column_Name + " Text, " +
                    SuppliersContract.SupplierEntry.Column_Email + " Text, " +
                    SuppliersContract.SupplierEntry.Column_Phone + " Text, " +
                    SuppliersContract.SupplierEntry.Column_Address + " Text, " +
                    SuppliersContract.SupplierEntry.Column_CategoryID + " Integer, " +
                    SuppliersContract.SupplierEntry.Column_Latitude + " Real, " +
                    SuppliersContract.SupplierEntry.Column_Longitude + " Real" + ")";

    private static final String DropSuppliersTable = "Drop table If Exists " + SuppliersContract.SupplierEntry.TableName;
    Context mContext;

    public DBHelper(Context context) {
        super(context, DBName, null, DBVersion);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CreateDealsTable);
        db.execSQL(CreateCategoryTable);
        db.execSQL(CreateSuppliersTable);
        initCategories(db);
        initSupplier(db);
    }

    private void initSupplier(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(SuppliersContract.SupplierEntry.Column_Name, "Sumple Store");
        values.put(SuppliersContract.SupplierEntry.Column_Address, "Iben Gavirol 2 Tel Aviv Yafo");
        values.put(SuppliersContract.SupplierEntry.Column_CategoryID, 2);
        values.put(SuppliersContract.SupplierEntry.Column_Email, "ayalalax.h@gmail.com");
        values.put(SuppliersContract.SupplierEntry.Column_Phone, "050-5881171");
        Address address = Utilities.getLocation(mContext, "Iben Gavirol 2 Tel Aviv Yafo");
        if (address != null) {
            values.put(SuppliersContract.SupplierEntry.Column_Latitude, address.getLatitude());
            values.put(SuppliersContract.SupplierEntry.Column_Longitude, address.getLongitude());
        }
        db.insert(SuppliersContract.SupplierEntry.TableName, null, values);
    }

    private void initCategories(SQLiteDatabase db) {
        insertCategory(db, "Fashion", "#e9ie63");
        insertCategory(db, "Health", "#3f51b5");
        insertCategory(db, "Food", "#ff9800");
        insertCategory(db, "Sport", "#9c27b0");
        insertCategory(db, "Office", "#009688");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DropDealsTable);
        db.execSQL(DropCategoriesTable);
        db.execSQL(DropSuppliersTable);
        onCreate(db);
    }

    private void insertCategory(SQLiteDatabase db, String desc, String color) {
        ContentValues values = new ContentValues();
        values.put(CategoriesContract.CategoryEntry.Column_Description, desc);
        values.put(CategoriesContract.CategoryEntry.Column_Color, color);
        db.insert(CategoriesContract.CategoryEntry.TableName, null, values);
    }


}
