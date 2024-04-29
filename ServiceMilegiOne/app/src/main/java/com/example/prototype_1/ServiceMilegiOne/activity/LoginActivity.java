package com.example.prototype_1.ServiceMilegiOne.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prototype_1.ServiceMilegiOne.R;
import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(LoginActivity.this , MainActivity.class));
            finish();
        }

        CountryCodePicker countryCodePicker = findViewById(R.id.ccp);
        number = findViewById(R.id.MOBILE);
        countryCodePicker.registerCarrierNumberEditText(number);

        Button generate = findViewById(R.id.generate);

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( countryCodePicker.getFullNumber().length() < 10 || countryCodePicker.getFullNumber().isEmpty() ){
                    number.setError("Invalid number");
                    number.requestFocus();
                    return;
                }

                Intent intent = new Intent(LoginActivity.this , Verify.class);
                intent.putExtra("Mobile" , countryCodePicker.getFullNumberWithPlus().trim().replace(" " ,""));
                startActivity(intent);

                finish();

            }
        });





    }
}
