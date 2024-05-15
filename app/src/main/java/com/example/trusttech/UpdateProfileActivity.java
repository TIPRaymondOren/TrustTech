package com.example.trusttech;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateProfileActivity extends AppCompatActivity {

    private EditText nameTxt, usernameTxt, emailTxt, phoneTxt, ageTxt;
    private Button updateBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private static final int SHIFT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile); // replace with your layout

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        nameTxt = findViewById(R.id.nameTxt);
        usernameTxt = findViewById(R.id.usernameTxt);
        emailTxt = findViewById(R.id.emailTxt);
        phoneTxt = findViewById(R.id.phoneTxt);
        ageTxt = findViewById(R.id.ageTxt);
        updateBtn = findViewById(R.id.updateBtn);

        // TODO: Retrieve the current user's profile information from the database
        // and pre-fill the EditText fields.

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = mAuth.getCurrentUser().getUid();
                String name = nameTxt.getText().toString();
                String username = usernameTxt.getText().toString();
                String email = emailTxt.getText().toString();
                String rawPhone = phoneTxt.getText().toString();  // Do not encrypt the phone number
                String age = ageTxt.getText().toString();

                // Encrypt data
                String encryptedName = encrypt(name);
                String encryptedUsername = encrypt(username);
                String encryptedEmail = encrypt(email);
                String encryptedAge = encrypt(age);

                // Add country code to phone number
                String phone = "+63" + rawPhone;

                updateProfile(userId, encryptedName, encryptedUsername, encryptedEmail, phone, encryptedAge);
            }
        });
    }

    private void updateProfile(String userId, String name, String username, String email, String phoneNumber, String age) {
        DatabaseReference userRef = mDatabase.child("users").child(userId);

        userRef.child("name").setValue(name);
        userRef.child("username").setValue(username);
        userRef.child("email").setValue(email);
        userRef.child("phoneNumber").setValue(phoneNumber);
        userRef.child("age").setValue(age);

        Toast.makeText(UpdateProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
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
