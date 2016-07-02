package com.zaks.ayala.nearbydeals.ui;

import android.app.DatePickerDialog;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.zaks.ayala.nearbydeals.R;
import com.zaks.ayala.nearbydeals.bl.models.Deal;
import com.zaks.ayala.nearbydeals.common.Utilities;
import com.zaks.ayala.nearbydeals.data.datacontracts.DealsContract;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddDealActivity extends AppCompatActivity {

    private static final Uri DealsProviderURL = Uri.parse("content://com.zaks.ayala.provider.deals/items");
    int mYear, mMonth, mDay;
    Place selectedPlace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deal);
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                selectedPlace=place;
            }

            @Override
            public void onError(Status status) {

            }
        });
    }

    public void OpenCalendar(View view) {
        Calendar mcurrentDate = Calendar.getInstance();
        mYear = mcurrentDate.get(Calendar.YEAR);
        mMonth = mcurrentDate.get(Calendar.MONTH);
        mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        final EditText text = (EditText) view;
        DatePickerDialog mDatePicker = new DatePickerDialog(AddDealActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                Calendar date = Calendar.getInstance();
                date.set(selectedyear, selectedmonth, selectedday);
                text.setText(Utilities.getDateForDisplay(date.getTime()));
                text.setTag(date.getTime());
            }
        }, mYear, mMonth, mDay);
        mDatePicker.setTitle(R.string.calendar_haeder);
        mDatePicker.show();

    }


    public void SaveNewDeal(View view) {
        EditText editDesc = (EditText) findViewById(R.id.deal_description);
        EditText editFrom = (EditText) findViewById(R.id.deal_from);
        EditText editTo = (EditText) findViewById(R.id.deal_to);
        ContentValues values = new ContentValues();
        values.put(DealsContract.DealEntry.Column_Description, editDesc.getText().toString());
        values.put(DealsContract.DealEntry.Column_FromDate, Utilities.getDateForDB((Date) editFrom.getTag()));
        values.put(DealsContract.DealEntry.Column_ToDate, Utilities.getDateForDB((Date) editTo.getTag()));
        if (selectedPlace != null) {
            values.put(DealsContract.DealEntry.Column_Address, selectedPlace.getAddress().toString());
            values.put(DealsContract.DealEntry.Column_Latitude, selectedPlace.getLatLng().latitude);
            values.put(DealsContract.DealEntry.Column_Longitude, selectedPlace.getLatLng().longitude);
        }
        values.put(DealsContract.DealEntry.Column_CategoryID, 2);
        values.put(DealsContract.DealEntry.Column_SupplierID, 0);
        Uri res = getContentResolver().insert(DealsProviderURL, values);
        this.finish();
    }

}
