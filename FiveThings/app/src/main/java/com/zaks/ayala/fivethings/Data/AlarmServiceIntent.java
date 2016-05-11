package com.zaks.ayala.fivethings.Data;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zaks.ayala.fivethings.R;
import com.zaks.ayala.fivethings.UI.MainActivity;
import com.zaks.ayala.fivethings.UI.fiveThingsList;

/**
 * Created by אילה on 09-May-16.
 */
public class AlarmServiceIntent extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AlarmServiceIntent() {
        super("AlarmService");
    }

    private static final int notificationID = 1;

    @Override
    protected void onHandleIntent(Intent intent) {
        String URL = "content://com.zaks.ayala.provider.facts/items";
        Uri uri = Uri.parse(URL);
        getContentResolver().delete(uri, null, null);
        insertRow(uri, 1, "עד גרסה 4.0 שהוצגה בסוף 2011, לא הייתה תמיכה בעברית באופן מובנה במערכת אנדרואיד", R.drawable.android_hebrew);
        insertRow(uri, 2, "אב הטיפוס הראשון של אנדרואיד נראה כמו Blackberry. היו לו מקשים, מקלדת פיזית מלאה, מקשים למענה ניתוק וחיצים לשליטה – ללא מסך מגע.", R.drawable.android_blackberry);
        insertRow(uri, 3, "בשנת 2010 הציגה סוני (או Sony Ericsson דאז) את ה-LiveView, שעון חכם מבוסס אנדרואיד שמתחבר לאנדרואיד ", R.drawable.android_liveview);
        insertRow(uri, 4, "לאנדרואיד יש כ-1.4 מיליארד משתמשים פעילים ובכל יום מצטרפים אליהם עוד מיליון וחצי", R.drawable.android_many);
        insertRow(uri, 5, "ומה שמו של הרובוט הירוק? שם רשמי עדיין אין לו. אבל המהנדסים בגוגל מכנים אותו Bugdroid", R.drawable.bugdroid);

        Intent i= new Intent(this,fiveThingsList.class);
        TaskStackBuilder taskStackBuilder=TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(MainActivity.class);
        taskStackBuilder.addNextIntent(i);
        PendingIntent pi= taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification n = new NotificationCompat.Builder(this).setContentTitle("חמישה עובדות חדשות!").setContentText("חמישה עובדות חדשות על אנדרואיד כעת באפליקציה!").setSmallIcon(R.drawable.android_icon).setContentIntent(pi).build();
        NotificationManager manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notificationID,n);
    }

    private void insertRow(Uri uri, int order, String text, int image) {
        ContentValues values = new ContentValues();
        values.put(FactsContract.FactsEntry.COLUMN_ORDER, order);
        values.put(FactsContract.FactsEntry.COLUMN_TEXT, text);
        values.put(FactsContract.FactsEntry.COLUMN_IMAGE, image);
        getContentResolver().insert(uri, values);
    }
}
