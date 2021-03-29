package com.example.application;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class SelectDeliveryManAdapter extends RecyclerView.Adapter<SelectDeliveryManAdapter.SelectDeliveryManViewHolder> {


    List<DeliverAccountCreatedByAdmin> deliveryMenList ;
    Context context;
    private RecyclerViewOnItemClick recyclerViewOnItemClick;



    public SelectDeliveryManAdapter(List<DeliverAccountCreatedByAdmin> deliveryMenList, Context context, RecyclerViewOnItemClick recyclerViewOnItemClick) {
        this.deliveryMenList = deliveryMenList;
        this.context = context;
        this.recyclerViewOnItemClick = recyclerViewOnItemClick;
    }

    @NonNull
    @Override
    public SelectDeliveryManViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.select_delivery_man_model,parent,false );
        return new SelectDeliveryManAdapter.SelectDeliveryManViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectDeliveryManViewHolder holder, int position) {
        final DeliverAccountCreatedByAdmin dMan = deliveryMenList.get(position);
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

    public class SelectDeliveryManViewHolder extends RecyclerView.ViewHolder{
        LinearLayout statusBG;
        ImageView dManImage;
        TextView dManName, dManAddress, dManPhoneNumber,status;
        public SelectDeliveryManViewHolder(@NonNull View itemView) {

            super(itemView);
            dManImage = itemView.findViewById(R.id.avatar);
            dManName = itemView.findViewById(R.id.dileveryManName);
            dManAddress = itemView.findViewById(R.id.dileveryManAddress);
            dManPhoneNumber = itemView.findViewById(R.id.dileveryManPhoneNumber);
            status = itemView.findViewById(R.id.status);
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
