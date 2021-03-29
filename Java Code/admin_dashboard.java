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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.application.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class admin_dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    TextView adminName ;
    private FirebaseUser user ;
    private DatabaseReference reference ;

    private String userID;

    DrawerLayout drawerLayout ;
    NavigationView navigationView ;
    Toolbar toolbar ;
    CircleImageView profileImage ;
    DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference("Admins")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_dashboard);

        //Define the hooks
        drawerLayout = findViewById(R.id.adminDrawer_layout);
        navigationView = findViewById(R.id.adminnav_view);
        toolbar = findViewById(R.id.admintoolbar);
        setSupportActionBar(toolbar);

        navigationView.bringToFront(); // to make the icons clickable !!!
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //To make the menu clickable
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        //Home screen and button will be selected by default !
        navigationView.setCheckedItem(R.id.adminnav_home);

        adminName = findViewById(R.id.admin_name);
       user = FirebaseAuth.getInstance().getCurrentUser();
       reference = FirebaseDatabase.getInstance().getReference("Admins");
       userID = user.getUid();
        profileImage = findViewById(R.id.profileImageAdmin);



//        readUserData(new FirebaseUserCallBack() {
//            @Override
//            public void getUser(UserHelperClass user) {
//                String admin_name = user.getName();
//                adminName.setText(admin_name);
//                Glide.with(getApplicationContext()).load(user.getProfilePicture()).into(profileImage);
//            }
//        });



//        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                UserHelperClass userProfile =snapshot.getValue(UserHelperClass.class);
//                if(snapshot != null)
//                {
//                    String admin_name = userProfile.name;
//                    adminName.setText(admin_name);
//                    String profilePicture = userProfile.getProfilePicture();
//                    Glide.with(getApplicationContext()).load(profilePicture).into(profileImage);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(admin_dashboard.this,"Something wrong happened !",Toast.LENGTH_LONG).show();
//            }
//        });
    }
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
            case R.id.adminnav_home: //because we are already in home section we are going to do nadda !
                break;
            case R.id.addNewAdmin :
                startActivity(new Intent(getApplicationContext(),SignUpOfAdmin.class));
                break;
            case R.id.addNewDeliveryMan:
                startActivity(new Intent(getApplicationContext(),NewDileveryMan.class));
                break;
            case R.id.myProfile :
                startActivity(new Intent(getApplicationContext(),AdminProfile.class));
                break;
            case R.id.manageArticles :
                startActivity(new Intent(getApplicationContext(),manage_articles.class));
                break;
            case R.id.logout :
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),User_admin_deliver.class));
                break;
            case R.id.todayPurchases :
                startActivity(new Intent(getApplicationContext(),Purchases.class));
                break;
            case R.id.dileveryMan :
                startActivity(new Intent(getApplicationContext(),DeliveryMan.class));
                break;
            case R.id.states :
                startActivity(new Intent(getApplicationContext(),stats.class));
                break;


        }
        //close the drawer navigation with the home button is pressed !
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }



    public void goToDeliveryMan(View view) {
        startActivity(new Intent(getApplicationContext(),DeliveryMan.class));
    }

    public void goToTodayPurchases(View view) {
        startActivity(new Intent(getApplicationContext(),Purchases.class));
    }

    public void manageArticles(View view) {
        startActivity(new Intent(getApplicationContext(),manage_articles.class));
    }

    public void gotoStats(View view) {
        startActivity(new Intent(getApplicationContext(),stats.class));
    }
//    public void readUserData(FirebaseUserCallBack userData){
//        ValueEventListener userListner = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                String name = dataSnapshot.child("name").getValue().toString();
//                String pp = dataSnapshot.child("profilePicture").getValue().toString();
//
//                UserHelperClass user = dataSnapshot.getValue(UserHelperClass.class);
//                //get the data on callback
//                userData.getUser(user);
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {}
//        };
//        currentUser.addListenerForSingleValueEvent(userListner);
//
//    }
//    private interface FirebaseUserCallBack{
//        void getUser(UserHelperClass user);
//    }
}