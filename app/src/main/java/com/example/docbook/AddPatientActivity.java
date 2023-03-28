package com.example.docbook;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddPatientActivity extends AppCompatActivity {
    ScrollView scrollView;
    public String name,specialization;
EditText uname,uage,ugender,upnumber,uward,ureason,uadmission,udischarge,uoccupation,uaddress,uphone,uemail,uspecialization;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    static final int REQUEST_CODE_IMAGE_PICKER = 1;
ImageView uimageView;
    Button upload,addp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
        scrollView=findViewById(R.id.scroll);
        uname=scrollView.findViewById(R.id.name);
        uage=scrollView.findViewById(R.id.age);
     ugender=scrollView.findViewById(R.id.gender);
     uemail=scrollView.findViewById(R.id.mail);
        uaddress=scrollView.findViewById(R.id.address);
        uward=scrollView.findViewById(R.id.ward);
        uphone=scrollView.findViewById(R.id.contact);
        uimageView=findViewById(R.id.photo);
        addp=scrollView.findViewById(R.id.add);
        upload=findViewById(R.id.uploadb);
        upnumber=scrollView.findViewById(R.id.pnumber);
        ureason=scrollView.findViewById(R.id.reason);
        uadmission=scrollView.findViewById(R.id.admission);
      udischarge=scrollView.findViewById(R.id.discharge);
uoccupation=scrollView.findViewById(R.id.occupation);
uspecialization=scrollView.findViewById(R.id.specialization);

name=uname.getText().toString();
        SharedPreferences sharedPreferences2 = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        specialization = sharedPreferences2.getString("specialization", "").toString();
        uspecialization.setText(specialization);
        uspecialization.setEnabled(false);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namecheck=uname.getText().toString();
                if (namecheck.equals("")){
                    Toast.makeText(AddPatientActivity.this, "Please Enter Name before Uploading image", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQUEST_CODE_IMAGE_PICKER);
                }

            }
        });
       addp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        // Get the user input from the EditText views
                        String nname = uname.getText().toString().trim();
                        String age = uage.getText().toString().trim();
                        String gender = ugender.getText().toString().trim();
                        String patientNo = upnumber.getText().toString().trim();
                        String ward = uward.getText().toString().trim();
                        String specialization = uspecialization.getText().toString().trim();
                        String reason = ureason.getText().toString().trim();
                        String admission = uadmission.getText().toString().trim();
                        String discharge = udischarge.getText().toString().trim();
                        String occupation = uoccupation.getText().toString().trim();
                        String contactNumber = uphone.getText().toString().trim();
                        String address = uaddress.getText().toString().trim();




                        // Validate the user input
                        if (nname.isEmpty()) {
                            uname.setError("Name is required.");
                            uname.requestFocus();
                            return;
                        }

                        if (age.isEmpty()) {
                            uage.setError("Age is required.");
                            uage.requestFocus();
                            return;
                        }

                        if (gender.isEmpty()) {
                            ugender.setError("Gender is required.");
                            ugender.requestFocus();
                            return;
                        }

                        if (patientNo.isEmpty()) {
                            upnumber.setError("Patient number is required.");
                            upnumber.requestFocus();
                            return;
                        }

                        if (ward.isEmpty()) {
                            uward.setError("Ward number is required.");
                            uward.requestFocus();
                            return;
                        }

                        if (specialization.isEmpty()) {
                            uspecialization.setError("Specialization is required.");
                            uspecialization.requestFocus();
                            return;
                        }

                        if (reason.isEmpty()) {
                            ureason.setError("Reason is required.");
                            ureason.requestFocus();
                            return;
                        }

                        if (admission.isEmpty()) {
                            uadmission.setError("Admission date is required.");
                            uadmission.requestFocus();
                            return;
                        }

                        if (discharge.isEmpty()) {
                            udischarge.setError("Discharge date is required.");
                            udischarge.requestFocus();
                            return;
                        }

                        if (occupation.isEmpty()) {
                            uoccupation.setError("Occupation is required.");
                            uoccupation.requestFocus();
                            return;
                        }
                if (contactNumber.isEmpty() || contactNumber.length() < 10) {
                    uphone.setError("Please enter a valid contact number");
                    return;
                }  if (address.isEmpty()) {
                    uaddress.setError("Please enter an address");
                    return;
                }
           name=uname.getText().toString();

               addpatient();

            }
        });

    }

    private void addpatient() {

name=uname.getText().toString();
        String age=uage.getText().toString();
        String email=uemail.getText().toString();
        String address=uaddress.getText().toString();
        String pnumber=upnumber.getText().toString();
        String ward=uward.getText().toString();
        String admission=uadmission.getText().toString();
        String discharge=udischarge.getText().toString();
        String gender=ugender.getText().toString();
        String occupation=uoccupation.getText().toString();
        String contact=uphone.getText().toString();
        String reason=ureason.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("age", age);
        user.put("email", email);
        user.put("address", address);
        user.put("contact", contact);
        user.put("occupation",occupation);
        user.put("reason",reason);
        user.put("gender", gender);
        user.put("discharge", discharge);
        user.put("admission", admission);
        user.put("ward", ward);
        user.put("pnumber", pnumber);
        user.put("specialization",specialization);

        db.collection("patients").document(name)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        new SweetAlertDialog(AddPatientActivity.this)
                                .setTitleText("Patient Added Successfully")
                                .show();
                        uname.setText("");
                        uage.setText("");
                        ugender.setText("");
                        upnumber.setText("");
                        uward.setText("");
                        ureason.setText("");
                        uadmission.setText("");
                        udischarge.setText("");
                        uoccupation.setText("");
             uphone.setText("");
             uaddress.setText("");
             uemail.setText("");
                        uimageView.setImageResource(R.drawable.baseline_image_24);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing user information to Firestore", e);
                    }
                });

    }
    private void uploadImageToFirebaseStorage(Uri imageUri) {
name=uname.getText().toString();


        // Create a new StorageReference with the user's UID as the file name
        StorageReference imageRef = storageRef.child("patients").child(specialization).child(name);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        FirebaseStorage storage = FirebaseStorage.getInstance();

                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();

// Create a reference to the image file you want to download
                        StorageReference imageRef = storage.getReference().child("patients").child(specialization).child(name);

// Download the image file into a byte array
                        Dialog dialog = new Dialog(AddPatientActivity.this);
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
                                uimageView.setImageBitmap(bmp);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors that occur during the download
                                dialog.dismiss();
                                Toast.makeText(AddPatientActivity.this, "Failed to Download Image", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.dismiss();
                        new SweetAlertDialog(AddPatientActivity.this)

                                .setTitleText("Profile Picture Updated Successfully \n Now Click On Add Patient")
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

                        new SweetAlertDialog(AddPatientActivity.this)
                                .setTitleText("Failed to upload Image")
                                .show();
                    }
                });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE_PICKER && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Dialog dialog = new Dialog(AddPatientActivity.this);
            dialog.setContentView(R.layout.loading_dialog);
            dialog.setCancelable(false);
            dialog.show();
            Uri imageUri = data.getData();
            uploadImageToFirebaseStorage(imageUri);
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("patients").child(specialization).child(name);

// Download the contents of the file as a byte array
            imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {

                @Override
                public void onSuccess(byte[] bytes) {

                    // Create a bitmap image from the byte array
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    // Display the bitmap image in an ImageView
dialog.dismiss();
                    uimageView.setImageBitmap(bitmap);
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

}