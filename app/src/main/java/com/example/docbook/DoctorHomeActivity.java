package com.example.docbook;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;

public class DoctorHomeActivity extends AppCompatActivity {
CardView lgout,appointment,profile,editprofile,aboutsel,patientadd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);
profile=findViewById(R.id.radiologist);
editprofile=findViewById(R.id.orthologist);
        patientadd=findViewById(R.id.addpatient);
       aboutsel =findViewById(R.id.about);
        SharedPreferences sharedPreferences = getSharedPreferences("doctor", Context.MODE_PRIVATE);
        String displayname = sharedPreferences.getString("doctor", "").toString();

        lgout=findViewById(R.id.bookings);
        appointment=findViewById(R.id.appointments);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isToastDisplayed = prefs.getBoolean("toast_displayed", false);
        if (!isToastDisplayed) {
            Toast.makeText(this, "Welcome Dr." +displayname, Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("toast_displayed", true);
            editor.apply();
        }
        lgout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                // When the user clicks the logout button
                FirebaseAuth.getInstance().signOut();
                Context context = view.getContext();
// Reset the value of toast_displayed to false
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editors = prefs.edit();
                editors.putBoolean("toast_displayed", false);
                editors.apply();
                startActivity(new Intent(DoctorHomeActivity.this, MainActivity.class));
            }
        });
        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DoctorHomeActivity.this,DoctorAppointmentActivity.class));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DoctorHomeActivity.this,DoctorProfileActivity.class));
            }
        });
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DoctorHomeActivity.this, DoctorPatientsActivity.class));
            }
        });
        aboutsel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DoctorHomeActivity.this,AboutActivity.class));
            }
        });
        patientadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DoctorHomeActivity.this,AddPatientActivity.class));
            }
        });
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit app")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finishAffinity(); // Close all activities
                        System.exit(0); // Exit the app
                       DoctorHomeActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

}