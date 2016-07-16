package com.zaks.ayala.nearbydeals.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initPlacesAutoComplete();
        initCategoriesSpinner();
        initMail();
    }

    private void initMail() {
        EditText editEmail = (EditText) findViewById(R.id.supplier_details_supplier_email);
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        editEmail.setText(user.getEmail());
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
        autocompleteFragment.setHint(getString(R.string.add_deal_address_header));
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
//        if(!Validate())
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

//    private boolean Validate() {
//        EditText etUserName = (EditText) findViewById(R.id.txtUsername);
//        String strUserName = etUserName.getText().toString();
//
//        if(TextUtils.isEmpty(strUserName)) {
//            etUserName.setError("Your message");
//            return;
//        }
//
//        EditText etUserName = (EditText) findViewById(R.id.txtUsername);
//        String strUserName = etUserName.getText().toString();
//
//        if(TextUtils.isEmpty(strUserName)) {
//            etUserName.setError("Your message");
//            return;
//        }
//
//        EditText etUserName = (EditText) findViewById(R.id.txtUsername);
//        String strUserName = etUserName.getText().toString();
//
//        if(TextUtils.isEmpty(strUserName)) {
//            etUserName.setError("Your message");
//            return;
//        }
//    }
}
