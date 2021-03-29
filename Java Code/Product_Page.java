package com.example.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.application.databinding.ActivityProductPageBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Product_Page extends AppCompatActivity implements RecyclerViewOnItemClick{

    ActivityProductPageBinding binding;
    DatabaseReference mainProductRef;
    List<MainProduct> myProductList = new ArrayList<MainProduct>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setReference();

        ValueEventListener productListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                // ..
                List<MainProduct> products = new ArrayList<MainProduct>();



                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    MainProduct product = snapshot.getValue(MainProduct.class);
                    products.add(product);

                }
                if(products.isEmpty() == true){
                    Toast.makeText(Product_Page.this,"No Product is available for sale!",Toast.LENGTH_SHORT).show();
                }else{
                    MainAdapter adapter = new MainAdapter(products,Product_Page.this,Product_Page.this::onItemClick    );
                    binding.recyclerView.setAdapter(adapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(Product_Page.this);
                    binding.recyclerView.setLayoutManager(layoutManager);

                }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Product_Page.this,"Failed to Load",Toast.LENGTH_SHORT).show();
            }
        };
        mainProductRef.addValueEventListener(productListener);




















    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(Product_Page.this,Product_overview.class);

        //SEND INFOS OF EACH PRODUCT TO THE OVERVIEW
        intent.putExtra("Category",getCategory());
        intent.putExtra("Store",getStore());
        intent.putExtra("Product Position",position);
        startActivity(intent);
    }

    private String getCategory(){
        Bundle extras = getIntent().getExtras();
        String category="";
        if(extras != null){
            category = extras.getString("Category");
        }
        return category;
    }
    private String getStore(){
        Bundle extras = getIntent().getExtras();
        String store="";
        if(extras != null){
            store = extras.getString("Store");
        }
        return store;
    }
    private void setReference(){
        Bundle extras = getIntent().getExtras();
        String category,store;
        if(extras != null){
            category = extras.getString("Category");
            store = extras.getString("Store");
            mainProductRef = FirebaseDatabase.getInstance().getReference("Products").child(category).child(store);
        }
    }

}