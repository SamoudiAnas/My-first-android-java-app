package com.example.application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Random;
import java.util.UUID;

public class addArticles extends AppCompatActivity {

    EditText productName, productSeller, productPrice, productDescription,productImageText;
    Button AddArticle;
    ImageView productImage;
    private Uri productImageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private String imageURL;

    String reference;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_articles);



        Bundle extras = getIntent().getExtras();
        if(extras != null){
            reference = extras.getString("Reference");
        }
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Products").child(reference);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        productSeller = (EditText) findViewById(R.id.productSeller);
        productName = (EditText) findViewById(R.id.productName);
        productImageText = (EditText) findViewById(R.id.productImage);

        productPrice = (EditText) findViewById(R.id.productPrice);
        productDescription = (EditText) findViewById(R.id.descriptionText);


        AddArticle = (Button) findViewById(R.id.addNewProduct);

        AddArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name, Price, Description,store;
                int price;

                Name = productName.getText().toString();

                Price = productPrice.getText().toString();
                price = Integer.parseInt(Price);
                Description = productDescription.getText().toString();
                store = productSeller.getText().toString();

                String pid =  UUID.randomUUID().toString();
                MainProduct newProduct = new MainProduct(pid,imageURL,Name,Description,price,store);
                DatabaseReference newStore = database.child(store);

                newStore.child(pid).setValue(newProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(addArticles.this,"Product Added Successfully!", Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });




        



    }




    public void chooseImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an Image "), 1);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            productImageUri = data.getData();
            //productImage.setImageURI(productImageUri);
            uploadImage();
        }

    }

    private void uploadImage() {
//        final ProgressDialog pd = new ProgressDialog(getApplicationContext());
//        pd.setTitle("Uploading image ...");
//        pd.show();
        //random key
        final String randomKey = UUID.randomUUID().toString();
        StorageReference productsImagesRef = storageReference.child("images/"+randomKey);

        UploadTask uploadTask = productsImagesRef.putFile(productImageUri);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                Toast.makeText(getApplicationContext(),"Image has been uploaded Successfully",Toast.LENGTH_SHORT).show();
                productImageText.setText("Image uploaded!");

                // Continue with the task to get the download URL
                return productsImagesRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    imageURL = downloadUri.toString();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });














    }
}