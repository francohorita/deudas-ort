package com.example.deudas_ort;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
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
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.deudas_ort.data.Contact;
import com.example.deudas_ort.data.ContactAdapter;

import java.io.IOException;
import java.util.ArrayList;

public class HomeActivity extends Activity {

    /** Request code for READ_CONTACTS. It can be any number > 0. */
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_PHONE = "USER_PHONE";
    public static final String USER_PHOTO_THUMBNAIL_URI = "USER_PHOTO_THUMBNAIL_URI";
    private ArrayList<Contact> contacts;
    private ListView contactListView;
    private Cursor contactPhonesCursor;
    private ContentResolver popUpResolver;
    private SearchView searchView;
    private ContactAdapter contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getContacts();
    }

    /** Overload needed because of view sent from layout.xml */
    public void getContacts(View view){
        getContacts();
    }

    public void getContacts() {
        /** Check the SDK version and whether the permission is already granted or not. */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            /** Android version is lesser than 6.0 or the permission is already granted. */
            setContentView(R.layout.activity_home);
            final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
            pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getContacts();
                    pullToRefresh.setRefreshing(false);
                }
            });

            contacts = new ArrayList<>();
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
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    contactAdapter.search(newText);
                    return false;
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        getContacts();
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
                            Toast.makeText(HomeActivity.this, "No contacts in your contact list.", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                while (contactPhonesCursor.moveToNext()) {
                    Bitmap bitMap = null;
                    String id = contactPhonesCursor.getString(contactPhonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                    String name = contactPhonesCursor.getString(contactPhonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phoneNumber = contactPhonesCursor.getString(contactPhonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String email = contactPhonesCursor.getString(contactPhonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA2));
                    String photoThumbnailUri = contactPhonesCursor.getString(contactPhonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));

                    try {
                        if (photoThumbnailUri != null) {
                            bitMap = MediaStore.Images.Media.getBitmap(popUpResolver, Uri.parse(photoThumbnailUri));
                        } else {
                            Log.e("No Image Thumb", "--------------");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Contact contact = new Contact();

                    contact.setBitMap(bitMap);
                    contact.setName(name);
                    contact.setPhone(phoneNumber);
                    contact.setEmail(id);
                    contact.setPhotoThumbnailUri(photoThumbnailUri);
                    contacts.add(contact);
                }
            } else {
                Log.e("Cursor close 1", "----------------");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            contactAdapter = new ContactAdapter(contacts, HomeActivity.this);
            contactListView.setAdapter(contactAdapter);

            /** Select item on ListClick */
            contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e("searchView", "here---------------- listener");
                    Contact selectedContactData = contacts.get(i);
                    navigateToContactInformation(selectedContactData);
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

    public void navigateToContactInformation(Contact contactData){
        Intent intent = new Intent(this, ContactActivity.class);
        intent.putExtra(USER_NAME, contactData.getName());
        intent.putExtra(USER_EMAIL, contactData.getEmail());
        intent.putExtra(USER_PHONE, contactData.getPhone());
        intent.putExtra(USER_PHOTO_THUMBNAIL_URI, contactData.getPhotoThumbnailUri());
        startActivity(intent);
    }
}