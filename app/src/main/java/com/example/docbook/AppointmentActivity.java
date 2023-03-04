package com.example.docbook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AppointmentActivity extends AppCompatActivity {
Button backs;
FirebaseAuth mAuth;
    FirebaseUser mUser;
    ImageView neuro,radio,pedia,cardi,ortho;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
radio=findViewById(R.id.radiologist);
neuro=findViewById(R.id.neurologist);
pedia=findViewById(R.id.pediatrician);
cardi=findViewById(R.id.cardiologist);
ortho=findViewById(R.id.orthologist);

radio.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("docselect",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("specialization", "Radiologist");
        myEdit.commit();
        startActivity(new Intent(AppointmentActivity.this,DoctorDetailsActivity.class));
    }
});
neuro.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("docselect",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("specialization", "Neurologist");
        myEdit.commit();
        startActivity(new Intent(AppointmentActivity.this, DoctorDetailsActivity.class));
    }
});
cardi.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("docselect",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("specialization", "Cardiologist");
        myEdit.commit();
        startActivity(new Intent(AppointmentActivity.this, DoctorDetailsActivity.class));
    }
});
        ortho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("docselect",MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("specialization", "Orthologist");
                myEdit.commit();
                startActivity(new Intent(AppointmentActivity.this, DoctorDetailsActivity.class));
            }
        });
        pedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("docselect",MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("specialization", "Pediatrician");
                myEdit.commit();
                startActivity(new Intent(AppointmentActivity.this, DoctorDetailsActivity.class));
            }
        });

    }

}