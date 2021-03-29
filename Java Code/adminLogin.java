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

public class adminLogin extends AppCompatActivity {
    Button  fPassword, loginBtn,toSignUp;
    TextInputLayout textEmail, textPassword;

    private FirebaseAuth mAuth;
    private ProgressBar progressBarLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_login);

        fPassword = findViewById(R.id.forgetPasswordOfAdmin);
        fPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminLogin.this, ForgetPassword.class);
                startActivity(intent);
                finish();
            }
        });

        textEmail = findViewById(R.id.usernameOfAdmin);
        textPassword = findViewById(R.id.passwordOfAdmin);
        progressBarLog = findViewById(R.id.progressBarOfAdmin);
        mAuth = FirebaseAuth.getInstance();
        loginBtn = findViewById(R.id.loginButtonOfAdmin);
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
                        startActivity(new Intent(getApplicationContext(), admin_dashboard.class));
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(adminLogin.this, "Check your email to verify your account.", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(adminLogin.this, "Failed to login! Please check your credentials.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
