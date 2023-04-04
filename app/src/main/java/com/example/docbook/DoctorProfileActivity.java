package com.example.docbook;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DoctorProfileActivity extends AppCompatActivity {

TextView uname,uspec,ufees,uqualification,uexp,uhospital,ucontact,mail;
ImageView uimageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);
        uname=findViewById(R.id.dname);
        uspec=findViewById(R.id.dspec);
        ufees=findViewById(R.id.contact);
        uqualification=findViewById(R.id.dqualification);
        uexp=findViewById(R.id.dexp);
        uhospital=findViewById(R.id.dhospital);
        ucontact=findViewById(R.id.dcontact);

        uimageView=findViewById(R.id.dphoto);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String spec= sharedPreferences.getString("specialization","").toString();
        // Get a reference to the Firebase Storage location where the image is stored
        StorageReference imageRef = FirebaseStorage.getInstance().getReference().child(spec+".jpg");
        SharedPreferences sharedPreferences2 = getSharedPreferences("doctor", Context.MODE_PRIVATE);
        String docname= sharedPreferences2.getString("doctor","").toString();


        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {

            @Override
            public void onSuccess(byte[] bytes) {

                // Create a bitmap image from the byte array
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                // Display the bitmap image in an ImageView

                uimageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(DoctorProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
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

                                String name = document.getString("name");
                                String specialization = document.getString("specialization");
                                String qualification = document.getString("qualification");
String experience=document.getString("experience");

                                String hospital = document.getString("hospital");
                                String phone=document.getString("contact");
                                String fees=document.getString("fees");
                                String email=document.getString("email");
                                uname.setText(name);
                                uspec.setText(specialization);
                               uqualification.setText(qualification);
uexp.setText(experience);
                               uhospital.setText(hospital);
                                ucontact.setText(phone);
                                ufees.setText(fees);

                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }

                }) .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DoctorProfileActivity.this, "fail", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}