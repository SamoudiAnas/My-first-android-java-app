package com.example.application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;


import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.SignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GithubAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;


public class Login extends AppCompatActivity implements View.OnClickListener {
    Button callSignUp, fPassword, loginBtn;
    TextInputLayout textEmail, textPassword;


    private CallbackManager callbackManager;



    private FirebaseAuth mAuth;
    private ProgressBar progressBarLog;
    private Button login_btn;
    static int stateOfUser = 0;


    //Initialize variables
    Button signInButton ;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        callSignUp = findViewById(R.id.signup_screen);
        fPassword = findViewById(R.id.forgetPassword);
        fPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, ForgetPassword.class);
                startActivity(intent);
                finish();
            }
        });
        callSignUp.setOnClickListener(this);


        login_btn = findViewById(R.id.login_button);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackManager = CallbackManager.Factory.create();

                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                Log.d("Demo", "Login Successful!");
                            }

                            @Override
                            public void onCancel() {
                                // App code
                                Log.d("Demo", "Login Cancelled!");
                            }

                            @Override
                            public void onError(FacebookException exception) {
                                // App code
                                Log.d("Demo", "Login Error!");
                            }
                        });
            }
        });
        textEmail = findViewById(R.id.username);
        textPassword = findViewById(R.id.password);
        progressBarLog = findViewById(R.id.progressBar2);
        mAuth = FirebaseAuth.getInstance();
        loginBtn = findViewById(R.id.loginButton);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

        signInButton = findViewById(R.id.continueWithGoogle);

        //initialize sign in options
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("375418938423-t4ar9s8fbuasn74g5qja5q4d8gptf30t.apps.googleusercontent.com")
                .requestEmail().build();

        //initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(Login.this, googleSignInOptions);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize sign in intent
                Intent intent = googleSignInClient.getSignInIntent();
                //Start activity for result
                startActivityForResult(intent, 100);

            }
        });
        //initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance();


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //check condition
        if(requestCode == 100)
        {
            //initialize task
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            //check condition
            if (signInAccountTask.isSuccessful())
            {
                String s = "Google sign in successfully !";
                //Display Toast
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                //initialize sign in account
                try {
                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
                    //check condition
                    if (googleSignInAccount != null)
                    {
                        //when sign in is not equal to null
                        //initialize auth credential
                        AuthCredential authCredential =
                        GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(),null);
                        //check credentials
                        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //check condition
                                if (task.isSuccessful())
                                {
                                    //Redirect to main menu page
                                    startActivity(new Intent(getApplicationContext(),Mainmenu.class));

                                }
                            }
                        });


                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }


            }

        }
    }

    private void userLogin() {


        String email = textEmail.getEditText().getText().toString();
        String password = textPassword.getEditText().getText().toString();

        if (email.isEmpty()) {
            textEmail.setError("Email is required");
            textEmail.requestFocus();
            return;
        }
        else
        {
            textEmail.setErrorEnabled(false);
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textEmail.setError("Please enter a valid email !");
            textEmail.requestFocus();
            return;
        }
        else
        {
            textEmail.setErrorEnabled(false);
        }

        if (password.isEmpty()) {
            textPassword.setError("Password is required !");
            textPassword.requestFocus();
            return;
        }
        else
        {
            textPassword.setErrorEnabled(false);
        }
        if (password.length() < 4) {
            textPassword.setError("Min password length is 4 characters !");
            textPassword.requestFocus();
            return;
        }
        else
        {
            textPassword.setErrorEnabled(false);
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
                        stateOfUser = 1;
                        startActivity(new Intent(getApplicationContext(), Mainmenu.class));
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(getApplicationContext(),"Check your email to verify your account !",Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(Login.this, "Failed to login! Please check your credentials.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup_screen:
                startActivity(new Intent(getApplicationContext(), SignUp.class));
                break;
        }
    }

    public void setToZero() {
        stateOfUser = 0;
    }
}