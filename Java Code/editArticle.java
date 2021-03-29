package com.example.application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class editArticle extends AppCompatActivity {



    EditText productName, productSeller, productPrice, productDescription;
    Button EditArticle;
    ImageView productImage;
    private Uri productImageUri;
    private DatabaseReference products;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private String imageURL;
    public int positionOfProductInList;




    public List<MainProduct> myProductList = new ArrayList<MainProduct>();
    public static List<String> keys = new ArrayList<>();
    public Button confirmEdit;
    public static String URL;
    public static String name, description;
    public static int price;
    public static MainProduct productToEdit = new MainProduct();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_article);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        //Content
        productName = (EditText) findViewById(R.id.productName);
        productPrice = (EditText) findViewById(R.id.productPrice);
        productDescription = (EditText) findViewById(R.id.descriptionText);



        getDataBaseRef();


        confirmEdit = findViewById(R.id.editArticle);
        confirmEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                readData(new FirebaseCallBack() {
                    @Override
                    public void getCartList(List<MainProduct> list) {
                        productToEdit = list.get(positionOfProductInList);
                        if(productName.getText().toString() != ""){
                            name = productName.getText().toString();
                        }

                        if(productPrice.getText().toString()!= ""){
                            String  Price= productPrice.getText().toString().trim();
                            price= Integer.parseInt(Price);
                        }

                        if(productDescription.getText().toString() != ""){
                            description = productDescription.getText().toString();
                        }

                        productToEdit.setName(name);
                        productToEdit.setImage(URL);
                        productToEdit.setDescription(description);
                        productToEdit.setPrice(price);


                        getDataBaseRef();
                        products.child(productToEdit.getpId()).setValue(productToEdit).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(),"Changes are made!",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }



    // CHANGE IMAGE CODE
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

                // Continue with the task to get the download URL
                return productsImagesRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    URL = downloadUri.toString();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });














    }


    private void getDataBaseRef(){
        //Getting extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            positionOfProductInList = extras.getInt("Product Position");
            String reference = extras.getString("Reference");
            String store = extras.getString("Store");
            products = FirebaseDatabase.getInstance().getReference(reference).child(store);
        }
    }

    private void readData(FirebaseCallBack myCallBack){
        getDataBaseRef();
        ValueEventListener cartListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<MainProduct> list = new ArrayList<>();

                //get the data in a list
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    MainProduct product = snapshot.getValue(MainProduct.class);
                    list.add(product);
                }

                //calculatePrice();
                //get the data on callback
                myCallBack.getCartList(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        products.addListenerForSingleValueEvent(cartListener);

    }

    private interface FirebaseCallBack{
        void getCartList(List<MainProduct> list);
    }
}