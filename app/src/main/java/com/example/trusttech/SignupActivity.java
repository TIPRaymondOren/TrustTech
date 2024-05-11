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
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

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
        passwordTxt = findViewById(R.id.passwordTxt);
        confirm_passwordTxt = findViewById(R.id.confirm_passwordTxt);

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
                String name = encrypt(nameTxt.getText().toString());
                String username = encrypt(usernameTxt.getText().toString());
                String email = encrypt(emailTxt.getText().toString());
                String rawPhone = encrypt(phoneTxt.getText().toString());
                String age = encrypt(ageTxt.getText().toString());
                String password = encrypt(passwordTxt.getText().toString());
                String confirm_password = encrypt(confirm_passwordTxt.getText().toString());

                // Create userHelperClass instance with encrypted data
                userHelperClass helperClass = new userHelperClass(name, username, email, rawPhone, age, password, confirm_password);

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
