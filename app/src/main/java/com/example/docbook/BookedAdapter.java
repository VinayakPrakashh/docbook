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

public class BookedAdapter extends RecyclerView.Adapter<BookedAdapter.ProductViewHolder> {
    private Context context;
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
        View view = inflater.inflate(R.layout.layout_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        if(holder.getAdapterPosition()>lastPosition){
            Animation animation= AnimationUtils.loadAnimation(context,R.anim.slide_in);
            ((BookedAdapter.ProductViewHolder)holder).itemView.startAnimation(animation);
            holder.textViewname.setText(product.getName());
            holder.textViewdate.setText(product.getDate());
            holder.textViewspec.setText(product.getSpecialization());
            lastPosition=holder.getAdapterPosition();
        }

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


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewname, textViewdate,textViewspec;
        ImageView imageView;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewname = itemView.findViewById(R.id.product_name);
            textViewdate= itemView.findViewById(R.id.product_price);
            textViewspec = itemView.findViewById(R.id.product_user);
imageView=itemView.findViewById(R.id.product_image);
        }
    }

    public static class Product {
        private String name;
        private String date;
        private String specialization;

        public Product(String name,String specialization, String date) {
            this.name = name;
            this.date =date;
            this.specialization=specialization;

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
    }
}
