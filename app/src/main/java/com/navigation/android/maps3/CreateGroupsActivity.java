package com.navigation.android.maps3;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

public class CreateGroupsActivity extends AppCompatActivity {
    ListView listToCreateGroup;
    private SQLiteDatabase DB = null;
    private DataBase database;
    private TextView contactName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_groups);
        listToCreateGroup = (ListView)findViewById(R.id.listview_createGroup);

    }
    protected void onResume()
    {
        super.onResume();
        database = new DataBase(getApplicationContext());
        DB = database.getDB();

        Log.d("act", "view contact");

        ContactsDBHelper dbHelper = new ContactsDBHelper(this);
        DB = dbHelper.getWritableDatabase();
        final Cursor cursor = DB.rawQuery("SELECT * FROM CONTACTS", null);

        ContactCursorAdapter contactCursorAdapter = new ContactCursorAdapter(this, cursor);
        // Attach cursor adapter to the ListView
        listToCreateGroup.setAdapter(contactCursorAdapter);

        contactCursorAdapter.changeCursor(cursor);

    }

}
