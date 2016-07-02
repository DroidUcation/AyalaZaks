package com.zaks.ayala.nearbydeals.ui;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zaks.ayala.nearbydeals.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DealListFragment extends Fragment {
    private RecyclerView dealsRecyclerView;
    private RecyclerView.Adapter dealsAdapter;
    private RecyclerView.LayoutManager dealsLayoutManager;

    public DealListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_deal_list, container, false);
        dealsRecyclerView = (RecyclerView) view.findViewById(R.id.deals_List);
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
