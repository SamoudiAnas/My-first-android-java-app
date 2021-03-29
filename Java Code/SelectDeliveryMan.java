package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.application.databinding.ActivitySelectDeliveryManBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SelectDeliveryMan extends AppCompatActivity implements RecyclerViewOnItemClick{
    ActivitySelectDeliveryManBinding binding;
    int clientPosition,deliveryManPosition;


    //reference to delivery Men in firebase
    DatabaseReference deliveryManDB = FirebaseDatabase.getInstance().getReference("Delivery Man");

    //reference to clients in firebase
    DatabaseReference clientsDB = FirebaseDatabase.getInstance().getReference("Clients").child("All Commands");

    //reference to clients in firebase
    DatabaseReference OnProcessOrders = FirebaseDatabase.getInstance().getReference("Clients").child("On Process");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectDeliveryManBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            clientPosition = extras.getInt("Client Position");
        }


        readData(new FirebaseData() {
            @Override
            public void getDeliveryManAndKeys(List<DeliverAccountCreatedByAdmin> deliveryMen, List<String> keys) {
                SelectDeliveryManAdapter adapter = new SelectDeliveryManAdapter(deliveryMen,getApplicationContext(),SelectDeliveryMan.this::onItemClick);
                //show the list of the items
                binding.recyclerView.setAdapter(adapter);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                binding.recyclerView.setLayoutManager(layoutManager);
            }
        });


    }



    private void readData(FirebaseData firebaseData){
        //load the list of item from firebase in the list
        ValueEventListener productListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clear the list to renew it
                List<DeliverAccountCreatedByAdmin> deliveryMen = new ArrayList<>();
                List<String> keys = new ArrayList<>();


                //get the list of clients with their keys
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    DeliverAccountCreatedByAdmin deliveryMan = snapshot.getValue(DeliverAccountCreatedByAdmin.class);
                    deliveryMen.add(deliveryMan);

                    String key = snapshot.getKey();
                    keys.add(key);

                }

                firebaseData.getDeliveryManAndKeys(deliveryMen,keys);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //empty
            }
        };
        deliveryManDB.addValueEventListener(productListener);
    }

    private void readData2(FirebaseData firebaseData){
        //load the list of item from firebase in the list
        ValueEventListener productListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clear the list to renew it
                List<DeliverAccountCreatedByAdmin> deliveryMen = new ArrayList<>();
                List<String> keys = new ArrayList<>();


                //get the list of clients with their keys
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    DeliverAccountCreatedByAdmin deliveryMan = snapshot.getValue(DeliverAccountCreatedByAdmin.class);
                    deliveryMen.add(deliveryMan);

                    String key = snapshot.getKey();
                    keys.add(key);

                }

                firebaseData.getDeliveryManAndKeys(deliveryMen,keys);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //empty
            }
        };
        deliveryManDB.addListenerForSingleValueEvent(productListener);
    }

    @Override
    public void onItemClick(int position) {
        readData2(new FirebaseData() {
            @Override
            public void getDeliveryManAndKeys(List<DeliverAccountCreatedByAdmin> deliveryMen, List<String> keys1) {


                readClientsData(new FirebaseClientsData() {
                    @Override
                    public void getClientAndKey(List<Client> clients, List<String> keys) {
                        Client client = clients.get(clientPosition);
                        client.setStatus("Assigned to delivery man");

                        deliveryManDB.child(keys1.get(position)).child("To Deliver").child(client.getId()).setValue(client)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(),"Selected",Toast.LENGTH_SHORT).show();
                            }
                        });
                        OnProcessOrders.child(client.getId()).setValue(clients.get(clientPosition));

                    }
                });








            }
        });


        Intent intent = new Intent(getApplicationContext(),admin_dashboard.class);
        startActivity(intent);
        finish();
    }


    private interface FirebaseData{
        void getDeliveryManAndKeys(List<DeliverAccountCreatedByAdmin> deliveryMen,List<String> keys);

    }


    private void readClientsData(FirebaseClientsData firebaseData){
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
        clientsDB.addListenerForSingleValueEvent(productListener);
    }



    private interface FirebaseClientsData{
        void getClientAndKey(List<Client> clients,List<String> keys);

    }


}