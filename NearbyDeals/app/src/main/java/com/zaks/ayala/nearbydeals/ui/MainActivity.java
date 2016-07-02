package com.zaks.ayala.nearbydeals.ui;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.zaks.ayala.nearbydeals.R;
import com.zaks.ayala.nearbydeals.common.PreferencesHelper;

public class MainActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
       {
    private static final int SIGNED_IN = 0;
    private static final int STATE_SIGNING_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;
    private static final int RC_SIGN_IN = 0;


    private GoogleApiClient mGoogleApiClient;
    private int mSignInProgress;
    private PendingIntent mSignInIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGoogleApiClient = buildGoogleApiClient();
    }
    private GoogleApiClient buildGoogleApiClient() {
        return new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(new Scope("email"))
                .build();
    }
    public void GoToDealsMap(View view) {
        if(PreferencesHelper.isExistPreferences(this)) {
            Intent i = new Intent(this, DealsActivity.class);
            startActivity(i);
        }
        else {
            Intent i = new Intent(this, SettingActivity.class);
            startActivity(i);
        }
    }

    public void GoToSupplierLogin(View view) {
        Intent i=new Intent(this, SupplierDealsListActivity.class);
        startActivity(i);
    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }
    @Override
    public void onConnected(Bundle connectionHint) {
        try {
            String emailAddress = Plus.AccountApi.getAccountName(mGoogleApiClient);

        }
        catch(Exception ex){

        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
