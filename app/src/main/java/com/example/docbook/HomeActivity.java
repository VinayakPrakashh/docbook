package com.example.docbook;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.checkerframework.checker.nullness.qual.NonNull;

public class HomeActivity extends AppCompatActivity {

LinearLayout store,cart,appointment;
TextView user;
ConstraintLayout finddoctor;
ImageView img;

    LinearLayout lgout, abouts, article,profile,s_cart,s_appointment,s_store,settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_home);
        lgout = findViewById(R.id.blogout);
        appointment = findViewById(R.id.bappointment);
        store = findViewById(R.id.bstore);
        cart = findViewById(R.id.bcart);
        abouts = findViewById(R.id.babout);
        article = findViewById(R.id.barticle);
        img=findViewById(R.id.imagepic);
        finddoctor=findViewById(R.id.finddoc);
profile=findViewById(R.id.bprofile);
s_cart=findViewById(R.id.scart);
s_appointment=findViewById(R.id.sappointment);
s_store=findViewById(R.id.sstore);
settings=findViewById(R.id.bsettings);
user=findViewById(R.id.username);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

            String uid2 = currentUser.getUid();

        FirebaseFirestore db2 = FirebaseFirestore.getInstance();
        DocumentReference docRef2 = db2.collection("users").document(uid2);

        docRef2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String username = documentSnapshot.getString("username");
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    user.setText(username);
                    myEdit.putString("name", username);
                    myEdit.commit();
                } else {
                    Log.d(TAG, "No such document");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error getting document: " + e);
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String uname = sharedPreferences.getString("name", "").toString();
user.setText(uname.trim());
        String suname=uname.trim();

        StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("users/" + suname + "/image");

// Download the contents of the file as a byte array
        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {

            @Override
            public void onSuccess(byte[] bytes) {

                // Create a bitmap image from the byte array
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                // Display the bitmap image in an ImageView

                img.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@androidx.annotation.NonNull Exception exception) {
                // Handle any errors that occur
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,EditProfileActivity.class));
            }
        });
        String mail = sharedPreferences.getString("mail", "").toString();
        String password = sharedPreferences.getString("password", "").toString();
        // Inside your authentication method (e.g. email/password authentication)
        FirebaseAuth.getInstance().signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String uid = user.getUid();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference docRef = db.collection("users").document(uid);
                        docRef.get().addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                String username = documentSnapshot.getString("username");
                                // Display the toast message if it hasn't been displayed yet
                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                                boolean isToastDisplayed = prefs.getBoolean("toast_displayed", false);
                                if (!isToastDisplayed) {
                                    Toast.makeText(this, "Welcome, " + username + "!", Toast.LENGTH_SHORT).show();
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putBoolean("toast_displayed", true);
                                    editor.apply();
                                }

                            }
                        });
                    } else {
                        // Handle authentication error
                    }
                });
        finddoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,AppointmentActivity.class));
            }
        });
profile.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        startActivity(new Intent(HomeActivity.this,ProfileActivity.class));
    }
});
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
                startActivity(new Intent(HomeActivity.this, MainActivity.class));

            }
        });
        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, AppointmentActivity.class));
            }
        });
        s_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, BookedActivity.class));
            }
        });
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, MedicineActivity.class));
            }
        });
        s_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, MedicineActivity.class));
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
            }
        });
        s_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
            }
        });
        abouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, AboutActivity.class));
            }
        });
        article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ImageSliderActvity.class));
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
            }
        });
        user.setText(uname);
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
                        HomeActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

}