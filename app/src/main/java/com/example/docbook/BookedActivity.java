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

import com.example.docbook.BookedAdapter.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BookedActivity extends AppCompatActivity {
    Button storenav;
    SweetAlertDialog sDialog;
    private RecyclerView recyclerView;
    private BookedAdapter bookedAdapter;
    private List<Product> productList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked);
     sDialog = new SweetAlertDialog(BookedActivity.this);
storenav=findViewById(R.id.store);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        bookedAdapter = new BookedAdapter(this, productList);
        recyclerView.setAdapter(bookedAdapter);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        String uid = auth.getCurrentUser().getUid();
        Dialog dialog = new Dialog(BookedActivity.this);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.setCancelable(false);
        dialog.show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference appointmentRef = db.collection("appointments").document(uid);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference doctorRef = appointmentRef.collection(doctorName).document("appointment");

        doctorRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Appointment details exist, get the data
                        String date = document.getString("date");
                        String doctorName = document.getString("doctorName");
                        // Do something with the appointment data
                    } else {
                        // Appointment details do not exist
                    }
                }
                            else{

                                new SweetAlertDialog(BookedActivity.this, SweetAlertDialog.ERROR_TYPE)

                                        .setContentText("Your Cart is Empty!")
                                        .show();

                            }
                           bookedAdapter.notifyDataSetChanged();
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
                startActivity(new Intent(BookedActivity.this,MedicineActivity.class));
            }
        });

    }
    public void onBackPressed(){
        startActivity(new Intent(BookedActivity.this,HomeActivity.class));
    }
}
