package com.example.docbook;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ProductViewHolder> {
    private Context context;

    public String prod,name,price,manufacturer,quantity,item,expiry,direction,number;
    private List<Product> productList;
    private List<Product> productListFiltered;
    int lastPosition=-1;
    public CartAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        this.productListFiltered = productList;
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

        if(holder.getAdapterPosition()>lastPosition){
            Animation animation= AnimationUtils.loadAnimation(context,R.anim.slide_in);
            ((CartAdapter.ProductViewHolder)holder).itemView.startAnimation(animation);
          Product product = productListFiltered.get(position);
            holder.textViewProductName.setText(product.getName());
            holder.textViewProductPrice.setText("Rs:"+product.getPrice());
            holder.textViewProductQuantity.setText(" x "+product.getQuantity());
            lastPosition=holder.getAdapterPosition();
        }
   Product product = productListFiltered.get(position);
        holder.textViewProductName.setText(product.getName());
        holder.textViewProductPrice.setText("Rs:"+product.getPrice());
        holder.textViewProductQuantity.setText(" x "+product.getQuantity());
        prod=product.getName();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Get a reference to the Firebase Storage instance
        FirebaseStorage storage = FirebaseStorage.getInstance();

// Create a reference to the image file
        String imagePath = "medicine/" + prod + "/image.jpg";
        StorageReference imageRef = storage.getReference().child(imagePath);

// Download the image into a byte array
        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Convert the byte array into a bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                // Set the bitmap to the ImageView
                holder.imageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.e(TAG, "Error downloading image", exception);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        prod=product.getName();
        showBottomDialog();
    }
});

    }

    @Override
    public int getItemCount() {
        return productListFiltered.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewProductName, textViewProductPrice,textViewProductQuantity;
        ImageView imageView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewProductName = itemView.findViewById(R.id.product_name);
            textViewProductPrice = itemView.findViewById(R.id.product_user);
            textViewProductQuantity = itemView.findViewById(R.id.product_price);
            imageView=itemView.findViewById(R.id.product_image);

        }
    }

    public static class Product {
        private String name;
        private String price;
        private String quantity;

        public Product(String name, String price,String quantity) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        public String getName() {
            return name;
        }

        public String getPrice() {
            return price;
        }
        public String getQuantity() {
            return quantity;
        }
    }
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchText = charSequence.toString().toLowerCase().trim();

                if (searchText.isEmpty()) {
                    productListFiltered = productList;
                } else {
                    List<Product> filteredList = new ArrayList<>();

                    for (Product product : productList) {
                        if (product.getName().toLowerCase().contains(searchText)) {
                            filteredList.add(product);
                        }
                    }

                    productListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productListFiltered = (List<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void  showBottomDialog(){

        String[] quantityOptions = {"1", "2", "3","4","5"};

        final Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.bottomsheet_cart);
        TextView pname=dialog.findViewById(R.id.product_name);
        ImageView imageView=dialog.findViewById(R.id.product_image);
        TextView  pprice=dialog.findViewById(R.id.priceTextView);
        TextView pexpiry=dialog.findViewById(R.id.exp);
        TextView pitem=dialog.findViewById(R.id.item);
        TextView pquantity=dialog.findViewById(R.id.stripTextView);
        TextView pdirection=dialog.findViewById(R.id.discription);
        TextView Qnumber=dialog.findViewById(R.id.numofitems);
        Button cancel=dialog.findViewById(R.id.cancelorder);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are You Sure?")
                        .setContentText("Money will not be returned if already made Payment")
                        .setConfirmText("Confirm")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                Dialog dialog2 = new Dialog(context);
                                dialog2.setContentView(R.layout.loading_dialog);
                                dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog2.setCancelable(false);
                                dialog2.show();
                                SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                                String mail = sharedPreferences.getString("mail", "").toString();
                                String password = sharedPreferences.getString("password", "").toString();
                                // Inside your authentication method (e.g. email/password authentication)
                                FirebaseAuth.getInstance().signInWithEmailAndPassword(mail, password)
                                        .addOnCompleteListener(task2 -> {
                                            if (task2.isSuccessful()) {
                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                String uid = user.getUid();
                                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                DocumentReference docRef = db.collection("users").document(uid);
                                                docRef.get().addOnSuccessListener(documentSnapshot -> {
                                                    if (documentSnapshot.exists()) {
                                                        String username2 = documentSnapshot.getString("username");

                                                    }
                                                });
                                                Query itemDetailsRef =db.collectionGroup("itemdetails");

                                                itemDetailsRef.get().addOnCompleteListener(task -> {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot itemDetailsDoc : task.getResult()) {

                                                            String name2 = itemDetailsDoc.getString("name");
                                                            String user2 = itemDetailsDoc.getString("user");




                                                            if(user2.trim().equals("vinayak prakash") && name2.trim().equals(name)){

                                                              itemDetailsDoc.getReference().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                dialog2.dismiss();
                                                                                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                                                                        .setTitleText("Order has been Cancelled Successfully")
                                                                                        .setConfirmText("OK")
                                                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                            @Override
                                                                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                                                // Start the new activity
                                                                                    dialog2.dismiss();
                                                                                                Intent intent = new Intent(context, CartActivity.class);
                                                                                                context.startActivity(intent);
                                                                                                // Dismiss the dialog
                                                                                                sweetAlertDialog.dismiss();
                                                                                            }
                                                                                        })
                                                                                        .show();

                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                dialog.dismiss();
                                                                                Toast.makeText(context, "Error Please Try again", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });

                                                            }
                                                            else {

                                                            }

                                                                                               }
                                                    } else {
                                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                                    }
                                                });


                                            } else {
                                                // Handle authentication error
                                            }
                                        });
                                // perform action on confirm button click
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                // perform action on cancel button click
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
                dialog.dismiss();
            }
        });
        TextView pmanufacturer=dialog.findViewById(R.id.manufacturerTextView);
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String uname = sharedPreferences.getString("name", "").toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Get a reference to the Firebase Storage instance
        FirebaseStorage storage = FirebaseStorage.getInstance();

// Create a reference to the image file
        String imagePath = "medicine/" + prod + "/image.jpg";
        StorageReference imageRef = storage.getReference().child(imagePath);

// Download the image into a byte array
        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Convert the byte array into a bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                // Set the bitmap to the ImageView
                imageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.e(TAG, "Error downloading image", exception);
            }
        });

        CollectionReference usersRef = db.collection("bookings");
        DocumentReference docRef = usersRef.document(uname).collection("itemdetails").document(prod);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    name = documentSnapshot.getString("name");
                    price = documentSnapshot.getString("cost");
                    expiry = documentSnapshot.getString("expiry");
                    item = documentSnapshot.getString("item");
                    manufacturer = documentSnapshot.getString("manufacturer");
                    direction = documentSnapshot.getString("direction");
                    quantity = documentSnapshot.getString("quantity");
number=documentSnapshot.getString("numberofitems");
                    // Do something with the data, e.g. update UI
                    pname.setText(name);
                    pprice.setText("Rs: "+price);
                    pexpiry.setText("Expiry Date: "+expiry);
                    pitem.setText(item);
                    pmanufacturer.setText("Manufacturer: "+manufacturer);
Qnumber.setText("Quantity: "+number);
                    pdirection.setText(direction);

                }
            }
        });




        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}
