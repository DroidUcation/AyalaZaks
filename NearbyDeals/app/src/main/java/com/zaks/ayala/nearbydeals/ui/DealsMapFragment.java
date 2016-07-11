package com.zaks.ayala.nearbydeals.ui;


import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.zaks.ayala.nearbydeals.R;
import com.zaks.ayala.nearbydeals.bl.models.Deal;
import com.zaks.ayala.nearbydeals.common.PreferencesHelper;
import com.zaks.ayala.nearbydeals.common.Utilities;
import com.zaks.ayala.nearbydeals.data.datacontracts.CategoriesContract;
import com.zaks.ayala.nearbydeals.data.datacontracts.DealsContract;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class DealsMapFragment extends Fragment implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, LoaderManager.LoaderCallbacks<Cursor>
    {

        private GoogleMap mMap;
        Location mLastLocation;
        private GoogleApiClient mGoogleApiClient;
        private LocationRequest mLocationRequest;
        String lat, lon;
        private static final int LOCATION_PERMISSION = 583;
        HashMap<String,Deal> dealHashMap=new HashMap<>();
        private static final int DealsLoaderId = 1;
        private static final Uri DealsProviderUri = Uri.parse("content://com.zaks.ayala.provider.deals/items");



        public DealsMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_deals_map, container, false);
        buildGoogleApiClient();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mLocationRequest = new LocationRequest();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return  v;
    }
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    if(dealHashMap.containsKey(marker.getId())) {
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        DealItemFragment dealItem = new DealItemFragment();
                        dealItem.setDeal(dealHashMap.get(marker.getId()));
                        dealItem.show(fm, null);
                        return true;
                    }
                    return  true;
                }
            });

        }

        synchronized void buildGoogleApiClient() {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        @Override
        public void onStart() {
            super.onStart();
            mGoogleApiClient.connect();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mGoogleApiClient.disconnect();
        }

        @Override
        public void onConnected(Bundle bundle) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);
            } else {
                GetLocation();
            }

            getActivity().getSupportLoaderManager().initLoader(DealsLoaderId, null, this);
        }

        private void GetLocation() {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED) {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLastLocation != null) {
                    setMapCenter();
                }
                else
                {

                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
                }
            }
        }

        private void setMapCenter() {
            LatLng loc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(loc));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 14));
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
            switch (requestCode) {
                case LOCATION_PERMISSION: {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        GetLocation();
                    } else {
                        SetDefaultLocation();
                    }
                    return;
                }

            }
        }

        private void SetDefaultLocation() {
        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onLocationChanged(Location location) {
            mLastLocation=location;
            setMapCenter();
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        }


        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            buildGoogleApiClient();
        }


        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            ArrayList userCategories = PreferencesHelper.GetAllUserCategories(getActivity());
            String categories = TextUtils.join("', '", userCategories);
            String selection = CategoriesContract.CategoryEntry.addPrefix(CategoriesContract.CategoryEntry.Column_Description)
                    + " IN ('" + categories + "')";
            return new CursorLoader(getActivity(), DealsProviderUri, Deal.getProjectionMap(), selection, null, DealsContract.DealEntry.Column_FromDate);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if(data.moveToFirst()) {
                do {
                    addMarkers(Deal.fromCursor(data));
                }
                while (data.moveToNext());
            }
        }

        private void addMarkers(Deal deal) {
            Marker marker=mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(Utilities.getCategoryIcon(deal.getCategory().getDescription())))
                    .position(new LatLng(deal.getLatitude(), deal.getLongitude())));
            dealHashMap.put(marker.getId(),deal);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
}
