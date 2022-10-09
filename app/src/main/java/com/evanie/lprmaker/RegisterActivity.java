package com.evanie.lprmaker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button register;
    TextView tvLogin;
    TextInputEditText regEmail;
    TextInputEditText regPassword;
    TextInputEditText regConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        register = findViewById(R.id.btnRegister);
        regEmail = findViewById(R.id.etRegEmail);
        regPassword = findViewById(R.id.etRegPassword);
        regConfirmPassword = findViewById(R.id.etConfirmRegPassword);
        tvLogin = findViewById(R.id.tvLogin);

        register.setOnClickListener(view ->{
            if (Utils.isNetworkAvailable(RegisterActivity.this)) {
                createUser();
            }else {
                Toast.makeText(RegisterActivity.this, "Network is not available", Toast.LENGTH_SHORT).show();
            }
        });

        tvLogin.setOnClickListener(view -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
    }

    private void createUser() {
        String email = regEmail.getText().toString();
        String password = regPassword.getText().toString();
        String confirmPassword = regConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            regEmail.setError("Email cannot be empty");
            regEmail.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            regPassword.setError("Password cannot be empty");
            regPassword.requestFocus();
        }else if (TextUtils.isEmpty(confirmPassword)){
            regConfirmPassword.setError("This field cannot be empty");
            regConfirmPassword.requestFocus();
        }else {
            if (password.equals(confirmPassword)){
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        user.sendEmailVerification();
                        Toast.makeText(RegisterActivity.this, "Verification email has been sent", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    }else {
                        Toast.makeText(RegisterActivity.this, "Registration error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                regConfirmPassword.setError("Passwords don't match");
                regConfirmPassword.requestFocus();
            }
        }
    }
}