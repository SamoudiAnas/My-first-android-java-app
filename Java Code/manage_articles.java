package com.example.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class manage_articles extends AppCompatActivity {
    DatabaseReference mainProductRef = FirebaseDatabase.getInstance().getReference("Products").child("Produits");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_articles);

    }




    public void addArticle(View view) {
        Intent intent = new Intent(manage_articles.this,SelectCategory.class);
        startActivity(intent);
    }

    public void editArticle(View view) {
        Intent intent = new Intent(manage_articles.this,SelectCategoryToEdit.class);
        startActivity(intent);


    }


    public void deteleAnArticle(View view) {
        Intent intent = new Intent(manage_articles.this,delete_article.class);
        startActivity(intent);
    }
}