package com.example.stocksimulator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LoginActivity extends AppCompatActivity {

        private static final String TAG = "SignInActivity";

        private DatabaseReference mDatabase;
        private FirebaseAuth mAuth;

        private EditText mEmailField;
        private EditText mPasswordField;
        private Button mSignInButton;
        private Button mSignUpButton;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            mDatabase = FirebaseDatabase.getInstance().getReference();
            mAuth = FirebaseAuth.getInstance();


            // Views
            mEmailField = findViewById(R.id.editTextTextEmailAddress);
            mEmailField.setHint("Email");
            mPasswordField = findViewById(R.id.editTextTextPassword);
            mSignInButton = findViewById(R.id.loginButton);

            mSignUpButton = findViewById(R.id.signupButton);
            // Click listeners
//            mSignInButton.setOnClickListener(this);
//            mSignUpButton.setOnClickListener(this);

            mSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signIn();
                }
            });

            mSignUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signUp();
                }
            });

            if (mAuth.getCurrentUser() != null) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void signIn() {
            Log.d(TAG, "signIn");
            if (!validateForm()) {
                return;
            }

            String email = mEmailField.getText().toString();
            String password = mPasswordField.getText().toString();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());

                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Sign In Successfully",
                                        Toast.LENGTH_LONG).show();
                                onAuthSuccess(task.getResult().getUser());

                            } else {
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

        private void signUp() {
            Log.d(TAG, "signUp");
            if (!validateForm()) {
                return;
            }

            String email = mEmailField.getText().toString();
            String password = mPasswordField.getText().toString();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());

                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Sign Up Successfully",
                                        Toast.LENGTH_LONG).show();
                                onAuthSuccess(task.getResult().getUser());

                            } else {
                                Toast.makeText(LoginActivity.this,  task.getException().getMessage() ,
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

        private void onAuthSuccess(FirebaseUser user) {
            String username = usernameFromEmail(user.getEmail());


            // Write new user
            writeNewUser(user.getUid(), username, user.getEmail());

            // Go to MainActivity
            startActivity(new Intent(LoginActivity.this, MainActivity.class));

        }

        private String usernameFromEmail(String email) {
            if (email.contains("@")) {
                return email.split("@")[0];
            } else {
                return email;
            }
        }

        private boolean validateForm() {
            boolean result = true;
            if (TextUtils.isEmpty(mEmailField.getText().toString())) {
                mEmailField.setError("Required");
                result = false;
            } else {
                mEmailField.setError(null);
            }

            if (TextUtils.isEmpty(mPasswordField.getText().toString())) {
                mPasswordField.setError("Required");
                result = false;
            } else {
                mPasswordField.setError(null);
            }

            return result;
        }

        // [START basic_write]
        private void writeNewUser(String userId, String name, String email) {
            User user = new User(name, email);

            mDatabase.child("users").child(userId).setValue(user);
        }
        // [END basic_write]


}