package com.zaks.ayala.nearbydeals.bl.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.location.Address;
import android.net.Uri;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.zaks.ayala.nearbydeals.bl.models.Deal;
import com.zaks.ayala.nearbydeals.bl.models.Supplier;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SupplierIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_SAVE = "com.zaks.ayala.nearbydeals.bl.services.action.savesupplier";
    private static final String ACTION_CLONE = "com.zaks.ayala.nearbydeals.bl.services.action.clone";
   // private static final String ACTION_BAZ = "com.zaks.ayala.nearbydeals.bl.services.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_SUPPLIER = "com.zaks.ayala.nearbydeals.bl.services.extra.supplier";
   // private static final String EXTRA_PARAM2 = "com.zaks.ayala.nearbydeals.bl.services.extra.PARAM2";

    private static final Uri SuppliersProviderUri = Uri.parse("content://com.zaks.ayala.provider.suppliers/items");
    private DatabaseReference firebaseDatabaseReference;
    private  static final String SupplierFirebaseName="Suppliers";

    public SupplierIntentService() {
        super("SupplierIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startSaveSupplier(Context context, Supplier supplier) {
        Intent intent = new Intent(context, SupplierIntentService.class);
        intent.setAction(ACTION_SAVE);
        intent.putExtra(EXTRA_SUPPLIER, supplier);
        //intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }


    public static void startActionClone(Context context) {
        Intent intent = new Intent(context, SupplierIntentService.class);
        intent.setAction(ACTION_CLONE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SAVE.equals(action)) {
                final Supplier supplier = (Supplier) intent.getSerializableExtra(EXTRA_SUPPLIER);
                handleActionSave(supplier);
            } else if (ACTION_CLONE.equals(action)) {
                handleActionClone();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionSave(Supplier supplier) {
        firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference(SupplierFirebaseName);
        Gson gson=new Gson();
        firebaseDatabaseReference.child(((Integer)supplier.getId()).toString()).setValue(gson.toJson(supplier));
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionClone() {
        firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference(SupplierFirebaseName);
        final Gson gson = new Gson();
        firebaseDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Supplier supplier;
                for (DataSnapshot child : snapshot.getChildren()) {
                    supplier= gson.fromJson(child.getValue().toString(), Supplier.class);
                    Uri res = getContentResolver().insert(SuppliersProviderUri, supplier.getContentValues());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
