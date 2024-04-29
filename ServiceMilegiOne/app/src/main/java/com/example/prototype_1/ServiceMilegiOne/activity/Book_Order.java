package com.example.prototype_1.ServiceMilegiOne.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.type.DateTime;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Book_Order extends AppCompatActivity {


    private EditText first, last, mobile, email , address;
    private  String first_name , last_name , mob, user_address;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseFirestore db;
    private String Userid;
    private String booking;
    private DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__order);
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

        Intent intent = getIntent();
        booking = intent.getStringExtra("job");

        documentReference = db.collection("Users").document(Userid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                first_name = value.getString("first_name");
                last_name = value.getString("last_name");
                mob = value.getString("mob_no");
                user_address = value.getString("address");

                first.setText(first_name);
                last.setText(last_name);
                mobile.setText(mob);
                email.setText(value.getString("email"));
                address.setText(user_address);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdaterUser();
//                Book_Now();
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
                Log.d("Edit","updated");
                Book_Now();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Edit","Not updated");
                Toast.makeText(Book_Order.this ,"Booking Failed" , Toast.LENGTH_LONG).show();
                startActivity(new Intent(Book_Order.this , MainActivity.class));
                finish();
            }
        });


    }

    private void Book_Now() {
        Calendar calendar = Calendar.getInstance();
//        Long tsLong = System.currentTimeMillis()/1000;
//        String ts = tsLong.toString();
        Boolean flag = false;
        Map<String,Object> mp = new HashMap<>();
        mp.put("is_complete" , flag);
        mp.put("job" , booking);
        mp.put("messageTime", calendar.getTime());
        mp.put("first_name" , first_name );
        mp.put("last_name" , last_name);
        mp.put("address" , user_address);
        mp.put("mobile" , mob);
        mp.put("user_id" , Userid);


//        mp.put("cancel" , false);

        db.collection("Users").document(Userid).collection("Orders").add(mp)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(Book_Order.this ,"Booking Successful\nWe will reach out to you shortly. " , Toast.LENGTH_LONG).show();


                        }
                        else{
                            Toast.makeText(Book_Order.this ,"Booking Failed" , Toast.LENGTH_LONG).show();
                        }
                        startActivity(new Intent(Book_Order.this , MainActivity.class));
                        finish();
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