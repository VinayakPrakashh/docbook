package com.example.docbook;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docbook.AdminProductAdapter.Product;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminMedicineActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    private AdminProductAdapter adminproductAdapter;
    Button b1;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_medicine);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        adminproductAdapter = new AdminProductAdapter(this, productList);
        recyclerView.setAdapter(adminproductAdapter);
b1=findViewById(R.id.button);
        SearchView searchView = findViewById(R.id.searchView);
        // Add the DividerItemDecoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this);
        recyclerView.addItemDecoration(dividerItemDecoration);
        Dialog dialog = new Dialog(AdminMedicineActivity.this);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.setCancelable(false);
        dialog.show();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adminproductAdapter.getFilter().filter(newText);
                return false;
            }
        });
        // Add the DividerItemDecoration
b1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        startActivity(new Intent(AdminMedicineActivity.this,AdminHomeActivity.class));
    }
});
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query itemDetailsRef =db.collectionGroup("itemdetails");

        itemDetailsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot itemDetailsDoc : task.getResult()) {

                    String name = itemDetailsDoc.getString("name");
                    String user = itemDetailsDoc.getString("user");
                    String amount = itemDetailsDoc.getString("cost");

                    AdminProductAdapter.Product product = new AdminProductAdapter.Product(name, user,amount);
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
