package com.example.prototype_1.ServiceMilegiOne.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prototype_1.ServiceMilegiOne.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Verify extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText otp;
    private Button verify;
    private String mobile_number;
    private String otp_id;
    private ProgressBar progressBar;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        mAuth = FirebaseAuth.getInstance();

        otp = findViewById(R.id.otp);
        verify = findViewById(R.id.verify);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        mobile_number = getIntent().getStringExtra("Mobile");


        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks
                = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                signInWithPhoneAuthCredential(phoneAuthCredential);


            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                Toast.makeText(getApplicationContext(), "Verification failed try again", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                progressBar.setVisibility(View.INVISIBLE);
                otp_id = s;
            }
        };


        validate_otp(mCallbacks); //Validate otp;


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = otp.getText().toString();
                if (code.isEmpty() || code.length() < 6) {
                    otp.setError("Invalid OTP");
                    otp.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.INVISIBLE);
                verify_code(code);
            }
        });


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    }


    private void validate_otp(PhoneAuthProvider.OnVerificationStateChangedCallbacks Callbacks) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(mobile_number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(Callbacks)          // OnVerificationStateChangedCallbacks
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            check_login_firstTime();

                        } else {

                            Toast.makeText(getApplicationContext(), "Failed to SignIn", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    private void verify_code(String user_code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otp_id, user_code);
        signInWithPhoneAuthCredential(credential);

    }


    private void check_login_firstTime() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();


        if (firebaseUser != null) {
            user_id = firebaseUser.getUid();
        }

        db = FirebaseFirestore.getInstance();
        final DocumentReference userRef = db.collection("Users").document(user_id);

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                progressBar.setVisibility(View.INVISIBLE);
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document != null && document.exists()) {

                        startActivity(new Intent(Verify.this, MainActivity.class));
                    } else {
                        startActivity(new Intent(Verify.this, Register.class));

                    }
                    finish();

                } else {

                    Log.d("Failed with: ", Objects.requireNonNull(task.getException()).toString());
                }

            }
        });

    }
}