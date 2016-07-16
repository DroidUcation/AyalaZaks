package com.zaks.ayala.nearbydeals.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zaks.ayala.nearbydeals.R;
import com.zaks.ayala.nearbydeals.bl.SupplierHelper;
import com.zaks.ayala.nearbydeals.bl.models.Deal;
import com.zaks.ayala.nearbydeals.bl.models.Supplier;
import com.zaks.ayala.nearbydeals.data.datacontracts.CategoriesContract;
import com.zaks.ayala.nearbydeals.data.datacontracts.DealsContract;
import com.zaks.ayala.nearbydeals.data.datacontracts.SuppliersContract;

public class SupplierDealsListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DealsLoaderId = 1;
    private static final Uri DealsProviderUri = Uri.parse("content://com.zaks.ayala.provider.deals/items");

    SupplierDealsListFragment fragment;
    Supplier currSupplier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_deals_list);
        getSupplier();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Activity currActivity = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =
                        new Intent(SupplierDealsListActivity.this, AddDealActivity.class);
                intent.putExtra("supplier", currSupplier);
//
                ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight());
                ActivityCompat.startActivity(
                        SupplierDealsListActivity.this, intent, transitionActivityOptions.toBundle());

//
            }
        });
        getSupportLoaderManager().initLoader(DealsLoaderId, null, this);
        fragment = (SupplierDealsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_deals_list);
    }

    private void getSupplier() {
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null) {
            String supplierEmail = user.getEmail();
            SupplierHelper helper = new SupplierHelper(this);
            currSupplier = helper.GetSupplierByEmail(supplierEmail);
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = DealsContract.DealEntry.Column_SupplierID + " = " + currSupplier.getId();
        return new CursorLoader(this, DealsProviderUri, Deal.getProjectionMap(), selection, null, DealsContract.DealEntry.Column_FromDate);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        fragment.setCursorAdapter(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
