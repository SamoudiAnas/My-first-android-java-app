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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewholder> {

    List<MainProduct> list ;
    Context context;
    static List<String> keys = new ArrayList<>();
    private RecyclerViewOnItemClick recyclerViewOnItemClick;

    //reference to the current user
    DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    //reference to the current user cart
    DatabaseReference userCart = currentUser.child("cart");;

    public CartAdapter(List<MainProduct> list, Context context,RecyclerViewOnItemClick recyclerViewOnItemClick) {
        this.list = list;
        this.context = context;
        this.recyclerViewOnItemClick = recyclerViewOnItemClick;
    }

    @NonNull
    @Override
    public CartAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_model,parent,false );

        return new CartAdapter.viewholder(view);
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

            delete = itemView.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(getAdapterPosition());
                }
            });

            TextView quantity = itemView.findViewById(R.id.quantity);
            quantity.setText("1");
            //read the data
            readData(new FirebaseData() {
                @Override
                public void getProductsAndKeys(List<MainProduct> list, List<String> keys) {
                    quantity.setText("" +list.get(getAdapterPosition()).getQuantity());
                }
            });

            View plus = itemView.findViewById(R.id.plus);
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    plus(getAdapterPosition());
                    notifyDataSetChanged();
                    readData(new FirebaseData() {
                        @Override
                        public void getProductsAndKeys(List<MainProduct> list, List<String> keys) {
                            quantity.setText("" +list.get(getAdapterPosition()).getQuantity());
                        }
                    });

                }
            });

            View minus = itemView.findViewById(R.id.minus);
            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    minus(getAdapterPosition());
                    notifyDataSetChanged();
                    readData(new FirebaseData() {
                        @Override
                        public void getProductsAndKeys(List<MainProduct> list, List<String> keys) {
                            quantity.setText("" +list.get(getAdapterPosition()).getQuantity());
                        }
                    });
                }
            });



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewOnItemClick.onItemClick(getAdapterPosition());
                }
            });
        }

    }


    public void removeItem(int position){
        readData(new FirebaseData() {
            @Override
            public void getProductsAndKeys(List<MainProduct> list, List<String> keys) {
                //remove the item
                userCart.child(list.get(position).getpId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userCart.child(keys.get(position)).removeValue();
                        Toast.makeText(context.getApplicationContext(),"Item Removed!", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context.getApplicationContext(),"Failed to delete! Try again later", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }


    public void setQuantityFromProduct(int position, TextView quantity){
        //read the data
         readData2(new FirebaseData() {
             @Override
             public void getProductsAndKeys(List<MainProduct> list, List<String> keys) {
                 quantity.setText(""+list.get(position).getQuantity());
             }
         });
    }




    public void plus(int position) {
        readData(new FirebaseData() {
            @Override
            public void getProductsAndKeys(List<MainProduct> list, List<String> keys) {
                list.get(position).setQuantity(list.get(position).getQuantity()+1);
                userCart.child(list.get(position).getpId()).setValue(list.get(position));
            }

        });
    }

    public void minus(int position) {
        readData(new FirebaseData() {
            @Override
            public void getProductsAndKeys(List<MainProduct> list, List<String> keys) {
                if(list.get(position).getQuantity() == 1){}
                else{
                    list.get(position).setQuantity(list.get(position).getQuantity()-1);
                    userCart.child(list.get(position).getpId()).setValue(list.get(position));
                }
            }
        });
    }

    private void readData(FirebaseData firebaseData){
        //load the list of items with keys from firebase in the lists
        ValueEventListener productListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clear the list to renew it
                list.clear();
                keys.clear();

                MainProduct Product = new MainProduct();
                //add the selected item in the user wishlist
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    MainProduct product = snapshot.getValue(MainProduct.class);
                    list.add(product);
                    keys.add(snapshot.getKey());
                }
                firebaseData.getProductsAndKeys(list, keys);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //empty
            }
        };
        userCart.addListenerForSingleValueEvent(productListener);
    }

    private void readData2(FirebaseData firebaseData){
        //load the list of items with keys from firebase in the lists
        ValueEventListener productListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clear the list to renew it
                list.clear();
                keys.clear();

                MainProduct Product = new MainProduct();
                //add the selected item in the user wishlist
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    MainProduct product = snapshot.getValue(MainProduct.class);
                    list.add(product);
                    keys.add(snapshot.getKey());
                }
                firebaseData.getProductsAndKeys(list, keys);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //empty
            }
        };
        userCart.addValueEventListener(productListener);
    }

    private interface FirebaseData{
        void getProductsAndKeys(List<MainProduct> list, List<String> keys);
    }
}
