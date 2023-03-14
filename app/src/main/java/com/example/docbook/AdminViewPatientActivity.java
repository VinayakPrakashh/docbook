package com.example.docbook;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
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

public class AdminViewPatientActivity extends AppCompatActivity {
    ScrollView scrollView;
    public String name,specialization,keyname,special;
TextView uname,uage,ugender,upnumber,uward,ureason,uadmission,uspecialization,udischarge,uoccupation,uaddress,uphone,uemail;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    private static final int REQUEST_CODE_IMAGE_PICKER = 1;
ImageView uimageView;
    Button addp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_patient);
        scrollView=findViewById(R.id.scroll);
        uname=scrollView.findViewById(R.id.name);
        uage=scrollView.findViewById(R.id.age);
     ugender=scrollView.findViewById(R.id.gender);
     uemail=scrollView.findViewById(R.id.mail);
        uaddress=scrollView.findViewById(R.id.address);
        uward=scrollView.findViewById(R.id.ward);
        uphone=scrollView.findViewById(R.id.contact);
        uimageView=findViewById(R.id.photo);
        addp=scrollView.findViewById(R.id.add);
uspecialization=findViewById(R.id.specialization);
upnumber=scrollView.findViewById(R.id.pnumber);
        ureason=scrollView.findViewById(R.id.reason);
        uadmission=scrollView.findViewById(R.id.admission);
      udischarge=scrollView.findViewById(R.id.discharge);
uoccupation=scrollView.findViewById(R.id.occupation);
name=uname.getText().toString();
        Intent intent = getIntent();
    keyname = intent.getStringExtra("key");


        Dialog dialog = new Dialog(AdminViewPatientActivity.this);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.setCancelable(false);
        dialog.show();
      performauth();

        dialog.dismiss();
    }


    private void performauth(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
// Create a reference to the image file you want to download

        db.collection("patients").document(keyname)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {


                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                String name = document.getString("name");
                                String age = document.getString("age");
                                String address = document.getString("address");
                                String mail = document.getString("email");
                                String pnumber=document.getString("pnumber");
                                String ward=document.getString("ward");
                                String admission=document.getString("admission");
                                String discharge=document.getString("discharge");
                                String gender=document.getString("gender");
                                String occupation=document.getString("occupation");
                                String contact=document.getString("contact");
                                String reason=document.getString("reason");
                                 special=document.getString("specialization");

                                uname.setText("Name: " +name);
                                uage.setText("Age: "+age);
                                uemail.setText("Email id"+mail);
                                upnumber.setText("Patient Number: "+pnumber);
                                uaddress.setText("Address: "+address);
                                uward.setText("Ward No: "+ward);
                                uadmission.setText("Admission Date: "+admission);
                                ugender.setText("Gender: "+gender);
                                uoccupation.setText("Occupation: "+occupation);
                                uphone.setText("Phone No: "+contact);
                                ureason.setText("Reason For Admitting: "+reason);
                                udischarge.setText("Discharge Date: "+discharge);
uspecialization.setText("Specialization of Doctor: "+special);
                                searchForImage();
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

    }
    private void searchForImage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String imageName = keyname; // replace with your image name

        storageRef.child("patients/"+special+"/" + imageName).getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                uimageView.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error getting image from Firebase Storage", e);
            }
        });

    }

    }


