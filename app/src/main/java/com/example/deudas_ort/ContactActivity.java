package com.example.deudas_ort;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import static com.example.deudas_ort.HomeActivity.USER_NAME;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        Intent intent = getIntent();
        String userName = intent.getStringExtra(USER_NAME);

        TextView userNameTextView = findViewById(R.id.textView);
        userNameTextView.setText(userName);
    }
}
