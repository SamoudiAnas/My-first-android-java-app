package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application.databinding.ActivityMyCartBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class My_cart extends AppCompatActivity implements RecyclerViewOnItemClick{
    TextView Price;
    ActivityMyCartBinding binding;
    List<MainProduct> myCartList = new ArrayList<MainProduct>();

    //reference to the current user
    DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    //reference to the current user cart
    DatabaseReference userCart = currentUser.child("cart");
    //reference to the current user cart
    DatabaseReference userCommands = currentUser.child("commands");
    //reference to the clients
    DatabaseReference allClients = FirebaseDatabase.getInstance().getReference("Clients").child("Failed");


    DatabaseReference onProcessDB = FirebaseDatabase.getInstance().getReference("Clients").child("On Process");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        List<MainProduct> cartList = new ArrayList<>();
        List<String> keys1 = new ArrayList<>();

        //read the data
        readData2(new FirebaseCallBack() {
            @Override
            public void getCartList(List<MainProduct> list) {
                if(list.isEmpty() == true)
                {
                    Toast.makeText(getApplicationContext(),"Your cart is empty!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    CartAdapter adapter = new CartAdapter(list,getApplicationContext(),My_cart.this::onItemClick  );
                    //show the list of the items
                    binding.recyclerView.setAdapter(adapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    binding.recyclerView.setLayoutManager(layoutManager);
                    calculatePrice();
                }
            }
        });
    }

    public void calculatePrice(){
        //read the data
        readData2(new FirebaseCallBack() {
            @Override
            public void getCartList(List<MainProduct> list) {
                double price = 0;
                if(list.isEmpty() == true){
                    Price.setText( ""+price+"");
                }
                else
                {
                    Price = findViewById(R.id.totalPrice);
                    for(int i = 0;i<list.size();i++){
                        price += list.get(i).getPrice() *list.get(i).getQuantity();
                    }
                    Price.setText( ""+price+"");
                }
            }
        });
    }

    @Override
    public void onItemClick(int position) {

    }

    public void checkout(View view) {

        //read the data
        readData(new FirebaseCallBack() {
            @Override
            public void getCartList(List<MainProduct> list) {
                readUserData(new FirebaseUserCallBack() {
                    @Override
                    public void getUser(UserHelperClass user,String uid) {
                        String name = user.getName();
                        String address = user.getAddress();
                        String phone = user.getPhone();
                        String profilePicture = user.getProfilePicture();
                        double priceToPay=0;
                        if(list.isEmpty() == true){}
                        else
                        {
                            for(int i = 0;i<list.size();i++){
                                priceToPay += list.get(i).getPrice() *  list.get(i).getQuantity();
                            }
                        }
                        Calendar calendar = Calendar.getInstance();
                        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());

                        String commandKey = UUID.randomUUID().toString();
                        Client client = new Client(commandKey,uid, name,address, phone,list, profilePicture, priceToPay,currentDate);


                        allClients.child(commandKey).setValue(client).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(),"Your purchases are saved. We will process them as soon as possible",Toast.LENGTH_SHORT).show();
                            }
                        });

                        //add the command in the user data
                        userCommands.child(commandKey).setValue(client);

                        onProcessDB.child(commandKey).setValue(client);
                        //returnToDashboard();

                    }
                });



            }
        });






    }

    private void returnToDashboard(){
        Intent intent = new Intent(getApplicationContext(),Mainmenu.class);
        intent.putExtra("Delete Cart",true);
        startActivity(intent);

    }
    private void readData(FirebaseCallBack myCallBack){
        ValueEventListener cartListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<MainProduct> list = new ArrayList<>();

                //get the data in a list
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    MainProduct product = snapshot.getValue(MainProduct.class);
                    list.add(product);
                }

                //calculatePrice();
                //get the data on callback
                myCallBack.getCartList(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        userCart.addListenerForSingleValueEvent(cartListener);

    }
    private void readData2(FirebaseCallBack myCallBack){
        ValueEventListener cartListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<MainProduct> list = new ArrayList<>();

                //get the data in a list
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    MainProduct product = snapshot.getValue(MainProduct.class);
                    list.add(product);
                }

                //calculatePrice();
                //get the data on callback
                myCallBack.getCartList(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        userCart.addValueEventListener(cartListener);

    }

    private interface FirebaseCallBack{
        void getCartList(List<MainProduct> list);
    }
    public void readUserData(FirebaseUserCallBack userData){
        ValueEventListener user = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String phone = dataSnapshot.child("phone").getValue().toString();
                String address = dataSnapshot.child("address").getValue().toString();
                String profilePicture = dataSnapshot.child("profilePicture").getValue().toString();

                String uid = dataSnapshot.child("uid").getValue().toString();

                UserHelperClass ourClient = new UserHelperClass(name,address,phone,profilePicture);

                //get the data on callback
                userData.getUser(ourClient,uid);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        currentUser.addListenerForSingleValueEvent(user);

    }




    private interface FirebaseUserCallBack{
        void getUser(UserHelperClass user,String uid);
    }

}