package com.example.docbook;

import static com.example.docbook.Connection.isNetworkAvailable;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
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

        if (isNetworkAvailable(this)) {

        } else {
            showNoInternetSnackbar();
        }

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
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }


    }
    private void showNoInternetSnackbar() {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No internet connection. Please try again later.", Snackbar.LENGTH_LONG);
        snackbar.setDuration(100000);
        snackbar.setAction("TRY AGAIN", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Call the method to check network availability and retry the operation
                checkNetworkAvailabilityAndRetry();

            }
        });
        snackbar.show();
    }
   public void checkNetworkAvailabilityAndRetry(){

        if (isNetworkAvailable(this)) {
            SharedPreferences sharedPreferences =getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
            String emailAUTH= sharedPreferences.getString("mail","").toString();
            String passwordAUT=sharedPreferences.getString("password","").toString();
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
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }

        } else {
            showNoInternetSnackbar();
        }

    }

}