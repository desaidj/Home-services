package com.example.prototype_1.ServiceMilegiOne.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prototype_1.ServiceMilegiOne.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.prototype_1.ServiceMilegiOne.utils.*;
public class Register extends AppCompatActivity {

    private EditText first, last, mobile, email;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String Userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();


        first = findViewById(R.id.reg_first_name);
        last = findViewById(R.id.reg_last_name);
        mobile = findViewById(R.id.reg_mob);
        email = findViewById(R.id.reg_email);


        Button register = findViewById(R.id.reg_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }

        });

    }

    private void registerUser() {

        final String fname = first.getText().toString().trim();
        final String lname = last.getText().toString().trim();
        final String mob = mobile.getText().toString();
        final String Email = email.getText().toString().trim();

        if (fname.isEmpty()) {
            first.setError("First Name is required");
            first.requestFocus();
            return;
        }

        if (lname.isEmpty()) {
            last.setError("Last Name is required");
            last.requestFocus();
            return;
        }

        if (mob.isEmpty() || mob.length() < 10) {
            mobile.setError("Mobile Number upto 10 digits is required");
            mobile.requestFocus();
            return;
        }



        if ( !Email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            email.setError("Invalid Email");
            email.requestFocus();

            return;
        }


        User user = new User(fname, lname, mob, Email);

        FirebaseUser Firebase_User = mAuth.getCurrentUser();

        if (Firebase_User != null) {
            Userid = Firebase_User.getUid();
        }


        db = FirebaseFirestore.getInstance();

        db.collection("Users").document(Userid).set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            Toast.makeText(Register.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Register.this, MainActivity.class));
                            finish();

                        } else {
                            Toast.makeText(Register.this, "Failed to Register try again", Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }
}