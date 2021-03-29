package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DeliveryManDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout ;
    NavigationView navigationView ;
    Toolbar toolbar ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_delivery_man_dashboard);



        //Define the hooks
        drawerLayout = findViewById(R.id.deliveryManDrawerLayout);
        navigationView = findViewById(R.id.DeliveryMan_nav_view);
        toolbar = findViewById(R.id.DeliveryManToolbar);

        //Tell the system that we are going to use the toolbar as our actionBar
        setSupportActionBar(toolbar);
        navigationView.bringToFront(); // to make the icons clickable !!!
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //To make the menu clickable
        navigationView.setNavigationItemSelectedListener(this);
        //Home screen and button will be selected by default !
        navigationView.setCheckedItem(R.id.deliveryHome);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId())
        {
            case R.id.deliveryHome : //because we are already in home section we are going to nadda !
                break;
            case R.id.toDeliver :
                startActivity(new Intent(getApplicationContext(),ToDeliver.class));
                break;
            case R.id.history :
                Intent intentOfCart = new Intent(getApplicationContext(),HistoryOfDeliveredOrders.class);
                startActivity(intentOfCart);
                break;
            case R.id.profileOfDeliveryMan :
               Intent locationIntent = new Intent(getApplicationContext(),DeliveryManProfile.class);
               startActivity(locationIntent);
                break;
            case R.id.location :
                Intent loginIntent = new Intent(getApplicationContext(),DetectLocationOfDeliveryMan.class);
                startActivity(loginIntent);
                break;
            case R.id.deliverymanLogout :
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),User_admin_deliver.class));
                break;
            case R.id.problem :
                startActivity(new Intent(getApplicationContext(),Problem.class));
                break;

        }
        //close the drawer navigation with the home button is pressed !
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void goToToDeliver(View view) {
        startActivity(new Intent(getApplicationContext(),ToDeliver.class));
    }

    public void myProfile(View view) {
        startActivity(new Intent(getApplicationContext(),DeliveryManProfile.class));
    }

    public void goToGoogleTracking(View view) {
        startActivity(new Intent(getApplicationContext(),GoogleTracking.class));

    }

    public void goToHistory(View view) {
        startActivity(new Intent(getApplicationContext(),HistoryOfDeliveredOrders.class));
    }
}