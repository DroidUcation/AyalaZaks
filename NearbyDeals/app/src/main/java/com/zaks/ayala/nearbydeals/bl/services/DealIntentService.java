package com.zaks.ayala.nearbydeals.bl.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
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
    private static final String ACTION_DELETE = "com.zaks.ayala.nearbydeals.bl.services.action.deal.delete";
    private static final String ACTION_CLONE = "com.zaks.ayala.nearbydeals.bl.services.action.deal.clone";

    // TODO: Rename parameters
    private static final String EXTRA_DEAL = "com.zaks.ayala.nearbydeals.bl.services.extra.DEAL";
    private static final String EXTRA_DEALID = "com.zaks.ayala.nearbydeals.bl.services.extra.DEALID";
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

    public static void startActionDelete(Context context, int dealID) {
        Intent intent = new Intent(context, DealIntentService.class);
        intent.setAction(ACTION_CLONE);
        intent.putExtra(EXTRA_DEALID, dealID);
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
            } else if (ACTION_CLONE.equals(action)) {
                handleActionClone();
            } else if (ACTION_DELETE.equals(action)) {
                final int dealID = (int) intent.getIntExtra(EXTRA_DEALID, 0);
                handleActionDelete(dealID);
            }
        }
    }


    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionSave(Deal deal) {
        try {
            Uri res = getContentResolver().insert(DealsProviderURL, deal.getContentValues(true));
            Integer id = Integer.valueOf(res.getLastPathSegment());
            deal.setId(id);
            firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference(DealFirebaseName);
            Gson gson = new Gson();
            firebaseDatabaseReference.child(id.toString()).setValue(gson.toJson(deal));
        } catch (Exception ex) {
            Log.e(getClass().getSimpleName(), "Insert new deal failed! exception:" + ex.toString());
        }
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionEdit(Deal deal) {
        try {
            int res = getContentResolver().update(DealsProviderURL, deal.getContentValues(false), DealsContract.DealEntry.Column_ID + " = " + deal.getId(), null);
            firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference(DealFirebaseName);
            Gson gson = new Gson();
            firebaseDatabaseReference.child(((Integer) deal.getId()).toString()).setValue(gson.toJson(deal));
        } catch (Exception ex) {
            Log.e(getClass().getSimpleName(), "Update deal failed! exception:" + ex.toString());
        }
    }

    private void handleActionDelete(int dealID) {
        try {
            int res = getContentResolver().delete(DealsProviderURL, DealsContract.DealEntry.Column_ID + " = " + dealID, null);
            firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference(DealFirebaseName);
            Gson gson = new Gson();
            firebaseDatabaseReference.child(((Integer) dealID).toString()).removeValue();
        } catch (Exception ex) {
            Log.e(getClass().getSimpleName(), "Delete deal failed! exception:" + ex.toString());
        }
    }

    private void handleActionClone() {
        try {
            firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference(DealFirebaseName);
            final Gson gson = new Gson();
//            firebaseDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot snapshot) {
//                    Deal deal;
//                    for (DataSnapshot child : snapshot.getChildren()) {
//                        try {
//                            deal = gson.fromJson(child.getValue().toString(), Deal.class);
//                            Cursor c = getContentResolver().query(DealsProviderURL, Deal.getProjectionMap(), DealsContract.DealEntry.Column_ID + " = " + deal.getId(), null, null);
//                            Uri res;
//                            if (!c.moveToFirst())
//                                res = getContentResolver().insert(DealsProviderURL, deal.getContentValues(true));
//                        } catch (Exception ex) {
//                            Log.e(getClass().getSimpleName(), "Insert single deal from firebase failed! exception:" + ex.toString());
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    Log.e(getClass().getSimpleName(), "firebase call cancelled! errer:" + databaseError.toString());
//                }
//            });

            firebaseDatabaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    try {
                        Deal deal = gson.fromJson(dataSnapshot.getValue().toString(), Deal.class);
                        Cursor c = getContentResolver().query(DealsProviderURL, Deal.getProjectionMap(), DealsContract.DealEntry.Column_ID + " = " + deal.getId(), null, null);
                        Uri res;
                        if (!c.moveToFirst())
                            res = getContentResolver().insert(DealsProviderURL, deal.getContentValues(true));
                    } catch (Exception ex) {
                        Log.e(getClass().getSimpleName(), "Insert new deal from firebase failed! exception:" + ex.toString());
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    try {
                        Deal deal = gson.fromJson(dataSnapshot.getValue().toString(), Deal.class);
                        int res = getContentResolver().update(DealsProviderURL, deal.getContentValues(false), DealsContract.DealEntry.Column_ID + " = " + deal.getId(), null);
                    } catch (Exception ex) {
                        Log.e(getClass().getSimpleName(), "Update new deal from firebase failed! exception:" + ex.toString());
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    try {
                        Deal deal = gson.fromJson(dataSnapshot.getValue().toString(), Deal.class);
                        int res = getContentResolver().delete(DealsProviderURL, DealsContract.DealEntry.Column_ID + " = " + deal.getId(), null);
                    } catch (Exception ex) {
                        Log.e(getClass().getSimpleName(), "Delete new deal from firebase failed! exception:" + ex.toString());
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(getClass().getSimpleName(), "firebase child call cancelled! error:" + databaseError.toString());
                }
            });
        } catch (Exception ex) {
            Log.e(getClass().getSimpleName(), "Get all deals from firebase failed! exception:" + ex.toString());
        }
    }
}
