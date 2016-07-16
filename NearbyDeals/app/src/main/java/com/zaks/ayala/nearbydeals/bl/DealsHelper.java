package com.zaks.ayala.nearbydeals.bl;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;
import com.zaks.ayala.nearbydeals.common.PreferencesHelper;
import com.zaks.ayala.nearbydeals.common.Utilities;
import com.zaks.ayala.nearbydeals.data.datacontracts.CategoriesContract;
import com.zaks.ayala.nearbydeals.data.datacontracts.DealsContract;

import java.util.ArrayList;

/**
 * Created by אילה on 15-Jul-16.
 */
public class DealsHelper {
    Context context;

    public DealsHelper(Context cntxt) {
        context = cntxt;
    }

    public String getSelectionForCloseDeals(double lat, double lon) {
        if (lat == -1 || lon == -1)
            return "";
        LatLng center = new LatLng(lat, lon);
        final double radius = 1000;
        LatLng p1 = Utilities.calculateDerivedPosition(center, radius, 0);
        LatLng p2 = Utilities.calculateDerivedPosition(center, radius, 90);
        LatLng p3 = Utilities.calculateDerivedPosition(center, radius, 180);
        LatLng p4 = Utilities.calculateDerivedPosition(center, radius, 270);
        ArrayList userCategories = PreferencesHelper.GetAllUserCategories(context);
        String categories = TextUtils.join("', '", userCategories);
        String selection = DealsContract.DealEntry.Column_Latitude + " > " + String.valueOf(p3.latitude) + " AND "
                + DealsContract.DealEntry.Column_Latitude + " < " + String.valueOf(p1.latitude) + " AND "
                + DealsContract.DealEntry.Column_Longitude + " < " + String.valueOf(p2.longitude) + " AND "
                + CategoriesContract.CategoryEntry.Column_Description + " IN ('" + categories + "')";
        return selection;
    }
}
