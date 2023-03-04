package com.example.docbook;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DoctorDetailsActivity extends AppCompatActivity {
TextView docname2,docspec2,dochospital2,dexp2,doccontact2;
Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);



        setContentView(R.layout.activity_doctor_details);
        docname2=findViewById(R.id.dname);
        docspec2=findViewById(R.id.dspec);
        dochospital2=findViewById(R.id.dhospital);
        dexp2=findViewById(R.id.dexp);
        doccontact2=findViewById(R.id.dcontact);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
b1=findViewById(R.id.book_appointment_button);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("cardiologists.jpg");
        ImageView imageView = findViewById(R.id.dphoto);
        Glide.with(this /* context */)
                .load(imageRef)
                .into(imageView);

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
                                String specialization = document.getString("Specialization");
                                int experience = document.getLong("experience").intValue();
                                String hospital = document.getString("hospital");
                                long contact = document.getLong("contact");
                                String exp = Integer.toString(experience);
                                // Update UI with doctor details
                                String dcon= Long.toString(contact);




                                docname2.setText("Name: "+name);
                                docspec2.setText("Specialization: "+specialization);
                                dochospital2.setText("Hospital: "+hospital);
                                dexp2.setText("Experience: "+exp+" Years");
                                doccontact2.setText("Contact No: "+dcon);


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
                startActivity(new Intent(DoctorDetailsActivity.this,neurologist.class));
            }
        });

    }
}