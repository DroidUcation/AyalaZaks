package com.zaks.ayala.nearbydeals.ui;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zaks.ayala.nearbydeals.R;
import com.zaks.ayala.nearbydeals.bl.models.Deal;
import com.zaks.ayala.nearbydeals.data.datacontracts.DealsContract;

/**
 * A simple {@link Fragment} subclass.
 */
public class DealListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>  {
    private RecyclerView dealsRecyclerView;
    private RecyclerView.Adapter dealsAdapter;
    private static final int DealsLoaderId = 1;
    private static final Uri DealsProviderUri = Uri.parse("content://com.zaks.ayala.provider.deals/items");
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
        final RecyclerView.LayoutManager dealsLayoutManager = new LinearLayoutManager(getContext());
        dealsRecyclerView.setLayoutManager(dealsLayoutManager);
        getActivity().getSupportLoaderManager().initLoader(DealsLoaderId, null, this);
        return view;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(getContext(), DealsProviderUri, Deal.getProjectionMap(), null, null, DealsContract.DealEntry.Column_FromDate);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        dealsAdapter = new DealListAdapter(getContext(), data);
        dealsRecyclerView.setAdapter(dealsAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
