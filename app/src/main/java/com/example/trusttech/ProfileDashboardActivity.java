package com.example.trusttech;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ProfileDashboardActivity extends AppCompatActivity {

    private ImageView profilePicture;
    private TextView name;
    private TextView email;
    private ImageView btnUpdateProfile;
    private ImageView btnChangePassword;
    private ImageView btnAboutApp;
    private ImageView btnTermsConditions;
    private ImageView btnPrivacyPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Remove the title from the toolbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Initialize profile information
        profilePicture = findViewById(R.id.profile_picture);
        name = findViewById(R.id.textviewname);
        email = findViewById(R.id.textviewemail);

        // Initialize buttons
        btnUpdateProfile = findViewById(R.id.btn_update_profile);
        btnChangePassword = findViewById(R.id.btn_change_password);
        btnAboutApp = findViewById(R.id.btn_about_app);
        btnTermsConditions = findViewById(R.id.btn_terms_conditions);
        btnPrivacyPolicy = findViewById(R.id.btn_privacy_policy);

        // Fetch name and email from login activity
        String nameFromLogin = getIntent().getStringExtra("nameFromLogin");
        String emailFromLogin = getIntent().getStringExtra("emailFromLogin");

        // Set the fetched name and email to the corresponding views
        name.setText(nameFromLogin);
        email.setText(emailFromLogin);

        // Set click listeners for buttons
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpdateProfileActivity.class);
                startActivity(intent);
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        btnAboutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AboutAppActivity.class);
                startActivity(intent);
            }
        });

        btnTermsConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TermsConditionsActivity.class);
                startActivity(intent);
            }
        });

        btnPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PrivacyPolicyActivity.class);
                startActivity(intent);
            }
        });
    }
}