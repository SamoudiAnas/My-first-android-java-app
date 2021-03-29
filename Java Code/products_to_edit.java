package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;




import com.example.application.databinding.ActivityProductsToEditBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

import java.util.List;

public class products_to_edit extends AppCompatActivity implements RecyclerViewOnItemClick{

    ActivityProductsToEditBinding binding;
    DatabaseReference mainProductRef;
    List<MainProduct> myProductList = new ArrayList<MainProduct>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductsToEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getReference();
        readData(new FirebaseCallBack() {
            @Override
            public void getList(List<MainProduct> products) {
                if(products.isEmpty() == true){
                    Toast.makeText(products_to_edit.this,"Your list is empty",Toast.LENGTH_SHORT).show();
                }else{
                    MainAdapter adapter = new MainAdapter(products,products_to_edit.this,products_to_edit.this::onItemClick);
                    binding.recyclerView.setAdapter(adapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(products_to_edit.this);
                    binding.recyclerView.setLayoutManager(layoutManager);
                }
            }
        });
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getApplicationContext(),editArticle.class);
        //SEND INFOS OF EACH PRODUCT TO THE OVERVIEW
        intent.putExtra("Product Position",position);
        startActivity(intent);
    }

    private void readData(FirebaseCallBack firebaseCallBack){
        getReference();
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
                Toast.makeText(products_to_edit.this,"Failed to Load",Toast.LENGTH_SHORT).show();
            }
        };
        mainProductRef.addValueEventListener(productListener);

    }

    private interface FirebaseCallBack{
        void getList(List<MainProduct> products);
    }

    private void getReference(){
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String reference = extras.getString("Reference");
            String store = extras.getString("Store");
            mainProductRef = FirebaseDatabase.getInstance().getReference(reference).child(store);
        }
    }
}