package com.example.application;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.ClientViewholder> {
    List<Client> clients ;
    Context context;
    private RecyclerViewOnItemClick recyclerViewOnItemClick;



    public PurchaseAdapter(List<Client> clients, Context context, RecyclerViewOnItemClick recyclerViewOnItemClick){
        this.clients = clients;
        this.context = context;
        this.recyclerViewOnItemClick = recyclerViewOnItemClick;
    }

    @NonNull
    @Override
    public ClientViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.purchases_model,parent,false );
        return new PurchaseAdapter.ClientViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientViewholder holder, int position) {
        final Client client = clients.get(position);
        Glide.with(context.getApplicationContext()).load(client.getProfilePicture()).into(holder.clientImage);
        holder.clientName.setText(client.getName());
        holder.clientAddress.setText(client.getAddress());
        holder.clientPhoneNumber.setText(client.getPhoneNumber());
        holder.status.setText(client.getStatus());
        holder.date.setText(client.getDate());
        holder.price.setText(""+client.getPriceToPay()+" Dhs");
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }


    public class ClientViewholder extends RecyclerView.ViewHolder{

        ImageView clientImage;
        TextView clientName, clientAddress, clientPhoneNumber,status,date,price;
        public ClientViewholder(@NonNull View itemView) {
            super(itemView);
            clientImage = itemView.findViewById(R.id.clientImage);
            clientName = itemView.findViewById(R.id.clientName);
            clientAddress = itemView.findViewById(R.id.clientAddress);
            clientPhoneNumber = itemView.findViewById(R.id.clientPhoneNumber);
            status = itemView.findViewById(R.id.status);
            date = itemView.findViewById(R.id.date);
            price = itemView.findViewById(R.id.price);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewOnItemClick.onItemClick(getAdapterPosition());
                }
            });
        }
    }














}
