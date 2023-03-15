package com.example.docbook;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AdminPatientsActivity extends AppCompatActivity {

    SweetAlertDialog sDialog;
    private RecyclerView recyclerView;
    private AdminPatientAdapter adminpatientadapter;
    private List<AdminPatientAdapter.Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_patients);
        sDialog = new SweetAlertDialog(AdminPatientsActivity.this);

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        adminpatientadapter = new AdminPatientAdapter(this, productList);
        recyclerView.setAdapter(adminpatientadapter);
        recyclerView.addItemDecoration(new CartItemDecoration(16));

        SearchView searchView = findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adminpatientadapter.getFilter().filter(newText);
                return false;
            }
        });

        Dialog dialog = new Dialog(AdminPatientsActivity.this);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.setCancelable(false);
        dialog.show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

// Replace "patients" with the name of the patient collection
        db.collection("patients").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Retrieve patient name and phone number
                                String patientName = document.getId();
                                String special=document.getString("specialization");
                                String ward=document.getString("ward");
                                String pnumber = document.getString("pnumber");

                                // Do something with the patient details
                                AdminPatientAdapter.Product product = new AdminPatientAdapter.Product(patientName,pnumber,special,ward);
                                productList.add(product);
                                adminpatientadapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d(TAG, "Error getting patient collection", task.getException());
                        }
                    }
                });


        dialog.dismiss();


    }
    public void onBackPressed(){
        startActivity(new Intent(AdminPatientsActivity.this,AdminHomeActivity.class));
    }
}