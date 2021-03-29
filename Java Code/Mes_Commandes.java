package com.example.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.application.databinding.ActivityMesCommandesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Mes_Commandes extends AppCompatActivity implements RecyclerViewOnItemClick{
    ImageView delete;
    ActivityMesCommandesBinding binding;
    DatabaseReference mainProductRef = FirebaseDatabase.getInstance().getReference("Products").child("Produits");
    List<MainProduct> myProductList = new ArrayList<MainProduct>();

    //reference to the current user
    DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    //reference to the current user orders
    DatabaseReference userOrders = currentUser.child("commands");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityMesCommandesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ValueEventListener productListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<Client> clients = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Client client = snapshot.getValue(Client.class);
                    clients.add(client);
                }

                if(clients.isEmpty() == true){
                    Toast.makeText(getApplicationContext(),"You haven't bought anything!",Toast.LENGTH_SHORT).show();
                }else{
                    DeliveredOrdersAdapter adapter = new DeliveredOrdersAdapter(clients,getApplicationContext(),Mes_Commandes.this::onItemClick);
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
        userOrders.addListenerForSingleValueEvent(productListener);



    }

    @Override
    public void onItemClick(int position) {

    }
}