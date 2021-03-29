package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class deliverLogin extends AppCompatActivity{
    Button fPassword, loginBtn;
    TextInputLayout textEmail, textPassword;

    private FirebaseAuth mAuth;
    private ProgressBar progressBarLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_deliver_login);

        //assign to delivery man available when he logged in

        fPassword = findViewById(R.id.forgetPasswordOfDeliver);
        fPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(deliverLogin.this, ForgetPasswordForDeliver.class);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        textEmail = findViewById(R.id.usernameOfDeliver);
        textPassword = findViewById(R.id.passwordOfDeliver);
        progressBarLog = findViewById(R.id.progressBarOfDeliver);
        loginBtn = findViewById(R.id.loginButtonOfDeliver);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });


    }

    private void userLogin() {
        String email = textEmail.getEditText().getText().toString();
        String password = textPassword.getEditText().getText().toString();

        if (email.isEmpty()) {
            textEmail.setError("Email is required");
            textEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textEmail.setError("Please enter a valid email !");
            textEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            textPassword.setError("Password is required !");
            textPassword.requestFocus();
            return;
        }
        if (password.length() < 4) {
            textPassword.setError("Min password length is 4 characters !");
            textPassword.requestFocus();
            return;
        }
        progressBarLog.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // we have to check if the email has been verified or not
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()) {
                        //redirect to Main Menu
                        //DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference("Delivery Man").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        //currentUser.child("status").setValue("Available");
                        startActivity(new Intent(getApplicationContext(), DeliveryManDashboard.class));
                        finish();
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(deliverLogin.this, "Check your email to verify your account.", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(deliverLogin.this, "Failed to login! Please check your credentials.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    }
