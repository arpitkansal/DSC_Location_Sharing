package com.navigation.android.maps3;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddContactsActivity extends AppCompatActivity {


    //database object
    private SQLiteDatabase DB = null;
    private DataBase database;

    private EditText textName, textEmail, textPhone;
    private String name, email, phone;
    private Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);
        textEmail = (EditText)findViewById(R.id.editText_email);
        textName = (EditText)findViewById(R.id.editText_name);
        textPhone = (EditText)findViewById(R.id.editText_phone);
        save = (Button)findViewById(R.id.button_save);


    }

    public  void onResume() {
        super.onResume();
        database = new DataBase(getApplicationContext());
        DB = database.getDB();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = textName.getText().toString();
                email = textEmail.getText().toString();
                phone = textPhone.getText().toString();


                //to check if any field isn't left empty
                if(name.trim().length() > 0) {
                    if (email.trim().length() > 0) {
                        if (phone.trim().length() > 0)
                        {
                       //     try {
                                String SQLSTATE;
                                ContentValues cvContact = new ContentValues();
                                cvContact.put(ContactsDBHelper.colName, name);
                                cvContact.put(ContactsDBHelper.colEmail, email);
                                cvContact.put(ContactsDBHelper.colPhone, phone);


                                if (DB == null)
                                {
                                    DB = database.getDB();
                                }
                            DB.insert("CONTACTS", null, cvContact);
//try now?save first
                            cvContact = null;
                                Log.d("save", "Contact Added");
                                finish();
                  /*          }
                            catch (Exception e) {
                                e.printStackTrace();
                            }*/
                        }
                    }
                }
            }

        });

    }

}
