package com.example.trusttech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    //variables
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
        setContentView(R.layout.activity_login);


        //calling out for hooks
        backBtn= (ImageView) findViewById(R.id.backBtn);
        button= (Button) findViewById(R.id.createBtn);
        usertxt= (EditText) findViewById(R.id.usernameTxt);
        passwordtxt= (EditText) findViewById(R.id.passwordTxt);
        login= (Button) findViewById(R.id.loginBtn);
        progressDialog= new ProgressDialog(this);
        message = findViewById(R.id.message);

        //action triggers for login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformLogin();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignup();

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
    private long cooldownTime = 10000; // 10 seconds
    private long lastAttemptTime = 0;

    private void PerformLogin() {
        String user = usertxt.getText().toString();
        String password = passwordtxt.getText().toString();

        if (user.isEmpty()) {
            usertxt.setError("Invalid User");
        } else if (password.isEmpty() || password.length() < 8) {
            passwordtxt.setError("Invalid Password");
        } else {
            if (System.currentTimeMillis() - lastAttemptTime < cooldownTime) {
                // Show a message to the user that they need to wait before trying again
                Toast.makeText(this, "Please wait before trying again", Toast.LENGTH_SHORT).show();
            }else if (unsuccessfulAttempts >= 3){
                Toast.makeText(this, "Login is disabled", Toast.LENGTH_SHORT).show();
            } else {
                isUser();
            }
        }
    }

    private void isUser() {
        final String userEnteredUser = usertxt.getText().toString().trim();
        final String userEnteredPassword = passwordtxt.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean userExists = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String encryptedUsernameFromDB = snapshot.child("username").getValue(String.class);
                    String usernameFromDB = decrypt(encryptedUsernameFromDB); // Decrypt the username
                    if (usernameFromDB != null && usernameFromDB.equals(userEnteredUser)) {
                        userExists = true;
                        String encryptedPasswordFromDB = snapshot.child("password").getValue(String.class);
                        String passwordFromDB = decrypt(encryptedPasswordFromDB);
                        if (userEnteredPassword.equals(passwordFromDB)) {
                            // Login successful, reset the counter and timer
                            unsuccessfulAttempts = 0;
                            lastAttemptTime = 0;

                            // Retrieve user data from snapshot
                            String nameFromDB = snapshot.child("name").getValue(String.class);
                            String emailFromDB = snapshot.child("email").getValue(String.class);
                            String phoneFromDB = snapshot.child("phone").getValue(String.class);
                            String ageFromDB = snapshot.child("age").getValue(String.class);
                            String passwordDB = snapshot.child("password").getValue(String.class);


                            Intent intent = new Intent(getApplicationContext(), OtpActivity.class);
                            intent.putExtra("usernameFromLogin", userEnteredUser);
                            intent.putExtra("nameFromLogin", nameFromDB);
                            intent.putExtra("emailFromLogin", emailFromDB);
                            intent.putExtra("phoneFromLogin", phoneFromDB);
                            intent.putExtra("ageFromLogin", ageFromDB);
                            intent.putExtra("passwordFromLogin", passwordDB);
                            startActivity(intent);
                            return;
                        } else {
                            // Increment the counter and update the timer
                            unsuccessfulAttempts++;
                            lastAttemptTime = System.currentTimeMillis();
                            if (unsuccessfulAttempts >= 3) {
                                // Show a message to the user that they need to wait before trying again
                                message.setVisibility(View.VISIBLE);
                            } else {
                                passwordtxt.setError("Wrong Password");
                                passwordtxt.requestFocus();
                            }
                            return;
                        }
                    }
                }
                // No such user found
                if (!userExists) {
                    unsuccessfulAttempts++;
                    lastAttemptTime = System.currentTimeMillis();
                    if (unsuccessfulAttempts >= 3) {
                        // Show a message to the user that they need to wait before trying again
                        Toast.makeText(LoginActivity.this, "Too many unsuccessful username attempts. Login is temporarily blocked.", Toast.LENGTH_SHORT).show();
                    } else {
                        usertxt.setError("No such user exists");
                        usertxt.requestFocus();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }



    public void openMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openSignup(){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    // Decryption method using Caesar cipher
    private String decrypt(String input) {
        StringBuilder decryptedText = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            char shifted = (char) (c - 3); // Caesar cipher with a shift of 3
            decryptedText.append(shifted);
        }
        return decryptedText.toString();
    }
}
