package com.navigation.android.maps3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.navigation.android.maps3.ContactsDBHelper;

/**
 * Created by Arpit on 11-04-2018.
 */


    public class DataBase{

        private ContactsDBHelper dbHelper;

        private SQLiteDatabase DB;


        public DataBase(Context context){
            dbHelper = new ContactsDBHelper(context);
            DB = dbHelper.getWritableDatabase();
        }


        public SQLiteDatabase getDB()
        {
            return DB;
        }
        public void close()
        {
            DB.close();
        }


    }


