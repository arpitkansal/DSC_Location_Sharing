package com.navigation.android.maps3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
        Button deleteAllContact = (Button)findViewById(R.id.button_delete_all);

        deleteAllContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DB.execSQL("DELETE FROM CONTACTS");
            }
        });



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
//                Intent intent = new Intent(getApplicationContext(), ContactDetailsActivity.class);
//                int id = (int) view.getTag();
//                Log.d("id", Integer.toString(id));
//                intent.putExtra("contact_id", id);
//                startActivity(intent);
//

                Intent intent = getIntent();
                int id = intent.getIntExtra("coords_id", 0);

                Cursor cursorCoords = DB.rawQuery("SELECT * FROM COORDINATES WHERE _id = " + id, null);

                Log.d("id in vw", Integer.toString(id));
                cursorCoords.moveToFirst();

                String s_latitude = cursorCoords.getString(cursorCoords.getColumnIndexOrThrow(ContactsDBHelper.colLatitude));
                String s_longitude = cursorCoords.getString(cursorCoords.getColumnIndexOrThrow(ContactsDBHelper.colLongitude));
                String msgToSend = s_latitude + " , " + s_longitude;
                Log.d("msg",msgToSend);
                String phoneno = cursor.getString(cursor.getColumnIndexOrThrow(ContactsDBHelper.colPhone));
                Log.d("Phn_no",phoneno);

                Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"+phoneno));
                smsIntent.putExtra("sms", "28.5984,25.3654");
                startActivity(smsIntent);
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts(msgToSend, phoneno, null)));

            }
        });

//        listView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
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
//                Intent msgSend = new Intent(Intent.ACTION_VIEW, Uri.parse("To Reach : " + msgToSend));
//                msgSend.putExtra(msgToSend,0);
//                startActivity(msgSend);
//               Toast.makeText(getApplicationContext(),"good",Toast.LENGTH_LONG).show();
//                return true;
//            }
//        });
    }

}

