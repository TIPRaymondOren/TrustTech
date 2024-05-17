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

public class SignupActivity extends AppCompatActivity {

    Button login, signup;
    ImageView backBtn;
    EditText nameTxt, usernameTxt, emailTxt, phoneTxt, ageTxt, passwordTxt, confirm_passwordTxt;

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    private static final int SHIFT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Hooks for sign_up
        backBtn = findViewById(R.id.backBtn);
        signup = findViewById(R.id.signupBtn);
        login = findViewById(R.id.loginBtn);
        nameTxt = findViewById(R.id.nameTxt);
        usernameTxt = findViewById(R.id.usernameTxt);
        emailTxt = findViewById(R.id.emailTxt);
        phoneTxt = findViewById(R.id.phoneTxt);
        ageTxt = findViewById(R.id.ageTxt);
        passwordTxt = findViewById(R.id.old_passwordTxt);
        confirm_passwordTxt = findViewById(R.id.confirm_new_passwordTxt);

        FirebaseApp.initializeApp(this);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
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
                String password = passwordTxt.getText().toString();
                String confirm_password = confirm_passwordTxt.getText().toString();

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
                if (password.isEmpty()) {
                    passwordTxt.setError("Password is required");
                    passwordTxt.requestFocus();
                    return;
                } else if (password.length() < 8) {
                    passwordTxt.setError("Password must be at least 8 characters long");
                    passwordTxt.requestFocus();
                    return;
                } else if (!password.matches("^(?=.*[0-9])(?=.*[A-Z])(?=.*[!@#$%^&*+=])(?=\\S+$).{8,}$")) {
                    passwordTxt.setError("Password must contain at least one number, one uppercase letter, and one special character");
                    passwordTxt.requestFocus();
                    return;
                }

                // Validate confirm password
                if (confirm_password.isEmpty()) {
                    confirm_passwordTxt.setError("Confirm Password is required");
                    confirm_passwordTxt.requestFocus();
                    return;
                } else if (!confirm_password.equals(password)) {
                    confirm_passwordTxt.setError("Passwords do not match");
                    confirm_passwordTxt.requestFocus();
                    return;
                }

                // Encrypt data
                String encryptedName = encrypt(name);
                String encryptedUsername = encrypt(username);
                String encryptedEmail = encrypt(email);
                String encryptedAge = encrypt(age);
                String encryptedPassword = encrypt(password);
                String encryptedConfirmPassword = encrypt(confirm_password);

                // Add country code to phone number
                String phone = "+63" + rawPhone;

                // Create userHelperClass instance with encrypted data
                userHelperClass helperClass = new userHelperClass(encryptedName, encryptedUsername, encryptedEmail, phone, encryptedAge, encryptedPassword, encryptedConfirmPassword);

                // Store encrypted data in the database
                reference.child(username).setValue(helperClass);

                Toast.makeText(SignupActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoLogin();
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

    public void gotoLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
