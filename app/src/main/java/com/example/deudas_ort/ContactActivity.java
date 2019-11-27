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
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deudas_ort.Utils.RoundImage;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.example.deudas_ort.HomeActivity.USER_EMAIL;
import static com.example.deudas_ort.HomeActivity.USER_NAME;
import static com.example.deudas_ort.HomeActivity.USER_PHONE;
import static com.example.deudas_ort.HomeActivity.USER_PHOTO_THUMBNAIL_URI;

public class ContactActivity extends AppCompatActivity {

    private ContentResolver resolver;

    private String contactUserEmail = "";

    private TextView fullNameTextView;
    private TextView phoneTextView;
    private EditText amountEditText;
    private EditText descriptionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        resolver = this.getContentResolver();
        fullNameTextView = findViewById(R.id.fullName);
        phoneTextView = findViewById(R.id.phone);
        amountEditText = findViewById(R.id.amount);
        descriptionEditText = findViewById(R.id.description);
        String contactFullName = "";
        String contactPhone = "";
        String photoThumbnailUri = "";
        String amountText = "";
        String descriptionText = "";

        try {
            Intent intent = getIntent();
            contactUserEmail = intent.getStringExtra(USER_EMAIL);
            DBContactos db = new DBContactos(this);
            db.open();
            Map<String, String> data = db.getData(contactUserEmail);
            db.close();
            if (data.isEmpty()) {
                contactFullName = intent.getStringExtra(USER_NAME);
                contactPhone = intent.getStringExtra(USER_PHONE);
                photoThumbnailUri = intent.getStringExtra(USER_PHOTO_THUMBNAIL_URI);
            } else {
                contactFullName = data.get("name");
                contactUserEmail = data.get("id");
                contactPhone = data.get("phone");
                amountText = data.get("amount");
                descriptionText = data.get("description");
                photoThumbnailUri = intent.getStringExtra(USER_PHOTO_THUMBNAIL_URI);
            }

            fullNameTextView.setText(contactFullName);
            phoneTextView.setText(contactPhone);
            amountEditText.setText(amountText);
            descriptionEditText.setText(descriptionText);

            if (photoThumbnailUri != null) {
                Bitmap bitMap = MediaStore.Images.Media.getBitmap(resolver, Uri.parse(photoThumbnailUri));
                RoundImage contactPhoto = new RoundImage(bitMap);
                ImageView photo = findViewById(R.id.photo);
                photo.setImageDrawable(contactPhoto);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buttonEdit(View v) {
        String textName = fullNameTextView.getText().toString().trim();
        String textPhone = phoneTextView.getText().toString().trim();
        String textAmount = amountEditText.getText().toString().trim();
        String textDescription = descriptionEditText.getText().toString().trim();
        try {
            DBContactos db = new DBContactos(this);
            db.open();
            Map<String, String> data = db.getData("1");
            if (data.isEmpty()) {
                db.insert(contactUserEmail, textName, contactUserEmail, textPhone, textAmount, textDescription);
                db.close();
            } else {
                db.updateEntry(contactUserEmail, textName, contactUserEmail, textPhone, textAmount, textDescription);
            }
            showMessage("Contacto guardado!");
        } catch (Exception e) {
            showMessage("Error al borrar información en la tabla: " + e.getMessage());
        }
    }

    private void showMessage(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
