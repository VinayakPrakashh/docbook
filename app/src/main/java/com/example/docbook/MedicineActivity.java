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
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docbook.ProductAdapter.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class MedicineActivity extends AppCompatActivity {
    Button cartnav;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<ProductAdapter.Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);

        cartnav = findViewById(R.id.cart);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(productAdapter);

        // Add the DividerItemDecoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this);
        recyclerView.addItemDecoration(dividerItemDecoration);

        Dialog dialog = new Dialog(MedicineActivity.this);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.setCancelable(false);
        dialog.show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.getString("name");
                                String price = document.getString("cost");

                                ProductAdapter.Product product = new Product(name, price);
                                productList.add(product);
                                dialog.dismiss();
                            }
                            productAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            dialog.dismiss();
                        }
                    }
                });
        cartnav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
showBottomDialog();
            }
        });

    }

    public void onBackPressed(){
        startActivity(new Intent(MedicineActivity.this,HomeActivity.class));
    }
    public void  showBottomDialog(){
        final Dialog dialog=new Dialog(MedicineActivity.this);
        dialog.setContentView(R.layout.bottomsheet_medicine);


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}
