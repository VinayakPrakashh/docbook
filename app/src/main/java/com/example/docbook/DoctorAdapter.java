package com.example.docbook;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ProductViewHolder> {
    private Context context;
public String username;
    private List<Product> productList;
int lastPosition=-1;
    public DoctorAdapter(Context context, List<Product> productList) {


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


        if(holder.getAdapterPosition()>lastPosition){
            Animation animation= AnimationUtils.loadAnimation(context,R.anim.slide_in);
            ((DoctorAdapter.ProductViewHolder)holder).itemView.startAnimation(animation);
           Product product = productList.get(position);
            holder.textViewname.setText(product.getName());
            holder.textViewtime.setText(product.getTime());
            holder.textViewdate.setText(product.getDate());
            lastPosition=holder.getAdapterPosition();
        }
       Product product = productList.get(position);
        holder.textViewname.setText(product.getName());
        holder.textViewtime.setText(product.getTime());
        holder.textViewdate.setText(product.getDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username=product.getName();
                showBottomDialog();
            }
        });
        StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("users/" + product.getName() + "/image");

// Download the contents of the file as a byte array
        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {

            @Override
            public void onSuccess(byte[] bytes) {

                // Create a bitmap image from the byte array
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                // Display the bitmap image in an ImageView

                holder.imageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors that occur
            }
        });


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewname, textViewtime,textViewdate;

        ImageView imageView;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewname = itemView.findViewById(R.id.product_name);
            textViewtime = itemView.findViewById(R.id.product_price);
            textViewdate = itemView.findViewById(R.id.product_user);
imageView=itemView.findViewById(R.id.product_image);
        }
    }

    public static class Product {
        private String name;
        private String time;
        private String date;

        public Product(String name, String time,String date) {
            this.name = name;
            this.time = time;
            this.date = date;
        }

        public String getName() {
            return name;
        }

        public String getTime() {
            return time;
        }
        public String getDate() {
            return date;
        }
    }
    public void  showBottomDialog(){
        final Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.bottomsheet_appointment);

        Button cancel=dialog.findViewById(R.id.cancelorder);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you Sure?")
                        .setContentText("The Appointment Status will be set to completed")
                        .setConfirmText("Confirm")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {


                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                Query itemDetailsRef =db.collectionGroup("item");
                                SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref",MODE_PRIVATE);
                                String specs = sharedPreferences.getString("specialization", "").toString();
                                itemDetailsRef.get().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot itemDetailsDoc : task.getResult()) {

                                            String user = itemDetailsDoc.getString("name");

                                            String specialization=itemDetailsDoc.getString("specialization");

                                            if(user.trim().equals(username) && specialization.trim().equals(specs)) {

                                                    itemDetailsDoc.getReference().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    dialog.dismiss();
                                                                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                                                            .setTitleText("Appointment Status Set to Completed!")
                                                                            .setConfirmText("OK")
                                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                @Override
                                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                                    // Start the new activity
                                                                                    dialog.dismiss();
                                                                                    Intent intent = new Intent(context, DoctorAppointmentActivity.class);
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
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}
