package com.navigation.android.maps3;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private List<Address> addressList;
    private Geocoder geocoder;
    private Location mLastLocation;
    private int showMarker = 0;
    private Intent intent;
    private List<LatLng> checkpoints;
    private List<String> checkpointNames;
    private LatLng origin, destination;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        addressList = new ArrayList<Address>();
        intent = getIntent();
        checkpoints = MapActivity.locations;
        checkpointNames = MapActivity.placesList;
    }


    // method to set put a marker at users current location
    private void setLocation(Location mLocation) {

        LatLng userLocation = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        if (showMarker == 1) {
            mMap.addMarker(new MarkerOptions().position(userLocation).title("your location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(userLocation.latitude, userLocation.longitude), 12.0f));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults != null && grantResults.length > 0) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                mLocationManager.requestLocationUpdates(mLocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
                mLastLocation = mLocationManager.getLastKnownLocation(mLocationManager.GPS_PROVIDER);
                setLocation(mLastLocation);
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        int positon = intent.getIntExtra("position", 0);
        MapActivity.setFirsttAdd(false);

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };

        // checking the version to request GPS permission from the user
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationManager.requestLocationUpdates(mLocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
                mLastLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                mMap.setMyLocationEnabled(true);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }

        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

            mLocationManager.requestLocationUpdates(mLocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
            mLastLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            mMap.setMyLocationEnabled(true);
        }

        // checking if no checkpoint is added
        if (positon == 0 && intent.getIntExtra("first", 0) == 1) {

            LatLng loc;
            if (mLastLocation != null) {
                loc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            } else {
                loc = new LatLng(28.7041, 77.1025);
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 11));

        } else if (positon == 0 && intent.getIntExtra("first", 0) != 1) {

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MapActivity.locations.get(1), 11));
            plot();
        }
        // if elment from the list other than that present at 0th index is clicked than show that place on the map
        else {

            LatLng placeLocation = MapActivity.locations.get(positon);
            String placeName = MapActivity.placesList.get(positon);
            plot();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLocation, 12));
        }

        // adding a marker on clicking the map at the clicked point
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                String address = "";
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        int addlinesize = addressList.get(0).getMaxAddressLineIndex();
                        for (int i = 0; i < addlinesize; i++) {
                            if (addressList.get(0).getAddressLine(i) != null) {
                                address += addressList.get(0).getAddressLine(i);
                            }
                        }

                        if (addressList.get(0).getThoroughfare() != null) {
                            if (addressList.get(0).getSubThoroughfare() != null) {
                                address += addressList.get(0).getSubThoroughfare() + ", ";
                            }
                            address += addressList.get(0).getThoroughfare();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (address == "") {

                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm  dd-mm-yyyy");
                    address = dateFormat.format(new Date());
                }

                MarkerOptions options = new MarkerOptions();
                options.position(latLng)
                        .title(address);

                if (MapActivity.placesList.size() == 1) {
                    origin = latLng;
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else if (MapActivity.placesList.size() == 2) {
                    destination = latLng;
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                } else {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }


                // adding a marker at the clicked positon
                mMap.addMarker(options);
                Toast.makeText(getApplicationContext(), "Location saved", Toast.LENGTH_SHORT).show();
                MapActivity.placesList.add(address);
                MapActivity.locations.add(latLng);
                MapActivity.adapter.notifyDataSetChanged();

                if (origin != null && destination != null) {
                    url = getUrl(origin, destination);
                    Log.e("URL is", url);

                    MyDownloadTask downloadTask = new MyDownloadTask(mMap);
                    downloadTask.execute(url);
                }
            }
        });

    }

    // helper method to generate URL
    private String getUrl(LatLng orgn, LatLng dest) {

        String origin = "origin=" + orgn.latitude + "," + orgn.longitude;
        String destination = "destination=" + dest.latitude + "," + dest.longitude;
        ;
        String sensor = "sensor=false";
        String parameters = origin + "&" + destination + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    private void plot() {
        for (int i = 1; i < checkpoints.size(); i++) {

            // adding a marker at the clicked positon
            MarkerOptions options = new MarkerOptions();
            options.position(checkpoints.get(i))
                    .title(checkpointNames.get(i));

            if (i == 1) {
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                origin = checkpoints.get(1);
            } else if (i == 2) {
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                destination = checkpoints.get(2);
            } else {
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }

            // adding marker on the checkpoint
            mMap.addMarker(options);

        }

        if (origin != null && destination != null) {
            url = getUrl(origin, destination);
            Log.e("URL is", url);
            MyDownloadTask downloadTask = new MyDownloadTask(mMap);
            downloadTask.execute(url);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMap.clear();
    }
}
