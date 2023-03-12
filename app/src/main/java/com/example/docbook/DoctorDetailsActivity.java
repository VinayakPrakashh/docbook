package com.example.docbook;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DoctorDetailsActivity extends AppCompatActivity {
    TextView docname2, docspec2, dochospital2, dexp2, doccontact2, dquali, fees2;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);


        setContentView(R.layout.activity_doctor_details);
        docname2 = findViewById(R.id.dname);
        docspec2 = findViewById(R.id.dspec);
        dochospital2 = findViewById(R.id.dhospital);
        dexp2 = findViewById(R.id.dexp);
        doccontact2 = findViewById(R.id.dcontact);
        dquali = findViewById(R.id.dqualification);
        fees2 = findViewById(R.id.contact);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        b1 = findViewById(R.id.book_appointment_button);
        SharedPreferences sharedPreferences = getSharedPreferences("docselect", MODE_PRIVATE);
        String spec = sharedPreferences.getString("specialization", "").toString();

        // Get a reference to the Firebase Storage instance
        FirebaseStorage storage = FirebaseStorage.getInstance();
        String photo = spec + ".jpg";
// Create a reference to the image file you want to download
        StorageReference imageRef = storage.getReference().child(photo);

// Download the image file into a byte array
        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Convert the byte array into a Bitmap object
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                // Set the Bitmap object in an ImageView
                ImageView imageView = findViewById(R.id.dphoto);
                imageView.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors that occur during the download
                Log.e("TAG", "Failed to download image", exception);
            }
        });


        db.collection("doctor")
                .document(spec)
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
                             String exp = document.getString("experience");
                                String hospital = document.getString("hospital");
                                String dcon = document.getString("contact");
                                String qualification = document.getString("qualification");

                                String fees = document.getString("fees");
                                // Update UI with doctor details




                                docname2.setText("Name: " + name);
                                docspec2.setText("Specialization: " + specialization);
                                dochospital2.setText("Hospital: " + hospital);
                                dexp2.setText("Experience: " + exp + " Years");
                                doccontact2.setText("Contact No: " + dcon);
                                dquali.setText("Qualification: " + qualification);
                                fees2.setText("Appointment Fees: " + fees);


                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                myEdit.putString("doctor", name);
                                myEdit.commit();

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
                startActivity(new Intent(DoctorDetailsActivity.this, booking.class));
            }
        });

    }
}