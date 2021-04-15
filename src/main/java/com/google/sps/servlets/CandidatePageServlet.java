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
            // This line is getting null candidateIds when clicking a name in the defaultT50 table. 
            // Inspect this by uncommenting the System.out.println lines, running the server, clicking a name in the table, and looking at your cloudshell terminal.
            String candidateId = request.getParameter("candidateId");
            System.out.println("canpageserv accessed, candidateId = " + candidateId);
            candidateId = "P20003851";  // Hardcoded temporary solution for the first candidate
            ApiFuture<DocumentSnapshot> candidate_snapshot = db.collection("fec_data").document(candidateId).get(); // get candidate
            //System.out.println("test1");
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
                // System.out.println(y + totalC + row); // Shows that the candidate's stats are being gathered properly.
            }
            // response.setContentType("application/json;");
            // write your html here and return it
            // you may have to copy the processing code from the file CandidateFundingSourceServlet if you want that information too
            // Chart isn't showing, maybe it doesn't work because it's being written through Java, maybe it's because it has a height of 0 (I can't override that).
            // The charts work perfectly fine if I plug it in to index.html.
            // I've included everything necessary for the chart to work.

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
                "</head>" +
                "<body>" +
                    "<div id=\"content\">" +
                        "<p>Information for Miss Betsy Pauline Elgar</p>" +
                        "<div id=\"linechart\" style=\"width:500px; height:600px\"></div>" +   // The charts don't work even though their html shows up correctly in the in-page inspect 
                        "<p> Chart here </p>" +
                    "</div>" +
                "</body>" +
                "</html>");
            
            // response.getWriter().println(results.toString());
        }
        catch (Exception e) {
            System.out.println("Database query failed in cps: \n"+e);
        }
    }
}