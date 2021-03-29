package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.application.databinding.ActivityDeleteArticleBinding;
import com.example.application.databinding.ActivityProductsToEditBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

import java.util.List;

public class delete_article extends AppCompatActivity implements RecyclerViewOnItemClick{

    ActivityDeleteArticleBinding binding;
    DatabaseReference mainProductRef = FirebaseDatabase.getInstance().getReference("Products").child("Electronics").child("ShoppingMaroc");
    List<MainProduct> myProductList = new ArrayList<MainProduct>();
    List<String> keys = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeleteArticleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        ValueEventListener productListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                // ..
                List<MainProduct> products = new ArrayList<MainProduct>();



                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    MainProduct product = snapshot.getValue(MainProduct.class);
                    products.add(product);

                    String key  = snapshot.getKey();
                    keys.add(key);





//
                }





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Failed to Load",Toast.LENGTH_SHORT).show();
            }
        };
        mainProductRef.addValueEventListener(productListener);






        readData2(new FirebaseCallBack() {
            @Override
            public void getCartList(List<MainProduct> list, List<String> keys) {
                if(list.isEmpty() == true){
                    Toast.makeText(getApplicationContext(),"Your list is empty",Toast.LENGTH_SHORT).show();
                }else{
                    MainAdapter adapter = new MainAdapter(list,getApplicationContext(),delete_article.this::onItemClick);
                    binding.recyclerView.setAdapter(adapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    binding.recyclerView.setLayoutManager(layoutManager);
                }
            }
        });












    }


    @Override
    public void onItemClick(int position) {
        readData(new FirebaseCallBack() {
            @Override
            public void getCartList(List<MainProduct> list, List<String> keys) {
                mainProductRef.child(keys.get(position)).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"Item Removed!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


    }

    private void readData(FirebaseCallBack myCallBack){

        ValueEventListener cartListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<MainProduct> list = new ArrayList<>();
                List<String> keys = new ArrayList<>();

                //get the data in a list
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    MainProduct product = snapshot.getValue(MainProduct.class);
                    list.add(product);
                    String key = snapshot.getKey();
                    keys.add(key);
                }

                //get the data on callback
                myCallBack.getCartList(list,keys);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        mainProductRef.addListenerForSingleValueEvent(cartListener);

    }
    private void readData2(FirebaseCallBack myCallBack){

        ValueEventListener cartListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<MainProduct> list = new ArrayList<>();
                List<String> keys = new ArrayList<>();

                //get the data in a list
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    MainProduct product = snapshot.getValue(MainProduct.class);
                    list.add(product);
                    String key = snapshot.getKey();
                    keys.add(key);
                }

                //get the data on callback
                myCallBack.getCartList(list,keys);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        mainProductRef.addValueEventListener(cartListener);

    }

    private interface FirebaseCallBack{
        void getCartList(List<MainProduct> list,List<String> keys);
    }
}