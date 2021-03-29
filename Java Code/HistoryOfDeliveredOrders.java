package com.example.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import com.example.application.databinding.ActivityHistoryOfDeliveredOrdersBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryOfDeliveredOrders extends AppCompatActivity implements RecyclerViewOnItemClick {
    ActivityHistoryOfDeliveredOrdersBinding binding;
    //reference to the current user
    DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference("Delivery Man")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryOfDeliveredOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        readData(new FirebaseData() {
            @Override
            public void getListOfClients(List<Client> list) {
                if(list.isEmpty() == true){
                    Toast.makeText(getApplicationContext(),"The client list is empty!",Toast.LENGTH_SHORT).show();


                }else{
                    DeliveredOrdersAdapter adapter = new DeliveredOrdersAdapter(list,getApplicationContext(),HistoryOfDeliveredOrders.this::onItemClick);
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
                //get the data in the client list
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Client client = snapshot.getValue(Client.class);
                    clients.add(client);
                }
                //callBack function
                firebaseData.getListOfClients(clients);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //empty
            }
        };
        currentUser.child("Delivered").addListenerForSingleValueEvent(productListener);
    }

    @Override
    public void onItemClick(int position) {

    }

    private interface FirebaseData{
        void getListOfClients(List<Client> list);
    }
}