package com.example.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Problem extends AppCompatActivity {
    EditText problem ;
    Button accident , fuel , money , submit;
    String sProblem ;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_problem);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Delivery Man");
        userID = user.getUid();


        accident = findViewById(R.id.accident);
        accident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sProblem = "Accident";
                report(sProblem);
            }
        });
        fuel = findViewById(R.id.fuel);
        fuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sProblem = "Run out of fuel";
                report(sProblem);
            }
        });
        money = findViewById(R.id.money);
        money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sProblem = "Run out of money";
                report(sProblem);
            }
        });
        problem = findViewById(R.id.other);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sProblem = problem.getText().toString().trim();
                report(sProblem);
            }
        });



    }
    private void report(String str)
    {
        //reference.child(userID).child("status").setValue(str);
        Toast.makeText(getApplicationContext(),"Your problem"+userID,Toast.LENGTH_LONG).show();
        //startActivity(new Intent(getApplicationContext(),DeliveryManDashboard.class));
    }
}