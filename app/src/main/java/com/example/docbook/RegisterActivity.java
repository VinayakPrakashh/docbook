package com.example.docbook;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
Button b1;
    FirebaseAuth mauth;

EditText uname,pass,mail,cpass;
TextView login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        uname=findViewById(R.id.username);
        pass=findViewById(R.id.password);
        cpass=findViewById(R.id.cpassword);

        mail=findViewById(R.id.mail);
        b1=findViewById(R.id.b1);
        login=findViewById(R.id.tregister);
       mauth=FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username=uname.getText().toString();
                String email=mail.getText().toString();
                String password=pass.getText().toString();
                String confirmPassword=cpass.getText().toString();

                // Validate username
                if (username.isEmpty()) {
                    uname.setError("Username is required");
                    uname.requestFocus();
                    return;
                } else if (username.length() < 4) {
                    uname.setError("Username must be at least 4 characters");
                    uname.requestFocus();
                    return;
                }
                // Validate email
                if (email.isEmpty()) {
                    mail.setError("Email is required");
                    mail.requestFocus();
                    return;
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mail.setError("Invalid email address");
                    mail.requestFocus();
                    return;
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

                // Validate confirm password
                if (confirmPassword.isEmpty()) {
                    cpass.setError("Confirm password is required");
                    cpass.requestFocus();
                    return;
                } else if (!confirmPassword.equals(password)) {
                    cpass.setError("Passwords do not match");
                    cpass.requestFocus();
                    return;
                }



                // All fields are valid
                PerforAuth();
            }
        });


    }

    private void PerforAuth() {

        Dialog dialog = new Dialog(RegisterActivity.this);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.setCancelable(false);
        dialog.show();
        String username=uname.getText().toString();
        String email=mail.getText().toString();
        String password=pass.getText().toString();

        mauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

// Get the current user's UID
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    String uid = mAuth.getCurrentUser().getUid();

// Get a reference to the "users" collection
                    CollectionReference usersRef = db.collection("users");

// Create a new user object with the user's details
                    User user = new User(username, email,password);
                    usersRef.document(uid).set(user);
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Sucess", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                }
                else {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public class User {
        public String username=uname.getText().toString();;
        public String email=mail.getText().toString();;

        public String password=pass.getText().toString();;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String username, String email,String password) {
            this.username = username;
            this.email = email;
            this.password=password;
        }
    }

}