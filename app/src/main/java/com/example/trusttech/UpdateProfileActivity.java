package com.example.trusttech;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateProfileActivity extends AppCompatActivity {

    Button update;
    ImageView backBtn;
    EditText nameTxt, usernameTxt, emailTxt, phoneTxt, ageTxt, oldPasswordTxt, newPasswordTxt, confirmNewPasswordTxt;

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    private static final int SHIFT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        // Hooks for update_profile
        backBtn = findViewById(R.id.backBtn);
        update = findViewById(R.id.btn_update_profile);
        nameTxt = findViewById(R.id.nameTxt);
        usernameTxt = findViewById(R.id.usernameTxt);
        emailTxt = findViewById(R.id.emailTxt);
        phoneTxt = findViewById(R.id.phoneTxt);
        ageTxt = findViewById(R.id.ageTxt);
        oldPasswordTxt = findViewById(R.id.old_passwordTxt);
        newPasswordTxt = findViewById(R.id.new_passwordTxt);
        confirmNewPasswordTxt = findViewById(R.id.confirm_new_passwordTxt);

        // Get the Strings from the intent
        String username = getIntent().getStringExtra("usernameFromDashboard");
        String name = decrypt(getIntent().getStringExtra("nameFromDashboard"));
        String email = decrypt(getIntent().getStringExtra("emailFromDashboard"));
        String phone = getIntent().getStringExtra("phoneFromDashboard");
        String age = decrypt(getIntent().getStringExtra("ageFromDashboard"));
        String oldPassword = decrypt(getIntent().getStringExtra("passwordFromDashboard"));

        usernameTxt.setText(username);
        nameTxt.setText(name);
        emailTxt.setText(email);
        phoneTxt.setText(phone);
        ageTxt.setText(age);
        oldPasswordTxt.setText(oldPassword);

        FirebaseApp.initializeApp(this);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateProfileActivity.this, ProfileDashboardActivity.class);
                startActivity(intent);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");

                // Get all the values
                String name = nameTxt.getText().toString();
                String username = usernameTxt.getText().toString();
                String email = emailTxt.getText().toString();
                String rawPhone = phoneTxt.getText().toString();  // Do not encrypt the phone number
                String age = ageTxt.getText().toString();
                String old_password = oldPasswordTxt.getText().toString();
                String new_password = newPasswordTxt.getText().toString();
                String confirm_new_password = confirmNewPasswordTxt.getText().toString();

                // Validate name
                if (name.isEmpty()) {
                    nameTxt.setError("Name is required");
                    nameTxt.requestFocus();
                    return;
                }

                // Validate username
                if (username.isEmpty()) {
                    usernameTxt.setError("Username is required");
                    usernameTxt.requestFocus();
                    return;
                }

                // Validate email
                if (email.isEmpty()) {
                    emailTxt.setError("Email is required");
                    emailTxt.requestFocus();
                    return;
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailTxt.setError("Invalid email address");
                    emailTxt.requestFocus();
                    return;
                }

                // Validate phone number
                if (rawPhone.isEmpty()) {
                    phoneTxt.setError("Phone number is required");
                    phoneTxt.requestFocus();
                    return;
                } else if (!android.util.Patterns.PHONE.matcher(rawPhone).matches()) {
                    phoneTxt.setError("Invalid phone number");
                    phoneTxt.requestFocus();
                    return;
                }

                // Validate age
                if (age.isEmpty()) {
                    ageTxt.setError("Age is required");
                    ageTxt.requestFocus();
                    return;
                }

                // Validate password
                if (!new_password.isEmpty()) {
                    if (new_password.length() < 8) {
                        newPasswordTxt.setError("Password must be at least 8 characters long");
                        newPasswordTxt.requestFocus();
                        return;
                    } else if (!new_password.matches("^(?=.*[0-9])(?=.*[A-Z])(?=.*[!@#$%^&*+=])(?=\\S+$).{8,}$")) {
                        newPasswordTxt.setError("Password must contain at least one number, one uppercase letter, and one special character");
                        newPasswordTxt.requestFocus();
                        return;
                    }
                }

                // Validate confirm password
                if (!confirm_new_password.isEmpty()) {
                    if (!confirm_new_password.equals(new_password)) {
                        confirmNewPasswordTxt.setError("Passwords do not match");
                        confirmNewPasswordTxt.requestFocus();
                        return;
                    }
                }

                // Encrypt data
                String encryptedName = encrypt(name);
                String encryptedUsername = encrypt(username);
                String encryptedEmail = encrypt(email);
                String encryptedAge = encrypt(age);
                String encryptedPassword = encrypt(new_password);
                String encryptedConfirmPassword = encrypt(confirm_new_password);

                if (new_password.isEmpty() && confirm_new_password.isEmpty()) {
                    encryptedPassword = encrypt(old_password);
                    encryptedConfirmPassword = encrypt(old_password);
                } else if (!new_password.isEmpty() && !confirm_new_password.isEmpty()) {
                    encryptedPassword = encrypt(new_password);
                    encryptedConfirmPassword = encrypt(confirm_new_password);
                } else {
                    if (new_password.isEmpty()) {
                        newPasswordTxt.setError("Both Confirm and New Password should be filled out or None if you don't want to change");
                        newPasswordTxt.requestFocus();
                        return;
                    } else if (confirm_new_password.isEmpty()) {
                        confirmNewPasswordTxt.setError("Both Confirm and New Password should be filled out or None if you don't want to change");
                        confirmNewPasswordTxt.requestFocus();
                        return;
                    }
                }

                // Create userHelperClass instance with encrypted data
                userHelperClass helperClass = new userHelperClass(encryptedName, encryptedUsername, encryptedEmail, rawPhone, encryptedAge, encryptedPassword, encryptedConfirmPassword);

                // Store encrypted data in the database
                reference.child(username).setValue(helperClass);

                Toast.makeText(UpdateProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateProfileActivity.this, ProfileDashboardActivity.class);
                startActivity(intent);
            }
        });
    }

    // Encryption method using Caesar cipher
    private String encrypt(String input) {
        StringBuilder encryptedText = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            char shifted = (char) (c + SHIFT);
            encryptedText.append(shifted);
        }
        return encryptedText.toString();
    }

    // Decryption method using Caesar cipher
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
