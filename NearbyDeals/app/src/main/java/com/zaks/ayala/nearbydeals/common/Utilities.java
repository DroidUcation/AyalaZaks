package com.zaks.ayala.nearbydeals.common;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

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
            case "Fashion":return R.drawable.shopping;
            case "Health":return R.drawable.heart_pulse;
            case "Food":return R.drawable.food;
            case "Sport":return R.drawable.weight_kilogram;
            case "Office":return R.drawable.pencil;
        }
        return 0;
    }
}