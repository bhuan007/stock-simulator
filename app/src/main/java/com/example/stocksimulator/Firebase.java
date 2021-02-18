package com.example.stocksimulator;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

public class Firebase {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String userName;
    private String uid;
    private String email;

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


        this.db.collection("users").document(this.uid).set(userDoc);
    }

    public String get_userName(){
        return this.userName;
    }

    public String get_id(){
        return this.uid;
    }

    public FirebaseFirestore get_db(){return this.db; }

    public DocumentReference get_reference(){return FirebaseFirestore.getInstance().document(this.uid);}

}
