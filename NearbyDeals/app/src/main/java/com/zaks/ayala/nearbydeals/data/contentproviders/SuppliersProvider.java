package com.zaks.ayala.nearbydeals.data.contentproviders;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.zaks.ayala.nearbydeals.data.dal.DBHelper;
import com.zaks.ayala.nearbydeals.data.datacontracts.CategoriesContract;
import com.zaks.ayala.nearbydeals.data.datacontracts.DealsContract;
import com.zaks.ayala.nearbydeals.data.datacontracts.SuppliersContract;

import java.util.HashMap;

/**
 * Created by אילה on 05-Jul-16.
 */
public class SuppliersProvider extends ContentProvider {
    static final String PROVIDER_NAME = "com.zaks.ayala.provider.suppliers";
    static final String URL = "content://" + PROVIDER_NAME + "/items";
    static final Uri CONTENT_URI = Uri.parse(URL);
    private static HashMap<String, String> SuppliersMap;
    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "items", 1);
        uriMatcher.addURI(PROVIDER_NAME, "items/#", 2);
    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DBHelper dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        if (database == null)
            return false;
        else
            return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        StringBuilder sb = new StringBuilder();
        sb.append(SuppliersContract.SupplierEntry.TableName);
        sb.append(" LEFT JOIN ");
        sb.append(CategoriesContract.CategoryEntry.TableName);
        sb.append(" ON (");
        sb.append(SuppliersContract.SupplierEntry.addPrefix(SuppliersContract.SupplierEntry.Column_CategoryID));
        sb.append(" = ");
        sb.append(CategoriesContract.CategoryEntry.addPrefix(CategoriesContract.CategoryEntry.Column_ID));
        sb.append(")");

        queryBuilder.setTables(sb.toString());
        switch (uriMatcher.match(uri)) {
            case 1:
                queryBuilder.setProjectionMap(SuppliersMap);
                break;
            case 2:
                queryBuilder.appendWhere(SuppliersContract.SupplierEntry.Column_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (sortOrder == null || sortOrder == "") {
            sortOrder = SuppliersContract.SupplierEntry.Column_Name;
        }

        Cursor cursor = queryBuilder.query(database, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case 1:
                return "vnd.android.cursor.dir/vnd.nearbydeals.supplier";
            case 2:
                return "vnd.android.cursor.item/vnd.nearbydeals.supplier";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long row = database.insert(SuppliersContract.SupplierEntry.TableName, "", values);
        if (row > 0) {
            Uri newUri = ContentUris.withAppendedId(CONTENT_URI, row);
            getContext().getContentResolver().notifyChange(newUri, null);
            return newUri;
        }
        throw new SQLException("Fail to add a new record into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case 1:
                count = database.delete(SuppliersContract.SupplierEntry.TableName, selection, selectionArgs);
                break;
            case 2:
                count = database.delete(SuppliersContract.SupplierEntry.TableName,
                        SuppliersContract.SupplierEntry.Column_ID + " = " + uri.getLastPathSegment() +
                                (!selection.isEmpty() ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case 1:
                count = database.update(SuppliersContract.SupplierEntry.TableName, values, selection, selectionArgs);
                break;
            case 2:
                count = database.update(SuppliersContract.SupplierEntry.TableName, values,
                        SuppliersContract.SupplierEntry.Column_ID + " = " + uri.getLastPathSegment() +
                                (!selection.isEmpty() ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}


