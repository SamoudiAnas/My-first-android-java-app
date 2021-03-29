package com.example.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class Stores extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_stores);


    }




    public void goToElectroPlanet(View view) {
        Intent intent = new Intent(getApplicationContext(),Product_Page.class);
        intent.putExtra("Category",getCategory());
        intent.putExtra("Store","ElectroPlanet");
        startActivity(intent);
    }



    public void goToBim(View view) {
        Intent intent = new Intent(getApplicationContext(),Product_Page.class);
        intent.putExtra("Category",getCategory());
        intent.putExtra("Store","Bim");
        startActivity(intent);
    }

    public void goToCosmos(View view) {
        Intent intent = new Intent(getApplicationContext(),Product_Page.class);
        intent.putExtra("Category",getCategory());
        intent.putExtra("Store","Cosmos");
        startActivity(intent);
    }


    public void goShoppingMaroc(View view) {
        Intent intent = new Intent(getApplicationContext(),Product_Page.class);
        intent.putExtra("Category",getCategory());
        intent.putExtra("Store","ShoppingMaroc");
        startActivity(intent);
    }

    private String getCategory(){
        Bundle extras = getIntent().getExtras();
        String category="";
        if(extras  !=null){
            category = extras.getString("Category");
        }
        return category;
    }
}