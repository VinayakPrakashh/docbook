package com.example.docbook;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

   public static final int UPI_PAYMENT = 0;
    private Context context;
    public String prod,name,price,manufacturer,quantity,item,expiry,direction,date;
    private List<Product> productList;
    private List<Product> productListFiltered;
    int lastPosition=-1;

    public ProductAdapter(Context context, List<Product> productList) {
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
            ((ProductAdapter.ProductViewHolder)holder).itemView.startAnimation(animation);
            Product product = productListFiltered.get(position);
            holder.textViewProductName.setText(product.getName());
            holder.textViewProductPrice.setText( "Rs: "+product.getPrice());
            lastPosition=holder.getAdapterPosition();
        }
        Product product = productListFiltered.get(position);
        holder.textViewProductName.setText(product.getName());
        holder.textViewProductPrice.setText( "Rs: "+product.getPrice());
        prod=product.getName();


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
        TextView textViewProductName, textViewProductPrice;
        ImageView imageView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewProductName = itemView.findViewById(R.id.product_name);
            textViewProductPrice = itemView.findViewById(R.id.product_price);
            imageView=itemView.findViewById(R.id.product_image);

        }
    }

    public static class Product {
        private String name;
        private String price;

        public Product(String name, String price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public String getPrice() {
            return price;
        }
    }
    public Filter getFilter2() {
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
        dialog.setContentView(R.layout.bottomsheet_medicine);
       TextView pname=dialog.findViewById(R.id.product_name);
        ImageView imageView=dialog.findViewById(R.id.product_image);
       TextView  pprice=dialog.findViewById(R.id.priceTextView);
        TextView pexpiry=dialog.findViewById(R.id.exp);
        TextView pitem=dialog.findViewById(R.id.item);
        TextView pquantity=dialog.findViewById(R.id.stripTextView);
        TextView pdirection=dialog.findViewById(R.id.discription);
        TextView pmanufacturer=dialog.findViewById(R.id.manufacturerTextView);
        TextView qselects=dialog.findViewById(R.id.qselect);
        Button place=dialog.findViewById(R.id.placeorder);
        EditText pincode=dialog.findViewById(R.id.pin);
        EditText address=dialog.findViewById(R.id.addr);
TextView datedelivery=dialog.findViewById(R.id.product_date);



        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(qselects.getText().toString().equals("Select")) {
                    // Display error message
                    Toast.makeText(context, "Please select quantity", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(pincode.getText().toString().isEmpty()) {
                    // Display error message
                    pincode.setError("Pincode is required");
                    return;
                }

                if(address.getText().toString().isEmpty()) {
                    // Display error message
                    address.setError("Address is required");
                    return;
                }

                String pin=pincode.getText().toString();
                String addresses=address.getText().toString();
                String numberofitems=qselects.getText().toString();
                int amount=Integer.parseInt((qselects.getText().toString()));
                int cost=Integer.parseInt(price);
                int famount=amount*cost;
                String finalamount=String.valueOf(famount);
                SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                String uname = sharedPreferences.getString("name", "").toString();
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                // Check if user has already placed an order with a matching prod value
                db.collection("bookings").document(uname).collection("itemdetails").document(prod)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        // User has already placed an order with a matching prod value, show an error message
                                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText("Order Already Placed.")
                                                .setContentText("You have already placed an order for this product. Please wait for your previous order to be delivered or cancel it to place a new order.")
                                                .show();
                                    } else {
                                        dialog.dismiss();
                                        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                                .setTitleText("Confirm Purchase?")
                                                .setContentText("Total Amount: "+finalamount)
                                                .setConfirmText("Confirm")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {

                                                        final Dialog dialog2=new Dialog(context);
                                                        dialog2.setContentView(R.layout.bottomsheet_payment);

                                                        ImageView gpay=dialog2.findViewById(R.id.googlepay);
                                                        ImageView cod=dialog2.findViewById(R.id.cashondelivery);
                                                        dialog2.show();
                                                        dialog2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                                                        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                        dialog2.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
                                                        dialog2.getWindow().setGravity(Gravity.BOTTOM);
                                                        gpay.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                Uri uri = Uri.parse("upi://pay").buildUpon()
                                                                        .appendQueryParameter("pa", "shibitk1976-1@oksbi")
                                                                        .appendQueryParameter("pn", "Vinayak Prakash")
                                                                        .appendQueryParameter("mc", "")
                                                                        //.appendQueryParameter("tid", "02125412")
                                                                        .appendQueryParameter("tr", "25564594")
                                                                        .appendQueryParameter("tn", "")
                                                                        .appendQueryParameter("am", finalamount)
                                                                        .appendQueryParameter("cu", "INR")
                                                                        //.appendQueryParameter("refUrl", "blueapp")
                                                                        .build();


                                                                Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
                                                                upiPayIntent.setData(uri);

                                                                // will always show a dialog to user to choose an app
                                                                Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

                                                                // check if intent resolves
                                                                if(null != chooser.resolveActivity(context.getPackageManager())) {
                                                                    ((Activity) context).startActivityForResult(chooser, UPI_PAYMENT);
                                                                } else {
                                                                    Toast.makeText(context,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
                                                                }


                                                            }
                                                        });
                                                        cod.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                                                                String uname = sharedPreferences.getString("name", "").toString();
                                                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                                Map<String, Object> data = new HashMap<>();
                                                                data.put("cost", finalamount);
                                                                data.put("direction", direction);
                                                                data.put("expiry", expiry);
                                                                data.put("item", item);
                                                                data.put("quantity", quantity);
                                                                data.put("name", name);
                                                                data.put("manufacturer", manufacturer);
                                                                data.put("pincode", pin);
                                                                data.put("address", addresses);
                                                                data.put("user", uname);
                                                                data.put("numberofitems",numberofitems);
                                                                data.put("delivery",date.trim());

                                                                db.collection("bookings").document(uname).collection("itemdetails").document(prod)
                                                                        .set(data)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {

                                                                                new SweetAlertDialog(context)
                                                                                        .setTitleText("Order Placed.")
                                                                                        .show();
                                                                                dialog2.dismiss();
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                // Handle any errors here
                                                                                dialog2.dismiss();
                                                                            }
                                                                        });
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
                                    }
                                } else {
                                    // Handle any errors here
                                }
                            }

                        });

            }
        });

        qselects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Select");
                builder.setSingleChoiceItems(quantityOptions, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle gender selection here
                        String selectedquantity = quantityOptions[which];
                        qselects.setText(selectedquantity);
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
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

        CollectionReference usersRef = db.collection("products");
        DocumentReference docRef = usersRef.document(prod);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
date=documentSnapshot.getString("delivery");
                    // Do something with the data, e.g. update UI
                    pname.setText(name);
                    pprice.setText("Rs: "+price);
                    pexpiry.setText("Expiry Date: "+expiry);
                    pitem.setText(item);
                    pmanufacturer.setText("Manufacturer: "+manufacturer);
                    pquantity.setText(quantity);
                    pdirection.setText(direction);
                    datedelivery.setText("Expected Delivery Date: "+date);

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
