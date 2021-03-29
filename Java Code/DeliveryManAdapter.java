package com.example.application;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class DeliveryManAdapter extends RecyclerView.Adapter<DeliveryManAdapter.DeliveryManViewHolder> {
    List<DeliverAccountCreatedByAdmin> deliveryMenList ;
    Context context;
    private RecyclerViewOnItemClick recyclerViewOnItemClick;

    public DeliveryManAdapter(List<DeliverAccountCreatedByAdmin> deliveryMenList, Context context, RecyclerViewOnItemClick recyclerViewOnItemClick) {
        this.deliveryMenList = deliveryMenList;
        this.context = context;
        this.recyclerViewOnItemClick = recyclerViewOnItemClick;
    }

    @NonNull
    @Override
    public DeliveryManViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.delivery_man_model,parent,false );
        return new DeliveryManAdapter.DeliveryManViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryManViewHolder holder, int position) {
        final DeliverAccountCreatedByAdmin dMan =  deliveryMenList.get(position);
        Glide.with(context.getApplicationContext()).load(dMan.getProfilePicture()).into(holder.dManImage);
        holder.dManName.setText(dMan.getFullName());
        holder.dManAddress.setText(dMan.getAddress());
        holder.dManPhoneNumber.setText(dMan.getPhoneNumber());
        holder.status.setText(dMan.getStatus());



        switch (dMan.getStatus())
        {
            case "Available":
                holder.statusBG.setBackgroundResource(R.drawable.rounded_rectangle_green);
                break;
            case "On Mission":
                holder.statusBG.setBackgroundResource(R.drawable.rounded_rectangle_blue);
                break;
            case "Has Problem":
            case "Accident" :
                holder.statusBG.setBackgroundResource(R.drawable.rounded_rectangle_red);
                break;
            case "Offline":
                holder.statusBG.setBackgroundResource(R.drawable.rounded_rectangle_grey);
                break;
            case "Run out of fuel" :
                holder.statusBG.setBackgroundResource(R.drawable.rounded_rectangle_orange);
                break;
            case "Run out of money" :
                holder.statusBG.setBackgroundResource(R.drawable.rounded_rectangle_cyan);
                break;
            default:
                holder.statusBG.setBackgroundResource(R.drawable.rounded_rectangle_black);
        }

    }

    @Override
    public int getItemCount() {
        return deliveryMenList.size();
    }

    public class DeliveryManViewHolder extends RecyclerView.ViewHolder{
        LinearLayout statusBG;
        ImageView dManImage;
        TextView dManName, dManAddress, dManPhoneNumber,status;
        public DeliveryManViewHolder(@NonNull View itemView) {

            super(itemView);
            dManImage = itemView.findViewById(R.id.avatar);
            dManName = itemView.findViewById(R.id.dileveryManName);
            dManAddress = itemView.findViewById(R.id.dileveryManAddress);
            dManPhoneNumber = itemView.findViewById(R.id.dileveryManPhoneNumber);
            status =itemView.findViewById(R.id.status);
            statusBG = itemView.findViewById(R.id.status_bg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewOnItemClick.onItemClick(getAdapterPosition());
                }
            });
        }


    }



}
