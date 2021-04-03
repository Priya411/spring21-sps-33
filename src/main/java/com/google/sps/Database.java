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

            CandidateStats canStats = new CandidateStats(stats[0], stats[18], stats[19], stats[4], stats[11], 
            stats[12], stats[13], stats[17], stats[25], stats[26]);
                
            Map<String, Object> initialData = new HashMap<>();

            initialData.put(filePath.substring(39, 48), canStats);

            String candidateName = stats[1];

            /* The candidate name must be error-checked as this will become a document ID. 
             * Document IDs cannot contain forward slashes (/). 
             * The bulk data we are working with contains instances of forward slashes.
             * Therefore, these must be replaced to fit these constraints.
             */
        
            if (candidateName.contains("/")){
                candidateName = candidateName.replace("/", "-");
            }

            DocumentReference doc = db.collection("fec_data").document(candidateName);
            
            ApiFuture<WriteResult> result = doc.set(initialData, SetOptions.merge());

            result.get(); 
        }
        
        input.close();
    }
}