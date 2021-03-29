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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {
    TextInputLayout username, email, PhoneNumber, Password;
    TextView fullName;
    Button updateButton;
    String fullname, userName, Email, phoneNumber, password;
    CircleImageView profilePic;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth auth;

    //reference to the current user
    DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private FirebaseUser user;
    private DatabaseReference reference;

    private String userID;
    private String imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_profile);

        //Hooks

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        auth = FirebaseAuth.getInstance();


        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        fullName = findViewById(R.id.userFullName);
        username = findViewById(R.id.usernameEdit);
        email = findViewById(R.id.emailEdit);
        PhoneNumber = findViewById(R.id.phoneNumberEdit);
        Password = findViewById(R.id.passwordEdit);
        updateButton = findViewById(R.id.update);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (email.getEditText().getText().toString().isEmpty()) {
                    email.setError("Can't be empty");
                }
                if (Password.getEditText().getText().toString().isEmpty()) {
                    Password.setError("Can't be empty");
                }

                String em = email.getEditText().getText().toString();
                user.updateEmail(em).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        reference.child(userID).child("email").setValue(em);
                        Toast.makeText(getApplicationContext(),"Account has been modified!",Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(getApplicationContext(),"Account has been modified!",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"password already exist !",Toast.LENGTH_SHORT).show();

                    }
                });

            }

        });
        profilePic = findViewById(R.id.profileImage);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open gallery
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });

        //Your have to do the same, you just need to change the UserHelperClass to the class that you have used to register driver info

        readData(new FirebaseCallBack() {
            @Override
            public void getUser(UserHelperClass userProfile) {
                if (userProfile != null) {
                    fullname = userProfile.getName();
                    userName = userProfile.getUsername();
                    Email = userProfile.getEmail();
                    phoneNumber = userProfile.getPhone();
                    password = userProfile.getPassword();

                    fullName.setText(fullname);
                    username.getEditText().setText(userName);
                    email.getEditText().setText(Email);
                    PhoneNumber.getEditText().setText(phoneNumber);
                    Password.getEditText().setText(password);
                    Glide.with(getApplicationContext()).load(userProfile.getProfilePicture()).into(profilePic);


                }
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
        StorageReference fileReference = storageReference.child("Profile Images/*" + randomKey);
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
                    Toast.makeText(getApplicationContext(), "" + imageURL, Toast.LENGTH_SHORT).show();

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profilePicture").setValue(imageURL);

                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }

    private void readData(FirebaseCallBack firebaseCallBack){
        //load the list of item from firebase in the list
        ValueEventListener productListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserHelperClass userProfile = dataSnapshot.getValue(UserHelperClass.class);
                firebaseCallBack.getUser(userProfile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //empty
            }
        };
        currentUser.addListenerForSingleValueEvent(productListener);
    }
    private interface FirebaseCallBack{
        void getUser(UserHelperClass userProfile);
    }

}