package com.example.prototype_1.ServiceMilegiOne.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.prototype_1.ServiceMilegiOne.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class Update extends AppCompatActivity {

    private EditText first, last, mobile, email , address;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseFirestore db;
    private String Userid;
    private  DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Button edit = findViewById(R.id.edit);

        first = findViewById(R.id.reg_first_name);
        last = findViewById(R.id.reg_last_name);
        mobile = findViewById(R.id.reg_mob);
        email = findViewById(R.id.reg_email);
        address = findViewById(R.id.reg_address);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        Userid = mFirebaseUser.getUid();

        documentReference = db.collection("Users").document(Userid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                first.setText(value.getString("first_name"));
                last.setText(value.getString("last_name"));
                mobile.setText(value.getString("mob_no"));
                email.setText(value.getString("email"));
                address.setText(value.getString("address"));
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Are you sure ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                UpdaterUser();
                            }
                        }).setNegativeButton("No", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void UpdaterUser() {

        final String fname = first.getText().toString().trim();
        final String lname = last.getText().toString().trim();
        final String mob = mobile.getText().toString();
        final String Email = email.getText().toString().trim();
        final String Address = address.getText().toString().trim();


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

        if (Address.isEmpty()) {
            address.setError("Address is required");
            address.requestFocus();
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("first_name", fname);
        map.put("last_name", lname);
        map.put("mob_no", mob);
        map.put("email", Email);
        map.put("address", Address);


        documentReference.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Update.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Edit","Not updated");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}