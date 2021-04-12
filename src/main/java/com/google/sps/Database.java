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

        while (input.hasNextLine()){
            String can = input.nextLine(); 
            String[] stats = can.split("\\|"); 

            CandidateStats canStats = new CandidateStats(
                /*candidate id=*/stats[1], 
                /*state=*/stats[18], 
                /*district=*/stats[19], 
                /*affiliation=*/stats[4], 
                /*contributions from candidate=*/stats[11], 
                /*loans from candidate=*/stats[12], 
                /*other loans=*/stats[13], 
                /*individiual contributions=*/stats[17], 
                /*contributions from political committee=*/stats[25], 
                /*contributions from party committee=*/stats[26]);
                
            Map<String, Object> initialData = new HashMap<>();

            initialData.put(filePath.substring(39, 48), canStats);

            String candidateId = stats[0];

            /* The candidate name must be error-checked as this will become a document ID. 
             * Document IDs cannot contain forward slashes (/). 
             * The bulk data we are working with contains instances of forward slashes.
             * Therefore, these must be replaced to fit these constraints.
             */
        

            DocumentReference doc = db.collection("fec_data").document(candidateId);
            
            ApiFuture<WriteResult> result = doc.set(initialData, SetOptions.merge());

            result.get(); 
        }
        
        input.close();
    }
}