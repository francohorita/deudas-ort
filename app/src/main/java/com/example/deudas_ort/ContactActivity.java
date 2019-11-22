package com.example.deudas_ort;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.deudas_ort.Utils.RoundImage;

import java.io.IOException;

import static com.example.deudas_ort.HomeActivity.USER_EMAIL;
import static com.example.deudas_ort.HomeActivity.USER_NAME;
import static com.example.deudas_ort.HomeActivity.USER_PHONE;
import static com.example.deudas_ort.HomeActivity.USER_PHOTO_THUMBNAIL_URI;

public class ContactActivity extends AppCompatActivity {

    private ContentResolver resolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        resolver = this.getContentResolver();

        try {
            Intent intent = getIntent();
            String contactFullName = intent.getStringExtra(USER_NAME);
            String contactUserEmail = intent.getStringExtra(USER_EMAIL);
            String contactPhone = intent.getStringExtra(USER_PHONE);
            String photoThumbnailUri = intent.getStringExtra(USER_PHOTO_THUMBNAIL_URI);

            TextView fullNameTextView = findViewById(R.id.fullName);
            TextView phoneTextView = findViewById(R.id.phone);
            EditText amountEditText = findViewById(R.id.amount);
            EditText descriptionEditText = findViewById(R.id.description);

            fullNameTextView.setText(contactFullName);
            phoneTextView.setText(contactPhone);

            if (photoThumbnailUri != null) {
                Bitmap bitMap = MediaStore.Images.Media.getBitmap(resolver, Uri.parse(photoThumbnailUri));
                RoundImage contactPhoto = new RoundImage(bitMap);
                ImageView photo = findViewById(R.id.photo);
                photo.setImageDrawable(contactPhoto);
            }

            //TODO SQLite
            final String amount = "10.5";
            final String description = "Description";

            amountEditText.setText(amount);
            descriptionEditText.setText(description);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
