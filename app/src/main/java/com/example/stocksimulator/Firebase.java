package com.example.stocksimulator;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Firebase {

    private static final String TAG = "Firebase";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String userName;
    private String uid;
    private String email;
    private Timestamp lastSignIn;
//    private DocumentReference reference;
    private double wallet;

    Firebase() {
        this.uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        this.email = mAuth.getCurrentUser().getEmail();
        usernameFromEmail();

    }

    private void usernameFromEmail() {
        if (this.email.contains("@")) {
            this.userName = this.email.split("@")[0];
        } else {
            this.userName = this.email;
        }
    }

    void writeNewUser() {

        HashMap<String, Object> userDoc = new HashMap<>();
        Timestamp lastSignIn=null;
        userDoc.put("last_sign_in",lastSignIn);
        Map<String, Map<String,Integer>> stockList=null;
        userDoc.put("stock_list",stockList);
        String[] watchList = null;
        userDoc.put("watch_list",watchList);
        double wallet = -1;
        userDoc.put("wallet",wallet);
        this.db.collection("users"). document(this.uid).set(userDoc);

//        this.reference=FirebaseFirestore.getInstance().collection("users").document(this.uid);
    }

    public String get_userName(){
        return this.userName;
    }

    public interface OnSetWallet {
        void onSetWallet();
    }

    public void set_wallet(Context context, OnSetWallet onSetWallet){
        DocumentReference reference=FirebaseFirestore.getInstance().collection("users").document(this.uid);

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    lastSignIn = task.getResult().getTimestamp("last_sign_in");
                    wallet = task.getResult().getDouble("wallet");

                    if(lastSignIn==null && wallet==-1){
                        wallet=100000d;
                        lastSignIn=Timestamp.now();
                    }

                    else {
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.setTime(lastSignIn.toDate());
                        calendar1.add(Calendar.HOUR_OF_DAY, 24);
                        Calendar calendar2 = Calendar.getInstance();
                        calendar2.setTime(new Date());
                        if(calendar2.after(calendar1)){
                            wallet=wallet+5000;
                            Toast.makeText(context, "Added 5k to your wallet", Toast.LENGTH_SHORT).show();
                        }
                    }
                    HashMap<String, Object> userDoc = new HashMap<>();
                    userDoc.put("last_sign_in",Timestamp.now());
                    userDoc.put("wallet",wallet);
                    FirebaseFirestore.getInstance().collection("users"). document(uid).update(userDoc);

                    onSetWallet.onSetWallet();
                }
            }
        });

    }

    public interface OnGetWallet {
        void onGetWallet(Double resultWallet);
    }

    public void get_wallet(OnGetWallet onGetWallet){
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Double wallet = document.getDouble("wallet");
                        onGetWallet.onGetWallet(wallet);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


    public FirebaseFirestore get_db(){return this.db; }


}
