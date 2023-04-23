package com.example.docbook;

import static android.content.ContentValues.TAG;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

public class AdminHomeActivity extends AppCompatActivity {
LinearLayout settings,appointment,lgout,abouts,dallpatients,ddoctors,daddpatient,orders,allproducts,allproducts_t,doctors_t,patients_t;

TextView p_all,o_all,patie_all;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_home_admin);
orders=findViewById(R.id.allorders);
dallpatients=findViewById(R.id.allpatients);
        daddpatient=findViewById(R.id.addpatient);
       ddoctors =findViewById(R.id.doctors);
allproducts=findViewById(R.id.products);
        settings=findViewById(R.id.settings);
        appointment=findViewById(R.id.doctors);

        allproducts_t=findViewById(R.id.products_top);
        doctors_t=findViewById(R.id.doctors_top);
        patients_t=findViewById(R.id.allpatients_top);
        abouts=findViewById(R.id.about);
        lgout=findViewById(R.id.logout);
        p_all=findViewById(R.id.product_all);
        o_all=findViewById(R.id.order_all);
        patie_all=findViewById(R.id.patient_all);
        Intent intent = getIntent();

if(intent.hasExtra("animkey")){

    special();
}
else{

    normal();
}


        allproducts_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    startActivity(new Intent(AdminHomeActivity.this,AdminProductViewActivity.class));
                }
            }
        });
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

                startActivity(new Intent(AdminHomeActivity.this, MainActivity.class));
            }
        });
        doctors_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHomeActivity.this,AdminAppointmentActivity.class));
            }
        });
        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHomeActivity.this,DoctorAppointmentActivity.class));
            }
        });
        abouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHomeActivity.this,AboutActivity.class));
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
        patients_t.setOnClickListener(new View.OnClickListener() {
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
public void normal(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("products");

        collectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numDocuments = queryDocumentSnapshots.size();
                String numDocumentsString = String.valueOf(numDocuments);
                // Do something with the number of documents
                p_all.setText(numDocumentsString);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error getting number of documents: " + e.getMessage());
            }
        });
        CollectionReference collectionRef2 = db.collection("patients");

        collectionRef2.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numDocuments = queryDocumentSnapshots.size();

                String numDocumentsStr = Integer.toString(numDocuments); // convert to String

                patie_all.setText(numDocumentsStr);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error getting number of documents: " + e.getMessage());
            }
        });
        CollectionReference collectionRef3 = db.collection("products");

        collectionRef3.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numDocuments = queryDocumentSnapshots.size();
                String numDocumentsStr = Integer.toString(numDocuments); // convert to String

                p_all.setText(numDocumentsStr);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error getting number of documents: " + e.getMessage());
            }
        });

        FirebaseFirestore.getInstance().collectionGroup("itemdetails")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int numDocuments = task.getResult().size();
                            String numDocumentsStr = Integer.toString(numDocuments); // convert to String

                            o_all.setText(numDocumentsStr);


                        } else {
                            // Handle error
                        }
                    }
                });
    }
    public void special(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("products");

        collectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numDocuments = queryDocumentSnapshots.size();
                String numDocumentsString = String.valueOf(numDocuments);
                // Do something with the number of documents
                p_all.setText(numDocumentsString);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error getting number of documents: " + e.getMessage());
            }
        });
        CollectionReference collectionRef2 = db.collection("patients");

        collectionRef2.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numDocuments = queryDocumentSnapshots.size();
                ValueAnimator animator = ValueAnimator.ofInt(0, numDocuments);
                animator.setDuration(500);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        patie_all.setText(animation.getAnimatedValue().toString());
                    }
                });
                animator.start();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error getting number of documents: " + e.getMessage());
            }
        });
        CollectionReference collectionRef3 = db.collection("products");

        collectionRef3.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numDocuments = queryDocumentSnapshots.size();
                ValueAnimator animator = ValueAnimator.ofInt(0, numDocuments);
                animator.setDuration(500);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        p_all.setText(animation.getAnimatedValue().toString());
                    }
                });
                animator.start();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error getting number of documents: " + e.getMessage());
            }
        });

        FirebaseFirestore.getInstance().collectionGroup("itemdetails")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int numDocuments = task.getResult().size();

                            ValueAnimator animator = ValueAnimator.ofInt(0, numDocuments);
                            animator.setDuration(500);
                            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    o_all.setText(animation.getAnimatedValue().toString());
                                }
                            });
                            animator.start();

                        } else {
                            // Handle error
                        }
                    }
                });
    }
}