package com.example.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SelectCategory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);




    }


    public void addToMarketPlace(View view) {
        Intent intent = new Intent(getApplicationContext(),addArticles.class);
        intent.putExtra("Reference","MarketPlace");
        startActivity(intent);
    }

    public void addToGroceries(View view) {
        Intent intent = new Intent(getApplicationContext(),addArticles.class);
        intent.putExtra("Reference","Groceries");
        startActivity(intent);
    }



    public void addToElectronics(View view) {
        Intent intent = new Intent(getApplicationContext(),addArticles.class);
        intent.putExtra("Reference","Electronics");
        startActivity(intent);
    }

    public void addToFastFood(View view) {
        Intent intent = new Intent(getApplicationContext(),addArticles.class);
        intent.putExtra("Reference","FastFood");
        startActivity(intent);
    }
}