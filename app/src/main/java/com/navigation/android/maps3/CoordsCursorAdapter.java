package com.navigation.android.maps3;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.navigation.android.maps3.R;

/**
 * Created by Arpit on 17-04-2018.
 */

public class CoordsCursorAdapter extends android.widget.CursorAdapter{

    public CoordsCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.coords_listitem, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView listitem_coords = (TextView) view.findViewById(R.id.listitem_coords);
        // Extract properties from cursor
        String s_latitudes = cursor.getString(cursor.getColumnIndexOrThrow("Latitude"));
        String s_longitudes = cursor.getString(cursor.getColumnIndexOrThrow("Longitude"));
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        // Populate fields with extracted properties
        listitem_coords.setText(s_latitudes + "," + s_longitudes);

        view.setTag(id);
    }
}
