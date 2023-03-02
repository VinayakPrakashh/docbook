package com.example.docbook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class bookingpageActivity extends AppCompatActivity {
EditText name,address,num;
Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookingpage);
name=findViewById(R.id.fname);
address=findViewById(R.id.adress);
num=findViewById(R.id.contactnumber);
b1=findViewById(R.id.book);

b1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
PerforAuth();

    }
});
    }
    private void PerforAuth() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users/" + userId);
        String bookingId = userRef.child("bookings").push().getKey();
        String doctorName=name.getText().toString();
        String appointmentTime=address.getText().toString();
        String location=num.getText().toString();
        Booking booking = new Booking(doctorName, appointmentTime, location);
        userRef.child("bookings").child(bookingId).setValue(booking);
        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
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

}