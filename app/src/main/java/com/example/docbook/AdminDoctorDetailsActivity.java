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
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AdminDoctorDetailsActivity extends AppCompatActivity {
    ImageView imageView;
    TextView docname2, docspec2, dochospital2, dexp2, doccontact2, dquali, fees2;
    EditText pname,pspec,pexp,pfees,pqualification,phospital,pcontact,email,password;
    Button b1;
    private static final int REQUEST_CODE_IMAGE_PICKER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_doctor_details);
        docname2 = findViewById(R.id.dname);
        docspec2 = findViewById(R.id.dspec);
        dochospital2 = findViewById(R.id.dhospital);
        dexp2 = findViewById(R.id.dexp);
        doccontact2 = findViewById(R.id.dcontact);
        dquali = findViewById(R.id.dqualification);
        fees2 = findViewById(R.id.contact);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        b1 = findViewById(R.id.savebutton);
        SharedPreferences sharedPreferences = getSharedPreferences("docselect", MODE_PRIVATE);
        String spec = sharedPreferences.getString("specialization", "").toString();

        // Get a reference to the Firebase Storage instance
        FirebaseStorage storage = FirebaseStorage.getInstance();
        String photo = spec + ".jpg";
// Create a reference to the image file you want to download
        StorageReference imageRef = storage.getReference().child(photo);

// Download the image file into a byte array
        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Convert the byte array into a Bitmap object
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                // Set the Bitmap object in an ImageView
                ImageView imageView = findViewById(R.id.dphoto);
                imageView.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors that occur during the download
                Log.e("TAG", "Failed to download image", exception);
            }
        });


        db.collection("doctor")
                .document(spec)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {


                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                // Extract doctor details here
                                String name = document.getString("name");

                                String specialization = document.getString("specialization");
                             String exp = document.getString("experience");
                                String hospital = document.getString("hospital");
                                String dcon = document.getString("contact");
                                String qualification = document.getString("qualification");

                                String fees = document.getString("fees");
                                // Update UI with doctor details




                                docname2.setText("Dr."+name);
                                docspec2.setText(specialization);
                                dochospital2.setText(hospital);
                                dexp2.setText(exp + " Years");
                                doccontact2.setText(dcon);
                                dquali.setText(qualification);
                                fees2.setText(fees);


                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                myEdit.putString("doctor", name);
                                myEdit.commit();

                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              showBottomDialog();
            }
        });

    }
    public void  showBottomDialog(){


        final Dialog dialog=new Dialog(AdminDoctorDetailsActivity.this);
        dialog.setContentView(R.layout.bottomsheet_doctor);
        pname=dialog.findViewById(R.id.name);
        imageView=dialog.findViewById(R.id.product_image);
      pspec=dialog.findViewById(R.id.specialization);
         pexp=dialog.findViewById(R.id.experience);
         pqualification=dialog.findViewById(R.id.qualification);
         phospital=dialog.findViewById(R.id.hospital);
         pfees=dialog.findViewById(R.id.fees);
         pcontact=dialog.findViewById(R.id.contact);
        Button upload=dialog.findViewById(R.id.edit_photo_button);
        Button save=dialog.findViewById(R.id.update);
 email=dialog.findViewById(R.id.mail);
         password=dialog.findViewById(R.id.pass);
        SharedPreferences sharedPreferences = getSharedPreferences("docselect", MODE_PRIVATE);
        String spec = sharedPreferences.getString("specialization", "").toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Get a reference to the Firebase Storage instance
        FirebaseStorage storage = FirebaseStorage.getInstance();
        String photo = spec + ".jpg";
// Create a reference to the image file you want to download
        StorageReference imageRef = storage.getReference().child(photo);

// Download the image file into a byte array
        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Convert the byte array into a Bitmap object
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);


                imageView.setImageBitmap(bmp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors that occur during the download
                Log.e("TAG", "Failed to download image", exception);
            }
        });


        db.collection("doctor")
                .document(spec)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {


                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                // Extract doctor details here
                                String name = document.getString("name");

                                String specialization = document.getString("specialization");
                                String exp = document.getString("experience");
                                String hospital = document.getString("hospital");
                                String dcon = document.getString("contact");
                                String qualification = document.getString("qualification");
                                String dmail = document.getString("email");
                                String fees = document.getString("fees");
                                String dpass = document.getString("password");
                                // Update UI with doctor details



                                email.setText(dmail);
                                password.setText(dpass);

                                pname.setText(name);
                                pspec.setText(specialization);
                                phospital.setText(hospital);
                                pexp.setText(exp);
                                pcontact.setText(dcon);
                                pqualification.setText(qualification);
                                pfees.setText(fees);
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
if(validateInput()){

    String name=pname.getText().toString();
    String specialization=pspec.getText().toString();
    String contact=pcontact.getText().toString();
    String hospital=phospital.getText().toString();
    String experience=pexp.getText().toString();
    String qualification=pqualification.getText().toString();
    String fees=pfees.getText().toString();
    String mailid=email.getText().toString();
    String passw=password.getText().toString();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    Map<String, Object> user = new HashMap<>();
    user.put("name", name);
    user.put("specialization", specialization);
    user.put("hospital", hospital);
    user.put("experience", experience);
    user.put("contact", contact);
    user.put("qualification",qualification);
    user.put("fees",fees);
    user.put("email",mailid);
    user.put("password",passw);

    db.collection("doctor").document(specialization)
            .update(user)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    new SweetAlertDialog(AdminDoctorDetailsActivity.this)
                            .setTitleText("Doctor Profile Updated")
                            .show();

                    dialog.dismiss();
                    performauth();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error writing user information to Firestore", e);
                }
            });
}else {

}

            }
        });
upload.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_IMAGE_PICKER);
    }
});
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
    private void uploadImageToFirebaseStorage(Uri imageUri) {
        Dialog dialog2 = new Dialog(AdminDoctorDetailsActivity.this);
        dialog2.setContentView(R.layout.loading_dialog);

        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog2.setCancelable(false);
        dialog2.show();
        SharedPreferences sharedPreferences = getSharedPreferences("docselect", MODE_PRIVATE);
        String spec = sharedPreferences.getString("specialization", "").toString();

        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Create a new StorageReference with the user's UID as the file name
        StorageReference imageRef = storage.getReference().child(spec+".jpg");

        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        FirebaseStorage storage = FirebaseStorage.getInstance();



// Create a reference to the image file you want to download
                        StorageReference imageRef = storage.getReference().child(spec+".jpg");

// Download the image file into a byte array
                        Dialog dialog = new Dialog(AdminDoctorDetailsActivity.this);
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
                        String photo = spec + ".jpg";
// Create a reference to the image file you want to download


// Download the image file into a byte array
                        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                // Convert the byte array into a Bitmap object
                                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);


                                imageView.setImageBitmap(bmp);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors that occur during the download
                                Log.e("TAG", "Failed to download image", exception);
                            }
                        });
                        new SweetAlertDialog(AdminDoctorDetailsActivity.this)

                                .setTitleText("Profile Picture Updated Successfully!")
                                .show();
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

                        new SweetAlertDialog(AdminDoctorDetailsActivity.this)
                                .setTitleText("Failed to upload Image")
                                .show();
                    }
                });
dialog2.dismiss();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences sharedPreferences = getSharedPreferences("docselect", MODE_PRIVATE);
        String spec = sharedPreferences.getString("specialization", "").toString();

        if (requestCode == REQUEST_CODE_IMAGE_PICKER && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Dialog dialog = new Dialog(AdminDoctorDetailsActivity.this);
            dialog.setContentView(R.layout.loading_dialog);

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.setCancelable(false);
            dialog.show();
            Uri imageUri = data.getData();
            uploadImageToFirebaseStorage(imageUri);
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            StorageReference imageRef = FirebaseStorage.getInstance().getReference().child(spec+".jpg");

// Download the contents of the file as a byte array
            imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {

                @Override
                public void onSuccess(byte[] bytes) {

                    // Create a bitmap image from the byte array
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    // Display the bitmap image in an ImageView
                    dialog.dismiss();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors that occur
                }
            });
            dialog.dismiss();
        }
    }
    private void performauth(){

        SharedPreferences sharedPreferences = getSharedPreferences("docselect", MODE_PRIVATE);
        String spec = sharedPreferences.getString("specialization", "").toString();

        FirebaseStorage storage = FirebaseStorage.getInstance();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        StorageReference imageRef = storage.getReference().child(spec+"");

// Download the image file into a byte array
        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Convert the byte array into a Bitmap object
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                // Set the Bitmap object in an ImageView
                ImageView imageView = findViewById(R.id.dphoto);
                imageView.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors that occur during the download

            }
        });
        db.collection("doctor").document(spec)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {


                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                // Extract doctor details here
                                String name = document.getString("name");

                                String specialization = document.getString("specialization");
                                String exp = document.getString("experience");
                                String hospital = document.getString("hospital");
                                String dcon = document.getString("contact");
                                String qualification = document.getString("qualification");

                                String fees = document.getString("fees");
                                docname2.setText(name);
                                docspec2.setText(specialization);
                                dochospital2.setText(hospital);
                                dexp2.setText(exp + " Years");
                                doccontact2.setText( dcon);
                                dquali.setText(qualification);
                                fees2.setText(fees);



                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

    }
    private boolean validateInput() {
        String name = pname.getText().toString();
        String specialization = pspec.getText().toString();
        String contact = pcontact.getText().toString();
        String hospital = phospital.getText().toString();
        String experience = pexp.getText().toString();
        String qualification = pqualification.getText().toString();
        String fees = pfees.getText().toString();
        String mailid = email.getText().toString();
        String passw = password.getText().toString();

        if (name.isEmpty()) {
            pname.setError("Please enter your name");
            return false;
        } else if (!name.matches("[a-zA-Z ]+")) {
            pname.setError("Name should contain only letters");
            return false;
        } else if (name.length() < 3) {
            pname.setError("Name should be at least 3 characters long");
            return false;
        }

        if (specialization.isEmpty()) {
            pspec.setError("Please enter your specialization");
            return false;
        }

        if (contact.isEmpty()) {
            pcontact.setError("Please enter your contact number");
            return false;
        } else if (!contact.matches("\\d{10}")) {
            pcontact.setError("Contact number should be 10 digits long");
            return false;
        }

        if (hospital.isEmpty()) {
            phospital.setError("Please enter the hospital name");
            return false;
        }

        if (experience.isEmpty()) {
            pexp.setError("Please enter your experience");
            return false;
        } else if (!experience.matches("\\d+")) {
            pexp.setError("Experience should be a number");
            return false;
        }

        if (qualification.isEmpty()) {
            pqualification.setError("Please enter your qualification");
            return false;
        }

        if (fees.isEmpty()) {
            pfees.setError("Please enter your fees");
            return false;
        } else if (!fees.matches("\\d+")) {
            pfees.setError("Fees should be a number");
            return false;
        }

        if (mailid.isEmpty()) {
            email.setError("Please enter your email ID");
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mailid).matches()) {
            email.setError("Please enter a valid email ID");
            return false;
        }

        if (passw.isEmpty()) {
            password.setError("Please enter your password");
            return false;
        } else if (passw.length() < 8) {
            password.setError("Password should be at least 8 characters long");
            return false;
        }

        return true;
    }

}