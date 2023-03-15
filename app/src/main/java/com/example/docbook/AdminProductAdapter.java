package com.example.docbook;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.ProductViewHolder> {
    private Context context;
    public String prod,name,price,quantity,uuser,addr,pin,prodname,uname;
    private List<Product> productList;

    public AdminProductAdapter(Context context, List<Product> productList) {
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
        holder.textViewProductPrice.setText(product.getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                prod=product.getName();
                uuser=product.getPrice();

                showBottomDialog();
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

    public void  showBottomDialog(){


        final Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.bottomsheet_admin_medicine);
       TextView pname=dialog.findViewById(R.id.product_name);
        ImageView imageView=dialog.findViewById(R.id.product_image);
       TextView  pprice=dialog.findViewById(R.id.price);
        TextView username=dialog.findViewById(R.id.user);

        TextView pquantity=dialog.findViewById(R.id.quantity);


        Button status=dialog.findViewById(R.id.status);
        TextView pincode=dialog.findViewById(R.id.pin);
        TextView address=dialog.findViewById(R.id.addr);


       status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        Toast.makeText(context, uuser, Toast.LENGTH_SHORT).show();
        CollectionReference usersRef = db.collection("bookings");
        DocumentReference docRef = usersRef.document(uuser);
        DocumentReference docRef2=docRef.collection("itemdetails").document(prod);
        docRef2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                     prodname = documentSnapshot.getString("name");
                     uname=documentSnapshot.getString("user");
                     price = documentSnapshot.getString("cost");
                     quantity = documentSnapshot.getString("numberofitems");
                     pin=documentSnapshot.getString("pincode");
                    addr=documentSnapshot.getString("address");
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
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}
