package com.example.docbook;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminHomeActivity extends AppCompatActivity {
CardView settings,appointment,profile,dallpatients,ddoctors,daddpatient,orders,allproducts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
orders=findViewById(R.id.allorders);
dallpatients=findViewById(R.id.allpatients);
        daddpatient=findViewById(R.id.addpatient);
       ddoctors =findViewById(R.id.doctors);
allproducts=findViewById(R.id.products);
        settings=findViewById(R.id.settings);
        appointment=findViewById(R.id.doctors);
        allproducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    startActivity(new Intent(AdminHomeActivity.this,AdminProductViewActivity.class));
                }
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(AdminHomeActivity.this,AdminSettingsActivity.class));
            }
        });
        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHomeActivity.this,DoctorAppointmentActivity.class));
            }
        });
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHomeActivity.this,AdminMedicineActivity.class));
            }
        });
        dallpatients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHomeActivity.this, AdminPatientsActivity.class));
            }
        });
        ddoctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHomeActivity.this,AdminAppointmentActivity.class));
            }
        });
        daddpatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHomeActivity.this,AdminAddPatientActivity.class));
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
                       AdminHomeActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

}