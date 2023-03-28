package com.example.docbook;

import static android.content.ContentValues.TAG;

import static com.example.docbook.AddPatientActivity.REQUEST_CODE_IMAGE_PICKER;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docbook.AdminProductViewAdapter.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AdminProductViewActivity extends AppCompatActivity {
    Button cartnav;
    public String name;
    public ImageView imageView;
    private RecyclerView recyclerView;
    public  TextView pname;
    private AdminProductViewAdapter adminProductViewAdapter;
    public String product_name,product_price,product_expiry,product_quantity,product_packing,product_manufacturer,product_direction,product_delivery;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_view);

        cartnav = findViewById(R.id.cart);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        adminProductViewAdapter = new AdminProductViewAdapter(this, productList);
        recyclerView.setAdapter(adminProductViewAdapter);

        // Add the DividerItemDecoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this);
        recyclerView.addItemDecoration(dividerItemDecoration);
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adminProductViewAdapter.getFilter2().filter(newText);
                return false;
            }
        });

        Dialog dialog = new Dialog(AdminProductViewActivity.this);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                             name = document.getString("name");
                                String price = document.getString("cost");

                                Product product = new Product(name, price);
                                productList.add(product);
                                dialog.dismiss();
                            }
                            adminProductViewAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            dialog.dismiss();
                        }
                    }
                });

        cartnav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
showBottomDialog();
            }
        });

    }

    public void onBackPressed(){
        startActivity(new Intent(AdminProductViewActivity.this,AdminHomeActivity.class));
    }

    public void  showBottomDialog(){



        final Dialog dialog=new Dialog(AdminProductViewActivity.this);
        dialog.setContentView(R.layout.bottomsheet_add_medicine);
         pname=dialog.findViewById(R.id.name);
         imageView=dialog.findViewById(R.id.product_image);
        TextView  pprice=dialog.findViewById(R.id.price);
        TextView pexpiry=dialog.findViewById(R.id.expiry);
        TextView ppacking=dialog.findViewById(R.id.pack);
        TextView pquantity=dialog.findViewById(R.id.quantity);
        TextView pdirection=dialog.findViewById(R.id.direction);
        TextView pmanufacturer=dialog.findViewById(R.id.manufacturer);
        TextView addimage=dialog.findViewById(R.id.addimage);
        Button add=dialog.findViewById(R.id.addmedicine);
        TextView datedelivery=dialog.findViewById(R.id.delivery);


addimage.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_IMAGE_PICKER);
    }
});
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                product_name=pname.getText().toString();
               product_delivery=datedelivery.getText().toString();
               product_direction=pdirection.getText().toString();
               product_expiry=pexpiry.getText().toString();
               product_manufacturer=pmanufacturer.getText() .toString();
               product_packing=ppacking.getText().toString();
               product_quantity=pquantity.getText().toString();
               product_price=pprice.getText().toString();
// Access a Firestore instance from your Activity or Fragment
                // Access a Firestore instance from your Activity or Fragment
                FirebaseFirestore db = FirebaseFirestore.getInstance();

// Create a new data object with the desired fields
                Map<String, Object> newData = new HashMap<>();
                newData.put("delivery", product_delivery);
                newData.put("direction", product_direction);
                newData.put("cost", product_price);
                newData.put("manufacturer", product_manufacturer);
                newData.put("expiry", product_expiry);
                newData.put("quantity", product_packing);
                newData.put("item", product_quantity);
                newData.put("name", product_name);


// Add the new data to a Firestore document in the "orders" collection
                db.collection("products")
                        .document(product_name)
                        .set(newData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + name);
                                // Show a success message to the user
                                Toast.makeText(AdminProductViewActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                                // Show an error message to the user
                                Toast.makeText(AdminProductViewActivity.this, "Error placing order", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });








            }
        });





        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE_PICKER && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Dialog dialog = new Dialog(AdminProductViewActivity.this);
            dialog.setContentView(R.layout.loading_dialog);
            dialog.setCancelable(false);
            dialog.show();
            product_name=pname.getText().toString();
            Uri imageUri = data.getData();
            uploadImageToFirebaseStorage(imageUri);
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("medicine").child(product_name).child("image.jpg");

// Download the contents of the file as a byte array
            imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {

                @Override
                public void onSuccess(byte[] bytes) {

                    // Create a bitmap image from the byte array
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    // Display the bitmap image in an ImageView
                    dialog.dismiss();
                    imageView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@androidx.annotation.NonNull Exception exception) {
                    // Handle any errors that occur
                }
            });
            dialog.dismiss();
        }
    }
    private void uploadImageToFirebaseStorage(Uri imageUri) {
        product_name=pname.getText().toString();

        FirebaseStorage storageRef = FirebaseStorage.getInstance();

        // Create a new StorageReference with the user's UID as the file name
        StorageReference imageRef = storageRef.getReference().child("medicine").child(product_name).child("image.jpg");

        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        FirebaseStorage storage = FirebaseStorage.getInstance();

                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();

// Create a reference to the image file you want to download
                        StorageReference imageRef = storage.getReference().child("medicine").child(product_name).child("image.jpg");

// Download the image file into a byte array
                        Dialog dialog = new Dialog(AdminProductViewActivity.this);
                        dialog.setContentView(R.layout.loading_dialog);
                        dialog.setCancelable(false);
                        dialog.show();
                        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                // Convert the byte array into a Bitmap object
                                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                                // Set taddpahe Bitmap object in an ImageView
                                dialog.dismiss();
                                imageView.setImageBitmap(bmp);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@androidx.annotation.NonNull Exception exception) {
                                // Handle any errors that occur during the download
                                dialog.dismiss();
                                Toast.makeText(AdminProductViewActivity.this, "Failed to Download Image", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.dismiss();
                        new SweetAlertDialog(AdminProductViewActivity.this)

                                .setTitleText("Medicine Image added")
                                .show();
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                dialog.dismiss();

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                        // Image upload failed

                        new SweetAlertDialog(AdminProductViewActivity.this)
                                .setTitleText("Failed to upload Image")
                                .show();
                    }
                });

    }
}
