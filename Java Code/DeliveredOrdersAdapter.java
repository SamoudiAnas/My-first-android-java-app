package com.example.application;

import android.content.Context;
import android.view.LayoutInflater;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class DeliveredOrdersAdapter extends RecyclerView.Adapter<DeliveredOrdersAdapter.productViewholder>{

    List<Client> list ;
    Context context;
    RecyclerViewOnItemClick recyclerViewOnItemClick;

    public DeliveredOrdersAdapter(List<Client> list, Context context, RecyclerViewOnItemClick recyclerViewOnItemClick) {
        this.list = list;
        this.context = context;
        this.recyclerViewOnItemClick = recyclerViewOnItemClick;
    }

    @NonNull
    @Override
    public productViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.delivered_order_model,parent,false );
        return new productViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull productViewholder holder, int position) {
        final Client client = list.get(position);
        Glide.with(context.getApplicationContext()).load(client.getProfilePicture()).into(holder.clientImage);
        holder.clientName.setText(client.getName());
        holder.date.setText(client.getDate());
        holder.status.setText(client.getStatus());
        holder.price.setText(""+client.getPriceToPay());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class productViewholder extends RecyclerView.ViewHolder{

        ImageView clientImage;
        TextView  clientName, date,price,status;


        public productViewholder(@NonNull View itemView){
            super(itemView);
            clientImage =itemView.findViewById(R.id.clientImage);
            clientName =itemView.findViewById(R.id.clientName);
            date =itemView.findViewById(R.id.date);
            price =itemView.findViewById(R.id.price);
            status=itemView.findViewById(R.id.status);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewOnItemClick.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    public interface onItemClickListener{
        void onClick(View view, int position, boolean isLongClick);
    }


}
