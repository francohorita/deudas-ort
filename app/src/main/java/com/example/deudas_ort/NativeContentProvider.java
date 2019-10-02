package com.example.deudas_ort;

import java.util.ArrayList;

import android.app.Activity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.content.ContentResolver;
import android.database.Cursor;

import android.os.Bundle;
import android.os.Build;
import android.provider.ContactsContract;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Button;
import android.widget.ArrayAdapter;
import java.util.List;
import android.widget.Toast;

public class NativeContentProvider extends Activity {
    private ListView lstNames;

    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nativecontentlayout);

        Button view = (Button)findViewById(R.id.viewButton);
        super.onCreate(savedInstanceState);

        // Find the list view
        this.lstNames = (ListView) findViewById(R.id.lstNames);

        view.setOnClickListener(new OnClickListener() {
            public void onClick(View v){
                displayContacts();
                Log.i("NativeContentProvider", "Completed Displaying Contact list");
            }
        });
    }

    private void displayContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            List<String> contacts = getContactNames();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contacts);
            lstNames.setAdapter(adapter);
        }
    }

    /**
     * Read the name of all the contacts.
     *
     * @return a list of names.
     */
    private List<String> getContactNames() {
        List<String> contacts = new ArrayList<>();
        // Get the ContentResolver
        ContentResolver cr = getContentResolver();
        // Get the Cursor of all the contacts
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        // Move the cursor to first. Also check whether the cursor is empty or not.
        if (cursor.moveToFirst()) {
            // Iterate through the cursor
            do {
                // Get the contacts name
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                contacts.add(name);
            } while (cursor.moveToNext());
        }
        // Close the cursor
        cursor.close();

        return contacts;
    }
}