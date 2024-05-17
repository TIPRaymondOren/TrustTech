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
    private ImageView btnBestPractices;
    private ImageView btnAboutApp;
    private ImageView btnTermsConditions;
    private ImageView btnPrivacyPolicy;

    private static final int SHIFT = 3;

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
        btnBestPractices = findViewById(R.id.btnBestPractices);
        btnAboutApp = findViewById(R.id.btn_about_app);
        btnTermsConditions = findViewById(R.id.btn_terms_conditions);
        btnPrivacyPolicy = findViewById(R.id.btn_privacy_policy);

        // Fetch name and email from login activity
        String encryptedNameFromLogin = getIntent().getStringExtra("nameFromLogin");
        String encryptedEmailFromLogin = getIntent().getStringExtra("emailFromLogin");

        // Decrypt the fetched name and email
        String nameFromLogin = decrypt(encryptedNameFromLogin);
        String emailFromLogin = decrypt(encryptedEmailFromLogin);

        // Set the decrypted name and email to the corresponding views
        name.setText(nameFromLogin);
        email.setText(emailFromLogin);

        // Set click listeners for buttons
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = getIntent().getStringExtra("usernameFromLogin");
                String name = getIntent().getStringExtra("nameFromLogin");
                String email = getIntent().getStringExtra("emailFromLogin");
                String phone = getIntent().getStringExtra("phoneFromLogin");
                String age = getIntent().getStringExtra("ageFromLogin");

                Intent intent = new Intent(ProfileDashboardActivity.this, UpdateProfileActivity.class);
                intent.putExtra("usernameFromDashboard", username);
                intent.putExtra("nameFromDashboard", name);
                intent.putExtra("emailFromDashboard", email);
                intent.putExtra("phoneFromDashboard", phone);
                intent.putExtra("ageFromDashboard", age);
                startActivity(intent);
            }
        });

        btnBestPractices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BestPracticesActivity.class);
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

    // Decryption function
    private String decrypt(String input) {
        StringBuilder decryptedText = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            char shifted = (char) (c - SHIFT);
            decryptedText.append(shifted);
        }
        return decryptedText.toString();
    }
}
