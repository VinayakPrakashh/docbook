package com.example.docbook;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docbook.CartAdapter.Product;
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

public class CartActivity extends AppCompatActivity {
    Button storenav;
    SweetAlertDialog sDialog;
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<Product> productList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
     sDialog = new SweetAlertDialog(CartActivity.this);
storenav=findViewById(R.id.store);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        cartAdapter = new CartAdapter(this, productList);
        recyclerView.setAdapter(cartAdapter);
        recyclerView.addItemDecoration(new CartItemDecoration(16));
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                cartAdapter.getFilter().filter(newText);
                return false;
            }
        });

        Dialog dialog = new Dialog(CartActivity.this);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.setCancelable(false);
        dialog.show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String uname = sharedPreferences.getString("name", "").toString();
// Create a reference to the "itemdetails" collection for the user
        CollectionReference itemDetailsRef = db.collection("bookings").document(uname).collection("itemdetails");

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
                                String quantity=document.getString("numberofitems");

                                Product product = new Product(name, price,quantity);
                                productList.add(product);

                                dialog.dismiss();
                            }

                            }
                            else{

                                new SweetAlertDialog(CartActivity.this, SweetAlertDialog.ERROR_TYPE)

                                        .setContentText("Your Cart is Empty!")
                                        .show();

                            }
                           cartAdapter.notifyDataSetChanged();
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
                startActivity(new Intent(CartActivity.this,MedicineActivity.class));
            }
        });

    }
    public void onBackPressed(){
        startActivity(new Intent(CartActivity.this,HomeActivity.class));
    }
}
