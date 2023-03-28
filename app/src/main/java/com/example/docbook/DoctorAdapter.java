package com.example.docbook;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ProductViewHolder> {
    private Context context;

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
}
