package com.example.stocksimulator;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
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
        userDoc.put("login_tracker",0);

        Map<String, Map<String,Object>> stockList=new HashMap<>();

//        Map<String,Object> inner_stock = new HashMap<>();
//        inner_stock.put("invested", 0);
//        inner_stock.put("shares", 0);
//        stockList.put(null)

        userDoc.put("stock_list",stockList);
        ArrayList<String> watchList = new ArrayList<>();
        userDoc.put("watch_list",watchList);
        double wallet = -1;
        userDoc.put("wallet",wallet);
        this.db.collection("users"). document(this.uid).set(userDoc);

    }

    public String get_userName(){
        return this.userName;
    }

    public interface OnSetWallet {
        void onSetWallet();
    }


    public interface OnGetInvestedStock {
        public void getInvestedStock(StockTransaction returnedStock, boolean isOwned);
    }

    public void get_invested_stock(String ticker, OnGetInvestedStock onGetInvestedStock) {

        DocumentReference reference=FirebaseFirestore.getInstance().collection("users").document(this.uid);

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    try {
                        Object stock_map = ((HashMap)(task.getResult().get("stock_list"))).get(ticker);
                        double current_invested = Double.parseDouble(((HashMap) stock_map).get("invested").toString());
                        double current_number_of_shares = Double.parseDouble(((HashMap) stock_map).get("shares").toString());
                        StockTransaction stockTransaction = new StockTransaction(current_invested,current_number_of_shares,ticker);
                        onGetInvestedStock.getInvestedStock(stockTransaction, true);
                    }
                    catch (Exception e) {
                        onGetInvestedStock.getInvestedStock(null, false);
                    }

                }
            }
        });

    }


    public interface OnGetLoginTracker{
        void onGetLoginTracker(int days);
    }
    public void get_login_tracker(OnGetLoginTracker onGetLoginTracker){
        DocumentReference reference=FirebaseFirestore.getInstance().collection("users").document(this.uid);

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    int days = (int)(task.getResult().get("login_tracker"));
                    onGetLoginTracker.onGetLoginTracker(days);

                }
            }

        });

    }


    public interface OnUpdateLoginTracker{
        void onUpdateLoginTracker();
    }
    public void update_login_tracker(OnUpdateLoginTracker onUpdateLoginTracker){
        DocumentReference reference=FirebaseFirestore.getInstance().collection("users").document(this.uid);

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    int days = Integer.valueOf(task.getResult().get("login_tracker").toString());

                    lastSignIn = task.getResult().getTimestamp("last_sign_in");
                    if(lastSignIn==null) {
                        days = 0;
                    }
                    else{
                        Calendar lasttime = Calendar.getInstance();
                        lasttime.setTime(lastSignIn.toDate());
                        lasttime.add(Calendar.HOUR_OF_DAY, 24);
                        Calendar now = Calendar.getInstance();
                        now.setTime(new Date());
                        Calendar future = Calendar.getInstance();
                        future.setTime(lastSignIn.toDate());
                        future.add(Calendar.HOUR_OF_DAY, 48);

                        switch(days){
                            case 0:
                                if(now.after(lasttime) && now.before(future)){
                                    days = 1;
                                }
                                else{
                                    days = 1;
                                }
                                break;
                            case 1:
                                if(now.after(lasttime) && now.before(future)){
                                    days = 2;
                                }
                                else{
                                    days = 1;
                                }
                                break;
                            case 2:
                                if(now.after(lasttime) && now.before(future)){
                                    days = 3;
                                }
                                else{
                                    days = 1;
                                }
                                break;
                            case 3:
                                if(now.after(lasttime) && now.before(future)){
                                    days = 4;
                                }
                                else{
                                    days = 1;
                                }

                                break;
                            case 4:
                                if(now.after(lasttime) && now.before(future)){
                                    days = 5;
                                }
                                else{
                                    days = 1;
                                }

                                break;
                            case 5:
                                if(now.after(lasttime) && now.before(future)){
                                    days = 5;
                                }
                                else{
                                    days = 5;
                                }

                                break;

                            default:
                                break;

                        }

                    }
                    HashMap<String, Object> userDoc = new HashMap<>();

                    userDoc.put("login_track",days);
                    FirebaseFirestore.getInstance().collection("users"). document(uid).update(userDoc);
                    onUpdateLoginTracker.onUpdateLoginTracker();

                }
            }

        });

    }





    public void set_wallet(Context context, OnSetWallet onSetWallet){
        DocumentReference reference=FirebaseFirestore.getInstance().collection("users").document(this.uid);

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    lastSignIn = task.getResult().getTimestamp("last_sign_in");
                    wallet = task.getResult().getDouble("wallet");

                    if(lastSignIn == null && wallet == -1){
                        wallet = 100000d;
                        lastSignIn = Timestamp.now();
                    }

                    else {
                        int days = (int)task.getResult().get("login_tracker");
                        switch (days){
                            case 1:
                                wallet = wallet + 5000;
                                break;
                            case 2:
                                wallet = wallet + 7500;
                                break;
                            case 3:
                                wallet = wallet + 10000;
                                break;
                            case 4:
                                wallet = wallet + 15000;
                                break;
                            case 5:
                                wallet = wallet + 20000;
                                break;
                            default:
                                break;

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

    public interface OnSetStockList {
        void onSetStockList();
    }

    public void update_to_stocklist(StockTransaction stock, OnSetStockList onSetStockList){
        DocumentReference reference=FirebaseFirestore.getInstance().collection("users").document(this.uid);

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    double invested = stock.getInvested_amount();
                    String stock_symbol = stock.getStock_ticker();
                    double number_of_shares = stock.getShare_amount();


                    wallet = task.getResult().getDouble("wallet");

                    HashMap<String,Object> stocklist_map = (HashMap<String, Object>) ((HashMap)(task.getResult().get("stock_list")));
                    HashMap<String,Object> stock_map = (HashMap<String, Object>) ((HashMap)(task.getResult().get("stock_list"))).get(stock_symbol);

                    if(stocklist_map!=null){
                        if (stock_map != null) { //this stock in stocklist

                            double current_invested = Double.parseDouble(stock_map.get("invested").toString());
                            double current_number_of_shares = Double.parseDouble(stock_map.get("shares").toString());

                            if (stock.isBuy()) {

                                Map<String, Object> inner_stock = new HashMap<>();
                                inner_stock.put("invested", current_invested+invested);
                                inner_stock.put("shares", current_number_of_shares+number_of_shares);

                                stocklist_map.put(stock_symbol, inner_stock);

                                HashMap<String, Object> userDoc = new HashMap<>();
                                userDoc.put("wallet", wallet - invested);
                                userDoc.put("stock_list", stocklist_map);
                                FirebaseFirestore.getInstance().collection("users").document(uid).update(userDoc);
                            } else { //sell
                                if((current_number_of_shares - number_of_shares)>0) {
                                    Map<String, Object> inner_stock = new HashMap<>();
                                    inner_stock.put("invested", current_invested - invested);
                                    inner_stock.put("shares", current_number_of_shares - number_of_shares);
                                    stocklist_map.put(stock_symbol, inner_stock);

                                }
                                else{
                                    stocklist_map.remove(stock_symbol);
                                }
                                    HashMap<String, Object> userDoc = new HashMap<>();
                                    userDoc.put("wallet", wallet + invested);
                                    userDoc.put("stock_list", stocklist_map);
                                    FirebaseFirestore.getInstance().collection("users").document(uid).update(userDoc);
                            }
                            onSetStockList.onSetStockList();
                        }
                        else {
                            if (stock.isBuy()) {
                                Map<String, Object> inner_stock = new HashMap<>();
                                inner_stock.put("invested", invested);
                                inner_stock.put("shares", number_of_shares);


                                stocklist_map.put(stock_symbol, inner_stock);

                                HashMap<String, Object> userDoc = new HashMap<>();
                                userDoc.put("wallet", wallet - invested);
                                userDoc.put("stock_list", stocklist_map);
                                FirebaseFirestore.getInstance().collection("users").document(uid).update(userDoc);
                            }
                        }

                    }
                    else{//stocklist is empty
                        if (stock.isBuy()) {
                            Map<String, Object> inner_stock = new HashMap<>();
                            inner_stock.put("invested", invested);
                            inner_stock.put("shares", number_of_shares);

                            Map<String, Object> stocklist = new HashMap<>();
                            stocklist.put(stock_symbol, inner_stock);

                            HashMap<String, Object> userDoc = new HashMap<>();
                            userDoc.put("wallet", wallet - invested);
                            userDoc.put("stock_list", stocklist);
                            FirebaseFirestore.getInstance().collection("users").document(uid).update(userDoc);
                        }

                    }

                }
            }});}



    public interface OnGetStockList {
        void onGetStockList(ArrayList<String> tickers);
    }

    public void get_stocklist(OnGetStockList onGetStockList){
        DocumentReference reference=FirebaseFirestore.getInstance().collection("users").document(this.uid);

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    HashMap stock_map = (HashMap)(task.getResult().get("stock_list"));
                    onGetStockList.onGetStockList(new ArrayList<>(stock_map.keySet()));
                }
            }
        });
    }




    public interface OnAddWatchList {
        void onAddWatchList();
    }

    public void add_to_watchlist(String ticker, OnAddWatchList onAddWatchList) {

            DocumentReference reference=FirebaseFirestore.getInstance().collection("users").document(this.uid);

            reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {
                        HashMap<String, Object> userDoc = new HashMap<>();
                        ArrayList<String> watch_list = new ArrayList<>();

                        if(task.getResult().get("watch_list") != null) {
                            watch_list = (ArrayList)task.getResult().get("watch_list");
                            if(!watch_list.contains(ticker)) {
                                watch_list.add(ticker);
                            }

                        }
                        else {
                            watch_list.add(ticker);
                        }

                        userDoc.put("watch_list", watch_list);

                        FirebaseFirestore.getInstance().collection("users").document(uid).update(userDoc);

                        onAddWatchList.onAddWatchList();

                    }
                }
            });
    }



    public interface OnRemoveWatchList{
        void onRemoveWatchList(String ticker);
    }


    public void remove_watchlist(String ticker, OnRemoveWatchList onRemoveWatchList){
        DocumentReference reference=FirebaseFirestore.getInstance().collection("users").document(this.uid);

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    ArrayList<String> watch_list = (ArrayList) task.getResult().get("watch_list");
                    watch_list.remove(ticker);
                    HashMap<String, Object> userDoc = new HashMap<>();
                    userDoc.put("watch_list", watch_list);
                    FirebaseFirestore.getInstance().collection("users").document(uid).update(userDoc);
                    onRemoveWatchList.onRemoveWatchList(ticker);
                }
            }
        }
    );}



public interface OnGetWatchList {
        void onGetWatchList(ArrayList<String> tickers);
    }

    public void get_watchlist(OnGetWatchList onGetWatchList){
        DocumentReference reference=FirebaseFirestore.getInstance().collection("users").document(this.uid);

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    if(task.getResult().get("watch_list")!=null) {
                        ArrayList<String> watch_list = (ArrayList) task.getResult().get("watch_list");
                        onGetWatchList.onGetWatchList(new ArrayList<>(watch_list));
                    }
                }
            }
        });}
}
