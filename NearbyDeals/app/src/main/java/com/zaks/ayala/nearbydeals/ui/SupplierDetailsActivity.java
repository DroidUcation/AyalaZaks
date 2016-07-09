package com.zaks.ayala.nearbydeals.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.zaks.ayala.nearbydeals.R;
import com.zaks.ayala.nearbydeals.bl.CategoryHelper;
import com.zaks.ayala.nearbydeals.bl.SupplierHelper;
import com.zaks.ayala.nearbydeals.bl.models.Category;
import com.zaks.ayala.nearbydeals.bl.models.Supplier;
import com.zaks.ayala.nearbydeals.bl.services.SupplierIntentService;

import java.util.ArrayList;

public class SupplierDetailsActivity extends AppCompatActivity {
    Spinner categoriesSpinner;
    Place selectedPlace;
    private static final Uri SuppliersProviderUri = Uri.parse("content://com.zaks.ayala.provider.suppliers/items");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_details);

        initPlacesAutoComplete();
        initCategoriesSpinner();
    }

    private void initCategoriesSpinner() {
        categoriesSpinner = (Spinner) findViewById(R.id.supplier_details_supplier_category);
        CategoryHelper helper = new CategoryHelper(this);
        ArrayList<Category> categories = helper.GetCategories();
        ArrayAdapter<Category> categoriesAdapter = new ArrayAdapter<Category>(this,
                android.R.layout.simple_spinner_item, categories);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(categoriesAdapter);
    }

    private void initPlacesAutoComplete() {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.supplier_details_supplier_address);

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

    protected void SaveNewSupplier(View view) {
        EditText editName = (EditText) findViewById(R.id.supplier_details_supplier_name);
        EditText editEmail = (EditText) findViewById(R.id.supplier_details_supplier_email);
        EditText editPhone = (EditText) findViewById(R.id.supplier_details_supplier_phone);
        Supplier newSupplier = new Supplier();
        newSupplier.setName(editName.getText().toString());
        newSupplier.setEmail(editEmail.getText().toString());
        newSupplier.setPhone(editPhone.getText().toString());
        newSupplier.setCategory((Category) categoriesSpinner.getSelectedItem());
        if (selectedPlace != null) {
            newSupplier.setAddress(selectedPlace.getAddress().toString());
            newSupplier.setLatitude(selectedPlace.getLatLng().latitude);
            newSupplier.setLongitude(selectedPlace.getLatLng().longitude);
        }
        Uri res= getContentResolver().insert(SuppliersProviderUri, newSupplier.getContentValues());
        Integer id = Integer.valueOf(res.getLastPathSegment());
        newSupplier.setId(id);
        SupplierIntentService.startSaveSupplier(this, newSupplier);
        Intent i = new Intent(this, SupplierDealsListActivity.class);
        i.putExtra("supplierEmail", newSupplier.getEmail());
        startActivity(i);
        finish();
    }
}
