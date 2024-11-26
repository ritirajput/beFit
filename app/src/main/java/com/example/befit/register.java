package com.example.befit;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class register extends AppCompatActivity {
    EditText emailEditText, passwordEditText, confirmPasswordEditText;
    Button registerButton, loginLink;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        emailEditText = findViewById(R.id.emailRegister);
        passwordEditText = findViewById(R.id.passwordRegister);
        confirmPasswordEditText = findViewById(R.id.confirmPassword);
        registerButton = findViewById(R.id.registerbtn);
        loginLink = findViewById(R.id.loginLink);
        mAuth = FirebaseAuth.getInstance();
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(register.this, login.class);
                startActivity(intent);
                finish();
            }
        });




        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!= null) {
            //already logged in, start the main activity
            Intent intent = new Intent(register.this, login.class);
            startActivity(intent);
            finish();
        }

    }

    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String cPass = confirmPasswordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(cPass)) {
            Toast.makeText(register.this,"All fields are required",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Invalid Email");
            emailEditText.requestFocus();
            return;
        }

        if (password.length() < 8) {
            passwordEditText.setError("Password must be at least 8 characters long");
            passwordEditText.requestFocus();
            return;
        }
        if (!password.equals(cPass)) {
            confirmPasswordEditText.setError("Passwords do not match");
            confirmPasswordEditText.requestFocus();
            return;
        }
        //if everything is fine, start the registration process
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this , task -> {
            if (task.isSuccessful()) {
                Toast.makeText(register.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                //start the main activity
                Intent intent = new Intent(register.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else{
                Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
            }
        });

    }



}
