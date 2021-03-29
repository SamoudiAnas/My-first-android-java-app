package com.example.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class User_admin_deliver extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_admin_deliver);
    }

    public void toAdminLogin(View view) {
        startActivity(new Intent(getApplicationContext(),adminLogin.class));
    }

    public void toUserLogin(View view) {
        startActivity(new Intent(getApplicationContext(),Login.class));
    }

    public void toDeliveryLogin(View view) {
        startActivity(new Intent(getApplicationContext(),deliverLogin.class));
    }
}