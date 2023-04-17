package com.example.docbook;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class booking extends AppCompatActivity {
EditText name,reasons,contact,ages;
TextView docname2,docspec2;
public String val;
    private String[] genderOptions = {"Male", "Female", "Other"};
    private String[] timeslots=
            {"9 AM to 10 AM",
            "10 AM to 11 AM",
            "11 AM to 12 PM",
            "2 PM to 3 PM",
            "3 PM to 4 PM"};
ImageButton date,timeslot;
Button b1;
TextView genders,timetext;
    int day;
    int month;
    int year;
    Calendar cal;
   TextView datewrite2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_book);
name=findViewById(R.id.fname);
reasons=findViewById(R.id.reason);
contact=findViewById(R.id.contactnumber);
ages=findViewById(R.id.spec);
b1=findViewById(R.id.book);
genders=findViewById(R.id.gender);
timeslot=findViewById(R.id.time);
timetext=findViewById(R.id.timetex);
        datewrite2 =  findViewById(R.id.datetex);
        date =findViewById(R.id.date);
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        docname2 = findViewById(R.id.name_top);
        docspec2 = findViewById(R.id.special_top);
        ImageView imageView = findViewById(R.id.profile_photo);

        b1 = findViewById(R.id.book);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences("docselect", MODE_PRIVATE);
        String spec = sharedPreferences.getString("specialization", "").toString();
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
                                docname2.setText(name);
                                docspec2.setText(specialization);
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
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

                imageView.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors that occur during the download
                Log.e("TAG", "Failed to download image", exception);
            }
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db2 = FirebaseFirestore.getInstance();
        String uid = auth.getCurrentUser().getUid();
        db2.collection("users")
                .document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {


                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                               String nname = document.getString("username");
                                name.setText(nname);

                                name.setEnabled(false);

                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });


       timeslot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(booking.this);
                builder.setTitle("Select Time Slot");
                builder.setSingleChoiceItems(timeslots, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String selectedtime = timeslots[which];
                        timetext.setText(selectedtime);
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
        genders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(booking.this);
                builder.setTitle("Select Gender");
                builder.setSingleChoiceItems(genderOptions, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle gender selection here
                        String selectedGender = genderOptions[which];
                        genders.setText(selectedGender);
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current date and time
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Add one day to the current date to set it as the minimum date
                calendar.add(Calendar.DAY_OF_MONTH, 1);

                // Show a DatePickerDialog to get the date
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        booking.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Update the EditText field with the selected date
                                datewrite2.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        },
                        year,
                        month,
                        day
                );

                // Set the minimum date to the next day
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

                // Show the DatePickerDialog
                datePickerDialog.show();
            }
        });



        b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                                String nameValue = name.getText().toString().trim();
                                String reasonValue = reasons.getText().toString().trim();
                                String contactValue = contact.getText().toString().trim();
                                String ageValue = ages.getText().toString().trim();
                                String genderValue = genders.getText().toString().trim();
                                String dateValue = datewrite2.getText().toString().trim();

                                // Validate name
                                if (nameValue.isEmpty()) {
                                    name.setError("Please enter your name");
                                    name.requestFocus();
                                    return;
                                }


                        // Validate age
                        if(ageValue.isEmpty()) {
                            ages.setError("Please enter your age");
                            ages.requestFocus();
                            return;
                        }

                        if (genderValue.equals("Select Gender")) {
                            genders.requestFocus();
                            Toast.makeText(getApplicationContext(), "Please select your gender", Toast.LENGTH_SHORT).show();
                            return;
                        }

                      if(contactValue.length() != 10) {
                            contact.setError("Please enter a valid 10-digit contact number");
                            contact.requestFocus();
                            return;
                        }

                                // Validate contact number
                                else if(contactValue.isEmpty()) {
                                    contact.setError("Please enter your contact number");
                                    contact.requestFocus();
                                    return;
                                }
                        // Validate reason
                        if(reasonValue.isEmpty()) {
                            reasons.setError("Please enter your reason for booking");
                            reasons.requestFocus();
                            return;
                        }



                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        try {
                            Date date = format.parse(dateValue);
                            Date now = new Date();

                            // Check if date is in the past
                            if (date.before(now)) {
                                datewrite2.setError("Please select a future date");
                                datewrite2.requestFocus();
                                return;
                            }

                            // Proceed with further checks or actions using the parsed date.

                        } catch (ParseException e) {
                            datewrite2.setError("Please select a valid date");
                            datewrite2.requestFocus();
                            return;
                        }


                        PerforAuth();
                    }
                });
    }



    private void PerforAuth() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String dateinfo = datewrite2.getText().toString();
        String time = timetext.getText().toString();
        String uid = auth.getCurrentUser().getUid();
        SharedPreferences sharedPreferences = getSharedPreferences("docselect", MODE_PRIVATE);
        String spec = sharedPreferences.getString("specialization", "").toString();

        DocumentReference docRef2 = db.collection("bookingtime")
                .document(spec)
                .collection(dateinfo)
                .document("timeSlots")
                .collection("time")
                .document(time);

        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Document already exists, handle accordingly
                        new SweetAlertDialog(booking.this)
                                .setTitleText("This time slot is already booked.")
                                .show();
                    } else {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        String uid = auth.getCurrentUser().getUid();
                        SharedPreferences sharedPreferences = getSharedPreferences("docselect", MODE_PRIVATE);
                        String spec = sharedPreferences.getString("specialization", "");
                        String doctor= sharedPreferences.getString("doctor", "").toString();
// Query appointments sub-collection to check if there is already an appointment booked for this user with the given specialization
                        db.collection("appointments").document(uid).collection("item")
                                .whereEqualTo("specialization", spec)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (!task.getResult().isEmpty()) {
                                                // User has already booked an appointment for this specialization, show error message
                                                new SweetAlertDialog(booking.this)
                                                        .setTitleText("You Have Already Booked an appointment for Dr."+doctor)
                                                        .show();
                                            } else {
                                              bookit();
                                            }
                                        } else {
                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });


                    }
                } else {
                    Toast.makeText(booking.this, "Error Getting Documents", Toast.LENGTH_SHORT).show();
                    // Error getting document, handle accordingly
                }
            }
        });
    }

    private void bookit() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        String nameauth = name.getText().toString();
        int ageauth = Integer.parseInt(ages.getText().toString());
        String contactauth = contact.getText().toString();
        String reasonauth = reasons.getText().toString();
        String dateinfo = datewrite2.getText().toString();
        String time = timetext.getText().toString();
        String genderselect = genders.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences("docselect", MODE_PRIVATE);
        String spec = sharedPreferences.getString("specialization", "");
        String doctor= sharedPreferences.getString("doctor", "").toString();
        DocumentReference appointmentRef = db.collection("appointments").document(uid)
                .collection("item").document(spec);

        Appointment appointment = new Appointment(nameauth, ageauth, contactauth, reasonauth, dateinfo, time, genderselect,spec,doctor);

        appointmentRef.set(appointment).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Create the SweetAlertDialog
                new SweetAlertDialog(booking.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Appointment Booked!")
                        .setContentText("See booked appointments?")
                        .setCancelText("No")
                        .setConfirmText("Yes")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Intent intent = new Intent(booking.this, AppointmentActivity.class);
                                startActivity(intent);
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                // Do something when "Yes" button is clicked
                                Intent intent = new Intent(booking.this, BookedActivity.class);
                                startActivity(intent);
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to add appointment", Toast.LENGTH_SHORT).show();
            }
        });
    }




    public class Appointment {
        private String name;
        private int age;
        private String phone;
        private String reason;
        private String date;
        private String time;
        private String genderselect;
        private String specialization;
        private String doctor;
        public Appointment() {}

        public Appointment(String name, int age, String phone, String reason, String date, String time, String gender,String specialization,String doctor) {
            this.name = name;
            this.age = age;
            this.phone = phone;
            this.reason = reason;
            this.date = date;
            this.time = time;
            this.genderselect = gender;
            this.specialization = specialization;
            this.doctor = doctor;
        }

        public String getName() {
            return name;
        }
public String getSpecialization(){
            return specialization;

        }
        public void setSpecialization(String specialization) {
            this.specialization = specialization;
        }public String getDoctor(){
            return doctor;

        }
        public void setDoctor(String doctor) {
            this.doctor = doctor;
        }
        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDate() {
            return date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getGender() {
            return genderselect;
        }

        public void setGender(String gender) {
            this.genderselect = gender;
        }
    }

}


