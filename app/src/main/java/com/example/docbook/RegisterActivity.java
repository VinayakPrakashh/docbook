package com.example.docbook;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;

public class RegisterActivity extends AppCompatActivity {
Button b1;
    FirebaseAuth mauth;
    FirebaseUser mUser;

EditText uname,pass,mail;
TextView login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        uname=findViewById(R.id.username);
        pass=findViewById(R.id.password);
        b1=findViewById(R.id.b1);
        mail=findViewById(R.id.mail);
        login=findViewById(R.id.tregister);
       mauth=FirebaseAuth.getInstance();
       mUser=mauth.getCurrentUser();
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
                if(username.length()==0 || email.length()==0 || password.length()==0){
                    Toast.makeText(RegisterActivity.this, "Please fill the Required fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    PerforAuth();

                }
            }
        });
    }

    private void PerforAuth() {
        String username=uname.getText().toString();
        String email=mail.getText().toString();
        String password=pass.getText().toString();
        mauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/" + userId);

                    User user = new User(username, email,password);
                    ref.setValue(user);
                    Toast.makeText(getApplicationContext(), "Sucess", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                }
                else {
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