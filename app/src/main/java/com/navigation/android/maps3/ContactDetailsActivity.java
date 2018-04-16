package com.navigation.android.maps3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toolbar;

import org.w3c.dom.Text;

public class ContactDetailsActivity extends AppCompatActivity {

    //database object
    private SQLiteDatabase DB = null;
    private DataBase database;
    private TextView tvPhone, tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        //GET THE DATABSE HERE
        database = new DataBase(getApplicationContext());
        DB = database.getDB();

        tvPhone = (TextView)findViewById(R.id.tvPhone);
        tvEmail = (TextView)findViewById(R.id.tvEmail);

        Intent intent = getIntent();
        int id = intent.getIntExtra("contact_id", 0);

        Cursor cursor = DB.rawQuery("SELECT * FROM CONTACTS WHERE _id = " + id, null);

        Log.d("id in view", Integer.toString(id));
        cursor.moveToFirst();

        String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsDBHelper.colName));
        String phone = cursor.getString(cursor.getColumnIndexOrThrow(ContactsDBHelper.colPhone));
        String email = cursor.getString(cursor.getColumnIndexOrThrow(ContactsDBHelper.colEmail));

        getSupportActionBar().setTitle(name);

        tvPhone.setText(phone);
        tvEmail.setText(email);

        cursor.close();


    }

}
