package com.example.docbook;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DoctorHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);

        SharedPreferences sharedPreferences = getSharedPreferences("doctor", Context.MODE_PRIVATE);
        String displayname = sharedPreferences.getString("doctor", "").toString();
        Toast.makeText(this, "Welcome Dr." +displayname, Toast.LENGTH_SHORT).show();
    }
}