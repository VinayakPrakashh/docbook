package com.example.docbook;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docbook.RecyclerView.CartItemDecoration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DoctorAppointmentActivity extends AppCompatActivity {
    Button storenav;
    SweetAlertDialog sDialog;
    private RecyclerView recyclerView;
    private DoctorAdapter doctorAdapter;
    private List<DoctorAdapter.Product> productList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_appointment);
        sDialog = new SweetAlertDialog(DoctorAppointmentActivity.this);
        storenav=findViewById(R.id.store);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        doctorAdapter = new DoctorAdapter(this, productList);
        recyclerView.setAdapter(doctorAdapter);
        recyclerView.addItemDecoration(new CartItemDecoration(16));

        Dialog dialog = new Dialog(DoctorAppointmentActivity.this);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.setCancelable(false);
        dialog.show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String doctorName = "Cardiologist";

        db.collection("appointments")
                .whereEqualTo("item." + doctorName + ".name", doctorName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        // get the appointment details from the document snapshot
                        String name = documentSnapshot.getString("name");
                        String time = documentSnapshot.getString("item." + doctorName + ".time");
                        String date = documentSnapshot.getString("item." + doctorName + ".date");
                        DoctorAdapter.Product product = new DoctorAdapter.Product(name, time,date);
                        productList.add(product);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error getting appointments for " + doctorName, e);
                });



        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

// Create a reference to the "itemdetails" collection for the user
        CollectionReference itemDetailsRef = db.collection("bookings").document(userId).collection("itemdetails");

// Query the items for the user
        itemDetailsRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() > 0) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    String name = document.getString("name");
                                    String price = document.getString("cost");
                                    String quantity=document.getString("quantity");

                                    DoctorAdapter.Product product = new DoctorAdapter.Product(name, price,quantity);
                                    productList.add(product);

                                    dialog.dismiss();
                                }

                            }
                            else{

                                new SweetAlertDialog(DoctorAppointmentActivity.this, SweetAlertDialog.ERROR_TYPE)

                                        .setContentText("Your Cart is Empty!")
                                        .show();

                            }
                            doctorAdapter.notifyDataSetChanged();
                        } else {

                            dialog.dismiss();
                            Log.d(TAG, "Error getting documents: ", task.getException());

                        }
                        dialog.dismiss();
                    }
                });
        storenav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DoctorAppointmentActivity.this,MedicineActivity.class));
            }
        });

    }
    public void onBackPressed(){
        startActivity(new Intent(DoctorAppointmentActivity.this,HomeActivity.class));
    }
}