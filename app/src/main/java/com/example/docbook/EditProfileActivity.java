package com.example.docbook;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EditProfileActivity extends AppCompatActivity {
EditText uname,uage,uaddress,uphone,ucity,uemail,upassword;
Button save;
public String nname;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    private static final int REQUEST_CODE_IMAGE_PICKER = 1;
CardView uimageView;
public String uid;
    Button upload;
    FirebaseUser uuser;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_test);
        uname=findViewById(R.id.name);
        uage=findViewById(R.id.spec);
        uemail=findViewById(R.id.special_top);
        uaddress=findViewById(R.id.quali);
        ucity=findViewById(R.id.exp);
        uphone=findViewById(R.id.phone);
        uimageView=findViewById(R.id.profile_photo2);
        save=findViewById(R.id.save);
      img=findViewById(R.id.profile_photo);
        upassword=findViewById(R.id.password);

       uuser = FirebaseAuth.getInstance().getCurrentUser();
         uid = uuser.getUid();
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(EditProfileActivity.this);
                dialog.setContentView(R.layout.loading_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                dialog.show();
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_IMAGE_PICKER);
                dialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(EditProfileActivity.this);
                dialog.setContentView(R.layout.loading_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                dialog.show();
                bookit();
dialog.dismiss();
            }
        });
        Dialog dialog = new Dialog(EditProfileActivity.this);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
      performauth();

        dialog.dismiss();
    }

    private void bookit() {


        String username=uname.getText().toString();
        String age=uage.getText().toString();
        String email=uemail.getText().toString();
        String address=uaddress.getText().toString();
        String contact=uphone.getText().toString();
        String city=ucity.getText().toString();
        String password=upassword.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("age", age);
        user.put("email", email);
        user.put("address", address);
        user.put("contact", contact);
        user.put("city", city);
        user.put("password", password);
        db.collection("users").document(userUid)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {



                        uuser.updateEmail(email)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            uuser.updatePassword(password)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                sign();
                                                            } else {
                                                                // Password update failed
                                                            }
                                                        }
                                                    });

                                        } else {
                                            // Email update failed
                                            // Get the error message
                                            String errorMessage = task.getException().getMessage();
                                            Log.e("EditProfileActivity", "Email update failed: " + errorMessage);
                                            Toast.makeText(EditProfileActivity.this, "Email update failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                        new SweetAlertDialog(EditProfileActivity.this)
                                .setTitleText("Profile Updated!")
                                .show();
                        performauth();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing user information to Firestore", e);
                    }
                });

    }
    private void performauth(){
        FirebaseStorage storage = FirebaseStorage.getInstance();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = auth.getCurrentUser().getUid();

        db.collection("users")
                .document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {


                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                nname = document.getString("username");
                                String age = document.getString("age");
                                String address = document.getString("address");
                                String city=document.getString("city");
                                String mail = document.getString("email");
                                String phone=document.getString("contact");
                                String password=document.getString("password");
                                uname.setText(nname);
                                uage.setText(age);
                                uemail.setText(mail);
                                ucity.setText(city);
                                uphone.setText(phone);
                                uaddress.setText(address);
                                upassword.setText(password);
                                // Create a reference to the image file you want to download
                                StorageReference imageRef = storage.getReference().child("users/" + nname + "/image");

// Download the image file into a byte array
                                imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        // Convert the byte array into a Bitmap object
                                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                                        // Set the Bitmap object in an ImageView

                                        img.setImageBitmap(bmp);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors that occur during the download
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
    private void uploadImageToFirebaseStorage(Uri imageUri) {

        // Get the user's UID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        // Create a new StorageReference with the user's UID as the file name
        StorageReference imageRef = storageRef.child("users/" + nname + "/image");

        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        FirebaseStorage storage = FirebaseStorage.getInstance();

                        FirebaseAuth auth = FirebaseAuth.getInstance();

// Create a reference to the image file you want to download
                        StorageReference imageRef = storage.getReference().child("users/" + nname + "/image");

// Download the image file into a byte array
                        Dialog dialog = new Dialog(EditProfileActivity.this);
                        dialog.setContentView(R.layout.loading_dialog);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.setCancelable(false);
                        dialog.show();
                        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                // Convert the byte array into a Bitmap object
                                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                                // Set the Bitmap object in an ImageView
                                dialog.dismiss();
                                img.setImageBitmap(bmp);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors that occur during the download
                                dialog.dismiss();

                            }
                        });
                        dialog.dismiss();
                        new SweetAlertDialog(EditProfileActivity.this)

                                .setTitleText("Profile Picture Updated Successfully")
                                .show();
                        dialog.dismiss();
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                dialog.dismiss();
                                String imageURL = uri.toString();
                                // Do something with the image URL, such as saving it to Firebase Realtime Database or Firestore
                            }
                        });
                        dialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Image upload failed
                        new SweetAlertDialog(EditProfileActivity.this)
                                .setTitleText("Failed to upload Image")
                                .show();
                    }
                });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE_PICKER && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Dialog dialog = new Dialog(EditProfileActivity.this);
            dialog.setContentView(R.layout.loading_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.show();
            Uri imageUri = data.getData();
            uploadImageToFirebaseStorage(imageUri);
            FirebaseAuth auth = FirebaseAuth.getInstance();

            StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("users/" + nname + "/image");

// Download the contents of the file as a byte array
            imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {

                @Override
                public void onSuccess(byte[] bytes) {

                    // Create a bitmap image from the byte array
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    // Display the bitmap image in an ImageView
dialog.dismiss();
                    img.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors that occur
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EditProfileActivity.this,HomeActivity.class));
    }
     public void sign(){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(uemail.getText().toString(), upassword.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();


                            myEdit.putString("mail", uemail.getText().toString());
                            myEdit.putString("password",upassword.getText().toString());
                            myEdit.commit();
                            // Do something with the user's updated information, such as updating a user profile in Firestore
                        } else {

                            // Handle the error, such as displaying an error message to the user
                        }
                    }
                });
    }
}