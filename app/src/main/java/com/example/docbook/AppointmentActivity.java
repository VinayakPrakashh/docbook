package com.example.docbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AppointmentActivity extends AppCompatActivity {
Button backs;
FirebaseAuth mAuth;
    FirebaseUser mUser;
    CardView neuro,radio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
radio=findViewById(R.id.radiologist);
neuro=findViewById(R.id.neurologists);
radio.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        startActivity(new Intent(AppointmentActivity.this,radiologist.class));
    }
});
neuro.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        startActivity(new Intent(AppointmentActivity.this, DoctorDetailsActivity.class));
    }
});

    }

}