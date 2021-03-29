package com.example.application;



import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

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

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.viewholder> {
    List<MainProduct> list ;
    Context context;
    static List<String> keys = new ArrayList<>();
    RecyclerViewOnItemClick recyclerViewOnItemClick;

    //reference to the current user
    DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    //reference to the current user cart
    DatabaseReference userWishList = currentUser.child("wishlist");


    public WishlistAdapter(List<MainProduct> list, Context context,RecyclerViewOnItemClick recyclerViewOnItemClick) {
        this.list = list;
        this.context = context;
        this.recyclerViewOnItemClick = recyclerViewOnItemClick;
    }

    @NonNull
    @Override
    public WishlistAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wishlist_model,parent,false );

        return new WishlistAdapter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        final MainProduct product = list.get(position);
        Glide.with(context.getApplicationContext()).load(product.getImage()).into(holder.productImage);
        holder.mainName.setText(product.getName());
        holder.description.setText(product.getDescription());
        holder.price.setText(""+ product.getPrice()+"");


    }




    @Override
    public int getItemCount() {
        return list.size();
    }



    public class viewholder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView mainName, price, description;
        ImageView delete, addToCart;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id._productImage);
            mainName = itemView.findViewById(R.id._productName);
            price = itemView.findViewById(R.id._productPrice);
            description = itemView.findViewById(R.id._productDescription);



            addToCart = itemView.findViewById(R.id.addToCart);
            addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addItemToCart(getAdapterPosition());
                }
            });

            delete = itemView.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(getAdapterPosition());
                }
            });

        }

    }

    private void addItemToCart(int position) {
        readData(new FirebaseCallBack() {
            @Override
            public void getCartList(List<MainProduct> list) {
                MainProduct product = list.get(position);
                userWishList.child(product.getpId()).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context.getApplicationContext(),"Added to your cart!",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


    }


    public void removeItem(int position){
        readData(new FirebaseCallBack() {
            @Override
            public void getCartList(List<MainProduct> list) {
                MainProduct product = list.get(position);
                userWishList.child(product.getpId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context.getApplicationContext(),"Item Removed!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
    private void readData(FirebaseCallBack firebaseCallBack){
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
        userWishList.addListenerForSingleValueEvent(productListener);
    }
    private interface FirebaseCallBack{
        void getCartList(List<MainProduct> list);
    }

}
