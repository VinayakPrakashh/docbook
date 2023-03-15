package com.example.docbook;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docbook.AdminProductAdapter.Product;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminMedicineActivity extends AppCompatActivity {
    Button cartnav;
    private RecyclerView recyclerView;
    private AdminProductAdapter adminproductAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_medicine);

        cartnav = findViewById(R.id.cart);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        adminproductAdapter = new AdminProductAdapter(this, productList);
        recyclerView.setAdapter(adminproductAdapter);

        // Add the DividerItemDecoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this);
        recyclerView.addItemDecoration(dividerItemDecoration);
        Dialog dialog = new Dialog(AdminMedicineActivity.this);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.setCancelable(false);
        dialog.show();
        // Add the DividerItemDecoration

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query itemDetailsRef =db.collectionGroup("itemdetails");

        itemDetailsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot itemDetailsDoc : task.getResult()) {

                    String name = itemDetailsDoc.getString("name");
                    String price = itemDetailsDoc.getString("user");

                    AdminProductAdapter.Product product = new AdminProductAdapter.Product(name, price);
                    productList.add(product);
                    dialog.dismiss();
                    adminproductAdapter.notifyDataSetChanged();
                    // Add each 'product name' field to a list
                    // Do something with the product name here
                }
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });


    }

    public void onBackPressed(){
        startActivity(new Intent(AdminMedicineActivity.this,AdminHomeActivity.class));
    }

}
