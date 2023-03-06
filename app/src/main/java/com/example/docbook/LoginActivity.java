package com.example.docbook;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity {
    EditText mail,pass;
    Button b1;
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
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mail.getText().toString();
                String password = pass.getText().toString();


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
    }}
