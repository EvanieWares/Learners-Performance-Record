package com.evanie.lprmaker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    TextView tvRegister;
    TextView tvSkipLogin;
    TextView tvForgotPassword;
    TextInputEditText loginEmail;
    TextInputEditText loginPassword;
    SharedPreferences preferences;
    Button login;
    FirebaseAuth mAuth;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvRegister = findViewById(R.id.tvRegister);
        tvSkipLogin = findViewById(R.id.tvSkipLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        login = findViewById(R.id.btnLogin);
        loginEmail = findViewById(R.id.etLoginEmail);
        loginPassword = findViewById(R.id.etLoginPassword);
        mAuth = FirebaseAuth.getInstance();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        login.setOnClickListener(view ->{
            if(Utils.isNetworkAvailable(LoginActivity.this)){
                loginUser();
            }else {
                Toast.makeText(LoginActivity.this, "Network is not available", Toast.LENGTH_SHORT).show();
            }
        });

        tvForgotPassword.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, ForgotPassword.class)));
        tvRegister.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
        tvSkipLogin.setOnClickListener(view -> {
            preferences.edit().putString(Utils.keyAppMode, Utils.valueOffline).apply();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        });

    }

    private void loginUser() {
        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            loginEmail.setError("Email cannot be empty");
            loginEmail.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            loginPassword.setError("Password cannot be empty");
            loginPassword.requestFocus();
        }else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    assert user != null;
                    if (user.isEmailVerified()) {
                        preferences.edit().putString(Utils.keyAppMode, Utils.valueLoggedIn).apply();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else {
                        Toast.makeText(LoginActivity.this, "Your email is not verified. Check your mailbox", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "Login error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce){
            finishAffinity();
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();
        new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

}