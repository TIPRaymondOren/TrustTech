package com.example.trusttech;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {


    TextView tv1, tv2, tv3, tv4, tv5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tv1 = findViewById(R.id.textView);
        tv2 = findViewById(R.id.textView2);
        tv3 = findViewById(R.id.textView3);
        tv4 = findViewById(R.id.textView4);
        tv5 = findViewById(R.id.textView5);

        String username = getIntent().getStringExtra("usernameFromLogin");
        String name = getIntent().getStringExtra("nameFromLogin");
        String email = getIntent().getStringExtra("emailFromLogin");
        String phone = getIntent().getStringExtra("phoneFromLogin");
        String age = getIntent().getStringExtra("ageFromLogin");

        tv1.setText(username);
        tv2.setText(name);
        tv3.setText(email);
        tv4.setText(phone);
        tv5.setText(age);

    }
}