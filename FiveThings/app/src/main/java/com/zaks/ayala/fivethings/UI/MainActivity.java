package com.zaks.ayala.fivethings.UI;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.zaks.ayala.fivethings.Data.AlarmServiceIntent;
import com.zaks.ayala.fivethings.Data.DBHelper;
import com.zaks.ayala.fivethings.R;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    AlarmManager AlrmMng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
        SetAlarm();
    }

    private void SetAlarm() {
        AlrmMng = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        AlrmMng.set(AlarmManager.RTC_WAKEUP, 5000, CreateIntent());
    }

    private PendingIntent CreateIntent() {
        Intent i = new Intent(this, AlarmServiceIntent.class);
        PendingIntent p = PendingIntent.getService(this, 0, i, 0);
        return p;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void start5things(View view) {
        Intent intent = new Intent(this, fiveThingsList.class);
        startActivity(intent);
    }
}
