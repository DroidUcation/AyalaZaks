package com.zaks.ayala.nearbydeals.ui;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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
 * Created by אילה on 20-Jun-16.
 */
public class SupplierDealsListAdapter extends CursorRecyclerViewAdapter<SupplierDealsListAdapter.ViewHolder> {
    Context adapterContext;
    public SupplierDealsListAdapter(Context context, Cursor cursor){
        super(context,cursor);
        adapterContext=context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView card;
        public TextView description;
        public TextView address;
        public TextView dates;
        public ImageView image;
        public Button editButton;
        public Button deleteButton;
        public ViewHolder(View view) {
            super(view);
            description = (TextView)view.findViewById(R.id.deal_list_item_description);
            address = (TextView)view.findViewById(R.id.deal_list_item_address);
            dates = (TextView)view.findViewById(R.id.deal_list_item_dates);
            image = (ImageView) view.findViewById(R.id.deal_list_item_image);
            editButton=(Button)view.findViewById(R.id.card_button_edit);
            deleteButton=(Button)view.findViewById(R.id.card_button_delete);
            card=(CardView)view.findViewById(R.id.deal_list_item_card);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.deal_list_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final Cursor cursor) {
        final Deal deal = Deal.fromCursor(cursor);
        viewHolder.description.setText(deal.getDescription());
        viewHolder.address.setText(deal.getAddress());
        viewHolder.dates.setText(Utilities.getDateForDisplay(deal.getFromDate())+" - "+Utilities.getDateForDisplay(deal.getToDate()));
        viewHolder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(adapterContext,EditDealActivity.class);
                i.putExtra("Deal",deal);
                viewHolder.card.setDrawingCacheEnabled(true);
                viewHolder.card.setPressed(false);
                viewHolder.card.refreshDrawableState();
                Bitmap bitmap = viewHolder.card.getDrawingCache();
                ActivityOptions opts = ActivityOptions.makeThumbnailScaleUpAnimation(
                        viewHolder.card, bitmap, 0, 0);
                // Request the activity be started, using the custom animation options.
                adapterContext.startActivity(i,opts.toBundle());
                viewHolder.card.setDrawingCacheEnabled(false);

            }
        });
    }
}