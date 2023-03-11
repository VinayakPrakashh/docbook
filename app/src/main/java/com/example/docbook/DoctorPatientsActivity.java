package com.example.docbook;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DoctorPatientsActivity extends AppCompatActivity {
   TextView storenav;
    SweetAlertDialog sDialog;
    private RecyclerView recyclerView;
    private PatientAdapter patientadapter;
    private List<PatientAdapter.Product> productList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_patients);
        sDialog = new SweetAlertDialog(DoctorPatientsActivity.this);
        storenav=findViewById(R.id.store);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        patientadapter = new PatientAdapter(this, productList);
        recyclerView.setAdapter(patientadapter);
        recyclerView.addItemDecoration(new CartItemDecoration(16));

        Dialog dialog = new Dialog(DoctorPatientsActivity.this);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.setCancelable(false);
        dialog.show();
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        String specialization= sharedPreferences.getString("specialization","").toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference doctorsRef = db.collection("doctor");




        DocumentReference doctorDocRef = doctorsRef.document(specialization);

        CollectionReference patientRef = doctorDocRef.collection("patient");

        patientRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                    String patientName = documentSnapshot.getString("name");
                    String pnumber = documentSnapshot.getString("pnumber");
                    PatientAdapter.Product product = new PatientAdapter.Product(patientName,pnumber);
                    productList.add(product);
                    patientadapter.notifyDataSetChanged();
                    // Do something with patientName and patientPhone
                }
            }
        });




        dialog.dismiss();
      storenav.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              startActivity(new Intent(DoctorPatientsActivity.this,AddPatientActivity.class));
          }
      });

    }
    public void onBackPressed(){
        startActivity(new Intent(DoctorPatientsActivity.this,DoctorHomeActivity.class));
    }public void  showBottomDialog(){
        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.bottomsheet_layout);
        LinearLayout video=dialog.findViewById(R.id.layoutShorts);
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Toast.makeText(DoctorPatientsActivity.this, "okda", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}