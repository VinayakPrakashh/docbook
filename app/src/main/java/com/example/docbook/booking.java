package com.example.docbook;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class booking extends AppCompatActivity {
EditText name,reasons,contact,ages;
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
        setContentView(R.layout.activity_neurologist);
name=findViewById(R.id.fname);
reasons=findViewById(R.id.reason);
contact=findViewById(R.id.contactnumber);
ages=findViewById(R.id.age);
b1=findViewById(R.id.book);
genders=findViewById(R.id.gender);
timeslot=findViewById(R.id.time);
timetext=findViewById(R.id.timetex);
        datewrite2 = (TextView) findViewById(R.id.datetex);
        date = (ImageButton) findViewById(R.id.date);
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
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
                        // Document does not exist, create it
                        docRef2.set(new HashMap<>()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                DocumentReference appointmentRef = db.collection("appointments").document(uid)
                                        .collection(spec).document("appointment");

                                appointmentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                // Appointment already exists for the user and doctor, handle accordingly
                                                new SweetAlertDialog(booking.this)
                                                        .setTitleText("You already have an appointment with this doctor.")
                                                        .show();
                                            } else {
                                                bookit();
                                            }
                                        }
                                    }
                                });
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
        String spec = sharedPreferences.getString("specialization", "").toString();

        DocumentReference neurologistRef = db.collection("appointments").document(uid)
                .collection(spec).document("appointment");

        Appointment appointment = new Appointment(nameauth, ageauth, contactauth, reasonauth, dateinfo, time, genderselect);

        neurologistRef.set(appointment).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Success message
                new SweetAlertDialog(booking.this)
                        .setTitleText("Appointment Booked!")
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

        public Appointment() {}

        public Appointment(String name, int age, String phone, String reason, String date, String time, String gender) {
            this.name = name;
            this.age = age;
            this.phone = phone;
            this.reason = reason;
            this.date = date;
            this.time = time;
            this.genderselect = gender;
        }

        public String getName() {
            return name;
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


