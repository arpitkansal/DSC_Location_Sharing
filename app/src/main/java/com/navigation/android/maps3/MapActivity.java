package com.navigation.android.maps3;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MapActivity extends AppCompatActivity {
    public static List<String> placesList, mMembersList, mContactsList;
    public static List<LatLng> locations;
    public static ArrayAdapter adapter, mGroupAdapter;
    private ListView listView, mGroupList;
    private Button mAddButton, mRenewButton;
    private EditText mName, mContact;
    public static boolean firsttAdd = true;

    public static void setFirsttAdd(boolean first) {
        firsttAdd = first;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mGroupList = findViewById(R.id.group_list);
      //  mAddButton = findViewById(R.id.add_member_button);
        mRenewButton = findViewById(R.id.renew_button);
       // mName = findViewById(R.id.member_name);
        //mContact = findViewById(R.id.member_contact);

        placesList = new ArrayList<>();
        placesList.add("Add checkpoints");

        locations = new ArrayList<LatLng>();
        locations.add(new LatLng(0, 0));


        mMembersList = new ArrayList<>();
        mContactsList = new ArrayList<>();

        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, placesList);

        if (savedInstanceState != null) {
            firsttAdd = savedInstanceState.getBoolean("firsttAdd");
            placesList = savedInstanceState.getStringArrayList("placesList");
            mMembersList = savedInstanceState.getStringArrayList("membersList");
            mContactsList = savedInstanceState.getStringArrayList("contactsList");
        }

     //   mGroupAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mMembersList);
       // mGroupList.setAdapter(mGroupAdapter);

//        listView.setAdapter(adapter);
        // zooming to the checkpoint at the clicked position
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                if (placesList.size() == 1) {
                    intent.putExtra("first", 1);
                }
                intent.putExtra("position", i);
                intent.putStringArrayListExtra("placesList", (ArrayList<String>) placesList);
                intent.putExtra("firstAdd", firsttAdd);
                startActivity(intent);
            }
        });

        // sending a message to the members of the group mentioning the checkpoint clicked
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                sendMessages(placesList.get(position));
                return true;
            }
        });

        // adding a member to the group
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMembersList.size() == 0 || mMembersList == null){
                    Toast.makeText(getApplicationContext(), " first enter name of the user", Toast.LENGTH_SHORT).show();
                }

                String name = mName.getText().toString();
                String number = mContact.getText().toString();

                if (name != null && getNumber(number) == 10){
                    mMembersList.add(name);
                    mContactsList.add(number);
                    mGroupAdapter.notifyDataSetChanged();

                }else{
                    if (name == ""){
                        Toast.makeText(getApplicationContext(), "Enter a name", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "Enter a Valid Contact No.", Toast.LENGTH_SHORT).show();
                    }
                }
                mName.setText("");
                mContact.setText("");
            }
        });

        // creating a new list of group members
        mRenewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMembersList.clear();
                mContactsList.clear();
                mGroupAdapter.notifyDataSetChanged();
            }
        });

        // removing the clicked member from the group list
        mGroupList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mMembersList.remove(position);
                mContactsList.remove(position);
                mGroupAdapter.notifyDataSetChanged();
                return true;
            }
        });


    }

    // helper method to validate the phone number
    private int getNumber(String number){
        char[] numb = number.toCharArray();
        List<Integer> contact = new ArrayList<>();
        for (char n : numb){
            contact.add((int) n);
        }
        return contact.size();
    }



    // method to send message to the group members
    private void sendMessages(String place){
        List<String> numberList = mContactsList;

        if (numberList != null) {
            String toNumbers = "";
            for (String s : numberList) {
                toNumbers = toNumbers + s + ";";
            }
            toNumbers = toNumbers.substring(0, toNumbers.length() - 1);
            String message = mMembersList.get(0) + " Reached " + place;

            Uri sendSmsTo = Uri.parse("smsto:" + toNumbers);
            Intent intent = new Intent(
                    android.content.Intent.ACTION_SENDTO, sendSmsTo);
            intent.putExtra("sms_body", message);
            startActivity(intent);
        }else {
            Toast.makeText(getApplicationContext(), "No contacts to send messages", Toast.LENGTH_SHORT).show();
        }
    }

//        @Override
//        protected void onResume() {
//            super.onResume();
//            SharedPreferences preferences = getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
//            if ((List<String>) preferences.getStringSet("placesList", null) != null) {
//                placesList = (List<String>) preferences.getStringSet("placesList", null);
//            }
//            if ((List<String>) preferences.getStringSet("placesList", null) != null) {
//                mMembersList = (List<String>) preferences.getStringSet("placesList", null);
//            }
//            if ((List<String>) preferences.getStringSet("placesList", null) != null) {
//                mContactsList = (List<String>) preferences.getStringSet("placesList", null);
//            }
//            firsttAdd = preferences.getBoolean("first", true);
//        }

//        @Override
//        protected void onPause() {
//            super.onPause();
//            SharedPreferences preferences = getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//
////            public static List<String> placesList, mMembersList, mContactsList;
////            public static List<LatLng> locations;
//
//
//
//            editor.putStringSet("placesList", (Set<String>) placesList);
//            editor.putStringSet("membersList", (Set<String>) mMembersList);
//            editor.putStringSet("contactsList", (Set<String>) mContactsList);
//            editor.putBoolean("first", firsttAdd);
//            editor.commit();
////            editor.putStringSet("locations", (Set<String>) locations);
//        }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList("placesList", (ArrayList<String>) placesList);
        outState.putStringArrayList("membersList", (ArrayList<String>) mMembersList);
        outState.putStringArrayList("contactsList", (ArrayList<String>) mContactsList);
        outState.putBoolean("first", firsttAdd);
        super.onSaveInstanceState(outState);
    }

}
