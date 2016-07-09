package com.zaks.ayala.nearbydeals.bl;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.zaks.ayala.nearbydeals.bl.models.Category;
import com.zaks.ayala.nearbydeals.bl.models.Supplier;
import com.zaks.ayala.nearbydeals.data.datacontracts.CategoriesContract;
import com.zaks.ayala.nearbydeals.data.datacontracts.SuppliersContract;

import java.util.ArrayList;

/**
 * Created by אילה on 06-Jul-16.
 */
public class CategoryHelper {
    Context context;
    private static final Uri CategoriesProviderUri = Uri.parse("content://com.zaks.ayala.provider.categories/items");

    public CategoryHelper(Context cntxt) {
        context = cntxt;
    }

    public   ArrayList<Category> GetCategories() {
        Cursor data = context.getContentResolver().query(CategoriesProviderUri, null, null, null, CategoriesContract.CategoryEntry.Column_Description);
        ArrayList<Category> categories = new ArrayList<Category>();
        if (data.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(data.getInt(data.getColumnIndex(CategoriesContract.CategoryEntry.addPrefix(CategoriesContract.CategoryEntry.Column_ID))));
                category.setDescription(data.getString(data.getColumnIndex(CategoriesContract.CategoryEntry.addPrefix(CategoriesContract.CategoryEntry.Column_Description))));
                category.setColor(data.getString(data.getColumnIndex(CategoriesContract.CategoryEntry.addPrefix(CategoriesContract.CategoryEntry.Column_Color))));
                categories.add(category);
            }
            while (data.moveToNext());
        }
        return categories;
    }
}
