package com.example.docbook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {
TextView t1,t2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        t1=findViewById(R.id.tv);
        t2=findViewById(R.id.contact);
        String url=t1.getText().toString();
        t2.setOnClickListener(view -> {
            String phoneNumber="7902963981";
            Intent dialerIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));

// start the dialer activity
            startActivity(dialerIntent);
        });
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);


            }
        });

    }

}