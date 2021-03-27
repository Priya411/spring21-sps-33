package com.google.sps;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.firestore.DocumentReference;

import java.util.*;

public class Database {
    private Firestore db;

    public void initialize() throws Exception {
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
        // Initialize instance of Firestore
        FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
                .setProjectId("spring21-sps-33").setCredentials(credentials).build();
        db = firestoreOptions.getService();
    }

    public void addData(String document) throws Exception {
        DocumentReference docRef = db.collection("fec_data").document("bernie");
        // Add document data  with id "alovelace" using a hashmap
        Map<String, Object> data = new HashMap<>();
        data.put("first", "Bernie");
        data.put("last", "Sanders");
        data.put("from", "Vermont");

        //asynchronously write data
        ApiFuture<WriteResult> result = docRef.set(data);

        // ...
        // result.get() blocks on response
        System.out.println("Update time : " + result.get().getUpdateTime());
    }
}