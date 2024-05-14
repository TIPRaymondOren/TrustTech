package com.example.trusttech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminPageActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference database;
    MyAdapter myAdapter;
    ArrayList<userHelperClass> list;

    ImageView logout;

    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        recyclerView = findViewById(R.id.recyclerView);
        database = FirebaseDatabase.getInstance().getReference("users");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        logout = findViewById(R.id.logout_btn);
        builder = new AlertDialog.Builder(this);

        list = new ArrayList<>();
        myAdapter = new MyAdapter(this, list);
        recyclerView.setAdapter(myAdapter);

        // Fetch data from db then list using RecyclerView
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear(); // Clear the list before adding new data to avoid duplicates when deleting data

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    userHelperClass user = dataSnapshot.getValue(userHelperClass.class);
                    if (user != null) {
                        // Decrypt fields
                        user.setName(decrypt(user.getName()));
                        user.setEmail(decrypt(user.getEmail()));
                        list.add(user);
                    }
                }
                // Verify if data is fetched correctly by logging the size of the list
                Log.d("DataFetch", "List size after fetching data: " + list.size());
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DataFetch", "Failed to fetch data from database: " + error.getMessage());
            }
        });

        // Logout
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Setting the title and message
                builder.setTitle("Logout Confirmation");
                builder.setMessage("Are you sure you want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                Intent intent = new Intent(AdminPageActivity.this, MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Logged out successfully",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Action for 'NO' Button
                                dialog.cancel();
                                Toast.makeText(getApplicationContext(), "Logout cancelled",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                // Creating dialog box
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    // Method to decrypt a string using Caesar cipher with a shift of 3
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
