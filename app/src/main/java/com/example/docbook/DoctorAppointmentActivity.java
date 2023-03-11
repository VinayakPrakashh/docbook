package com.example.docbook;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
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
        SharedPreferences sharedPreferences = getSharedPreferences("doctor",MODE_PRIVATE);
        String doctorName= sharedPreferences.getString("doctor","").toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collectionGroup("item")
                .whereEqualTo("doctor",doctorName)
                .orderBy("date", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {


                            // access the name, time and date fields from the documentSnapshot
                            String name = documentSnapshot.getString("name");
                            String time = documentSnapshot.getString("time");
                            String date = documentSnapshot.getString("date");

                            // display or process the appointment details
                            DoctorAdapter.Product product = new DoctorAdapter.Product(name,time,date);
                            productList.add(product);
                           doctorAdapter.notifyDataSetChanged();

                            dialog.dismiss();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


        dialog.dismiss();
        storenav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DoctorAppointmentActivity.this,MedicineActivity.class));
            }
        });

    }
    public void onBackPressed(){
        startActivity(new Intent(DoctorAppointmentActivity.this,DoctorHomeActivity.class));
    }
}