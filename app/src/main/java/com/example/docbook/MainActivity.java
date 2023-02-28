package com.example.docbook;

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

public class MainActivity extends AppCompatActivity {
EditText uname,pass;
Button b1;
TextView t1,treg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uname=findViewById(R.id.username);
        pass=findViewById(R.id.password);
        b1=findViewById(R.id.b1);
        treg=findViewById(id.tregister);
        b1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String username = uname.getText().toString();
            String password = pass.getText().toString();
            Database db=new Database(getApplicationContext(),"healthcare",null,1);
            if (username.length()==0 || password.length()==0) {
                Toast.makeText(getApplicationContext(), "Please fill the fields", Toast.LENGTH_SHORT).show();
            }
            else {
                if(db.login(username,password)==1){

                    Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences=getSharedPreferences("shared_prefs",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("username",username);
                    editor.apply();
                    startActivity(new Intent(MainActivity.this,HomeActivity.class));
                }
                else {
                    Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                }
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

}