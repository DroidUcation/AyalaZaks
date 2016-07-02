package com.zaks.ayala.nearbydeals.ui;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zaks.ayala.nearbydeals.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SupplierDealsListFragment extends Fragment {
    private RecyclerView dealsRecyclerView;
    private RecyclerView.Adapter dealsAdapter;
    private RecyclerView.LayoutManager dealsLayoutManager;

    public SupplierDealsListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_supplier_deals_list, container, false);
        dealsRecyclerView = (RecyclerView) view.findViewById(R.id.supplier_deals_List);
        dealsRecyclerView.setHasFixedSize(true);
        dealsLayoutManager = new LinearLayoutManager(getContext());
        dealsRecyclerView.setLayoutManager(dealsLayoutManager);

        return view;
    }

    public void setCursorAdapter(Cursor cursor) {
        dealsAdapter = new SupplierDealsListAdapter(getContext(), cursor);
        dealsRecyclerView.setAdapter(dealsAdapter);
    }
}
