package com.navigation.android.maps3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewContacts extends AppCompatActivity {

    //database object
    private SQLiteDatabase DB = null;
    private DataBase database;
    private ListView listView;
    private TextView contactName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contacts);

        listView = (ListView) findViewById(R.id.listview_contact);



    }
    @Override
    protected void onResume()
    {
        super.onResume();
        //GET THE DATABSE HERE
        database = new DataBase(getApplicationContext());
        DB = database.getDB();

        Log.d("act", "view contact");




        ContactsDBHelper dbHelper = new ContactsDBHelper(this);
        DB = dbHelper.getWritableDatabase();
        final Cursor cursor = DB.rawQuery("SELECT * FROM CONTACTS", null);

        while(cursor.moveToNext()){
            Log.d("cursor", cursor.getString(cursor.getColumnIndexOrThrow(ContactsDBHelper.colName)));
        }

        ContactCursorAdapter contactCursorAdapter = new ContactCursorAdapter(this, cursor);
// Attach cursor adapter to the ListView
        listView.setAdapter(contactCursorAdapter);

        contactCursorAdapter.changeCursor(cursor);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ContactDetailsActivity.class);
                int id = (int) view.getTag();
                Log.d("id", Integer.toString(id));
                intent.putExtra("contact_id", id);
                startActivity(intent);
            }
        });

        listView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
//                Intent intent = getIntent();
//                int id = intent.getIntExtra("coords_id", 0);
//
//                Cursor cursorCoords = DB.rawQuery("SELECT * FROM COORDINATES WHERE _id = " + id, null);
//
//                Log.d("id in vw", Integer.toString(id));
//                cursorCoords.moveToFirst();
//
//                String s_latitude = cursorCoords.getString(cursorCoords.getColumnIndexOrThrow(ContactsDBHelper.colLatitude));
//                String s_longitude = cursorCoords.getString(cursorCoords.getColumnIndexOrThrow(ContactsDBHelper.colLongitude));
//                String msgToSend = s_latitude + " , " + s_longitude;
//                Log.d("Msg to be send", msgToSend);
//                Intent msgSend = new Intent();
//                msgSend.putExtra(msgToSend,0);
//                startActivity(msgSend);
               // Toast.makeText(this, "Why did you do that? That REALLY hurts!!!", Toast.LENGTH_LONG).show();
               Toast.makeText(getApplicationContext(),"good",Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }

}

