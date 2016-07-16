package com.zaks.ayala.nearbydeals.common;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;
import com.zaks.ayala.nearbydeals.R;
import com.zaks.ayala.nearbydeals.data.datacontracts.DealsContract;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by אילה on 20-Jun-16.
 */
public class Utilities {
    public static String getDateForDB(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        if (date != null)
            return dateFormat.format(date);
        else
            return "";
    }

    public static String getDateForDisplay(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd.MM.yyyy", Locale.getDefault());
        if (date != null)
            return dateFormat.format(date);
        else
            return "";
    }

    public static Date getDateFromString(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Address getLocation(Context context, String address) {
        try {
            Geocoder geocoder = new Geocoder(context);
            List<Address> addressList = geocoder.getFromLocationName(address, 1);
            if (addressList != null && addressList.size() > 0) {
                return addressList.get(0);
            }
            return null;
        } catch (IOException ex) {
            return null;
        }
    }

    public static int getCategoryIcon(String categoryName)
    {
        switch (categoryName)
        {
            case "Fashion":return R.drawable.ic_shopping_bag;
            case "Health":return R.drawable.ic_pill;
            case "Food":return R.drawable.ic_hamburger;
            case "Sport":return R.drawable.ic_trainers;
            case "Office":return R.drawable.ic_pen;
        }
        return 0;
    }
    public static int getCategoryBackground(String categoryName)
    {
        switch (categoryName)
        {
            case "Fashion":return R.color.category_4;
            case "Health":return R.color.category_3;
            case "Food":return R.color.category_1;
            case "Sport":return R.color.category_5;
            case "Office":return R.color.category_2;
        }
        return 0;
    }
    public static LatLng calculateDerivedPosition(LatLng point,
                                                  double range, double bearing)
    {
        double EarthRadius = 6371000; // m

        double latA = Math.toRadians(point.latitude);
        double lonA = Math.toRadians(point.longitude);
        double angularDistance = range / EarthRadius;
        double trueCourse = Math.toRadians(bearing);

        double lat = Math.asin(
                Math.sin(latA) * Math.cos(angularDistance) +
                        Math.cos(latA) * Math.sin(angularDistance)
                                * Math.cos(trueCourse));

        double dlon = Math.atan2(
                Math.sin(trueCourse) * Math.sin(angularDistance)
                        * Math.cos(latA),
                Math.cos(angularDistance) - Math.sin(latA) * Math.sin(lat));

        double lon = ((lonA + dlon + Math.PI) % (Math.PI * 2)) - Math.PI;

        lat = Math.toDegrees(lat);
        lon = Math.toDegrees(lon);

        LatLng newPoint = new LatLng((float) lat, (float) lon);

        return newPoint;

    }
}