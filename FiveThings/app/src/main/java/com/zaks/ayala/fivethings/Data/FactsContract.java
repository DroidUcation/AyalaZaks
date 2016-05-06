package com.zaks.ayala.fivethings.Data;

import android.provider.BaseColumns;

/**
 * Created by אילה on 4/18/2016.
 */
public class FactsContract {
    public static class FactsEntry implements BaseColumns {

        public static final String TABLE_NAME = "Facts";

        public static final String COLUMN_ORDER = "FactsOrder";
        public static final String COLUMN_TEXT = "Text";
        public static final String COLUMN_IMAGE = "ImageLink";
    }
}
