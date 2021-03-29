package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpOfAdmin extends AppCompatActivity implements View.OnClickListener{
    Button registerBtn;
    TextInputLayout fullName, username, email, phone, password;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_of_admin);

        mAuth = FirebaseAuth.getInstance();

        /*****************************Hooks *****************************************/
        registerBtn = findViewById(R.id.reg_btnOfAdmin);
        progressBar = findViewById(R.id.progressBarOfsuAdmin);

        fullName = findViewById(R.id.reg_nameOfAdmin);
        username = findViewById(R.id.reg_usernameOfAdmin);
        email = findViewById(R.id.reg_emailOfAdmin);
        phone = findViewById(R.id.reg_phoneNumberOfAdmin);
        password = findViewById(R.id.reg_passwordOfAdmin);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });


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
        }
        if (userName.isEmpty()) {
            username.setError("Username is required");
            username.requestFocus();
            return;
        }
        if (mail.isEmpty()) {
            email.setError("email is required");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches())
        {
            email.setError("Please provide valid email");
            email.requestFocus();
            return ;
        }
        if (phoneNumber.isEmpty()) {
            phone.setError("Phone Number is required");
            phone.requestFocus();
            return;
        }
        if (pass.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
            return;
        }
        if(pass.length() < 4)
        {
            password.setError("min password length should be 4 characters !");
            password.requestFocus();
            return ;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(mail,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            UserHelperClass helperClass = new UserHelperClass(fName,userName,mail,phoneNumber,pass);
                            FirebaseDatabase.getInstance().getReference("Admins")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(helperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(SignUpOfAdmin.this,"User has been registered successfully",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);

                                        //redirect to login layout
                                        startActivity(new Intent(getApplicationContext(),admin_dashboard.class));
                                    }
                                    else
                                    {
                                        Toast.makeText(SignUpOfAdmin.this,"Failed to register !Try again",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(SignUpOfAdmin.this,"Failed to register user !Try again",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
    }
}
