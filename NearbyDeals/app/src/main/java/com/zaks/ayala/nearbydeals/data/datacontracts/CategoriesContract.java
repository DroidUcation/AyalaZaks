package com.zaks.ayala.nearbydeals.data.datacontracts;

import android.provider.BaseColumns;

/**
 * Created by אילה on 17-May-16.
 */
public final class CategoriesContract {
    public CategoriesContract(){}

    public static abstract class CategoryEntry implements BaseColumns {
        public static final String TableName = "Categories";
        public static final String Column_ID = "_id";
        public static final String Column_Description = "CategoryDescription";
        public static final String Column_Color = "Color";

        public static String addPrefix(String field) {
            return TableName + "." + field;
        }
    }
}
