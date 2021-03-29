package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application.databinding.ActivityOnDeliveryBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OnDelivery extends AppCompatActivity implements RecyclerViewOnItemClick{
    ActivityOnDeliveryBinding binding;

    //reference to delivery Man account
    DatabaseReference deliveryManRef = FirebaseDatabase.getInstance().getReference("Delivery Man").child(FirebaseAuth.getInstance().getCurrentUser().getUid());



    //reference to database
    DatabaseReference toDeliverDB = deliveryManRef.child("To Deliver");

    //reference to database
    DatabaseReference deliveredDB = deliveryManRef.child("Delivered");


    //reference to clients in firebase
    DatabaseReference clientsDB = FirebaseDatabase.getInstance().getReference("Clients").child("All Commands");

    //reference to users in firebase
    DatabaseReference usersDB = FirebaseDatabase.getInstance().getReference("Users");


    DatabaseReference onProcessDB = FirebaseDatabase.getInstance().getReference("Clients").child("On Process");
    int clientPosition;

    TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnDeliveryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            clientPosition = extras.getInt("Client Position");
        }

        status = findViewById(R.id.status);

        readData(new FirebaseData() {
            @Override
            public void getClientWithKey(Client client, String clientKey) {
                readCommandsData(new FirebaseCommandsData() {
                    @Override
                    public void getProductsList(List<MainProduct> products, List<String> productsKeys) {
                        CommandAdapter adapter = new CommandAdapter(products,getApplicationContext(),OnDelivery.this::onItemClick);
                        //show the list of the items
                        binding.recyclerView.setAdapter(adapter);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        binding.recyclerView.setLayoutManager(layoutManager);
                    }
                },clientKey);
            }
        });
    }











    public void nextStatus(View view) {

        readData2(new FirebaseData() {
            @Override
            public void getClientWithKey(Client client, String clientKey) {
                switch (client.getStatus()){
                    case "Assigned to delivery man":
                        usersDB.child(client.getUid()).child("commands").child(client.getId()).child("status").setValue("Shop In Progress");
                        clientsDB.child(client.getId()).child("status").setValue("Shop In Progress");
                        toDeliverDB.child(client.getId()).child("status").setValue("Shop In Progress");
                        status.setText("Shop In Progress");
                        break;
                    case "Shop In Progress":
                        usersDB.child(client.getUid()).child("commands").child(client.getId()).child("status").setValue("Delivery In Progress");
                        clientsDB.child(client.getId()).child("status").setValue("Delivery In Progress");
                        toDeliverDB.child(client.getId()).child("status").setValue("Delivery In Progress");
                        status.setText("Delivery In Progress");
                        break;
                    case "Delivery In Progress":
                        usersDB.child(client.getUid()).child("commands").child(client.getId()).child("status").setValue("Delivered");
                        clientsDB.child(client.getId()).child("status").setValue("Delivered");
                        toDeliverDB.child(client.getId()).child("status").setValue("Delivered");
                        status.setText("Delivered");
                        finishDelivery();
                        break;
                    default:
                        //nothing
                }
            }
        });

    }

    public void reportProblem(View view) {
        Intent intent = new Intent(getApplicationContext(),Problem.class);
        startActivity(intent);
    }

    public void finished(View view) {
        finishDelivery();
    }

    private void finishDelivery(){
        readData2(new FirebaseData() {
            @Override
            public void getClientWithKey(Client client, String clientKey) {
                clientsDB.child(client.getId()).child("status").setValue("Delivered");
                deliveredDB.child(client.getId()).setValue(client);
                DatabaseReference deliveredOrders = FirebaseDatabase.getInstance().getReference("Clients").child("Delivered");
                deliveredOrders.child(client.getId()).setValue(client);
                Intent intent = new Intent(getApplicationContext(),DeliveryManDashboard.class);
                startActivity(intent);
                finish();
                onProcessDB.child(client.getId()).removeValue();
                toDeliverDB.child(client.getId()).removeValue();
            }
        });
    };


    public void track(View view) {
        readData(new FirebaseData() {
            @Override
            public void getClientWithKey(Client client, String clientKey) {
                readDeliveryManData(new FirebaseDeliveryManData() {
                    @Override
                    public void getDeliveryMan(DeliverAccountCreatedByAdmin dMan) {
                        String clientAddress = client.getAddress();
                        String deliveryManAddress = dMan.getAddress();
                        Intent intent = new Intent(getApplicationContext(),GoogleTracking.class);
                        intent.putExtra("Client Address", clientAddress);
                        intent.putExtra("Delivery Man Address", deliveryManAddress);
                        startActivity(intent);
                    }
                });

            }
        });

    }












    private void readData(FirebaseData firebaseData){
        //load the list of item from firebase in the list
        ValueEventListener clientListener = new ValueEventListener() {
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

                firebaseData.getClientWithKey(clients.get(clientPosition),keys.get(clientPosition));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //empty
            }
        };
        toDeliverDB.addValueEventListener(clientListener);
    }
    private void readData2(FirebaseData firebaseData){
        //load the list of item from firebase in the list
        ValueEventListener clientListener = new ValueEventListener() {
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


                firebaseData.getClientWithKey(clients.get(clientPosition),keys.get(clientPosition));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //empty
            }
        };
        toDeliverDB.addListenerForSingleValueEvent(clientListener);
    }




    private interface FirebaseData{
        void getClientWithKey(Client client,String clientKey);
    }




    private void readCommandsData(FirebaseCommandsData firebaseCommandsData, String clientID){
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
        toDeliverDB.child(clientID).child("commands").addValueEventListener(productListener);
    }

    private interface FirebaseCommandsData{
        void getProductsList(List<MainProduct> products,List<String> productsKeys);
    }

    private void readDeliveryManData(FirebaseDeliveryManData firebaseDeliveryManData){
        //load the list of item from firebase in the list
        ValueEventListener productListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               DeliverAccountCreatedByAdmin dMan = dataSnapshot.getValue(DeliverAccountCreatedByAdmin.class);

                firebaseDeliveryManData.getDeliveryMan(dMan);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //empty
            }
        };
        deliveryManRef.addValueEventListener(productListener);
    }

    private interface FirebaseDeliveryManData{
        void getDeliveryMan(DeliverAccountCreatedByAdmin dMan);
    }

    @Override
    public void onItemClick(int position) {
        //nothing
    }
}