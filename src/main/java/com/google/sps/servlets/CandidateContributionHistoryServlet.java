package com.google.sps.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;

@WebServlet("/candidatecontributionhistory")
public class CandidateContributionHistoryServlet extends SetupServlet {
    // not tested yet waiting on Henry's chart to test
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try { 
            String candidateId = request.getParameter("candidateId");
            ApiFuture<DocumentSnapshot> candidate_snapshot = db.collection("fec_data").document(candidateId).get(); // get candidate
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
