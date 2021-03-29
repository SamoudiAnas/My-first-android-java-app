package com.example.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import com.example.application.databinding.ActivityDeliveryManBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeliveryMan extends AppCompatActivity implements RecyclerViewOnItemClick{

    ActivityDeliveryManBinding binding;



    //reference to Delivery Man in database
    DatabaseReference deliveryMenDB = FirebaseDatabase.getInstance().getReference("Delivery Man");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeliveryManBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());





        readData(new FirebaseCallBack() {
            @Override
            public void getDeliveryMen(List<DeliverAccountCreatedByAdmin> deliveryMen, List<String> keys) {
                if(deliveryMen.isEmpty() == true){
                    Toast.makeText(getApplicationContext(),"The list is empty", Toast.LENGTH_SHORT).show();
                }else{
                    DeliveryManAdapter adapter = new DeliveryManAdapter(deliveryMen,getApplicationContext(),DeliveryMan.this::onItemClick);
                    //show the list of the items
                    binding.recyclerView.setAdapter(adapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    binding.recyclerView.setLayoutManager(layoutManager);

                }
            }
        });
    }










    private void readData(FirebaseCallBack firebaseCallBack){
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

                firebaseCallBack.getDeliveryMen(deliveryMen,keys);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //empty
            }
        };
        deliveryMenDB.addValueEventListener(productListener);
    }



    private interface FirebaseCallBack{
        void getDeliveryMen(List<DeliverAccountCreatedByAdmin> deliveryMen, List<String> keys);
    }


    @Override
    public void onItemClick(int position) {

    }
}