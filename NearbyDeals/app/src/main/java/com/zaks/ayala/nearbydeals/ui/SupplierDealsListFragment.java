package com.zaks.ayala.nearbydeals.ui;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zaks.ayala.nearbydeals.R;
import com.zaks.ayala.nearbydeals.bl.SupplierHelper;
import com.zaks.ayala.nearbydeals.bl.models.Deal;
import com.zaks.ayala.nearbydeals.bl.models.Supplier;
import com.zaks.ayala.nearbydeals.data.datacontracts.DealsContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class SupplierDealsListFragment extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor> {
    private RecyclerView dealsRecyclerView;
    private RecyclerView.Adapter dealsAdapter;
    private static final int DealsLoaderId = 1;
    private static final Uri DealsProviderUri = Uri.parse("content://com.zaks.ayala.provider.deals/items");
    Supplier supplier;

    public SupplierDealsListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_supplier_deals_list, container, false);
        dealsRecyclerView = (RecyclerView) view.findViewById(R.id.supplier_deals_List);
        dealsRecyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager dealsLayoutManager = new LinearLayoutManager(getContext());
        dealsRecyclerView.setLayoutManager(dealsLayoutManager);
        getActivity().getSupportLoaderManager().initLoader(DealsLoaderId, null, this);
        getSupplier();
        return view;
    }


    private void getSupplier() {
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null) {
            String supplierEmail = user.getEmail();
            SupplierHelper helper = new SupplierHelper(getContext());
            supplier = helper.GetSupplierByEmail(supplierEmail);
        }
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = DealsContract.DealEntry.Column_SupplierID + " = " + supplier.getId();
        return new CursorLoader(getContext(), DealsProviderUri, Deal.getProjectionMap(), selection, null, DealsContract.DealEntry.Column_FromDate);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        dealsAdapter = new SupplierDealsListAdapter(getContext(), data);
        dealsRecyclerView.setAdapter(dealsAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
