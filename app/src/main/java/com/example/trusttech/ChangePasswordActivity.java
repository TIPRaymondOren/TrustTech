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

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText currentPasswordTxt, newPasswordTxt, confirmNewPasswordTxt;
    private Button changePasswordBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private static final int SHIFT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        currentPasswordTxt = findViewById(R.id.currentPasswordTxt);
        newPasswordTxt = findViewById(R.id.newPasswordTxt);
        confirmNewPasswordTxt = findViewById(R.id.confirmNewPasswordTxt);
        changePasswordBtn = findViewById(R.id.changePasswordBtn);

        // TODO: Retrieve the current user's password from the database.

        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = mAuth.getCurrentUser().getUid();
                String currentPassword = currentPasswordTxt.getText().toString();
                String newPassword = newPasswordTxt.getText().toString();
                String confirmNewPassword = confirmNewPasswordTxt.getText().toString();

                // TODO: Check if the current password entered by the user matches the current password in the database.

                if (newPassword.equals(confirmNewPassword)) {
                    // Encrypt new password
                    String encryptedNewPassword = encrypt(newPassword);

                    changePassword(userId, encryptedNewPassword);
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "New passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void changePassword(String userId, String newPassword) {
        DatabaseReference userRef = mDatabase.child("users").child(userId);

        userRef.child("password").setValue(newPassword);
        userRef.child("confirm_password").setValue(newPassword);

        Toast.makeText(ChangePasswordActivity.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
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
