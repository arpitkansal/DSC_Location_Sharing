package com.navigation.android.maps3;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class CheckpPoints extends AppCompatActivity {

    public static List<String> mPlacesList;
    public static List<LatLng> mLocationsList;
    public static ArrayAdapter mAdapter;
    private ListView checkpointListView;
    private Button mTripButton, mAddCheckpoint;
    public static boolean firstAdd = true;

    public static void setFirstAdd(boolean firsttAdd) {
        CheckpPoints.firstAdd = firsttAdd;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkp_points);

        mPlacesList = new ArrayList<>();
        mLocationsList = new ArrayList<>();
        checkpointListView = findViewById(R.id.checkpoints_list);

        mTripButton = findViewById(R.id.make_trip);
        mAddCheckpoint = findViewById(R.id.add_checkpoint);
//        mPlacesList.add("Add checkpoints");
//        mLocationsList.add(new LatLng(0, 0));

        mAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mPlacesList);
        checkpointListView.setAdapter(mAdapter);

        mAddCheckpoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity2.class);
                if (mPlacesList.size() == 0) {
                    intent.putExtra("first", 0);
                }
//                intent.putExtra("position", position);
                intent.putStringArrayListExtra("placesList", (ArrayList<String>) mPlacesList);
                intent.putExtra("firstAdd", firstAdd);
                startActivity(intent);
            }
        });

//        checkpointListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.e("inside", "On Click ");
//
//                Intent intent = new Intent(getApplicationContext(), MapsActivity2.class);
//                if (mPlacesList.size() == 1) {
//                    intent.putExtra("first", 1);
//                }
//                intent.putExtra("position", position);
//                intent.putStringArrayListExtra("placesList", (ArrayList<String>) mPlacesList);
//                intent.putExtra("firstAdd", firstAdd);
//                startActivity(intent);
//            }
//        });

        // creating a new trip
        mTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocationsList.clear();
                mPlacesList.clear();
//                mPlacesList.add("Add checkpoints");
//                mLocationsList.add(new LatLng(0, 0));
                mAdapter.notifyDataSetChanged();

            }
        });

    }
}
