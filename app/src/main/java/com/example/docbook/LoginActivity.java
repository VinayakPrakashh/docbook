package com.example.docbook;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

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

AppCompatButton b1,treg;
    public String email,password,specialization,login;


    FirebaseAuth mauth;
    FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_test);
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
        Spinner spinner = findViewById(R.id.specialization);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spec_types, R.layout.spinner_item_layout);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.user_types, R.layout.spinner_item_layout);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loginAsSpinner.setAdapter(adapter2);


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
                } else if (selectedLoginAs.equals("Admin")) {
                    specializationTextView.setVisibility(View.INVISIBLE);
                    specializationSpinner.setVisibility(View.INVISIBLE);
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

                if (password.isEmpty()) {
                    pass.setError("Password is required");
                    pass.requestFocus();
                    return;
                } else if (password.length() < 6) {
                    pass.setError("Password must be at least 6 characters");
                    pass.requestFocus();
                    return;
                }
else if(Objects.equals(login, "Select")){

                    Toast.makeText(LoginActivity.this, "Please Select Login Type!", Toast.LENGTH_SHORT).show();
                return;
}

                if(Objects.equals(login, "Doctor")){
                    specialization = specializationSpinner.getItemAtPosition(specializationSpinner.getSelectedItemPosition()).toString();
if(specialization.equals("Select")){
    Toast.makeText(LoginActivity.this, "Please Select Your Specialization!", Toast.LENGTH_SHORT).show();
}else{
    doctor();
}

                } else if (Objects.equals(login,"Admin")) {
admin();
                } else if (Objects.equals(login, "Patient")) {

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
    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
    SharedPreferences.Editor myEdit = sharedPreferences.edit();
    myEdit.putString("login", login);
    myEdit.putString("specialization", special);
    myEdit.putString("mail", mail);
    myEdit.putString("password",pass);
    myEdit.commit();
    SharedPreferences sharedPreferences2 = getSharedPreferences("doctor",MODE_PRIVATE);
    SharedPreferences.Editor myEdit2 = sharedPreferences2.edit();
    myEdit2.putString("doctor", name);
    myEdit2.commit();
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
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
        String email = mail.getText().toString();
        String password = pass.getText().toString();
        mauth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    dialog.dismiss();
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("login", login);

                    myEdit.putString("mail", mail.getText().toString());
                    myEdit.putString("password",pass.getText().toString());
                    myEdit.commit();
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                }
                else {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed to Login", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void admin(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("admin")
                .document("vinayak")
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
                                String pass= document.getString("password");
                                String mail= document.getString("email");
                                // Update UI with doctor details

                                if(Objects.equals(mail, email) && Objects.equals(pass, password)){
                                    Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this,AdminHomeActivity.class);
                                    intent.putExtra("animkey", "play");
                                    startActivity(intent);
                                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                    myEdit.putString("login", login);
                            myEdit.putString("name",name);
                                    myEdit.putString("mail", mail);
                                    myEdit.putString("password",pass);
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
}
