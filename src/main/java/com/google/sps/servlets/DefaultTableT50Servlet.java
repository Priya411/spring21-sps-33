package com.google.sps.servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.firestore.Query.Direction;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;


@WebServlet("/default")
public class DefaultTableT50Servlet extends SetupServlet {

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try { 
            ApiFuture<QuerySnapshot> top_50_tc_snapshot = db.collection("fec_data")
                .orderBy("2019-2020.totalContributions", Direction.DESCENDING).limit(50).get(); 
            String htmlResponse = "<tr><th>Name</th><th>State</th><th>Political Party</th><th>Total Contibutions<br>(2019-2020)</th></tr>";
            for (DocumentSnapshot document : top_50_tc_snapshot.get().getDocuments()) {
                String name = document.getString("2019-2020.name");
                String state = document.getString("2019-2020.state");
                String party = document.getString("2019-2020.affiliation");
                Double totalContribution = document.getDouble("2019-2020.totalContributions");
                String candidateId = document.getId();
                if (state == "00")
                    state = "None";
                String query = "/candidatePage?"+candidateId;
                htmlResponse += String.format("<tr><td><a href=\"%s\">%s</a></td><td>%s</td><td>%s</td><td>%.2f</td></tr>",query,name,state,party,totalContribution);
            }
            response.setContentType("application/json;");
            response.getWriter().println(htmlResponse);
        }
        catch (Exception e) {
            System.out.println("Database query failed: \n"+e);
        }
    }
}
