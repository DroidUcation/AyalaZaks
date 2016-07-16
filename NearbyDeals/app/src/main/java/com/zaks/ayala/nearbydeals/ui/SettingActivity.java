package com.zaks.ayala.nearbydeals.ui;

import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.zaks.ayala.nearbydeals.R;

public class SettingActivity extends AppCompatActivity {
    final public static String extra_IsFirst = "IsFirst";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (!getIntent().getBooleanExtra(extra_IsFirst, false)) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getFragmentManager().beginTransaction().replace(R.id.settings, new SettingFragment()).commit();
    }
}
