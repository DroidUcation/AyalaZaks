package com.zaks.ayala.nearbydeals.ui;

import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.zaks.ayala.nearbydeals.R;
import com.zaks.ayala.nearbydeals.bl.SupplierHelper;
import com.zaks.ayala.nearbydeals.bl.models.Supplier;
import com.zaks.ayala.nearbydeals.common.PreferencesHelper;
import com.zaks.ayala.nearbydeals.data.datacontracts.SuppliersContract;

public class MainActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth mFirebaseAuth;
    private GoogleApiClient mGoogleApiClient;
    private static final Uri SuppliersProviderUri = Uri.parse("content://com.zaks.ayala.provider.suppliers/items");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }


    public void GoToDealsMap(View view) {
        if (PreferencesHelper.isExistPreferences(this)) {
            Intent i = new Intent(this, DealsActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(this, SettingActivity.class);
            startActivity(i);
        }
    }

    public void GoToSupplierLogin(View view) {
//        Intent i = new Intent(this, SupplierDealsListActivity.class);
//        startActivity(i);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {

            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            checkIfNewSupplier(acct.getEmail());
                        }
                    }
                });
    }

    private void checkIfNewSupplier(String email) {
        SupplierHelper helper = new SupplierHelper(this);
        Supplier supplier = helper.GetSupplierByEmail(email);
        if (supplier != null)
        {
            Intent i = new Intent(MainActivity.this, SupplierDealsListActivity.class);
            i.putExtra("supplierEmail",email);
            startActivity(i);
            finish();
        }
        else
        {
            Intent i = new Intent(MainActivity.this, SupplierDetailsActivity.class);
            startActivity(i);
            finish();
        }

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull com.google.android.gms.common.ConnectionResult connectionResult) {
        Toast.makeText(this, "No Connection!", Toast.LENGTH_SHORT).show();
    }
}
