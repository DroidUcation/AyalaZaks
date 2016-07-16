package com.zaks.ayala.nearbydeals.bl;

import android.content.Context;
import android.text.format.DateUtils;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.zaks.ayala.nearbydeals.bl.services.DealsNotificationsService;

/**
 * Created by אילה on 14-Jul-16.
 */
public class NotificationManager {
    static GcmNetworkManager networkManager;

    public NotificationManager() {
    }

    public static void startNotificationService(Context context) {
        PeriodicTask task = new PeriodicTask.Builder()
                .setService(DealsNotificationsService.class)
                .setTag("DealNotifications")
                .setPeriod(60L)
                .setUpdateCurrent(true)
                .setPersisted(true)
                .setRequiredNetwork(OneoffTask.NETWORK_STATE_CONNECTED)
                .build();
        GcmNetworkManager.getInstance(context).schedule(task);
    }

    public static void stopNotificationService(Context context) {
        GcmNetworkManager.getInstance(context).cancelAllTasks(DealsNotificationsService.class);
    }
}
