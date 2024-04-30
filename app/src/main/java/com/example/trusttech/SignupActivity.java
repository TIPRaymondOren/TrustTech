package com.example.trusttech;


import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
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
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //hooks for sign_up
        backBtn= findViewById(R.id.backBtn);
        signup = (Button) findViewById(R.id.signupBtn);
        login = (Button) findViewById(R.id.loginBtn);
        nameTxt = findViewById(R.id.nameTxt);
        usernameTxt = findViewById(R.id.usernameTxt);
        emailTxt = findViewById(R.id.emailTxt);
        phoneTxt =  findViewById(R.id.phoneTxt);
        ageTxt =  findViewById(R.id.ageTxt);
        passwordTxt =  findViewById(R.id.passwordTxt);
        confirm_passwordTxt =  findViewById(R.id.confirm_passwordTxt);



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

                //get all the values
                String name= nameTxt.getText().toString();
                String username= usernameTxt.getText().toString();
                String email= emailTxt.getText().toString();
                String phone= phoneTxt.getText().toString();
                String age= ageTxt.getText().toString();
                String password= passwordTxt.getText().toString();
                String confirm_password= confirm_passwordTxt.getText().toString();

               /* // Validate name
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
                if (phone.isEmpty()) {
                    phoneTxt.setError("Phone number is required");
                    phoneTxt.requestFocus();
                    return;
                } else if (!android.util.Patterns.PHONE.matcher(phone).matches()) {
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
                }*/

                // If all validations pass, create the account
                userHelperClass helperClass= new userHelperClass(name, username, email, phone, age, password, confirm_password);
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


    public void gotoLogin(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }


}