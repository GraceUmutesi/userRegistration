package com.example.userregistrationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class signUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ImageView UserPhoto;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText mName,mEmail,mPassword,mConfirmPass,mPhone;

    private Button sButtton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mName = findViewById(R.id.nameOfUserEditText);
        mEmail = findViewById(R.id.emailOfUserEditText);
        mPassword = findViewById(R.id.passwordOfUserEditText);
        mConfirmPass = findViewById(R.id.confirmPasswordEditText);
        mPhone = findViewById(R.id.phone);
        sButtton = findViewById(R.id.createUserButton);
        UserPhoto = findViewById(R.id.userPicture);
//        loading = (ProgressBar) findViewById(R.id.progressBar);
//        loading.setVisibility(View.INVISIBLE);
        mAuth= FirebaseAuth.getInstance();
        createAuthStateListener();
        
        UserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >=22){
                    
                    checkAndRequestForPermission();
                }
                else{
                    openGallery();
                }
            }
        });
        

        mAuth = FirebaseAuth.getInstance();

        sButtton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
//                loading.setVisibility(view.VISIBLE);
                final String name = mName.getText().toString();
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                final String confirmPassword = mPhone.getText().toString();
                final String phone = mName.getText().toString();

                if (name.isEmpty()|| email.isEmpty()||password.isEmpty()||confirmPassword.isEmpty()||phone.isEmpty()){
                    showMessage("Please fill the above fields");
//                    sButtton.setVisibility(View.VISIBLE);
//                    loading.setVisibility(View.INVISIBLE);
                }
                else {
                    CreateAccount(name,email,password,phone);
                }

            }
        });
    }

    private void createAuthStateListener() {
        mAuthListener =new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final  FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!= null){
                    Intent intent = new Intent(signUpActivity.this,HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

            }
        };
    }

    private void openGallery() {
    }

    private void checkAndRequestForPermission() {
    }

    private void CreateAccount(final String name, String email, String password, String phone) {
        //A method to create a user account with the above specs

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //when the account has been created succesfully
                    showMessage("successfully created");

//                    updatedUserInfo(name, pickedImgUri,mAuth.getCurrentUser());


                }
                else {
                    //when the account has failed to be created

                    showMessage("account creation failed");

//                    loading.setVisibility(View.INVISIBLE);


                }

            }
        });




    }
    private void updatedUserInfo(final String name, Uri pickedImgUri, final FirebaseUser currentUser){
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFile = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFile.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
                imageFile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(name).setPhotoUri(uri).build();

                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            showMessage("Registration complete");
                                            updateUi();

                                        }

                                    }
                                });

                    }
                });

            }



        });

    }

    private void updateUi() {
        Intent intent = new Intent(getApplicationContext(), com.example.userregistrationapp.HomeActivity.class);
        startActivity(intent);
        finish();




    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }
}
