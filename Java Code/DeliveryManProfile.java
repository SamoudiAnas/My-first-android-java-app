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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
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

public class DeliveryManProfile extends AppCompatActivity {
    TextInputLayout address, email, Password;
    TextView fullName;
    Button updateButton;
    String fullname, userName, Email, password;
    CircleImageView profilePic;
    FirebaseStorage storage;
    StorageReference storageReference;
    private FirebaseUser user;
    private DatabaseReference reference;

    private String userID;
    static String imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_delivery_man_profile);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Delivery Man");
        userID = user.getUid();

        fullName = findViewById(R.id.DeliveryManFullName);
        address = findViewById(R.id.usernameDeliveryManEdit);
        email = findViewById(R.id.emailDeliveryManEdit);
        Password = findViewById(R.id.passwordDeliveryManEdit);
        updateButton = findViewById(R.id.updateDeliveryMan);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (address.getEditText().getText().toString().isEmpty()) {
                    address.setError("Can't be empty");
                } else {
                    address.setErrorEnabled(false);
                }
                if (email.getEditText().getText().toString().isEmpty()) {
                    email.setError("Can't be empty");
                } else {
                    email.setErrorEnabled(false);
                }
                if (Password.getEditText().getText().toString().isEmpty()) {
                    Password.setError("Can't be empty");
                } else {
                    Password.setErrorEnabled(false);
                }

                String em = email.getEditText().getText().toString();
                user.updateEmail(em).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        reference.child(userID).child("email").setValue(em);
                        Toast.makeText(getApplicationContext(),"email has been changed",Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"email already exist !",Toast.LENGTH_SHORT).show();
                    }
                });
                String password = Password.getEditText().getText().toString();
                user.updatePassword(password).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        reference.child(userID).child("password").setValue(password);
                        Toast.makeText(getApplicationContext(),"password has been changed !",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"password already exist !",Toast.LENGTH_SHORT).show();

                    }
                });

            }

        });
        profilePic = findViewById(R.id.profileImageDeliveryMan);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open gallery
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DeliverAccountCreatedByAdmin userProfile = snapshot.getValue(DeliverAccountCreatedByAdmin.class);

                if (userProfile != null) {
                    fullname = userProfile.fullName;
                    userName = userProfile.address;
                    Email = userProfile.email;
                    password = userProfile.password;

                    fullName.setText(fullname);
                    address.getEditText().setText(userName);
                    email.getEditText().setText(Email);
                    Password.getEditText().setText(password);
                    Glide.with(getApplicationContext()).load(userProfile.getProfilePicture()).into(profilePic);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DeliveryManProfile.this, "Something wrong happened !", Toast.LENGTH_LONG).show();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                profilePic.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);

            }
        }
    }
    private void uploadImageToFirebase(Uri imageUri) {
        // upload image to firebase storage
        final String randomKey = UUID.randomUUID().toString();
        StorageReference fileReference = storageReference.child("Admin Profile Images/*" + randomKey);
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
                    Toast.makeText(getApplicationContext(), ""+imageURL, Toast.LENGTH_SHORT).show();

                    FirebaseDatabase.getInstance().getReference("Admins")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profilePicture").setValue(imageURL);

                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }
}