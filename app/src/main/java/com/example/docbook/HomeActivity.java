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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {



    CardView lgout, appointment, store, cart, abouts, article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        lgout = findViewById(R.id.bookings);
        appointment = findViewById(R.id.pediatrician);
        store = findViewById(R.id.orthologist);
        cart = findViewById(R.id.cardiologist);
        abouts = findViewById(R.id.neurologist);
        article = findViewById(R.id.radiologist);


        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
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

        lgout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, AppointmentActivity.class));
            }
        });
        store.setOnClickListener(new View.OnClickListener() {
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
        abouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
            }
        });
        article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ImageSliderActvity.class));
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
                        HomeActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

}