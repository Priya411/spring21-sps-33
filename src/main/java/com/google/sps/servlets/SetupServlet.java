package com.google.sps.servlets;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.http.HttpClient;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

/**
 * Handles requests sent to the /hello URL. Try running a server and navigating
 * to /hello!
 */
public class SetupServlet extends HttpServlet {
    protected HttpClient httpClient;
    protected String fec_api_key;
    protected Firestore db;

    private void set_fec_api_key() {
        // get api key
        try {
            final BufferedReader api_file = new BufferedReader(new FileReader("fec_api_key.txt"));
            fec_api_key = api_file.readLine();
            api_file.close();
        }
        catch (IOException e) {
            System.out.println("Unable to set FEC API KEY.");
        }
    }

    private void intitialize_firestore() {
        try {
            GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
            // Initialize instance of Firestore
            FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
                    .setProjectId("spring21-sps-33").setCredentials(credentials).build();
            db = firestoreOptions.getService();
        }
        catch (IOException e) {
            System.out.println("Unable to setup Firestore connection.");
        }
    }

    public void init() throws ServletException {
        super.init();
        // set up http client, fec api key, and firestore
        httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        set_fec_api_key();
        intitialize_firestore();
    }
}
