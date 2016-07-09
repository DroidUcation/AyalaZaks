package com.zaks.ayala.nearbydeals.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;

import com.zaks.ayala.nearbydeals.bl.CategoryHelper;
import com.zaks.ayala.nearbydeals.bl.models.Category;
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
        CategoryHelper helper=new CategoryHelper(context);
        ArrayList<Category> categoryArrayList= helper.GetCategories();
        ArrayList<String> categories = new ArrayList<String>();
        for (Category c: categoryArrayList) {
            if (GetCategoryEnabled(context, c.getDescription()))
                categories.add(c.getDescription());
        }
        return categories;
    }

    public static boolean isExistPreferences(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context).getAll().size()!=0;
    }
}
