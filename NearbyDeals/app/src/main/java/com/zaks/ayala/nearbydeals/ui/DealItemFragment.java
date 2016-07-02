package com.zaks.ayala.nearbydeals.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zaks.ayala.nearbydeals.R;
import com.zaks.ayala.nearbydeals.bl.models.Deal;

public class DealItemFragment extends DialogFragment {
    Deal theDeal;
    View v;

    public DealItemFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void setDeal(Deal deal) {
        theDeal = deal;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_deal_item, container, false);
        setFields();
        return v;
    }

    public void setFields() {
        TextView desc = (TextView) v.findViewById(R.id.deal_item_description);
        TextView supplierName = (TextView) v.findViewById(R.id.deal_item_supplier_name);
        TextView address = (TextView) v.findViewById(R.id.deal_item_address);
        desc.setText(theDeal.getDescription());
        supplierName.setText(theDeal.getSupplierName());
        address.setText(theDeal.getAddress());

    }


}
