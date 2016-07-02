package com.zaks.ayala.nearbydeals.ui;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.zaks.ayala.nearbydeals.R;
import com.zaks.ayala.nearbydeals.common.Utilities;
import com.zaks.ayala.nearbydeals.data.datacontracts.CategoriesContract;
import com.zaks.ayala.nearbydeals.data.datacontracts.DealsContract;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends PreferenceFragment {

    private static final int DealsDescriptionLoaderID = 1;
    PreferenceCategory categoriesList;
    private static final Uri CategoriesListURL = Uri.parse("content://com.zaks.ayala.provider.categories/items");

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        categoriesList = (PreferenceCategory) getPreferenceScreen().findPreference("pref_key_set_categories");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout v = (LinearLayout) super.onCreateView(inflater, container, savedInstanceState);
        View buttonView = inflater.inflate(R.layout.next_button, null);
        Button btn = (Button) buttonView.findViewById(R.id.preferences_activity_next_button);
        v.addView(buttonView);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), DealsActivity.class);
                startActivity(i);
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SwitchPreference switchPref;
        Cursor data = getActivity().getContentResolver().query(CategoriesListURL, null, null, null, CategoriesContract.CategoryEntry.Column_Description);

        if (data.moveToFirst()) {
            do {
                switchPref = new SwitchPreference(getActivity());
                switchPref.setDefaultValue(true);
                switchPref.setKey(data.getString(data.getColumnIndex(CategoriesContract.CategoryEntry.Column_Description)));
                switchPref.setTitle(data.getString(data.getColumnIndex(CategoriesContract.CategoryEntry.Column_Description)));
  //              Drawable icon = getResources().getDrawable(Utilities.getCategoryIcon(data.getInt(data.getColumnIndex(CategoriesContract.CategoryEntry.Column_ID))));
//                icon.setColorFilter(Color.parseColor(data.getString(data.getColumnIndex(CategoriesContract.CategoryEntry.Column_Color))), PorterDuff.Mode.MULTIPLY);
                switchPref.setIcon(Utilities.getCategoryIcon(data.getString(data.getColumnIndex(CategoriesContract.CategoryEntry.Column_Description))));
                categoriesList.addPreference(switchPref);
            }
            while (data.moveToNext());
        }
    }

}
