package com.example.docbook;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.ProductViewHolder> {
    private Context context;
    public String prod, name, price, quantity, uuser, addr, pin, prodname, uname, pprice;
    private List<Product> productList;
    private List<Product> productListFiltered;

    public AdminProductAdapter(Context context, List<Product> productList) {
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
        Product product = productListFiltered.get(position);

        holder.textViewProductName.setText(product.getName());
        holder.textViewProductUser.setText("Ordered By "+product.getUser());
        holder.textViewProductPrice.setText(product.getAmount());
        prod = product.getName();

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


                uuser = product.getUser();

                pprice = product.getAmount();

                showBottomDialog();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productListFiltered.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewProductName, textViewProductPrice, textViewProductUser;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewProductName = itemView.findViewById(R.id.product_name);
            textViewProductUser = itemView.findViewById(R.id.product_user);
            textViewProductPrice = itemView.findViewById(R.id.product_price);
            imageView = itemView.findViewById(R.id.product_image);

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

    public static class Product {
        private String name;
        private String user;
        private String amount;

        public Product(String name, String user, String amount) {
            this.name = name;
            this.user = user;
            this.amount = amount;
        }


        public String getName() {
            return name;
        }

        public String getUser() {
            return user;
        }

        public String getAmount() {
            return amount;
        }
    }



    public void showBottomDialog() {


        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.bottomsheet_admin_medicine);
        TextView pname = dialog.findViewById(R.id.product_name);
        ImageView imageView = dialog.findViewById(R.id.product_image);
        TextView pprice = dialog.findViewById(R.id.price);
        TextView username = dialog.findViewById(R.id.user);

        TextView pquantity = dialog.findViewById(R.id.quantity);


        Button status = dialog.findViewById(R.id.status);
        TextView pincode = dialog.findViewById(R.id.pin);
        TextView address = dialog.findViewById(R.id.addr);


        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
delete();
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Get a reference to the Firebase Storage instance
        FirebaseStorage storage = FirebaseStorage.getInstance();
// Create a reference to the image file
        String imagePath = "medicine/" + prod + "/image.jpg";
        StorageReference imageRef = storage.getReference().child(imagePath);

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
        DocumentReference docRef = usersRef.document(uuser);
        DocumentReference docRef2 = docRef.collection("itemdetails").document(prod);
        docRef2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    prodname = documentSnapshot.getString("name");
                    uname = documentSnapshot.getString("user");
                    price = documentSnapshot.getString("cost");
                    quantity = documentSnapshot.getString("numberofitems");
                    pin = documentSnapshot.getString("pincode");
                    addr = documentSnapshot.getString("address");
                    // Do something with the data, e.g. update UI
                    pname.setText(prodname);
                    pprice.setText(price);
                    username.setText(uname);
                    pincode.setText(pin);
                    address.setText(addr);
                    pquantity.setText(quantity);
                }
            }
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    public void delete() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Toast.makeText(context, uuser, Toast.LENGTH_SHORT).show();
        DocumentReference parentDocRef = db.collection("bookings").document(uuser).collection("itemdetails").document(prod);
        parentDocRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                parentDocRef.delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                                        .setTitleText("Status Set to Delivered")
                                                        .setConfirmText("OK")
                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                Intent intent = new Intent(context, AdminMedicineActivity.class);
                                                                context.startActivity(intent);
                                                                sweetAlertDialog.dismiss();
                                                            }
                                                        })
                                                        .show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                new SweetAlertDialog(context)
                                                        .setTitleText("Failed to Clear Data")
                                                        .show();
                                            }
                                        });
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

}

