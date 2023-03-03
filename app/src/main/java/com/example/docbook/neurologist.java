package com.example.docbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;


import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class neurologist extends AppCompatActivity {
EditText name,reasons,contact,ages;

ImageButton date;
Button b1;
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
        date = (ImageButton) findViewById(R.id.date);
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        datewrite2 = (TextView) findViewById(R.id.datetex);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a DatePickerDialog to get the date
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        neurologist.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Update the EditText field with the selected date
                                datewrite2.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                                // Show a TimePickerDialog to get the time
                                TimePickerDialog timePickerDialog = new TimePickerDialog(
                                        neurologist.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                // Append the selected time to the EditText field
                                                datewrite2.append(" " + hourOfDay + ":" + minute);
                                            }
                                        },
                                        hour,
                                        minute,
                                        DateFormat.is24HourFormat(neurologist.this)
                                );

                                // Show the TimePickerDialog
                                timePickerDialog.show();
                            }
                        },
                        year,
                        month,
                        day
                );

                // Show the DatePickerDialog
                datePickerDialog.show();
            }
        });

                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PerforAuth();

                    }
                });
    }



    private void PerforAuth() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        String nameauth = name.getText().toString();
        int ageauth = Integer.parseInt(ages.getText().toString());
        String contactauth = contact.getText().toString();
        String reasonauth = reasons.getText().toString();
        String dateinfo = datewrite2.getText().toString();
        Toast.makeText(getApplicationContext(), dateinfo, Toast.LENGTH_SHORT).show();

        DocumentReference neurologistRef =     db.collection("appointments").document(uid)
                .collection("neurologist").document("appointment");

        Appointment appointment = new Appointment(nameauth, ageauth, contactauth, reasonauth, dateinfo);

        neurologistRef.set(appointment).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Appointment added successfully", Toast.LENGTH_SHORT).show();
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

        public Appointment() {}

        public Appointment(String name, int age, String phone, String reason, String date) {
            this.name = name;
            this.age = age;
            this.phone = phone;
            this.reason = reason;
            this.date = date;
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
    }


}