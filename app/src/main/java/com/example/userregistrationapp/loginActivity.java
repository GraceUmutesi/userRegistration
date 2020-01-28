package com.example.userregistrationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = loginActivity.class.getSimpleName();
    private EditText mEmail,mPassword;
    private TextView mChoiceTextView;
    private Button mButton;
    private ProgressDialog mAuthProgressSpec;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmail = findViewById(R.id.emailOfUserEditText);
        mPassword = findViewById(R.id.passwordOfUserEditText);
        mChoiceTextView = findViewById(R.id.signupTextView);
        mButton = findViewById(R.id.loginUserButton);

        mAuth = FirebaseAuth.getInstance();
        mChoiceTextView.setOnClickListener(this);
        mButton.setOnClickListener(this);
        createAuthDialog();
        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(loginActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };

    }

    private void createAuthDialog() {
        mAuthProgressSpec = new ProgressDialog(this);
        mAuthProgressSpec.setTitle("Loading...");
        mAuthProgressSpec.setMessage("Authenticating with Firebase...");
        mAuthProgressSpec.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        if (v == mChoiceTextView) {
            Intent intent = new Intent(loginActivity.this, signUpActivity.class);
            startActivity(intent);
            finish();
        }
        if (v== mButton) {
            loginWithPassword();
        }

    }

    private void loginWithPassword() {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        if (email.equals("")) {
            mEmail.setError("Please enter your email");
            return;
        }

        if (password.equals("")) {
            mPassword.setError("Password cannot be blank");
            return;
        }
        mAuthProgressSpec.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mAuthProgressSpec.dismiss();
                Intent intent = new Intent(loginActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInWithEmail", task.getException());
                    Toast.makeText(loginActivity.this, "Authentication done.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
