package com.example.docbook;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        Product product = productList.get(position);
        holder.textViewProductName.setText(product.getName());
        holder.textViewProductPrice.setText(String.format("$%.2f", product.getPrice()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Create a new EditText view for quantity input
                final EditText inputQuantity = new EditText(context);
                inputQuantity.setTextColor(Color.BLACK);
                inputQuantity.setHint("Enter quantity here");
                inputQuantity.setHintTextColor(Color.GRAY);
                inputQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
                String prod=product.getName();
                String price = String.valueOf(product.getPrice());
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Select Quantity")
                        .setContentText(prod+" at Rs:"+price)
                        .setCustomView(inputQuantity)
                        .setConfirmText("ORDER")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
String quantity2=inputQuantity.getText().toString();
                                int quantity3=Integer.parseInt(quantity2);
                                int price2=Double.valueOf(product.getPrice()).intValue();
                                int tamount=(quantity3*price2);
                                String totalamount = String.valueOf(tamount);
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

// Create a new document in the "bookings" collection with the user's ID
                                DocumentReference userBookingRef = db.collection("bookings").document(userId);

// Create a new subcollection for the item
                                CollectionReference itemRef = userBookingRef.collection("itemdetails");
                                String quantity = inputQuantity.getText().toString();

// Create a new document with the item's name and cost
                                Map<String, Object> itemData = new HashMap<>();
                                itemData.put("name", prod);
                                itemData.put("cost",totalamount);
                                itemData.put("quantity",quantity);
// Use the item name as the document name within the subcollection
                                itemRef.document(prod).set(itemData)
                                        .addOnSuccessListener(documentReference -> {
                                            // Item added successfully


                                            new SweetAlertDialog(context)
                                                    .setTitleText("Order Placed Total Amount: "+tamount)
                                                    .show();
                                            sDialog.dismissWithAnimation();
                                        })
                                        .addOnFailureListener(e -> {
                                            // Error adding item
                                            // Error adding item
                                            new SweetAlertDialog(context)
                                                    .setTitleText("Failed to place order")
                                                    .show();
                                            sDialog.dismissWithAnimation();
                                        });

                                // 1. Success message


                            }
                        })
                        .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewProductName, textViewProductPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewProductName = itemView.findViewById(R.id.product_name);
            textViewProductPrice = itemView.findViewById(R.id.product_price);
        }
    }

    public static class Product {
        private String name;
        private double price;

        public Product(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }
    }
}
