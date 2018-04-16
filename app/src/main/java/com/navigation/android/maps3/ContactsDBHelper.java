package com.navigation.android.maps3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Arpit on 11-04-2018.
 */

public class ContactsDBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "DBNAME";
    public static final int DB_VERSION = 1;

/*then name your columns like this.. public static final helps in accessing the column name from anywehere so you dont need to come to this class again and again to see the names*/

    public static final String contactTable = "CONTACTS";
    public static final String colID = "_id";
    public static final String colName = "Name";
    public static final String colEmail = "Email";
    public static final String colPhone = "Phone";

    ContactsDBHelper(Context context)
    {
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //CREATE YOUR TABLES HERE... SIMPLE SQL
        db.execSQL("CREATE TABLE IF NOT EXISTS " + contactTable + "("
                + colID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + colName + " TEXT , "
                + colEmail + " TEXT , "
                + colPhone + " INTEGER);");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
        //leave empty
    }
}