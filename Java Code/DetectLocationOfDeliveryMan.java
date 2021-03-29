package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.SettingInjectorService;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DetectLocationOfDeliveryMan extends AppCompatActivity {
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private TextView textView1,textView2;
    private ProgressBar progressBar;
    private ResultReceiver resultReceiver;
    //reference to the current user
    DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference("Delivery Man")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_current_location);

        resultReceiver = new AddressResultReceiver(new Handler());
        textView1=findViewById(R.id.textView11);
        textView2=findViewById(R.id.textView12);
        progressBar = findViewById(R.id.progressBarOfCurrentLocationDetection);

        findViewById(R.id.buttonL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DetectLocationOfDeliveryMan.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE_LOCATION_PERMISSION);
                }
                else
                {
                    getCurrentLocation();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                getCurrentLocation();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Permission Denied !",Toast.LENGTH_LONG).show();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        progressBar.setVisibility(View.VISIBLE);
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(DetectLocationOfDeliveryMan.this).requestLocationUpdates(locationRequest,new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(DetectLocationOfDeliveryMan.this).removeLocationUpdates(this);
                if(locationRequest != null && locationResult.getLocations().size() > 0)
                {
                    int latestLocationIndex = locationResult.getLocations().size() - 1 ;
                    double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                    double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                    textView1.setText(
                            String.format("Latitude : %s\nLongitude : %s",latitude,longitude)
                    );
                    Location location = new Location("providerNA");
                    location.setLatitude(latitude);
                    location.setLongitude(longitude);
                    fetchAddressFromLatLong(location);
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                }

            }
        },Looper.getMainLooper());

    }
    private void fetchAddressFromLatLong(Location location)
    {
        Intent intent = new Intent(this,fetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER,resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA,location);
        startService(intent);
    }
    private class AddressResultReceiver extends ResultReceiver
    {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if(resultCode == Constants.SUCCESS_RESULT)
            {
                textView2.setText(resultData.getString(Constants.RESULT_DATA_KEY));






                currentUser.child("address").setValue(resultData.getString(Constants.RESULT_DATA_KEY)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"Location Added to your credentials",Toast.LENGTH_SHORT).show();
                    }
                });

                Intent intent = new Intent(getApplicationContext(),DetectionOfLocationDone.class);
                intent.putExtra("Location",resultData.getString(Constants.RESULT_DATA_KEY));
                startActivity(intent);

            }
            else
            {
                Toast.makeText(getApplicationContext(),resultData.getString(Constants.RESULT_DATA_KEY),Toast.LENGTH_LONG).show();
            }
            progressBar.setVisibility(View.GONE);
        }

    }

}