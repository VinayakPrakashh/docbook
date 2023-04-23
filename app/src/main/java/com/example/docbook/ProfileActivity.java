package com.example.docbook;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileActivity extends AppCompatActivity {

TextView uname,uage,uemail,uaddress,ucity,uphone,umail,upassword;
public String nname;
Button b1;
TextView name_t;
ImageView uimageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_test);
        uname=findViewById(R.id.name);
        uage=findViewById(R.id.spec);
        uemail=findViewById(R.id.special_top);
        uaddress=findViewById(R.id.quali);
        ucity=findViewById(R.id.exp);
        uphone=findViewById(R.id.hospital);
        uimageView=findViewById(R.id.profile_photo);
        b1=findViewById(R.id.button);
        name_t=findViewById(R.id.name_top);
        umail=findViewById(R.id.mail);
        upassword=findViewById(R.id.password);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        Dialog dialog = new Dialog(ProfileActivity.this);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = auth.getCurrentUser().getUid();
        db.collection("users")
                .document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {


                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                              nname = document.getString("username");
                                String age = document.getString("age");
                                String address = document.getString("address");
                                String city=document.getString("city");
                                String mail = document.getString("email");
                                String phone=document.getString("contact");

                                String password=document.getString("password");
                                uname.setText(nname);
                                name_t.setText(nname);
                                uage.setText(age);
                               uemail.setText(mail);
                                ucity.setText(city);
                               uphone.setText(phone);
                                uaddress.setText(address);
                                umail.setText(mail);
                                upassword.setText(password);
                                b1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startActivity(new Intent(ProfileActivity.this,EditProfileActivity.class));
                                    }
                                });
// Create a reference to the image file you want to download
                                // Get a reference to the Firebase Storage location where the image is stored
                                StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("users/" + nname + "/image");

// Download the contents of the file as a byte array
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
                                        // Handle any errors that occur
                                    }
                                });
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });


dialog.dismiss();
    }

}