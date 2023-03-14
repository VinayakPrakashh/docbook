package com.example.docbook;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
   TextView storenav;
    SweetAlertDialog sDialog;
    private RecyclerView recyclerView;
    private AdminPatientAdapter adminpatientadapter;
    private List<AdminPatientAdapter.Product> productList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_patients);
        sDialog = new SweetAlertDialog(AdminPatientsActivity.this);
        storenav=findViewById(R.id.store);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        adminpatientadapter = new AdminPatientAdapter(this, productList);
        recyclerView.setAdapter(adminpatientadapter);
        recyclerView.addItemDecoration(new CartItemDecoration(16));

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
                                String pnumber = document.getString("pnumber");

                                // Do something with the patient details
                                AdminPatientAdapter.Product product = new AdminPatientAdapter.Product(patientName,pnumber);
                                productList.add(product);
                                adminpatientadapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d(TAG, "Error getting patient collection", task.getException());
                        }
                    }
                });


        dialog.dismiss();
      storenav.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              startActivity(new Intent(AdminPatientsActivity.this,AddPatientActivity.class));
          }
      });

    }
    public void onBackPressed(){
        startActivity(new Intent(AdminPatientsActivity.this,AdminHomeActivity.class));
    }public void  showBottomDialog(){
        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.bottomsheet_layout);
        LinearLayout video=dialog.findViewById(R.id.layoutShorts);
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}