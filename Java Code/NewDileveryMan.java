package com.example.application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewDileveryMan extends AppCompatActivity {
    //Variables
    EditText fullName, address, emailOfD, passwordOfD;
    Button addBtn;
    ProgressBar progressBar;
    TextView fullname;
    CircleImageView  profilePictureOfDeliveryMan,profileImageOfAdmin;

    private FirebaseUser user;
    private DatabaseReference reference;

    private String userID;

    private FirebaseAuth mAuth;

    private StorageReference storageReference;
    private FirebaseStorage storage;
    private static String imageURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_new_dilevery_man);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Admins");
        userID = user.getUid();

        mAuth = FirebaseAuth.getInstance();

        //fullname = findViewById(R.id.nameOfAdmin);
        fullName = findViewById(R.id.fullNameOfDeliveryMan);
        address = findViewById(R.id.addressOfDeliveyMan);
        profilePictureOfDeliveryMan = findViewById(R.id.profileImageOfDeliveryMan);
        emailOfD = findViewById(R.id.emailOfDeliveryMan);
        passwordOfD = findViewById(R.id.passwordOfDeliveryMan);
        profileImageOfAdmin = findViewById(R.id.profileImageAdmin);
        progressBar=findViewById(R.id.progressBarOfDeliveryMan);


        addBtn = findViewById(R.id.addDeliveryManBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerDeliveryMan();
            }
        });

        profilePictureOfDeliveryMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open gallery
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });

//        // for the admin FullName and profile picture.
//        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                UserHelperClass userProfile = snapshot.getValue(UserHelperClass.class);
//
//                if (userProfile != null) {
//                    String fullName = userProfile.name;
//                    fullname.setText(fullName);
//                    String profilePicture = userProfile.getProfilePicture();
//                    Glide.with(getApplicationContext()).load(profilePicture).into(profileImageOfAdmin);
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(NewDileveryMan.this, "Something wrong happened !", Toast.LENGTH_LONG).show();
//            }
//        });
    }

    private void registerDeliveryMan() {
        String fName = fullName.getText().toString();
        String addresse = address.getText().toString();
        String emailDeL = emailOfD.getText().toString();
        String passwordDeL = passwordOfD.getText().toString();

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(emailDeL, passwordDeL).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    DeliverAccountCreatedByAdmin deliverAccountCreatedByAdmin = new DeliverAccountCreatedByAdmin(fName, addresse, emailDeL, passwordDeL,imageURL);
                    FirebaseDatabase.getInstance().getReference("Delivery Man").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(deliverAccountCreatedByAdmin).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(NewDileveryMan.this, "Delivery Man has been registered successfully", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(NewDileveryMan.this, "Failed to register ! Try again.", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                } else {
                    Toast.makeText(NewDileveryMan.this, "Failed to register the new deliver ! Try again.", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        startActivity(new Intent(getApplicationContext(),admin_dashboard.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                profilePictureOfDeliveryMan.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);

            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        // upload image to firebase storage
        final String randomKey = UUID.randomUUID().toString();
        StorageReference fileReference = storageReference.child("Delivery Men profile pictures/*" + randomKey);
        UploadTask uploadTask = fileReference.putFile(imageUri);
        Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                Toast.makeText(getApplicationContext(), "Image has been uploaded Successfully !", Toast.LENGTH_SHORT).show();
                // Continue with the task to get the download URL
                return fileReference.getDownloadUrl();
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