package com.example.docbook;

import static android.content.ContentValues.TAG;
import static com.example.docbook.Connection.isNetworkAvailable;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    EditText mail,pass;
Button b1;
TextView t1,treg;
public  String emailAUTH,passwordAUT,loginAUTH,specAUTH;
    FirebaseAuth mauth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences =getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        emailAUTH= sharedPreferences.getString("mail","").toString();
        passwordAUT=sharedPreferences.getString("password","").toString();
       loginAUTH= sharedPreferences.getString("login","").toString();
       specAUTH= sharedPreferences.getString("specialization","").toString();
        Log.d("MyApp", "Button clicked");

        if (isNetworkAvailable(this)) {
            if(Objects.equals(loginAUTH, "Doctor")){
                doctor();
            }
            else if(Objects.equals(loginAUTH, "Patient")){
                user();
            }else {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        } else {
            showNoInternetSnackbar();
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
           if(Objects.equals(loginAUTH, "Doctor")){
               doctor();
           }
           else if(Objects.equals(loginAUTH, "Patient")){
               user();
           }else {
               startActivity(new Intent(MainActivity.this,LoginActivity.class));
           }
        } else {
            showNoInternetSnackbar();
        }

    }
    public void user(){
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

    public void doctor(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("doctor")
                .document(specAUTH)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {


                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {



                                String special = document.getString("specialization");

                                String pass= document.getString("password");

                                String mail= document.getString("email");
                                // Update UI with doctor details

                                if(Objects.equals(mail, emailAUTH) && Objects.equals(pass, passwordAUT) && Objects.equals(special, specAUTH)){

                                    startActivity(new Intent(MainActivity.this,DoctorHomeActivity.class));

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