package com.zaks.ayala.nearbydeals.ui;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.zaks.ayala.nearbydeals.R;
import com.zaks.ayala.nearbydeals.bl.models.Deal;
import com.zaks.ayala.nearbydeals.bl.services.DealIntentService;
import com.zaks.ayala.nearbydeals.common.Utilities;
import com.zaks.ayala.nearbydeals.data.datacontracts.DealsContract;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EditDealActivity extends AppCompatActivity {

    private static final Uri DealsProviderURL = Uri.parse("content://com.zaks.ayala.provider.deals/items");
    int mYear, mMonth, mDay;
    Place selectedPlace;
    Deal theDeal;
    int dealID;
    EditText editDesc;
    EditText editFrom;
    EditText editTo;
    PlaceAutocompleteFragment autocompleteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deal);
        theDeal = (Deal) getIntent().getSerializableExtra("Deal");
        dealID = theDeal.getId();
        editDesc = (EditText) findViewById(R.id.deal_description);
        editFrom = (EditText) findViewById(R.id.deal_from);
        editTo = (EditText) findViewById(R.id.deal_to);
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                selectedPlace = place;
            }

            @Override
            public void onError(Status status) {

            }
        });

        fillDealFields();
    }

    private void fillDealFields() {
        editDesc.setText(theDeal.getDescription());
        editFrom.setText(Utilities.getDateForDisplay(theDeal.getFromDate()));
        editTo.setText(Utilities.getDateForDisplay(theDeal.getToDate()));
        autocompleteFragment.setText(theDeal.getAddress());
    }

    public void OpenCalendar(View view) {
        Calendar mcurrentDate = Calendar.getInstance();
        mYear = mcurrentDate.get(Calendar.YEAR);
        mMonth = mcurrentDate.get(Calendar.MONTH);
        mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        final EditText text = (EditText) view;
        DatePickerDialog mDatePicker = new DatePickerDialog(EditDealActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        Deal newDeal = new Deal();
        newDeal.setId(dealID);
        newDeal.setDescription(editDesc.getText().toString());
        newDeal.setFromDate((Date) editFrom.getTag());
        newDeal.setToDate((Date) editTo.getTag());
        if (selectedPlace != null) {
            newDeal.setAddress(selectedPlace.getAddress().toString());
            newDeal.setLatitude(selectedPlace.getLatLng().latitude);
            newDeal.setLongitude(selectedPlace.getLatLng().longitude);
        }
        DealIntentService.startActionEdit(this, newDeal);
        this.finish();
    }

}
