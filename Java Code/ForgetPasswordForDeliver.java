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
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordForDeliver extends AppCompatActivity {
    TextInputLayout emailAddress ;
    ProgressBar progressBar ;
    Button send;

    FirebaseAuth auth ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget_password_for_deliver);

        emailAddress = findViewById(R.id.emailAddressDeliver);
        progressBar = findViewById(R.id.forgetPasswordOfDeliver);
        send = findViewById(R.id.verification_buttonOfDeliver);

        auth = FirebaseAuth.getInstance();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = emailAddress.getEditText().getText().toString();

        if(email.isEmpty())
        {
            emailAddress.setError("Email is required !");
            emailAddress.requestFocus();
            return ;

        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailAddress.setError("Please provide valid email");
            emailAddress.requestFocus();
            return ;
        }
        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(ForgetPasswordForDeliver.this,"Check your email to reset password !",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(getApplicationContext(),deliverLogin.class));
                }
                else
                {
                    Toast.makeText(ForgetPasswordForDeliver.this, "Try again ! Something wrong happened.", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}