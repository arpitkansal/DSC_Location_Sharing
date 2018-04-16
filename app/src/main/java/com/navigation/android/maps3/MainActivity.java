package com.navigation.android.maps3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static List<LatLng> locations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void OpenMaps(View v)
    {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }
    public void AddContacts(View v)
    {
        Intent intent = new Intent(this, AddContactsActivity.class);
        startActivity(intent);
    }
    public void viewContacts(View v){
        Intent intent = new Intent(this, ViewContacts.class);
        startActivity(intent);
    }
    public void openmapsActivity(View v){
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }
}
