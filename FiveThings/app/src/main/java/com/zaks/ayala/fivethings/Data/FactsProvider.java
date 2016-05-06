package com.zaks.ayala.fivethings.Data;

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

import java.util.HashMap;

/**
 * Created by אילה on 05-May-16.
 */
public class FactsProvider extends ContentProvider {
    static final String PROVIDER_NAME = "com.zaks.ayala.provider.facts";
    static final String URL = "content://" + PROVIDER_NAME + "/items";
    static final Uri CONTENT_URI = Uri.parse(URL);
    private static HashMap<String, String> FactsMap;
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
            queryBuilder.setTables(FactsContract.FactsEntry.TABLE_NAME);
            switch (uriMatcher.match(uri)) {
                case 1:
                    queryBuilder.setProjectionMap(FactsMap);
                    break;
                case 2:
                    queryBuilder.appendWhere(FactsContract.FactsEntry.COLUMN_ORDER + "=" + uri.getLastPathSegment());
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI " + uri);
            }
            if (sortOrder == null || sortOrder == "") {
                sortOrder = FactsContract.FactsEntry.COLUMN_ORDER;
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
                return "vnd.android.cursor.dir/vnd.fivethings.fact";
            case 2:
                return "vnd.android.cursor.item/vnd.fivethings.fact";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long row = database.insert(FactsContract.FactsEntry.TABLE_NAME, "", values);
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
                count = database.delete(FactsContract.FactsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case 2:
                count = database.delete(FactsContract.FactsEntry.TABLE_NAME,
                        FactsContract.FactsEntry.COLUMN_ORDER + " = " + uri.getLastPathSegment() +
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
                count = database.update(FactsContract.FactsEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case 2:
                count = database.update(FactsContract.FactsEntry.TABLE_NAME, values,
                        FactsContract.FactsEntry.COLUMN_ORDER + " = " + uri.getLastPathSegment() +
                                (!selection.isEmpty() ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
