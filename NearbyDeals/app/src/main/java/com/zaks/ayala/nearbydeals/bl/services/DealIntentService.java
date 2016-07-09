package com.zaks.ayala.nearbydeals.bl.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.zaks.ayala.nearbydeals.bl.models.Deal;
import com.zaks.ayala.nearbydeals.data.datacontracts.DealsContract;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DealIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_SAVE = "com.zaks.ayala.nearbydeals.bl.services.action.deal.save";
    private static final String ACTION_EDIT = "com.zaks.ayala.nearbydeals.bl.services.action.deal.edit";
    private static final String ACTION_CLONE = "com.zaks.ayala.nearbydeals.bl.services.action.deal.clone";

    // TODO: Rename parameters
    private static final String EXTRA_DEAL = "com.zaks.ayala.nearbydeals.bl.services.extra.DEAL";
    //  private static final String EXTRA_PARAM2 = "com.zaks.ayala.nearbydeals.bl.services.extra.PARAM2";
    private static final Uri DealsProviderURL = Uri.parse("content://com.zaks.ayala.provider.deals/items");
    private DatabaseReference firebaseDatabaseReference;
    private static final String DealFirebaseName = "Deals";

    public DealIntentService() {
        super("DealIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionSave(Context context, Deal deal) {
        Intent intent = new Intent(context, DealIntentService.class);
        intent.setAction(ACTION_SAVE);
        intent.putExtra(EXTRA_DEAL, deal);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionEdit(Context context, Deal deal) {
        Intent intent = new Intent(context, DealIntentService.class);
        intent.setAction(ACTION_EDIT);
        intent.putExtra(EXTRA_DEAL, deal);
        context.startService(intent);
    }

    public static void startActionClone(Context context) {
        Intent intent = new Intent(context, DealIntentService.class);
        intent.setAction(ACTION_CLONE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SAVE.equals(action)) {
                final Deal deal = (Deal) intent.getSerializableExtra(EXTRA_DEAL);
                handleActionSave(deal);
            } else if (ACTION_EDIT.equals(action)) {
                final Deal deal = (Deal) intent.getSerializableExtra(EXTRA_DEAL);
                handleActionEdit(deal);
            }else if (ACTION_CLONE.equals(action)){
                handleActionClone();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionSave(Deal deal) {
        Uri res = getContentResolver().insert(DealsProviderURL, deal.getContentValues(true));
        Integer id = Integer.valueOf(res.getLastPathSegment());
        deal.setId(id);
        firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference(DealFirebaseName);
        Gson gson = new Gson();
        firebaseDatabaseReference.child(id.toString()).setValue(gson.toJson(deal));
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionEdit(Deal deal) {
        int res = getContentResolver().update(DealsProviderURL, deal.getContentValues(false), DealsContract.DealEntry.Column_ID+" = "+deal.getId(), null);
        firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference(DealFirebaseName);
        Gson gson = new Gson();
        firebaseDatabaseReference.child(((Integer)deal.getId()).toString()).setValue(gson.toJson(deal));
    }

    private void handleActionClone() {
        firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference(DealFirebaseName);
        final Gson gson = new Gson();
        firebaseDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Deal deal;
                for (DataSnapshot child : snapshot.getChildren()) {
                    deal= gson.fromJson(child.getValue().toString(), Deal.class);
                    Uri res = getContentResolver().insert(DealsProviderURL, deal.getContentValues(true));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
