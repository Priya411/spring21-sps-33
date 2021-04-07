package com.google.sps;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.Query.Direction;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.DocumentSnapshot;

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


        // Database Queries Setup:
        // This could lead to having a table that repsonds in real-time to partial phrases typed by users, or sorts based on filters chosen by users.
    
        Query query = db.collection("fec_data").whereEqualTo("2019-2020.affiliation", "DEM").limit(5); // Access the fields of the map by using the "." operator

        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        // Queries Demo showing that we can get database stats, perform math on them, then display those values:
        // This can be useful for finding the top 50 contribution earners for any period, as well as for totaling the contributions per party for any period.
        Double candidateTotal = 0.00; // Use a Double because some candidates have cents while others do not.
        Double runningTotal = 0.00;

        System.out.println("\n\nStart of Testing \n\n");
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            System.out.println("Current Candidate: " + document.getId());
            candidateTotal = Double.parseDouble(document.getString("2019-2020.conFromCandidate")) + Double.parseDouble(document.getString("2019-2020.individualCon"));  
            runningTotal += Double.parseDouble(document.getString("2019-2020.conFromCandidate")) + Double.parseDouble(document.getString("2019-2020.individualCon"));
            System.out.println("Candidate's total = " + candidateTotal + "\n");
        }
        System.out.printf("Grand total = %.2f \n", runningTotal);   // Example of using a formatted print to display two decimal points. Only necessary when user will see number on screen.
    }

    public void addData(String filePath) throws Exception {
        File fecData = new File(filePath);
        
        Scanner input = new Scanner(fecData); 

        while (input.hasNextLine()){
            String can = input.nextLine();
            String[] stats = can.split("\\|");

            CandidateStats canStats = new CandidateStats(
                /*candidate id=*/stats[0], 
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