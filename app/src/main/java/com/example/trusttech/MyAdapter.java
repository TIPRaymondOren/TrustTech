package com.example.trusttech;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private static final int SHIFT = 3;
    Context context;
    ArrayList<userHelperClass> list;

    public MyAdapter(Context context, ArrayList<userHelperClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final userHelperClass user = list.get(position);
        holder.userName.setText(decrypt(user.getUsername()));
        holder.fullName.setText(decrypt(user.getName()));
        holder.age.setText(decrypt(user.getAge()));
        holder.emailAddress.setText(decrypt(user.getEmail()));
        holder.phoneNo.setText(user.getPhone()); // Phone numbers are not encrypted

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition(); // Get the current position
                if (position != RecyclerView.NO_POSITION) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("User Removal Confirmation");
                    builder.setMessage("Are you sure you want to remove this user?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    deleteItem(position); // Pass the current position
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    Toast.makeText(context, "Removal cancelled", Toast.LENGTH_SHORT).show();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
    }


    private void deleteItem(final int position) {
        try {
            String username = list.get(position).getUsername();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
            reference.child(username).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        try {
                            list.remove(position);
                            notifyItemRemoved((position));
                            Toast.makeText(context, "User removed successfully", Toast.LENGTH_SHORT).show();
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(context, "Failed to remove user", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView fullName, emailAddress, phoneNo, userName, age;
        ImageButton deleteBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.showUsername);
            fullName = itemView.findViewById(R.id.showFullName);
            age = itemView.findViewById(R.id.showAge);
            emailAddress = itemView.findViewById(R.id.showEmail);
            phoneNo = itemView.findViewById(R.id.showPhoneNo);
            deleteBtn = itemView.findViewById(R.id.deleteButton);
        }
    }
    //decryption
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
