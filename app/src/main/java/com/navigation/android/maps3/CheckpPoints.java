package com.navigation.android.maps3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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


    private SQLiteDatabase DB = null;
    private DataBase database;
    private ListView listViewCoordsDB;

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

        listViewCoordsDB = (ListView) findViewById(R.id.listview_coords_db);

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

                DB.execSQL("DELETE FROM COORDINATES");
//                mPlacesList.add("Add checkpoints");
//                mLocationsList.add(new LatLng(0, 0));
                mAdapter.notifyDataSetChanged();

            }
        });

    }

    protected void onResume() {
        super.onResume();
        //GET THE DATABSE HERE
        database = new DataBase(getApplicationContext());
        DB = database.getDB();

        Log.d("act", "view Coords");

        ContactsDBHelper dbHelper = new ContactsDBHelper(this);
        DB = dbHelper.getWritableDatabase();
        final Cursor cursor = DB.rawQuery("SELECT * FROM COORDINATES", null);

        while (cursor.moveToNext()) {
            Log.d("cursor", cursor.getString(cursor.getColumnIndexOrThrow(ContactsDBHelper.colLatitude)));
        }

       CoordsCursorAdapter coordsCursorAdapter = new CoordsCursorAdapter(this,cursor);

// Attach cursor adapter to the ListView
        listViewCoordsDB.setAdapter(coordsCursorAdapter);

        coordsCursorAdapter.changeCursor(cursor);

        listViewCoordsDB.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Intent intent = new Intent(getApplicationContext(), ViewContacts.class);
                                                int id = (int) view.getTag();
                                                Log.d("id", Integer.toString(id));
                                                intent.putExtra("coords_id", id);
                                                startActivity(intent);
                                            }
                                        }
        );

    }
}
