package com.zaks.ayala.nearbydeals.bl;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.zaks.ayala.nearbydeals.bl.models.Supplier;
import com.zaks.ayala.nearbydeals.data.datacontracts.SuppliersContract;

/**
 * Created by אילה on 06-Jul-16.
 */
public class SupplierHelper {
    Context context;
    private static final Uri SuppliersProviderUri = Uri.parse("content://com.zaks.ayala.provider.suppliers/items");

    public SupplierHelper(Context cntxt) {
        context = cntxt;
    }

    public Supplier GetSupplierByEmail(String Email) {
        String selection = SuppliersContract.SupplierEntry.Column_Email + " = '" + Email + "'";
        Cursor data = context.getContentResolver().query(SuppliersProviderUri, Supplier.getProjectionMap(), selection, null, null);
        if (data != null && data.moveToFirst())
            return Supplier.fromCursor(data);
        return null;
    }

    public void SaveNewSupplier(Supplier newSupplier) {

    }
}
