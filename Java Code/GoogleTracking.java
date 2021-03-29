package com.example.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GoogleTracking extends AppCompatActivity {
    //Initialize variables
    EditText etSource,etDestination;
    Button btTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_google_tracking);

        //Assign Variables
        etSource = findViewById(R.id.et_source);
        etDestination = findViewById(R.id.et_destination);
        btTrack = findViewById(R.id.bt_track);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String clientAddress = extras.getString("Client Address");
            String dManAddress = extras.getString("Delivery Man Address");
            etSource.setText(clientAddress);
            etDestination.setText(dManAddress);
        }

        btTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get value from Edit Text
                String sSource = etSource.getText().toString();
                String sDestination = etDestination.getText().toString();

                //Check condition
                if(sSource.equals("") && sDestination.equals(""))
                {
                    //When both value blank
                    Toast.makeText(getApplicationContext(),"Enter both location",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //When both value fill
                    //Display track
                    DisplayTrack(sSource,sDestination);
                }
            }
        });
    }

    private void DisplayTrack(String sSource, String sDestination) {
        //if the device does not have a map installed, then redirect to play store
        try {
            //when google map is installed
            //Initialize Uri
            Uri uri = Uri.parse("https://www.google.co.in/maps/dir/" + sSource + "/" + sDestination );
            //Initialize intent with action view
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            //Set package
            intent.setPackage("com.google.android.apps.maps");
            //Set flag
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //Start activity
            startActivity(intent);
        }catch (ActivityNotFoundException e)
        {
            //When google maps is not installed
            //Initialize uri
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            //Initialize intent with action view
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            //Set flag
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //Start activity
            startActivity(intent);
        }
    }
}