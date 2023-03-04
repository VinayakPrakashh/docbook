package com.example.docbook;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DoctorDetailsActivity extends AppCompatActivity {
TextView docname,docspec,docexp,dochospital,doccontact;
Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        docname=findViewById(R.id.doctor_name);
        docspec=findViewById(R.id.doctor_specialization);
        docexp=findViewById(R.id.experience);
        dochospital=findViewById(R.id.hospital_name);
        doccontact=findViewById(R.id.contact_number);
        setContentView(R.layout.activity_doctor_details);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
b1=findViewById(R.id.book_appointment_button);
        db.collection("doctor")
                .document("neurologist")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {


                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Extract doctor details here
                                String name = document.getString("name");
                                String specialization = document.getString("specialization");
                                int experience = document.getLong("experience").intValue();
                                String hospital = document.getString("hospital");
                                String contact = document.getString("contact");

                                // Update UI with doctor details
                                docname.setText(name);
                                docspec.setText(specialization);
                                docexp.setText(String.valueOf(experience));
                                dochospital.setText(hospital);
                                doccontact.setText(contact);

                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DoctorDetailsActivity.this,radiologist.class));
            }
        });

    }
}