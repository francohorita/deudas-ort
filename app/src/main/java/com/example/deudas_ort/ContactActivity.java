package com.example.deudas_ort;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deudas_ort.Utils.RoundImage;

import java.io.IOException;
import java.util.Map;

import static com.example.deudas_ort.HomeActivity.CONTACT_ID;
import static com.example.deudas_ort.HomeActivity.USER_NAME;
import static com.example.deudas_ort.HomeActivity.USER_PHONE;
import static com.example.deudas_ort.HomeActivity.USER_PHOTO_THUMBNAIL_URI;

public class ContactActivity extends AppCompatActivity {

    private ContentResolver resolver;
    private String contactId;
    private TextView fullNameTextView;
    private TextView phoneTextView;
    private EditText amountEditText;
    private EditText descriptionEditText;
    private String contactFullName;
    private String contactPhone;
    private String photoThumbnailUri;
    private String amountValue;
    private String descriptionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        resolver = this.getContentResolver();

        /** Getting view components */
        fullNameTextView = findViewById(R.id.inputFullName);
        phoneTextView = findViewById(R.id.inputPhone);
        amountEditText = findViewById(R.id.inputAmount);
        descriptionEditText = findViewById(R.id.inputComments);

        try {
            /** Grabbing Intent */
            Intent intent = getIntent();
            contactId = intent.getStringExtra(CONTACT_ID);

            /** Initializing MySQLITE DataBase */
            ContactDataBase sqliteDataBase = new ContactDataBase(this);
            /** Opening MySQLITE DataBase connection */
            sqliteDataBase.open();
            /** Grabbing Data from MySQLITE DataBase open connection */
            Map<String, String> dataMap = sqliteDataBase.getData(contactId);
            /** Closing MySQLITE DataBase connection */
            sqliteDataBase.close();

            if (dataMap.isEmpty()) {
                /** Grabbing data from Intent */
                contactFullName = intent.getStringExtra(USER_NAME);
                contactPhone = intent.getStringExtra(USER_PHONE);
                photoThumbnailUri = intent.getStringExtra(USER_PHOTO_THUMBNAIL_URI);
            } else {
                /** Grabbing the data from the DataMap */
                contactFullName = dataMap.get("name");
                contactId = dataMap.get("id");
                contactPhone = dataMap.get("phone");
                amountValue = dataMap.get("amount");
                descriptionText = dataMap.get("description");
                photoThumbnailUri = intent.getStringExtra(USER_PHOTO_THUMBNAIL_URI);
            }

            /** Setting values to view */
            fullNameTextView.setText(contactFullName);
            phoneTextView.setText(contactPhone);
            amountEditText.setText(amountValue);
            descriptionEditText.setText(descriptionText);

            /** Contact hast Photo */
            if (photoThumbnailUri != null) {
                /** Parsing Uri to Bitmap */
                Bitmap bitMap = MediaStore.Images.Media.getBitmap(resolver, Uri.parse(photoThumbnailUri));
                /** Rounding Image */
                RoundImage contactPhoto = new RoundImage(bitMap);
                /** Grabbing & Populating ImageView from the view.xml */
                ImageView photo = findViewById(R.id.photo);
                photo.setImageDrawable(contactPhoto);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Edit button logic */
    public void buttonEdit(View v) {
        String textName = fullNameTextView.getText().toString().trim();
        String textPhone = phoneTextView.getText().toString().trim();
        String textAmount = amountEditText.getText().toString().trim();
        String textDescription = descriptionEditText.getText().toString().trim();

        try {
            /** Initializing MySQLITE DataBase */
            ContactDataBase sqliteDataBase = new ContactDataBase(this);
            /** Opening MySQLITE DataBase connection */
            sqliteDataBase.open();
            /** Grabbing Data from MySQLITE DataBase open connection */
            Map<String, String> data = sqliteDataBase.getData(contactId);

            if (data.isEmpty()) {
                /** If Contact Data not founded we perform an Insert */
                sqliteDataBase.insert(contactId, textName, contactId, textPhone, textAmount, textDescription);
                sqliteDataBase.close();
            } else {
                /** If Contact Data founded we perform an Update */
                sqliteDataBase.updateEntry(contactId, textName, contactId, textPhone, textAmount, textDescription);
                sqliteDataBase.close();
            }
            showMessage("Saved!");
        } catch (Exception e) {
            showMessage("Erasing data error: " + e.getMessage());
        }
    }

    /** Toast message function */
    private void showMessage(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
