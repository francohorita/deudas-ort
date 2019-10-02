package com.example.deudas_ort;

import java.util.ArrayList;

import android.app.Activity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.database.Cursor;

import android.os.Bundle;
import android.os.RemoteException;
import android.os.Build;
import android.provider.ContactsContract;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class NativeContentProvider extends Activity {
    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nativecontentlayout);

        Button view = (Button)findViewById(R.id.viewButton);

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
            ContentResolver cr = getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    if (Integer.parseInt(cur.getString(
                            cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        Cursor pCur = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            Toast.makeText(NativeContentProvider.this, "Name: " + name + ", Phone No: " + phoneNo, Toast.LENGTH_SHORT).show();
                        }
                        pCur.close();
                    }
                }
            }
        }
    }
}