package com.zaks.ayala.nearbydeals.data.datacontracts;

import android.provider.BaseColumns;

/**
 * Created by אילה on 18-May-16.
 */
public final class SuppliersContract {
    public SuppliersContract(){}

    public  static  abstract class SupplierEntry implements BaseColumns {
        public static final String TableName = "Suppliers";
        public static final String Column_ID = "ID_Suppliers";
        public static final String Column_Name = "Name";
        public static final String Column_Email = "Email";
        public static final String Column_Phone= "Phone";
        public static final String Column_Address = "SupplierAddress";
        public static final String Column_CategoryID = "CategoryID";
        public static final String Column_Latitude = "SupplierLatitude";
        public static final String Column_Longitude = "SupplierLongitude";

        public static String addPrefix(String field) {
            return TableName + "." + field;
        }
    }
}
