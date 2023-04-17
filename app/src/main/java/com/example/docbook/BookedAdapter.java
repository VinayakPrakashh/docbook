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
import android.icu.text.SimpleDateFormat;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BookedAdapter extends RecyclerView.Adapter<BookedAdapter.ProductViewHolder> {

 public Context context;
 public  String spec;
    private List<Product> productList;

    int lastPosition=-1;
    public BookedAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_appointment, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        if(holder.getAdapterPosition()>lastPosition){
            Animation animation= AnimationUtils.loadAnimation(context,R.anim.slide_in);
            ((BookedAdapter.ProductViewHolder)holder).itemView.startAnimation(animation);
           Product product = productList.get(position);
            holder.textViewname.setText(product.getName());
            holder.textViewdate.setText(product.getDate());
            holder.textViewspec.setText(product.getSpecialization());
            holder.textViewtime.setText(product.getTime());
            lastPosition=holder.getAdapterPosition();
        }
       Product product = productList.get(position);
        holder.textViewname.setText(product.getName());
        holder.textViewdate.setText(product.getDate());
        holder.textViewspec.setText(product.getSpecialization());
        holder.textViewtime.setText(product.getTime());
        StorageReference imageRef = FirebaseStorage.getInstance().getReference().child(product.getSpecialization()+".jpg");
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

            }
        });

holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        spec=product.getSpecialization();
        showBottomDialog();
    }
});
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewname, textViewdate,textViewspec,textViewtime;
        ImageView imageView;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewname = itemView.findViewById(R.id.product_name);
            textViewdate= itemView.findViewById(R.id.product_price);
            textViewspec = itemView.findViewById(R.id.product_user);
imageView=itemView.findViewById(R.id.product_image);
textViewtime=itemView.findViewById(R.id.time);

        }
    }

    public static class Product {
        private String name;
        private String date;
        private String specialization;
        private String time;
        public Product(String name,String specialization, String date,String time) {
            this.name = name;
            this.date =date;
            this.specialization=specialization;
            this.time=time;

        }

        public String getName() {
            return name;
        }

        public String getDate() {
            return date;
        }
        public String getSpecialization() {
            return specialization;
        }
        public String getTime() {
            return time;
        }
    }
    public void  showBottomDialog(){
        final Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.bottomsheet_appointment_patient);

        Button cancel=dialog.findViewById(R.id.cancelorder);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(uid);

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {


                String username2 = documentSnapshot.getString("username");
                SharedPreferences sharedPreferences2 = context.getSharedPreferences("username",MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences2.edit();
                myEdit.putString("user", username2);

                myEdit.commit();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you Sure?")
                        .setContentText("Cancellation will fail if your appointment is Today")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {


                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                Query itemDetailsRef =db.collectionGroup("item");
                                SharedPreferences sharedPreferences3 = context.getSharedPreferences("username", MODE_PRIVATE);
                                String uu = sharedPreferences3.getString("user", "").toString();
                                itemDetailsRef.get().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot itemDetailsDoc : task.getResult()) {

                                            String user = itemDetailsDoc.getString("name");
                                            String time = itemDetailsDoc.getString("date");
                                            String specialization=itemDetailsDoc.getString("specialization");

                                            if(user.trim().equals(uu) && specialization.trim().equals(spec)) {

                                                SimpleDateFormat formatter = new SimpleDateFormat("d-M-yyyy");
                                                String date = formatter.format(new Date());


                                                if(time.trim().equals(date))  {
                                                    Toast.makeText(context, "Sorry you cannot cancel Apoointment right now", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                }
                                                else{

                                                    itemDetailsDoc.getReference().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    dialog.dismiss();
                                                                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                                                            .setTitleText("Appointment Cancelled Successfully!")
                                                                            .setConfirmText("OK")
                                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                @Override
                                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                                    // Start the new activity
                                                                                    dialog.dismiss();
                                                                                    Intent intent = new Intent(context, BookedActivity.class);
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
