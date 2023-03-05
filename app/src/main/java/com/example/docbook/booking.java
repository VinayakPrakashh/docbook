package com.example.docbook;

import static com.example.docbook.Connection.isNetworkAvailable;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class booking extends AppCompatActivity {
EditText name,reasons,contact,ages;
    private String[] genderOptions = {"Male", "Female", "Other"};
ImageButton date;
Button b1;
TextView genders;
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


        datewrite2 = (TextView) findViewById(R.id.datetex);
        date = (ImageButton) findViewById(R.id.date);
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

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
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

// Add one day to the current date to set it as the minimum date
                calendar.add(Calendar.DAY_OF_MONTH, 1);

// Show a DatePickerDialog to get the date
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        booking.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Update the EditText field with the selected date
                                datewrite2.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                                // Show a TimePickerDialog to get the time
                                TimePickerDialog timePickerDialog = new TimePickerDialog(
                                        booking.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                // Get the current time
                                                Calendar currentTime = Calendar.getInstance();

                                                // Set the selected time to the current time if it is in the past
                                                if (year == currentTime.get(Calendar.YEAR)
                                                        && monthOfYear == currentTime.get(Calendar.MONTH)
                                                        && dayOfMonth == currentTime.get(Calendar.DAY_OF_MONTH)
                                                        && hourOfDay < currentTime.get(Calendar.HOUR_OF_DAY)) {
                                                    hourOfDay = currentTime.get(Calendar.HOUR_OF_DAY);
                                                    minute = currentTime.get(Calendar.MINUTE);
                                                }

                                                // Check if the selected time is within the allowed range (9 AM - 4 PM)
                                                if (hourOfDay < 9 || (hourOfDay == 9 && minute < 0) || hourOfDay > 16) {
                                                    // Show an error message and return
                                                    Toast.makeText(booking.this, "Booking time must be between 9 AM and 4 PM", Toast.LENGTH_LONG).show();
                                                    return;
                                                }

                                                // Format the selected time as 12-hour format
                                                String timeFormat = hourOfDay >= 12 ? "PM" : "AM";
                                                int hour = hourOfDay % 12;
                                                hour = hour == 0 ? 12 : hour;
                                                SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a", Locale.getDefault());
                                                String formattedTime = timeFormatter.format(new Date(0, 0, 0, hour, minute));

                                                // Append the formatted time to the EditText field
                                                datewrite2.append(" " + formattedTime);
                                            }
                                        },
                                        hour,
                                        minute,
                                        false // Force time picker to display in 12-hour format
                                );

                                // Set the minimum time to the current time
                                timePickerDialog.updateTime(hour, minute);

                                timePickerDialog.show();
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



                                // Validate gender

                                // Validate date and time
                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy h:mm a", Locale.getDefault());
                                try {
                                    Date date = format.parse(dateValue);
                                    Date now = new Date();

                                    // Check if date is in the past
                                    if (date.before(now)) {
                                        datewrite2.setError("Please select a future date and time");
                                        datewrite2.requestFocus();
                                        return;
                                    }

                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(date);

                                    // Check if time is between 9am and 4pm
                                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                                    if (hour < 9 || hour > 16) {
                                        datewrite2.setError("Please select a time between 9am and 4pm");
                                        datewrite2.requestFocus();
                                        return;
                                    }

                                } catch (ParseException e) {
                                    datewrite2.setError("Please select a valid date and time");
                                    datewrite2.requestFocus();
                                    return;
                                }

PerforAuth();
                                // If all fields are valid, proceed with booking
                                // ...




                    }
                });
    }



    private void PerforAuth() {

        if (isNetworkAvailable(this)) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseAuth auth = FirebaseAuth.getInstance();
            String uid = auth.getCurrentUser().getUid();
            String nameauth = name.getText().toString();
            int ageauth = Integer.parseInt(ages.getText().toString());
            String contactauth = contact.getText().toString();
            String reasonauth = reasons.getText().toString();
            String dateinfo = datewrite2.getText().toString();
            String genderselect=genders.getText().toString();
            SharedPreferences sharedPreferences = getSharedPreferences("docselect",MODE_PRIVATE);
            String spec= sharedPreferences.getString("specialization","").toString();

            DocumentReference neurologistRef =db.collection("appointments").document(uid)
                    .collection(spec).document("appointment");

            Appointment appointment = new Appointment(nameauth, ageauth, contactauth, reasonauth, dateinfo,genderselect);

            neurologistRef.set(appointment).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {


// 1. Success message
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
        } else {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No internet connection. Please try again later.", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

    }

    public class Appointment {
        private String name;
        private int age;
        private String phone;
        private String reason;
        private String date;
        private String genderselect;

        public Appointment() {}

        public Appointment(String name, int age, String phone, String reason, String date,String gender) {
            this.name = name;
            this.age = age;
            this.phone = phone;
            this.reason = reason;
            this.date = date;
            this.genderselect=gender;
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
        public void setGender(String gender) {
            this.genderselect = gender;
        }

        public String getGender() {
            return genderselect;
        }
    }


}