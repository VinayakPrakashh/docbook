package com.example.docbook;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DoctorPatientsActivity extends AppCompatActivity {

    SweetAlertDialog sDialog;
    private RecyclerView recyclerView;

    private PatientAdapter patientadapter;
    private List<PatientAdapter.Product> productList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_patients);
        sDialog = new SweetAlertDialog(DoctorPatientsActivity.this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        patientadapter = new PatientAdapter(this, productList);
        recyclerView.setAdapter(patientadapter);

SearchView searchView=findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                patientadapter.getFilter().filter(newText);
                return false;
            }
        });
        Dialog dialog = new Dialog(DoctorPatientsActivity.this);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.setCancelable(false);
        dialog.show();
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        String specialization= sharedPreferences.getString("specialization","").toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();




        db.collection("patients")
                .whereEqualTo("specialization", specialization).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                    String patientName = documentSnapshot.getString("name");
                    String pnumber = documentSnapshot.getString("pnumber");
                    String ward=documentSnapshot.getString("ward");

                    PatientAdapter.Product product = new PatientAdapter.Product(patientName,pnumber,specialization,ward);
                    productList.add(product);
                    patientadapter.notifyDataSetChanged();
                    // Do something with patientName and patientPhone
                }
            }
        });




        dialog.dismiss();

    }
    public void onBackPressed(){
        startActivity(new Intent(DoctorPatientsActivity.this,DoctorHomeActivity.class));
    }
}