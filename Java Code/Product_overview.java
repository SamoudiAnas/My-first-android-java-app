package com.example.application;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

public class Product_overview extends AppCompatActivity {
    private ImageView productImage;
    private TextView productName,productPrice,productDescription,productQuantity;


    int positionOfProductInList,quantity = 1;


    //reference to the list of the product list
    DatabaseReference mainProductRef;
    static List<MainProduct> myProductList = new ArrayList<MainProduct>();
    static List<String> keys = new ArrayList<>();




    //reference to the current user
    DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());



    //reference to the current user wishlist
    DatabaseReference userWishList = currentUser.child("wishlist");

    //reference to the current user cart
    DatabaseReference userCart = currentUser.child("cart");;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_overview);



        //SET EACH COMPONENTS TO ITS VARIABLE
        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id._productName);
        productPrice = findViewById(R.id._productPrice);
        productDescription = findViewById(R.id._productDescription);
        productQuantity = findViewById(R.id.productQuantity);



        //GET ALL DETAILS THE CLICKED VIEW INTO THE PRODUCT OVERVIEW
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String category,store;
            category = extras.getString("Category");
            store = extras.getString("Store");
            mainProductRef = FirebaseDatabase.getInstance().getReference("Products").child(category).child(store);
            positionOfProductInList = extras.getInt("Product Position");
        }

        readData(new FirebaseCallBack() {
            @Override
            public void getCartList(List<MainProduct> list) {
                MainProduct product = list.get(positionOfProductInList);
                Glide.with(getApplicationContext()).load(product.getImage()).into(productImage);
                productName.setText(product.getName());
                productPrice.setText(""+product.getPrice()+"");
                productDescription.setText(product.getDescription());
            }
        });






    }

    public void addToMyWishlist(View view) {
       readData(new FirebaseCallBack() {
            @Override
            public void getCartList(List<MainProduct> list) {
                MainProduct product = list.get(positionOfProductInList);
                product.setQuantity(quantity);
                userWishList.child(product.getpId())
                        .setValue(product)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(),"Added To your wishlist",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
    public void addToMyCart(View view) {
        readData(new FirebaseCallBack() {
            @Override
            public void getCartList(List<MainProduct> list) {
                MainProduct product = list.get(positionOfProductInList);
                product.setQuantity(quantity);
                userCart.child(product.getpId())
                        .setValue(product)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(),"Added To your cart",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });




    }

    public void plus(View view) {
        quantity++;
        productQuantity.setText(""+quantity);
    }

    public void minus(View view) {
        quantity--;
        productQuantity.setText(""+quantity);
    }

    private void readData(FirebaseCallBack firebaseCallBack){
        setReference();
        //load the list of item from firebase in the list
        ValueEventListener productListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<MainProduct> products = new ArrayList<>();

                //add the selected item in the user wishlist
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    MainProduct product = snapshot.getValue(MainProduct.class);
                    products.add(product);

                }


                firebaseCallBack.getCartList(products);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //empty
            }
        };
        mainProductRef.addListenerForSingleValueEvent(productListener);
    }
    private interface FirebaseCallBack{
        void getCartList(List<MainProduct> list);
    }


    private void setReference(){
        Bundle extras = getIntent().getExtras();
        String category,store;
        if(extras != null){
            category = extras.getString("Category");
            store = extras.getString("Store");
            mainProductRef = FirebaseDatabase.getInstance().getReference("Products").child(category).child(store);
        }
    }
}