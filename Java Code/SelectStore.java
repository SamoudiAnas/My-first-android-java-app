package com.example.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.FirebaseDatabase;

public class SelectStore extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_store);
    }

    public void goToShoppingMaroc(View view) {
        Intent intent = new Intent(getApplicationContext(),products_to_edit.class);
        intent.putExtra("Reference",getReference());
        intent.putExtra("Store","ShoppingMaroc");
        startActivity(intent);
    }

    public void goToBim(View view) {
        Intent intent = new Intent(getApplicationContext(),products_to_edit.class);
        intent.putExtra("Reference",getReference());
        intent.putExtra("Store","Bim");
        startActivity(intent);
    }
    public void goToCosmos(View view) {
        Intent intent = new Intent(getApplicationContext(),products_to_edit.class);
        intent.putExtra("Reference",getReference());
        intent.putExtra("Store","Cosmos");
        startActivity(intent);
    }
    public void goToElectroPlanet(View view) {
        Intent intent = new Intent(getApplicationContext(),products_to_edit.class);
        intent.putExtra("Reference",getReference());
        intent.putExtra("Store","ElectroPlanet");
        startActivity(intent);
    }

    private String getReference(){
        Bundle extras = getIntent().getExtras();
        String reference="";
        if (extras != null) {
           reference = extras.getString("Reference");
        }
        return reference;
    }

}