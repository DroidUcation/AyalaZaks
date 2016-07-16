package com.zaks.ayala.nearbydeals.bl.services;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.zaks.ayala.nearbydeals.R;
import com.zaks.ayala.nearbydeals.bl.DealsHelper;
import com.zaks.ayala.nearbydeals.bl.models.Deal;
import com.zaks.ayala.nearbydeals.common.Utilities;
import com.zaks.ayala.nearbydeals.ui.MainActivity;

import java.util.ArrayList;

public class DealsNotificationsService extends GcmTaskService {
    GoogleApiClient mApiClient;
    private static final Uri DealsProviderUri = Uri.parse("content://com.zaks.ayala.provider.deals/items");

    public DealsNotificationsService() {
    }


    @Override
    public int onRunTask(TaskParams taskParams) {
        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();
        mApiClient.blockingConnect();

        Location lastLocation = getLocation();
        if (lastLocation != null) {
            ArrayList<Deal> deals = getCloseDeals(lastLocation);
            if (deals.size() > 0) {
                sendDealNotification(deals);
            }
            return GcmNetworkManager.RESULT_SUCCESS;
        }
        return GcmNetworkManager.RESULT_FAILURE;
    }


    private Location getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        final Location[] location = {null};

        LocationRequest request = LocationRequest.create()
                .setFastestInterval(1000)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationCallback listener = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult result) {
                location[0] = result.getLastLocation();
            }
        };

        boolean success = LocationServices.FusedLocationApi.requestLocationUpdates(mApiClient, request,
                listener, getMainLooper()).await().isSuccess();
        if (!success) return null;
        int i = 10;
        while (location[0] == null && i > 0) {
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i--;
        }

        LocationServices.FusedLocationApi.removeLocationUpdates(mApiClient, listener);

        return location[0];
    }

    private ArrayList<Deal> getCloseDeals(Location lastLocation) {
        ArrayList<Deal> deals = new ArrayList<>();
        DealsHelper helper = new DealsHelper(this);
        String selection = helper.getSelectionForCloseDeals(lastLocation.getLatitude(), lastLocation.getLongitude());
        Cursor data = getContentResolver().query(DealsProviderUri, Deal.getProjectionMap(), selection, null, null);
        if (data.moveToFirst()) {
            do {
                deals.add(Deal.fromCursor(data));
            }
            while (data.moveToNext());
        }
        return deals;
    }

    private void sendDealNotification(ArrayList<Deal> deals) {
        try {
            for (Deal deal : deals) {
                final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                Intent i=new Intent(getApplicationContext(),DealsNotificationsService.class);
                i.putExtra("Deal",deal);
                TaskStackBuilder stack=TaskStackBuilder.create(this);
                stack.addParentStack(MainActivity.class);
                stack.addNextIntent(i);
                PendingIntent pi=stack.getPendingIntent(1,PendingIntent.FLAG_UPDATE_CURRENT);

                builder.setContentTitle(getString(R.string.notification_title))
                        .setAutoCancel(true)
                        .setColor(getResources().getColor(R.color.accent))
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), Utilities.getCategoryIcon(deal.getCategory().getDescription())))
                        .setSmallIcon(R.drawable.ic_stat_nearby)
                        .setContentText(deal.getDescription())
                        .setContentIntent(pi);
                final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(1, builder.build());
            }
        } catch (Exception ex) {
            Log.e(getClass().getSimpleName(), ex.toString());
        }
    }
}
