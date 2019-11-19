package com.example.deudas_ort;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;


public class NativeContentProvider extends Activity {

    /**
     * Request code for READ_CONTACTS. It can be any number > 0.
     */
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private ArrayList<SelectUser> selectUsers;
    private ListView contactListView;
    private Cursor contactPhonesCursor;
    private ContentResolver popUpResolver;
    private SearchView searchView;
    private SelectUserAdapter selectUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getContacts();
    }

    /**
     * Overload needed because of view sent from layout.xml
     */
    public void getContacts(View view){
        getContacts();
    }

    public void getContacts() {
        /** Check the SDK version and whether the permission is already granted or not. */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overridden method
        } else {
            /** Android version is lesser than 6.0 or the permission is already granted. */
            setContentView(R.layout.nativecontentlayout);

            selectUsers = new ArrayList<>();
            popUpResolver = this.getContentResolver();
            contactListView = findViewById(R.id.contacts_list);
            contactPhonesCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            searchView = findViewById(R.id.searchView);

            LoadContact loadContact = new LoadContact();
            loadContact.execute();

            /** setOnQueryTextListener */
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // TODO Auto-generated method stub
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    // TODO Auto-generated method stub
                    selectUserAdapter.filter(newText);
                    return false;
                }
            });
        }
    }

    /** Load data on background */
    class LoadContact extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            /** Get Contact list from Phone */
            if (contactPhonesCursor != null) {
                Log.e("count", "" + contactPhonesCursor.getCount());
                if (contactPhonesCursor.getCount() == 0) {

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NativeContentProvider.this, "No contacts in your contact list.", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                while (contactPhonesCursor.moveToNext()) {
                    Bitmap bitMap = null;
                    String id = contactPhonesCursor.getString(contactPhonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                    String name = contactPhonesCursor.getString(contactPhonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phoneNumber = contactPhonesCursor.getString(contactPhonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String email = contactPhonesCursor.getString(contactPhonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA2));
                    String imageThumb = contactPhonesCursor.getString(contactPhonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));

                    try {
                        if (imageThumb != null) {
                            bitMap = MediaStore.Images.Media.getBitmap(popUpResolver, Uri.parse(imageThumb));
                        } else {
                            Log.e("No Image Thumb", "--------------");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    SelectUser selectUser = new SelectUser();

                    selectUser.setThumb(bitMap);
                    selectUser.setName(name);
                    selectUser.setPhone(phoneNumber);
                    selectUser.setEmail(id);
                    selectUser.setCheckedBox(false);
                    selectUsers.add(selectUser);
                }
            } else {
                Log.e("Cursor close 1", "----------------");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            selectUserAdapter = new SelectUserAdapter(selectUsers, NativeContentProvider.this);
            contactListView.setAdapter(selectUserAdapter);

            /** Select item on ListClick */
            contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e("searchView", "here---------------- listener");
                    SelectUser data = selectUsers.get(i);
                }
            });

            contactListView.setFastScrollEnabled(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        contactPhonesCursor.close();
    }
}