package com.google.sps.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;

@WebServlet("/candidatePage")
public class CandidatePageServlet extends SetupServlet {
    // not tested yet waiting on Henry's chart to test
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try { 
            String candidateId = request.getParameter("candidateId");
            System.out.println("CandidatePageServlet accessed, candidateId = " + candidateId);  // Check candidateId in console
            //candidateId = "P20003851";  // Hardcoded temporary solution for the first candidate
            ApiFuture<DocumentSnapshot> candidate_snapshot = db.collection("fec_data").document(candidateId).get(); // get candidate
            Map<String,Object> years =  candidate_snapshot.get().getData();
            ArrayList<ArrayList<Object>> results = new ArrayList<ArrayList<Object>>();
            ArrayList<Object> heading = new ArrayList<Object>();
            heading.add("Year");
            heading.add("Total Contributions");
            results.add(heading);
            String name = " "; 
            String y = " ";
            Double totalC = 0.00;
            for(Map.Entry<String, Object> year : years.entrySet()) {
                y = year.getKey();
                name = ((Map<String, String>) year.getValue()).get("name");
                totalC = ((Map<String, Double>) year.getValue()).get("totalContributions");
                ArrayList<Object> row = new ArrayList<Object>();
                row.add(y);
                row.add(totalC);
                results.add(row);
                System.out.println(y + " " + totalC + " " + row); // Shows that the candidate's stats are being gathered properly.
            }

            //response.setContentType("application/json;");
            //response.getWriter().println(results.toString());
            System.out.println("results.toString() = " + results.toString());      
            
            // you may have to copy the processing code from the file CandidateFundingSourceServlet if you want that information too

            // To-Do: Pass the array "results" to function drawDynamicLineChart() in script.js, change line 68 in this file to  "<div id=\"dynamicLineChart\"", finish styling
            String insertName = "<p>Information for " + name + "</p>";
            response.getWriter().println("<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                    "<meta charset=\"UTF-8\">" +
                    "<title>Candidate Chart Page</title>" +
                    "<link rel=\"preconnect\" href=\"https://fonts.gstatic.com\">" +
                    "<link href=\"https://fonts.googleapis.com/css2?family=Commissioner&family=Open+Sans&display=swap\" rel=\"stylesheet\">" +
                    "<link rel=\"stylesheet\" href=\"style.css\">" +
                    "<script src=\"https://www.gstatic.com/firebasejs/8.3.2/firebase-app.js\"></script>" +
                    "<script src=\"https://www.gstatic.com/firebasejs/8.3.2/firebase-firestore.js\"></script>" +
                    "<script src=\"https://www.gstatic.com/charts/loader.js\"></script>" +
                    "<script src=\"script.js\"></script>" +
                "</head>" +
                "<body>" +
                    "<div id=\"content\">" +
                        insertName +
                        "<div id=\"linechart\" style=\"width:500px; height:600px\"></div>" +
                        "<p> Chart here </p>" +
                    "</div>" +
                "</body>" +
                "</html>");
        }
        catch (Exception e) {
            System.out.println("Database query failed in cps: \n"+e);
        }
    }
}