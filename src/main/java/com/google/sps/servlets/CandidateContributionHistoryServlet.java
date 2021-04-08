package com.google.sps.servlets;

import java.io.IOException;
import java.net.URI;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.Query.Direction;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;

@WebServlet("/candidatecontributionhistory")
public class CandidateContributionHistoryServlet extends SetupServlet {

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try { 
            String candidateName = request.getParameter("name");
            ApiFuture<DocumentSnapshot> candidate_snapshot = db.collection("fec_data").document(candidateName).get(); // get candidate
            Map<String,Object> years =  candidate_snapshot.get().getData();
            ArrayList<ArrayList<Object>> results = new ArrayList<ArrayList<Object>>();
            ArrayList<Object> heading = new ArrayList<Object>();
            heading.add("Year");
            heading.add("Total Contributions");
            results.add(heading);
            for(Map.Entry<String, Object> year : years.entrySet()) {
                String y = year.getKey();
                Double totalC = ((Map<String, Double>) year.getValue()).get("totalContributions");
                ArrayList<Object> row = new ArrayList<Object>();
                row.add(y);
                row.add(totalC);
                results.add(row);
            }
            response.setContentType("application/json;");
            response.getWriter().println(results.toString());
        }
        catch (Exception e) {
            System.out.println("Database query failed: \n"+e);
        }
    }
}
