package com.example.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetectionDone_forDeleveryMan extends AppCompatActivity {
    TextView textView;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection_of_location_done);

        textView = findViewById(R.id.locationTextOfDeliveryMan);
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            String location = extras.getString("Location");
            textView.setText(location);
        }
        next = findViewById(R.id.nextButtonOfDeliveryMan);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),DeliveryManDashboard.class));
            }
        });
    }
}