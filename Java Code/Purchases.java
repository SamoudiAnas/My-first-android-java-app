package com.example.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.application.databinding.ActivityPurchasesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class Purchases extends AppCompatActivity implements RecyclerViewOnItemClick{

    ActivityPurchasesBinding binding;



    //reference to the clients
    DatabaseReference clients = FirebaseDatabase.getInstance().getReference("Clients").child("On Process");

    //reference to the delivery man
    DatabaseReference deliveryMan = FirebaseDatabase.getInstance().getReference("Delivery Men");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPurchasesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        readData(new FirebaseData() {
            @Override
            public void getListOfClients(List<Client> list, List<String> keys) {
                if(list.isEmpty() == true){
                    Toast.makeText(getApplicationContext(),"The client list is empty!",Toast.LENGTH_SHORT).show();


                }else{
                      PurchaseAdapter adapter = new PurchaseAdapter(list,Purchases.this,Purchases.this::onItemClick);
                      binding.recyclerView.setAdapter(adapter);
                      LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                      binding.recyclerView.setLayoutManager(layoutManager);
                }
            }
        });


    }











    private void readData(FirebaseData firebaseData){
        //load the list of items with keys from firebase in the lists
        ValueEventListener productListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Client> clients = new ArrayList<>();
                List<String> keys = new ArrayList<>();

                //get the data in the client list
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Client client = snapshot.getValue(Client.class);
                    clients.add(client);
                    keys.add(snapshot.getKey());
                }

                //callBack function
                firebaseData.getListOfClients(clients, keys);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //empty
            }
        };
        clients.addListenerForSingleValueEvent(productListener);
    }
    private interface FirebaseData{
        void getListOfClients(List<Client> list, List<String> keys);
    }


    @Override
    public void onItemClick(int position) {
            Intent intent = new Intent(getApplicationContext(),ClientOverview.class);
            intent.putExtra("Client Position",position);
            startActivity(intent);
            finish();
    }
}