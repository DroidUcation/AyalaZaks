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
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.zaks.ayala.nearbydeals.R;
import com.zaks.ayala.nearbydeals.bl.models.Category;
import com.zaks.ayala.nearbydeals.bl.models.Deal;
import com.zaks.ayala.nearbydeals.bl.models.Supplier;
import com.zaks.ayala.nearbydeals.bl.services.DealIntentService;
import com.zaks.ayala.nearbydeals.common.Utilities;
import com.zaks.ayala.nearbydeals.data.datacontracts.DealsContract;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddDealActivity extends AppCompatActivity {


    int mYear, mMonth, mDay;
    Place selectedPlace;
    Supplier supplier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint(getString(R.string.add_deal_address_hint));
        supplier = (Supplier) getIntent().getSerializableExtra("supplier");
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                selectedPlace = place;
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
        Deal newDeal = new Deal();
        newDeal.setDescription(editDesc.getText().toString());
        newDeal.setFromDate((Date) editFrom.getTag());
        newDeal.setToDate((Date) editTo.getTag());
        if (selectedPlace != null) {
            newDeal.setAddress(selectedPlace.getAddress().toString());
            newDeal.setLatitude(selectedPlace.getLatLng().latitude);
            newDeal.setLongitude(selectedPlace.getLatLng().longitude);
        }
        newDeal.setCategory(supplier.getCategory());
        newDeal.setSupplierID(supplier.getId());
        DealIntentService.startActionSave(this, newDeal);
        this.finish();
    }

}
