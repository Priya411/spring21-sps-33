package com.google.sps;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.SetOptions;

import java.util.*;
import java.io.*; 

public class Database {
    private Firestore db;

    public void initialize() throws Exception {
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
        // Initialize instance of Firestore
        FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
                .setProjectId("spring21-sps-33").setCredentials(credentials).build();
        db = firestoreOptions.getService();
    }

    public void addData(String filePath) throws Exception {
        File fecData = new File(filePath);
        
        Scanner input = new Scanner(fecData); 

        System.out.println("Test start \n");

        while (input.hasNextLine()){
            String can = input.nextLine(); 
            String[] stats = can.split("\\|"); 

            CandidateStats canStats = new CandidateStats(stats[0], stats[18], stats[19], stats[4], stats[11], 
            stats[12], stats[13], stats[17], stats[25], stats[26]);
                
            Map<String, Object> initialData = new HashMap<>();

            initialData.put(filePath.substring(39, 48), canStats);
            
            ApiFuture<WriteResult> result; 

            DocumentReference doc = db.collection("fec_data").document(stats[1]);

            try {
                result = doc.set(initialData);
            } catch(Exception e){
                result = doc.update(initialData);
            }

            result.get(); 
        }

        System.out.println("Test end \n");
        
        input.close();
    }
}