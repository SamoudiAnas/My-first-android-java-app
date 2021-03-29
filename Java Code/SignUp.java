package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    Button registerBtn, toLogin;
    TextInputLayout fullName, username, email, phone, password;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        /*****************************Hooks *****************************************/
        registerBtn = findViewById(R.id.reg_btn);
        toLogin = findViewById(R.id.tologact_btn);
        toLogin.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar3);

        fullName = findViewById(R.id.reg_name);
        username = findViewById(R.id.reg_username);
        email = findViewById(R.id.reg_email);
        phone = findViewById(R.id.reg_phoneNumber);
        password = findViewById(R.id.reg_password);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });


    }//Ends of onCreate Method

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tologact_btn:
                startActivity(new Intent(this, Login.class));
                break;
        }
    }


    private void registerUser() {
        String fName = fullName.getEditText().getText().toString();
        String userName = username.getEditText().getText().toString();
        String mail = email.getEditText().getText().toString();
        String phoneNumber = phone.getEditText().getText().toString();
        String pass = password.getEditText().getText().toString();

        if (fName.isEmpty()) {
            fullName.setError("Full Name is required");
            fullName.requestFocus();
            return;
        } else {
            fullName.setError(null);
            fullName.setErrorEnabled(false);
        }
        if (userName.isEmpty()) {
            username.setError("Username is required");
            username.requestFocus();
            return;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);

        }
        if (mail.isEmpty()) {
            email.setError("email is required");
            email.requestFocus();
            return;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            email.setError("Please provide valid email");
            email.requestFocus();
            return;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
        }
        if (phoneNumber.isEmpty()) {
            phone.setError("Phone Number is required");
            phone.requestFocus();
            return;
        } else {
            phone.setError(null);
            phone.setErrorEnabled(false);
        }
        if (pass.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
            return;
        }
        if (pass.length() < 4) {
            password.setError("min password length should be 4 characters !");
            password.requestFocus();
            return;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            List<MainProduct> wishlist = new ArrayList<>();
                            List<MainProduct> cart = new ArrayList<>();

                            List<MainProduct> commands = new ArrayList<>();

                            //My code
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            UserHelperClass helperClass = new UserHelperClass(uid,fName, userName, mail, phoneNumber, pass, wishlist, cart, commands);


                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(helperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(),"User has been registered successfully !",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);

                                        //redirect to login layout
                                        startActivity(new Intent(getApplicationContext(), ChooseProfileImage.class));
                                    } else {
                                        Toast.makeText(getApplicationContext(),"Failed to register User,it may exist already !",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(),"registration process has failed !",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    }
                });
    }
}