package com.navigation.android.maps3;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Arpit on 15-04-2018.
 */
public class ContactCursorAdapter extends CursorAdapter {
    public ContactCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.contact_listitem, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView listitem_name = (TextView) view.findViewById(R.id.listitem_name);
        // Extract properties from cursor
        String name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        // Populate fields with extracted properties
        listitem_name.setText(name);

        view.setTag(id);
        }
}