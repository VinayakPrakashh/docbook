package com.example.docbook;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    EditText mail,pass;
    Button b1;

    public String email,password,specialization,login;
    TextView t1,treg;

    FirebaseAuth mauth;
    FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mail=findViewById(R.id.username);
        pass=findViewById(R.id.password);
        b1=findViewById(R.id.b1);
        treg=findViewById(R.id.tregister);
        mauth=FirebaseAuth.getInstance();
        mUser=mauth.getCurrentUser();
         email = mail.getText().toString();
      password = pass.getText().toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Step 1: Initialize the Spinner and TextView variables
        Spinner loginAsSpinner = findViewById(R.id.loginas);
        TextView specializationTextView = findViewById(R.id.textView);
        Spinner specializationSpinner = findViewById(R.id.specialization);

// Step 2: Set an OnItemSelectedListener on the loginas spinner

        loginAsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Step 3: In the onItemSelected() method, get the selected item and check if it is equal to "Doctor"
                String selectedLoginAs = parent.getItemAtPosition(position).toString();
                if (selectedLoginAs.equals("Doctor")) {
                    // Step 4: If the selected item is "Doctor", then set the specialization TextView and Spinner visibility to View.VISIBLE
                    specializationTextView.setVisibility(View.VISIBLE);
                    specializationSpinner.setVisibility(View.VISIBLE);
                } else {
                    // Otherwise, set their visibility to View.INVISIBLE
                    specializationTextView.setVisibility(View.INVISIBLE);
                    specializationSpinner.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mail.getText().toString();
                String password = pass.getText().toString();

                specialization = specializationSpinner.getItemAtPosition(specializationSpinner.getSelectedItemPosition()).toString();

login=loginAsSpinner.getItemAtPosition(loginAsSpinner.getSelectedItemPosition()).toString();
// Validate email
                if (email.isEmpty()) {
                    mail.setError("Email is required");
                    mail.requestFocus();
                    return;
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mail.setError("Invalid email address");
                    mail.requestFocus();
                    return ;
                }

// Validate password
                if (password.isEmpty()) {
                    pass.setError("Password is required");
                    pass.requestFocus();
                    return;
                } else if (password.length() < 6) {
                    pass.setError("Password must be at least 6 characters");
                    pass.requestFocus();
                    return;
                }

                if(Objects.equals(login, "Doctor")){

                    doctor();
                } else if (login=="Patient") {
                    user();
                }
            }
        });
        treg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });


    }
    @Override
    public void onBackPressed() {

            new AlertDialog.Builder(this)
                    .setTitle("Exit app")
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Start the launcher activity and finish this activity@Override



                                finishAffinity(); // Close all activities
                                System.exit(0); // Exit the app
                            LoginActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
    }
    public void doctor(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("doctor")
                .document(specialization)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {


                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String email = mail.getText().toString();
                                String password = pass.getText().toString();
                                // Extract doctor details here
                                String name = document.getString("name");

                                String special = document.getString("specialization");

                                String pass= document.getString("password");

                                String mail= document.getString("email");
                                // Update UI with doctor details

if(Objects.equals(mail, email) && Objects.equals(pass, password) && Objects.equals(special, specialization)){
    Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
    startActivity(new Intent(LoginActivity.this,DoctorHomeActivity.class));
    SharedPreferences sharedPreferences = getSharedPreferences("doctor",MODE_PRIVATE);
    SharedPreferences.Editor myEdit = sharedPreferences.edit();
    myEdit.putString("doctor", name);
    myEdit.commit();
}else{
    Toast.makeText(LoginActivity.this, "Failed! Incorrect Email or Password", Toast.LENGTH_SHORT).show();
}



                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

    }
    public void user(){
        Dialog dialog = new Dialog(LoginActivity.this);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.setCancelable(false);

// When the user clicks the login button
        dialog.show();
        mauth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    dialog.dismiss();
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("mail", mail.getText().toString());
                    myEdit.putString("password",pass.getText().toString());
                    myEdit.commit();
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                }
                else {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
