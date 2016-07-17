package com.zaks.ayala.nearbydeals.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zaks.ayala.nearbydeals.R;
import com.zaks.ayala.nearbydeals.bl.models.Deal;
import com.zaks.ayala.nearbydeals.common.Utilities;

public class DealItemFragment extends DialogFragment {
    Deal theDeal;

    static DealItemFragment newInstance(Deal deal) {
        DealItemFragment dealItem = new DealItemFragment();
        Bundle args = new Bundle();
        args.putSerializable("deal", deal);
        dealItem.setArguments(args);
        return dealItem;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
    public DealItemFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theDeal = (Deal) getArguments().getSerializable("deal");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_deal_item, container, false);
        setFields(v);
        return v;
    }

    public void setFields(View v) {
        if (theDeal != null) {
            ImageView icon = (ImageView) v.findViewById(R.id.deal_popup_icon);
            TextView desc = (TextView) v.findViewById(R.id.deal_popup_description);
            TextView supplierName = (TextView) v.findViewById(R.id.deal_popup_supplier_name);
            TextView address = (TextView) v.findViewById(R.id.deal_popup_address);
            TextView phone = (TextView) v.findViewById(R.id.deal_popup_supplier_phone);
            TextView email = (TextView) v.findViewById(R.id.deal_popup_supplier_email);
            RelativeLayout card = (RelativeLayout) v.findViewById(R.id.deal_popup_image);
            LinearLayout addressBlock = (LinearLayout) v.findViewById(R.id.deal_popup_address_block);
            LinearLayout phoneBlock = (LinearLayout) v.findViewById(R.id.deal_popup_phone_block);
            LinearLayout emailBlock = (LinearLayout) v.findViewById(R.id.deal_popup_email_block);
            card.setBackgroundResource(Utilities.getCategoryBackground(theDeal.getCategory().getDescription()));
            icon.setImageResource(Utilities.getCategoryIcon(theDeal.getCategory().getDescription()));
            desc.setText(theDeal.getDescription());
            supplierName.setText(theDeal.getSupplierName());
            address.setText(theDeal.getAddress());
            phone.setText(theDeal.getSupplierPhone());
            email.setText(theDeal.getSupplierEmail());
            addressBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("geo:" +theDeal.getLatitude() + "," +
                            theDeal.getLongitude() + "?q=" +theDeal.getAddress()));
                    getActivity().startActivity(intent);
                }
            });
            phoneBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uri = "tel:" +theDeal.getSupplierPhone() ;
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                }
            });
            emailBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(Intent.ACTION_SENDTO,Uri.parse("mailto:"+theDeal.getSupplierEmail()));
                    startActivity(i);
                }
            });

        }

    }


}
