package com.example.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.application.databinding.ActivityWishlistBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Wishlist extends AppCompatActivity implements RecyclerViewOnItemClick{
    ImageView delete;
    ActivityWishlistBinding binding;
    List<MainProduct> myProductList = new ArrayList<MainProduct>();

    //reference to the current user
    DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    //reference to the current user wishlist
    DatabaseReference userWishList = currentUser.child("wishlist");

    //reference to the current user cart
    DatabaseReference userCart = currentUser.child("cart");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWishlistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ValueEventListener productListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<MainProduct> products = new ArrayList<MainProduct>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MainProduct product = snapshot.getValue(MainProduct.class);
                    products.add(product);
                }

                if(products.isEmpty() == true){
                    Toast.makeText(getApplicationContext(),"Your wishlist is empty!",Toast.LENGTH_SHORT).show();
                }else{
                    myProductList.addAll(products);

                    WishlistAdapter adapter = new WishlistAdapter(myProductList,getApplicationContext(),Wishlist.this::onItemClick  );
                    binding.recyclerView.setAdapter(adapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    binding.recyclerView.setLayoutManager(layoutManager);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Failed to Load",Toast.LENGTH_SHORT).show();
            }
        };
        userWishList.addValueEventListener(productListener);



    }

    @Override
    public void onItemClick(int position) {
    }

    public void addAllToCart(View view) {
        //load the wishlist in a list
        List<MainProduct> wishlist = new ArrayList<>();

        readData(new FirebaseCallBack() {
            @Override
            public void getList(List<MainProduct> products) {
                if(products.isEmpty() == true){
                    Toast.makeText(getApplicationContext(),"Your wishlist is empty!",Toast.LENGTH_SHORT).show();
                }else{
                    wishlist.addAll(products);
                    //pass all items to the cart
                    for(MainProduct p: wishlist){
                        userCart.child(p.getpId()).setValue(p);
                    }
                    Toast.makeText(getApplicationContext(),"All items added successfully",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),My_cart.class));
                }
            }
        });








    }

    private void readData(FirebaseCallBack firebaseCallBack){
        ValueEventListener productListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<MainProduct> products = new ArrayList<MainProduct>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MainProduct product = snapshot.getValue(MainProduct.class);
                    products.add(product);
                }

                firebaseCallBack.getList(products);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Failed to Load",Toast.LENGTH_SHORT).show();
            }
        };
        userWishList.addListenerForSingleValueEvent(productListener);

    }

    private interface FirebaseCallBack{
        void getList(List<MainProduct> products);
    }
}