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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";
    private FirebaseAuth mAuth;
    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mSignInButton;
    private Button mSignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        // Views
        mEmailField = findViewById(R.id.editTextTextEmailAddress);
        mEmailField.setHint("Email");
        mPasswordField = findViewById(R.id.editTextTextPassword);
        mSignInButton = findViewById(R.id.loginButton);

        mSignUpButton = findViewById(R.id.signupButton);

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
            onSignInAuthSuccess();
        }

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
                            onSignInAuthSuccess();
                            Toast.makeText(LoginActivity.this, "Sign In Successfully",
                                    Toast.LENGTH_LONG).show();

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
                            onSignUpAuthSuccess();
                            Toast.makeText(LoginActivity.this, "Sign Up Successfully",
                                    Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void onSignInAuthSuccess() {

        Firebase firebase = new Firebase();
        firebase.updateLoginTracker(new Firebase.OnUpdateLoginTracker() {
            @Override
            public void onUpdateLoginTracker() {
            }
        });

        initAlarm();

        firebase.setWallet(getApplicationContext(), new Firebase.OnSetWallet() {
            @Override
            public void onSetWallet() {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

    private void onSignUpAuthSuccess() {

        Firebase firebase = new Firebase();
        firebase.writeNewUser();
    }

    private void initAlarm() {
        Firebase firebase = new Firebase();
        Calendar calendar = Calendar.getInstance();
        Calendar currentTime = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 20);
        calendar.set(Calendar.SECOND, 0);
        firebase.getLastSignInDate(new Firebase.OnGetLastSignInDate() {
            @Override
            public void onGetLastSignInDate(Calendar lastSignInReturned) {
                Log.d(TAG, "onGetLastSignInDate: lastSignIn is " + lastSignInReturned.getTime().toString());
                if (currentTime.get(Calendar.DAY_OF_MONTH) > lastSignInReturned.get(Calendar.DAY_OF_MONTH)) {
                    Log.d(TAG, "onGetLastSignInDate: About to set alarm");
                    AlarmHelper.initAlarm(getApplicationContext(), 0, calendar.getTimeInMillis(), 86400000);
                    Log.d(TAG, "initAlarm: alarm set");
                }
            }
        });

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


}