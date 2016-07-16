package com.zaks.ayala.nearbydeals.ui;

import android.app.Activity;
import android.app.ActivityOptions;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.app.DialogFragment;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zaks.ayala.nearbydeals.R;
import com.zaks.ayala.nearbydeals.bl.models.Deal;
import com.zaks.ayala.nearbydeals.common.Utilities;

/**
 * Created by אילה on 02-Jul-16.
 */
public class DealListAdapter extends CursorRecyclerViewAdapter<DealListAdapter.ViewHolder> {
    Context adapterContext;

    public DealListAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        adapterContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView card;
        public TextView description;
        public TextView address;
        public TextView supplierName;
        public ImageView image;

        public ViewHolder(View view) {
            super(view);
            description = (TextView) view.findViewById(R.id.deal_item_description);
            address = (TextView) view.findViewById(R.id.deal_item_address);
            supplierName = (TextView) view.findViewById(R.id.deal_item_supplier_name);
            image = (ImageView) view.findViewById(R.id.deal_item_image);
            card = (CardView) view.findViewById(R.id.deal_item_card);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.deal_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final Cursor cursor) {
        final Deal deal = Deal.fromCursor(cursor);
        viewHolder.description.setText(deal.getDescription());
        viewHolder.address.setText(deal.getAddress());
       viewHolder.supplierName.setText(deal.getSupplierName());
        viewHolder.image.setImageResource(Utilities.getCategoryIcon(deal.getCategory().getDescription()));
        viewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDealDialog(deal);
            }
        });

    }
    private void showDealDialog(Deal deal) {
        FragmentTransaction ft = ((AppCompatActivity)adapterContext).getSupportFragmentManager().beginTransaction();
        Fragment prev = ((AppCompatActivity)adapterContext).getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = DealItemFragment.newInstance(deal);
        newFragment.show(ft, "dialog");
    }
}
