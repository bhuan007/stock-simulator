package com.example.stocksimulator;

import android.content.Context;
import android.util.Log;

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
    private boolean bonusReceived = false;

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

    public void writeNewUser() {

        HashMap<String, Object> userDoc = new HashMap<>();
        Timestamp lastSignIn=null;
        userDoc.put("last_sign_in",lastSignIn);
        userDoc.put("login_track",0);
        userDoc.put("bonus_received",false);

        Map<String, Map<String,Object>> stockList=new HashMap<>();


        userDoc.put("stock_list",stockList);
        ArrayList<String> watchList = new ArrayList<>();
        userDoc.put("watch_list",watchList);
        double wallet = -1;
        userDoc.put("wallet",wallet);
        this.db.collection("users"). document(this.uid).set(userDoc);

    }

    public String getUserName(){
        return this.userName;
    }

    public interface OnSetWallet {
        void onSetWallet();
    }




    public interface OnGetBonusReceived {
        public void getBonusReceived(boolean bonusReceived);
    }

    public void getBonusReceived(OnGetBonusReceived onGetBonusReceived) {

        DocumentReference reference=FirebaseFirestore.getInstance().collection("users").document(this.uid);

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    bonusReceived = task.getResult().getBoolean("bonus_received");
                    onGetBonusReceived.getBonusReceived(bonusReceived);

                }
            }
        });

    }





    public interface OnGetInvestedStock {
        public void getInvestedStock(StockTransaction returnedStock, boolean isOwned);
    }

    public void getInvestedStock(String ticker, OnGetInvestedStock onGetInvestedStock) {

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
    public void getLoginTracker(OnGetLoginTracker onGetLoginTracker){
        DocumentReference reference=FirebaseFirestore.getInstance().collection("users").document(this.uid);

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    int days = Integer.parseInt(task.getResult().get("login_track").toString());
                    onGetLoginTracker.onGetLoginTracker(days);
                }
            }

        });

    }

    public interface OnGetLastSignInDate {
        void onGetLastSignInDate(Calendar lastSignInReturned);
    }

    public void getLastSignInDate(OnGetLastSignInDate onGetLastSignInDate) {
        DocumentReference reference = FirebaseFirestore.getInstance().collection("users").document(this.uid);
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    lastSignIn = task.getResult().getTimestamp("last_sign_in");
                    Calendar lastTime = Calendar.getInstance();
                    lastTime.setTime(lastSignIn.toDate());
                    onGetLastSignInDate.onGetLastSignInDate(lastTime);
                }
            }
        });

    }


    public interface OnUpdateLoginTracker{
        void onUpdateLoginTracker();
    }
    public void updateLoginTracker(OnUpdateLoginTracker onUpdateLoginTracker){
        DocumentReference reference=FirebaseFirestore.getInstance().collection("users").document(this.uid);

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    int days = Integer.valueOf(task.getResult().get("login_track").toString());
                    lastSignIn = task.getResult().getTimestamp("last_sign_in");
                    if(lastSignIn == null) {
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
                                    bonusReceived = false;
                                }
                                else{
                                    days = 1;
                                    bonusReceived = true;
                                }
                                break;
                            case 1:
                                if(now.after(lasttime) && now.before(future)){
                                    days = 2;
                                    bonusReceived = false;
                                }
                                else{
                                    days = 1;
                                    bonusReceived = true;
                                }
                                break;
                            case 2:
                                if(now.after(lasttime) && now.before(future)){
                                    days = 3;
                                    bonusReceived = false;
                                }
                                else{
                                    days = 1;
                                    bonusReceived = true;
                                }
                                break;
                            case 3:
                                if(now.after(lasttime) && now.before(future)){
                                    days = 4;
                                    bonusReceived = false;
                                }
                                else{
                                    days = 1;
                                    bonusReceived = true;
                                }

                                break;
                            case 4:
                                if(now.after(lasttime) && now.before(future)){
                                    days = 5;
                                    bonusReceived = false;
                                }
                                else{
                                    days = 1;
                                    bonusReceived = true;
                                }

                                break;
                            case 5:
                                if(now.after(lasttime) && now.before(future)){
                                    days = 5;
                                    bonusReceived = false;
                                }
                                else{
                                    days = 5;
                                    bonusReceived = true;
                                }

                                break;

                            default:
                                break;

                        }

                    }
                    HashMap<String, Object> userDoc = new HashMap<>();

                    userDoc.put("login_track",days);
                    userDoc.put("bonus_received",bonusReceived);
                    FirebaseFirestore.getInstance().collection("users"). document(uid).update(userDoc);
                    onUpdateLoginTracker.onUpdateLoginTracker();

                }
            }

        });

    }





    public void setWallet(Context context, OnSetWallet onSetWallet){
        DocumentReference reference=FirebaseFirestore.getInstance().collection("users").document(this.uid);

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    lastSignIn = task.getResult().getTimestamp("last_sign_in");
                    wallet = task.getResult().getDouble("wallet");
                    bonusReceived = task.getResult().getBoolean("bonus_received");

                    if(lastSignIn == null && wallet == -1){
                        wallet = 100000d;
                        lastSignIn = Timestamp.now();
                    }

                    else if(!bonusReceived) {
                        int days = Integer.parseInt(task.getResult().get("login_track").toString());
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

    public void getWallet(OnGetWallet onGetWallet){
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

    public void updateToStocklist(StockTransaction stock, OnSetStockList onSetStockList){
        DocumentReference reference=FirebaseFirestore.getInstance().collection("users").document(this.uid);

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    double invested = stock.getInvestedAmount();
                    String stock_symbol = stock.getStockTicker();
                    double number_of_shares = stock.getShareAmount();


                    wallet = task.getResult().getDouble("wallet");

                    HashMap<String,Object> stocklist_map = (HashMap<String, Object>) ((HashMap)(task.getResult().get("stock_list")));
                    HashMap<String,Object> stock_map = (HashMap<String, Object>) ((HashMap)(task.getResult().get("stock_list"))).get(stock_symbol);

                    if(stocklist_map!=null){
                        if (stock_map != null) {
                            //this stock in stocklist

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
                            } else {
                                //sell
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
                    else{
                        //stocklist is empty
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

    public void getStocklist(OnGetStockList onGetStockList){
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

    public void addToWatchlist(String ticker, OnAddWatchList onAddWatchList) {
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


    public void removeWatchlist(String ticker, OnRemoveWatchList onRemoveWatchList){
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

    public void getWatchlist(OnGetWatchList onGetWatchList){
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
