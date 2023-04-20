package com.example.docbook;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DoctorHomeActivity extends AppCompatActivity {
LinearLayout lgout,appointment,profile,editprofile,patientadd,appointment_t,patient_t;
TextView name;
ImageView imageRefe;
ConstraintLayout order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_home_doctor);
profile=findViewById(R.id.prof);
editprofile=findViewById(R.id.mypatients);
        patientadd=findViewById(R.id.addpatient);
        patient_t=findViewById(R.id.patients_top);
        appointment_t=findViewById(R.id.appointment_top);
        imageRefe=findViewById(R.id.photo);
        order=findViewById(R.id.orders_mid);
name=findViewById(R.id.username);
        SharedPreferences sharedPreferences = getSharedPreferences("doctor", Context.MODE_PRIVATE);
        String displayname = sharedPreferences.getString("doctor", "").toString();
name.setText(displayname);
        lgout=findViewById(R.id.lgout);
        appointment=findViewById(R.id.doctors);
        SharedPreferences sharedPreferences2 = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String spec= sharedPreferences2.getString("specialization","").toString();

        StorageReference imageRef = FirebaseStorage.getInstance().getReference().child(spec+".jpg");
        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {

            @Override
            public void onSuccess(byte[] bytes) {

                // Create a bitmap image from the byte array
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                // Display the bitmap image in an ImageView

                imageRefe.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(DoctorHomeActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });

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

                Context context = view.getContext();
// Reset the value of toast_displayed to false
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editors = prefs.edit();
                editors.putBoolean("toast_displayed", false);
                editors.apply();
                startActivity(new Intent(DoctorHomeActivity.this, MainActivity.class));
            }
        });
        order.setOnClickListener(view -> {
                    startActivity(new Intent(DoctorHomeActivity.this,DoctorAppointmentActivity.class));
                }
                );
        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DoctorHomeActivity.this,DoctorAppointmentActivity.class));
            }
        });
        appointment_t.setOnClickListener(new View.OnClickListener() {
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
        patient_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DoctorHomeActivity.this, DoctorPatientsActivity.class));
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