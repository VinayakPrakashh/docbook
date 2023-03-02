package com.example.docbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docbook.R.id;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
EditText mail,pass;
Button b1;
TextView t1,treg;
    FirebaseAuth mauth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences =getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String emailAUTH= sharedPreferences.getString("mail","").toString();
        String passwordAUT=sharedPreferences.getString("password","").toString();

        mail=findViewById(R.id.username);
        pass=findViewById(R.id.password);
        b1=findViewById(R.id.b1);
        treg=findViewById(id.tregister);
mauth=FirebaseAuth.getInstance();
mUser=mauth.getCurrentUser();
        if(emailAUTH.length()!=0){
            mauth.signInWithEmailAndPassword(emailAUTH,passwordAUT).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        startActivity(new Intent(MainActivity.this,HomeActivity.class));
                    }
                    else {

                    }
                }
            });
        }
     else {

        }
        b1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String email = mail.getText().toString();
            String password = pass.getText().toString();
            Database db=new Database(getApplicationContext(),"healthcare",null,1);
            if (email.length()==0 || password.length()==0) {
                Toast.makeText(getApplicationContext(), "Please fill the fields", Toast.LENGTH_SHORT).show();
            }
            else {
                mauth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                            myEdit.putString("mail", mail.getText().toString());
                            myEdit.putString("password",pass.getText().toString());
                            myEdit.commit();
                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this,HomeActivity.class));
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }
    });
    treg.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(MainActivity.this,RegisterActivity.class));
        }
    });

    }
    private void user(){

    }

}