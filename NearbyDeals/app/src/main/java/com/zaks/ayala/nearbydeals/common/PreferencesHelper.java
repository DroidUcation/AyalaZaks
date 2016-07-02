package com.zaks.ayala.nearbydeals.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;

import com.zaks.ayala.nearbydeals.data.datacontracts.CategoriesContract;

import java.util.ArrayList;

/**
 * Created by אילה on 28-Jun-16.
 */
public class PreferencesHelper {


    public static boolean GetCategoryEnabled(Context context, String categoryKey) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean(categoryKey, true);
    }

    public static ArrayList<String> GetAllUserCategories(Context context) {
        Uri CategoriesListURL = Uri.parse("content://com.zaks.ayala.provider.categories/items");
        Cursor data = context.getContentResolver().query(CategoriesListURL, null, null, null, CategoriesContract.CategoryEntry.Column_Description);
        ArrayList<String> categories = new ArrayList<String>();
        if (data.moveToFirst()) {
            do {

                if (GetCategoryEnabled(context, data.getString(data.getColumnIndex(CategoriesContract.CategoryEntry.Column_Description))))
                    categories.add(data.getString(data.getColumnIndex(CategoriesContract.CategoryEntry.Column_Description)));
            }
            while (data.moveToNext());
        }
        return categories;
    }

    public static boolean isExistPreferences(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context).getAll().size()!=0;
    }
}
