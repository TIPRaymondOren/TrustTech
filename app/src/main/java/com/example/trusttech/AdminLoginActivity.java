package com.example.trusttech;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

public class AdminLoginActivity extends AppCompatActivity {
    ImageView backBtn;
    Button button, login;

    TextView message;

    EditText usertxt, passwordtxt;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_login);


        //hooks
        backBtn= (ImageView) findViewById(R.id.backBtn);
        button= (Button) findViewById(R.id.createBtn);
        usertxt= (EditText) findViewById(R.id.usernameTxt);
        passwordtxt= (EditText) findViewById(R.id.old_passwordTxt);
        login= (Button) findViewById(R.id.loginBtn);
        progressDialog= new ProgressDialog(this);
        message = findViewById(R.id.message);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformLogin();
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMain();
            }
        });


    }

    private int unsuccessfulAttempts = 0;
    private long cooldownTime = 10000; // 30 seconds
    private long lastAttemptTime = 0;

    private void PerformLogin() {
        String user = usertxt.getText().toString();
        String password = passwordtxt.getText().toString();

        if (user.isEmpty()) {
            usertxt.setError("Username is required");
        } else if (password.isEmpty() || password.length() < 8) {
            passwordtxt.setError("Valid Password is required");
        } else {
            if (System.currentTimeMillis() - lastAttemptTime < cooldownTime) {
                // Show a message to the user that they need to wait before trying again
                Toast.makeText(this, "Please wait 10 seconds before trying again", Toast.LENGTH_SHORT).show();
            } else if (unsuccessfulAttempts >= 3){
                Toast.makeText(this, "Login is disabled", Toast.LENGTH_SHORT).show();
            }else {
                isUser();
            }
        }
    }

    private void isUser() {
        final String userEnteredUser = usertxt.getText().toString().trim();
        final String userEnteredPassword = passwordtxt.getText().toString().trim();
        String hardcodedUsername = "trust_admin";
        String hardcodedPassword = "Tru$t@dM1n!";
        String hardcodedPhone = "+639564931049";

        if (userEnteredUser.equals(hardcodedUsername) && userEnteredPassword.equals(hardcodedPassword)) {
            // Login successful, reset the counter and timer
            unsuccessfulAttempts = 0;
            lastAttemptTime = 0;

            Intent intent = new Intent(getApplicationContext(), AdminOtpActivity.class);
            intent.putExtra("phone", hardcodedPhone);
            startActivity(intent);
        } else {
            // Increment the counter and update the timer
            unsuccessfulAttempts++;
            lastAttemptTime = System.currentTimeMillis();

            if (unsuccessfulAttempts >= 3) {
                // Show a message to the user that they need to wait before trying again
                message.setVisibility(View.VISIBLE);

            } else {
                if (!userEnteredUser.equals(hardcodedUsername)) {
                    usertxt.setError("Invalid Username");
                    usertxt.requestFocus();
                } else {
                    passwordtxt.setError("Wrong Password");
                    passwordtxt.requestFocus();
                }
            }
        }
    }

    public void openMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}