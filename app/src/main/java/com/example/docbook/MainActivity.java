package com.example.docbook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
EditText uname,pass;
Button b1;
TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uname=findViewById(R.id.username);
        pass=findViewById(R.id.password);
        b1=findViewById(R.id.b1);
    b1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String username = uname.getText().toString();
            String password = pass.getText().toString();

            if (username.equals("aslam")) {
                Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_SHORT).show();
            }
        }
    });
    }

}