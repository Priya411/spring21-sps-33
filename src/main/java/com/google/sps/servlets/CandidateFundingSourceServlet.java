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
public class CandidateFundingSourceServlet extends SetupServlet {
    // not tested yet waiting on Henry's chart to test
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try { 
            String candidateName = request.getParameter("name");
            String year = request.getParameter("year");
            ApiFuture<DocumentSnapshot> candidate_snapshot = db.collection("fec_data").document(candidateName).get(); // get candidate
            Object year_data = candidate_snapshot.get().get(year);
            Double conFromCandidate = ((Map<String, Double>) year_data).get("conFromCandidate");
            Double loansFromCandidate = ((Map<String, Double>) year_data).get("loansFromCandidate");
            Double otherLoans = ((Map<String, Double>) year_data).get("otherLoans");
            Double individualCon = ((Map<String, Double>) year_data).get("individualCon");
            Double conFromPoliticalComm = ((Map<String, Double>) year_data).get("conFromPoliticalComm");
            Double conFromPartyComm = ((Map<String, Double>) year_data).get("conFromPartyComm");
            ArrayList<ArrayList<Object>> results = new ArrayList<ArrayList<Object>>();
            ArrayList<Object> heading = new ArrayList<Object>();
            ArrayList<Object> row = new ArrayList<Object>();
            heading.add("Candidate");
            heading.add("Loan Candidate");
            heading.add("Other Loans");
            heading.add("Individuals");
            heading.add("Party");
            heading.add("Comitee");
            results.add(heading);
            row.add(conFromCandidate);
            row.add(loansFromCandidate);
            row.add(otherLoans);
            row.add(individualCon);
            row.add(conFromPoliticalComm);
            row.add(conFromPartyComm);
            results.add(row);
            response.setContentType("application/json;");
            response.getWriter().println(results.toString());

        }
        catch (Exception e) {
            System.out.println("Database query failed: \n"+e);
        }
    }
}
