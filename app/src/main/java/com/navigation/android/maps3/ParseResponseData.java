package com.navigation.android.maps3;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by acer on 15-04-2018.
 */

public class ParseResponseData extends AsyncTask<String, Void, List<List<HashMap<String, String>>>> {

    private GoogleMap mMap;
    private Context context;

    public ParseResponseData(GoogleMap mMap, Context context){
        this.mMap = mMap;
        this.context = context;
    }

    // parsing the data downloaded in JSON form
    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonResponse) {
        List<List<HashMap<String, String>>> routes = null;
        JSONObject jsonObject = null;

        Log.e("doInBackground", "inside ParseResponseData");
        try {
            jsonObject = new JSONObject(jsonResponse[0]);
            DataParser parser = new DataParser();
            routes = parser.parse(jsonObject);
            Log.e("ParseResponseData", String.valueOf(routes));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return routes;
    }

    // plotting the route
    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {

        PolylineOptions polylineOptions = null;
        List<LatLng> points;
        Log.e("ParseResponseData", String.valueOf(result));
//        if (result != null) {

        for (int i = 0; i < result.size(); i++) {
            polylineOptions = new PolylineOptions();
            points = new ArrayList<>();

            List<HashMap<String, String>> path = result.get(i);

            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);

                Double latitude = Double.valueOf(point.get("lat"));
                Double longitude = Double.valueOf(point.get("lng"));

                LatLng position = new LatLng(latitude, longitude);

                points.add(position);
            }

            polylineOptions.addAll(points);
            Log.e("PolyLines", String.valueOf(polylineOptions));
            polylineOptions.width(20);
            polylineOptions.color(Color.BLUE);
            polylineOptions.geodesic(true);

        }

        if (polylineOptions != null && this.mMap!= null) {
            mMap.addPolyline(polylineOptions);
        }else {
            Log.e("polylines", String.valueOf(polylineOptions));
            Log.e("map", String.valueOf(this.mMap));
            Toast.makeText(context, "Unable to plot route Try Again!", Toast.LENGTH_SHORT).show();
        }

    }
}