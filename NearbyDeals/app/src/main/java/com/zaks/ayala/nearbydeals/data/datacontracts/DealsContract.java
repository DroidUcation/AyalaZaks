package com.zaks.ayala.nearbydeals.data.datacontracts;

import android.provider.BaseColumns;

/**
 * Created by אילה on 17-May-16.
 */
public final class DealsContract {
    public DealsContract() {
    }

    public static abstract class DealEntry implements BaseColumns {
        public static final String TableName = "Deals";
        public static final String Column_ID = "ID";
        public static final String Column_SupplierID = "SupplierID";
        public static final String Column_CategoryID = "CategoryID";
        public static final String Column_Description = "DealDescription";
        public static final String Column_Image = "Image";
        public static final String Column_Address = "DealAddress";
        public static final String Column_FromDate = "FromDate";
        public static final String Column_ToDate = "ToDate";
        public static final String Column_Longitude = "DealLongitude";
        public static final String Column_Latitude = "DealLatitude";

        public static String addPrefix(String field) {
            return TableName + "." + field;
        }
    }
}
