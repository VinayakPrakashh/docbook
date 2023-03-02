package com.example.docbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class bookingpageActivity extends AppCompatActivity {
EditText name,reasons,contact,ages;

EditText dateButton;
Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookingpage);
name=findViewById(R.id.fname);
reasons=findViewById(R.id.reason);
contact=findViewById(R.id.contactnumber);
ages=findViewById(R.id.age);
b1=findViewById(R.id.book);

b1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
PerforAuth();

    }
});
    }



    private void PerforAuth() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth=FirebaseAuth.getInstance();;
        String uid = auth.getCurrentUser().getUid();
        String nameauth=name.getText().toString();
        int ageauth=Integer.parseInt(ages.getText().toString());
        String contactauth=contact.getText().toString();
        String reasonauth=reasons.getText().toString();
        Appointment appointment = new Appointment(nameauth, ageauth, contactauth,reasonauth);
        db.collection("appointments").document(uid).set(appointment)  .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Appointment added successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to add appointment", Toast.LENGTH_SHORT).show();
                    }});
    }
    public class Booking {
        private String doctorName;
        private String appointmentTime;
        private String location;

        public Booking() {
            // Default constructor required for calls to DataSnapshot.getValue(Booking.class)
        }

        public Booking(String doctorName, String appointmentTime, String location) {
            this.doctorName = doctorName;
            this.appointmentTime = appointmentTime;
            this.location = location;
        }

        public String getDoctorName() {
            return doctorName;
        }

        public String getAppointmentTime() {
            return appointmentTime;
        }

        public String getLocation() {
            return location;
        }
    }
    public class Appointment {
        private String name;
        private int age;
        private String phone;
        private String reason;

        public Appointment() {}

        public Appointment(String name, int age, String phone, String reason) {
            this.name = name;
            this.age = age;
            this.phone = phone;
            this.reason = reason;
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
    }


}