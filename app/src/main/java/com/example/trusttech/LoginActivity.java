package com.example.trusttech;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    ImageView backBtn;
    Button button, login;

    EditText usertxt, passwordtxt;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);


        //hooks
        backBtn= (ImageView) findViewById(R.id.backBtn);
        button= (Button) findViewById(R.id.createBtn);
        usertxt= (EditText) findViewById(R.id.usernameTxt);
        passwordtxt= (EditText) findViewById(R.id.passwordTxt);
        login= (Button) findViewById(R.id.loginBtn);
        progressDialog= new ProgressDialog(this);


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

    private void PerformLogin() {
        String user= usertxt.getText().toString();
        String password= passwordtxt.getText().toString();

        if (user.isEmpty()){
            usertxt.setError("Invalid User");

        }
        else if(password.isEmpty() || password.length()<8){
            passwordtxt.setError("Invalid Password");
        }
        else {

            //dito sa line na to magreredirect sa profile page, meaning dito mo ilalagay yung OTP palitan mo yung intent.
            // pero maglagay ka ng function sa OTP.java mo then paste mo si isUser para magcatch ng mga data.

            isUser();

        }
    }

    private void isUser(){
        final String userEnteredUser= usertxt.getText().toString().trim();
        final String userEnteredPassword= passwordtxt.getText().toString().trim();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        Query checkUser =  reference.orderByChild("username").equalTo(userEnteredUser);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){


                    String passwordFromDB = dataSnapshot.child(userEnteredUser).child("password").getValue(String.class);

                    if(userEnteredPassword.equals(passwordFromDB)){

                        String userFromDB = dataSnapshot.child(userEnteredUser).child("username").getValue(String.class);
                        String nameFromDB = dataSnapshot.child(userEnteredUser).child("name").getValue(String.class);
                        String emailFromDB = dataSnapshot.child(userEnteredUser).child("email").getValue(String.class);
                        String phoneFromDB = dataSnapshot.child(userEnteredUser).child("phone").getValue(String.class);
                        String ageFromDB = dataSnapshot.child(userEnteredUser).child("age").getValue(String.class);


                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);

                        intent.putExtra("username", userFromDB);
                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("phone", phoneFromDB);
                        intent.putExtra("age", ageFromDB);

                        startActivity(intent);
                    }
                    else{
                        passwordtxt.setError("Wrong Password");
                        passwordtxt.requestFocus();
                    }
                }
                else{
                    usertxt.setError("No such user exist");
                    usertxt.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
}