package com.example.docbook;

import static android.content.ContentValues.TAG;
import static com.example.docbook.AddPatientActivity.REQUEST_CODE_IMAGE_PICKER;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AdminEditMedicine extends AppCompatActivity {
    public ImageView imageView;
public String prod;
public Context context=AdminEditMedicine.this;
    public String name,price,manufacturer,quantity,item,expiry,direction,date,product_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_medicine);
        TextView pname=findViewById(R.id.product_name);
       imageView=findViewById(R.id.product_image);
        TextView  pprice=findViewById(R.id.product_price);
        TextView pexpiry=findViewById(R.id.product_expiry);
        TextView ppacking=findViewById(R.id.product_packing);
        TextView pquantity=findViewById(R.id.product_quantity);
        TextView pdirection=findViewById(R.id.product_direction);
        TextView pmanufacturer=findViewById(R.id.product_manufacturer);
        Button  addimage=findViewById(R.id.addimage);
        Button edit=findViewById(R.id.editmedicine);
        TextView datedelivery=findViewById(R.id.product_delivery);
        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 product_name=pname.getText().toString();
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_IMAGE_PICKER);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
               String product_name=pname.getText().toString();
                String  product_delivery=datedelivery.getText().toString();
                String product_direction=pdirection.getText().toString();
                String product_expiry=pexpiry.getText().toString();
                String product_manufacturer=pmanufacturer.getText() .toString();
                String product_packing=ppacking.getText().toString();
                String product_quantity=pquantity.getText().toString();
                String product_price=pprice.getText().toString();
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
                                Toast.makeText(context, "Changes Saved successfully", Toast.LENGTH_SHORT).show();
startActivity(new Intent(AdminEditMedicine.this,AdminProductViewActivity.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@org.checkerframework.checker.nullness.qual.NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                                // Show an error message to the user
                                Toast.makeText(context, "Error placing order", Toast.LENGTH_SHORT).show();

                            }
                        });








            }
        });


        prod = getIntent().getStringExtra("product_name");
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
                    pprice.setText(price);
                    pexpiry.setText(expiry);
                    ppacking.setText(item);
                    pmanufacturer.setText(manufacturer);
                    pquantity.setText(quantity);
                    pdirection.setText(direction);
                    datedelivery.setText(date);

                }
            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE_PICKER && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.loading_dialog);
            dialog.setCancelable(false);
            dialog.show();

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

        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Create a new StorageReference with the user's UID as the file name
        StorageReference imageRef = storage.getReference().child("medicine").child(product_name).child("image.jpg");

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
                        Dialog dialog = new Dialog(context);
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

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors that occur during the download
                                dialog.dismiss();

                            }
                        });
                        dialog.dismiss();
                        new SweetAlertDialog(context)

                                .setTitleText("Profile Picture Updated Successfully!")
                                .show();


// Download the contents of the file as a byte array
                        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {

                            @Override
                            public void onSuccess(byte[] bytes) {

                                // Create a bitmap image from the byte array
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                                // Display the bitmap image in an ImageView

                                imageView.setImageBitmap(bitmap);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@androidx.annotation.NonNull Exception exception) {
                                // Handle any errors that occur
                            }
                        });
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                dialog.dismiss();
                                String imageURL = uri.toString();

                                // Do something with the image URL, such as saving it to Firebase Realtime Database or Firestore
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Image upload failed

                        new SweetAlertDialog(context)
                                .setTitleText("Failed to upload Image")
                                .show();
                    }
                });

    }
    public void onBackPressed(){
        startActivity(new Intent(context,AdminProductViewActivity.class));
    }
}