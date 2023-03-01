package com.example.docbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
Button lgout,appointment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        lgout=findViewById(R.id.logout);
        appointment=findViewById(R.id.doctor);
        SharedPreferences sharedPreferences =getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String mail= sharedPreferences.getString("mail","").toString();
        Toast.makeText(getApplicationContext(), "Welcome "+mail,Toast.LENGTH_SHORT).show();
        lgout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.clear();
                editor.apply();;
                startActivity(new Intent(HomeActivity.this,MainActivity.class));
            }
        });
        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,AppointmentActivity.class));
            }
        });
    }
}