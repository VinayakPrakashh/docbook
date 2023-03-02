package com.example.docbook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class NeurologistActivity extends AppCompatActivity {
String doctorname="Aslam Suneer",phone="9441002930",exp="5 years";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neurologist);

    }
    private String[][] neurologist=

    {
        {"Doctorname : Prasanth Vaghese", "EXP : 5years", "phone:9744084710"}
    };

}