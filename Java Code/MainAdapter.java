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


import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.productViewholder>{

    List<MainProduct> list ;
    Context context;
    private RecyclerViewOnItemClick recyclerViewOnItemClick;

    public MainAdapter(List<MainProduct> list, Context context, RecyclerViewOnItemClick recyclerViewOnItemClick) {
        this.list = list;
        this.context = context;
        this.recyclerViewOnItemClick = recyclerViewOnItemClick;
    }

    @NonNull
    @Override
    public productViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_model,parent,false );

        return new productViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull productViewholder holder, int position) {
        final MainProduct product = list.get(position);
        Glide.with(context.getApplicationContext()).load(product.getImage()).into(holder.foodImage);


        holder.mainName.setText(product.getName());
        holder.description.setText(product.getDescription());
        holder.price.setText(""+ product.getPrice()+"");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class productViewholder extends RecyclerView.ViewHolder{

        ImageView foodImage;
        TextView  mainName, price, description;


        public productViewholder(@NonNull View itemView){
            super(itemView);
            foodImage =itemView.findViewById(R.id._productImage);
            mainName =itemView.findViewById(R.id._productName);
            price =itemView.findViewById(R.id._productPrice);
            description =itemView.findViewById(R.id._productDescription);

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
