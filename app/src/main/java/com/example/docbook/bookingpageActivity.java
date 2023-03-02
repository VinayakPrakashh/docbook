package com.example.docbook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class bookingpageActivity extends AppCompatActivity {
TextView name,address,num;
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
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users/" + userId);

// Create a new node under the user's node for the booking, with fields for the booking details
        String bookingId = userRef.child("bookings").push().getKey();
        Map<String, Object> bookingDetails = new HashMap<>();
        bookingDetails.put("full name", name);
        bookingDetails.put("address", address);
        bookingDetails.put("number", num);
        userRef.child("bookings").child(bookingId).setValue(bookingDetails);
    }
});
    }

}