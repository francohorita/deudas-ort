package com.example.deudas_ort;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import static com.example.deudas_ort.HomeActivity.USER_EMAIL;
import static com.example.deudas_ort.HomeActivity.USER_NAME;
import static com.example.deudas_ort.HomeActivity.USER_PHONE;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        Intent intent = getIntent();
        String userName = intent.getStringExtra(USER_NAME);
        String userEmail = intent.getStringExtra(USER_EMAIL);
        String userPhone = intent.getStringExtra(USER_PHONE);

        TextView userNameTextView = findViewById(R.id.textView);
        userNameTextView.setText(userName);
    }
}
