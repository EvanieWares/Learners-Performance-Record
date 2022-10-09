package com.evanie.lprmaker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    Button btnResetPassword;
    FirebaseAuth mAuth;
    TextInputEditText etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.etResetEmail);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        btnResetPassword.setOnClickListener(view ->{
            if (Utils.isNetworkAvailable(ForgotPassword.this)) {
                String email = etEmail.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    etEmail.setError("Email cannot be empty");
                    etEmail.requestFocus();
                } else {
                    mAuth.sendPasswordResetEmail(email).addOnSuccessListener(unused -> {
                        Toast.makeText(ForgotPassword.this, "Reset code sent!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgotPassword.this, LoginActivity.class));
                    }).addOnFailureListener(e -> Toast.makeText(ForgotPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }else {
                Toast.makeText(ForgotPassword.this, "Network is not available", Toast.LENGTH_SHORT).show();
            }
        });
    }
}