package com.example.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.application.databinding.ActivityClientOverviewBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClientOverview extends AppCompatActivity implements RecyclerViewOnItemClick{
    int clientPosition;


    ActivityClientOverviewBinding binding;
    RecyclerViewOnItemClick recyclerViewOnItemClick;

    //reference to clients in firebase
    DatabaseReference clientsDB = FirebaseDatabase.getInstance().getReference("Clients").child("All Commands");

    ImageView clientImage;
    TextView clientName,clientAddress, clientPhoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClientOverviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        clientImage = findViewById(R.id.clientImage);
        clientName = findViewById(R.id.clientName);
        clientAddress = findViewById(R.id.clientAddress);
        clientPhoneNumber = findViewById(R.id.clientPhoneNumber);


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            clientPosition = extras.getInt("Client Position");
        }


        readData(new FirebaseData() {
            @Override
            public void getClientAndKey(List<Client> clients, List<String> keys) {
                //Get the client info
                Client client = clients.get(clientPosition);
                Glide.with(getApplicationContext()).load(client.getProfilePicture()).into(clientImage);
                clientName.setText(client.getName());
                clientAddress.setText(client.getAddress());
                clientPhoneNumber.setText(client.getPhoneNumber());

                //get client items into the adapter
                readCommandsData(new FirebaseCommandsData() {
                    @Override
                    public void getProductsList(List<MainProduct> products, List<String> productsKeys) {
                        MainAdapter adapter = new MainAdapter(products,getApplicationContext(),ClientOverview.this::onItemClick);
                        //show the list of the items
                        binding.recyclerView.setAdapter(adapter);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        binding.recyclerView.setLayoutManager(layoutManager);
                    }
                },keys.get(clientPosition));


            }
        });






    }

    public void selectDeliveryMan(View view) {
       Intent intent = new Intent(getApplicationContext(),SelectDeliveryMan.class);
        intent.putExtra("Client Position", clientPosition);
       startActivity(intent);
       finish();
    }



    private void readData(FirebaseData firebaseData){
        //load the list of item from firebase in the list
        ValueEventListener productListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clear the list to renew it
                List<Client> clients = new ArrayList<>();
                List<String> keys = new ArrayList<>();


                //get the list of clients with their keys
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Client client = snapshot.getValue(Client.class);
                    clients.add(client);

                    String key = snapshot.getKey();
                    keys.add(key);

                }

                firebaseData.getClientAndKey(clients,keys);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //empty
            }
        };
        clientsDB.addValueEventListener(productListener);
    }



    private interface FirebaseData{
        void getClientAndKey(List<Client> clients,List<String> keys);

    }

    private void readCommandsData(FirebaseCommandsData firebaseCommandsData,String clientID){
        //load the list of item from firebase in the list
        ValueEventListener productListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clear the list to renew it
                List<MainProduct> products = new ArrayList<>();
                List<String> keys = new ArrayList<>();


                //get the list of clients with their keys
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    MainProduct product = snapshot.getValue(MainProduct.class);
                    products.add(product);

                    String key = snapshot.getKey();
                    keys.add(key);

                }

                firebaseCommandsData.getProductsList(products,keys);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //empty
            }
        };
        clientsDB.child(clientID).child("commands").addValueEventListener(productListener);
    }

    private interface FirebaseCommandsData{
        void getProductsList(List<MainProduct> products,List<String> productsKeys);
    }



    @Override
    public void onItemClick(int position) {
        //nothing should happen
    }









}