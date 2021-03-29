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

public class Mainmenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Variables
    DrawerLayout drawerLayout ;
    NavigationView navigationView ;
    Toolbar toolbar ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //This line will hide the status bar from the screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mainmenu);

        //Define the hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        //Tell the system that we are going to use the toolbar as our actionBar
        setSupportActionBar(toolbar);
        //Navigation Drawer Menu
        //Hide or show items of Logout and login ...
        Menu menu = navigationView.getMenu();
        checkState(menu);

        navigationView.bringToFront(); // to make the icons clickable !!!
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //To make the menu clickable
        navigationView.setNavigationItemSelectedListener(this);
        //Home screen and button will be selected by default !
        navigationView.setCheckedItem(R.id.nav_home);


    }//End of onCreate Method
    //to avoid closing the application on Back pressed
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId())
        {
            case R.id.nav_home : //because we are already in home section we are going to nadda !
                break;
            case R.id.wishlist :
                Intent intent = new Intent(Mainmenu.this,Wishlist.class);
                startActivity(intent);
                break;
            case R.id.mycart :
                Intent intentOfCart = new Intent(Mainmenu.this,My_cart.class);
                startActivity(intentOfCart);
                break;
            case R.id.nav_location :
                Intent locationIntent = new Intent(getApplicationContext(),CurrentLocation.class);
                startActivity(locationIntent);
                break;
            case R.id.nav_login :
                Intent loginIntent = new Intent(getApplicationContext(),User_admin_deliver.class);
                startActivity(loginIntent);
                break;
            case R.id.nav_share :
                Toast.makeText(this,"Share",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_rate :
                Toast.makeText(this,"Thank you",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout :
                FirebaseAuth.getInstance().signOut();
                Login logout = new Login();
                logout.setToZero();
                Menu menu = navigationView.getMenu();
                checkState(menu);
                startActivity(new Intent(getApplicationContext(),User_admin_deliver.class));
                break;
            case R.id.myCommands :
                startActivity(new Intent(getApplicationContext(),Mes_Commandes.class));
                break;
            case R.id.nav_profile :
                startActivity(new Intent(getApplicationContext(),UserProfile.class));
                break;

        }
        //close the drawer navigation with the home button is pressed !
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void checkState(Menu menu)
    {
        Login login = new Login();
        if(Login.stateOfUser == 1)
        {
            menu.findItem(R.id.nav_login).setVisible(false);
        }
        else
        {
            menu.findItem(R.id.wishlist).setVisible(false);
            menu.findItem(R.id.mycart).setVisible(false);
            menu.findItem(R.id.nav_login).setVisible(true);
            menu.findItem(R.id.nav_location).setVisible(false);
            menu.findItem(R.id.nav_profile).setVisible(false);
            menu.findItem(R.id.nav_logout).setVisible(false);
            menu.findItem(R.id.myCommands).setVisible(false);
            startActivity(new Intent(getApplicationContext(),User_admin_deliver.class));

        }

    }


    public void goToProducts(View view) {
        Intent intent = new Intent(getApplicationContext(),Stores.class);
        intent.putExtra("Category","Electronics");
        startActivity(intent);
    }

}