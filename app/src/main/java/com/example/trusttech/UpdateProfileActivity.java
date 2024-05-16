package com.example.trusttech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateProfileActivity extends AppCompatActivity {

    private EditText nameTxt, usernameTxt, emailTxt, phoneTxt, ageTxt, passwordTxt;
    private Button updateBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private static final int SHIFT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        nameTxt = findViewById(R.id.nameTxt);
        usernameTxt = findViewById(R.id.usernameTxt);
        emailTxt = findViewById(R.id.emailTxt);
        phoneTxt = findViewById(R.id.phoneTxt);
        ageTxt = findViewById(R.id.ageTxt);
        passwordTxt = findViewById(R.id.passwordTxt);
        updateBtn = findViewById(R.id.btn_update_profile);

        // Retrieve the current user's profile information from the database
        // and pre-fill the EditText fields.
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference userRef = mDatabase.child("users").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String encryptedName = dataSnapshot.child("name").getValue(String.class);
                String encryptedUsername = dataSnapshot.child("username").getValue(String.class);
                String encryptedEmail = dataSnapshot.child("email").getValue(String.class);
                String phoneNumber = dataSnapshot.child("phoneNumber").getValue(String.class);
                String encryptedAge = dataSnapshot.child("age").getValue(String.class);

                // Decrypt the fetched data
                String name = decrypt(encryptedName);
                String username = decrypt(encryptedUsername);
                String email = decrypt(encryptedEmail);
                String age = decrypt(encryptedAge);

                nameTxt.setText(name);
                usernameTxt.setText(username);
                emailTxt.setText(email);
                phoneTxt.setText(phoneNumber);
                ageTxt.setText(age);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(UpdateProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = mAuth.getCurrentUser().getUid();
                String name = nameTxt.getText().toString();
                String username = usernameTxt.getText().toString();
                String email = emailTxt.getText().toString();
                String rawPhone = phoneTxt.getText().toString();  // Do not encrypt the phone number
                String age = ageTxt.getText().toString();
                String password = passwordTxt.getText().toString();

                // Encrypt data
                String encryptedName = encrypt(name);
                String encryptedUsername = encrypt(username);
                String encryptedEmail = encrypt(email);
                String encryptedAge = encrypt(age);

                // Add country code to phone number
                String phone = "+63" + rawPhone;

                updateProfile(userId, encryptedName, encryptedUsername, encryptedEmail, phone, encryptedAge);
                updatePassword(password);
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

    // Update profile method
    private void updateProfile(String userId, String name, String username, String email, String phoneNumber, String age) {
        DatabaseReference userRef = mDatabase.child("users").child(userId);

        userRef.child("name").setValue(name);
        userRef.child("username").setValue(username);
        userRef.child("email").setValue(email);
        userRef.child("phoneNumber").setValue(phoneNumber);
        userRef.child("age").setValue(age);

        Toast.makeText(UpdateProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
    }

    // Update password method
    private void updatePassword(String newPassword) {
        FirebaseUser user = mAuth.getCurrentUser();

        user.updatePassword(newPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(UpdateProfileActivity.this, "Password Updated Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UpdateProfileActivity.this, "Failed to Update Password", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
