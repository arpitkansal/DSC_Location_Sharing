package com.navigation.android.maps3;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Arpit on 17-04-2018.
 */

public class SearchTask extends AsyncTask<LatLng, Void, LatLng> {

    private GoogleMap mMap;
    private Context context;
    SearchTask(GoogleMap map, Context context){
        this.mMap = map;
        this.context = context;
    }

    @Override
    protected LatLng doInBackground(LatLng... add) {
        LatLng addLatlng = null;
        if (add != null && add.length > 0 && add[0] !=null){
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            try {
                List<Address> results = geocoder.getFromLocation(add[0].latitude, add[0].longitude,1);
                Log.e("b", String.valueOf(add));
                if (results.size() == 0) {
                    Log.e("b", "fail");
                    addLatlng = new LatLng(MapsActivity2.mLastLocation.getLatitude(), MapsActivity2.mLastLocation.getLongitude());
                }else {
                    Address address = results.get(0);
                    addLatlng = new LatLng(address.getLatitude(), address.getLongitude());
                    return addLatlng;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return addLatlng;
    }

    @Override
    protected void onPostExecute(LatLng addlatLng) {
        this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(addlatLng, 12));
    }
}

