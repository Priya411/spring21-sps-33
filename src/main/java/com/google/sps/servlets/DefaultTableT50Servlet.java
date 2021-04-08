package com.google.sps.servlets;

import java.io.IOException;
import java.net.URI;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

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


@WebServlet("/default")
public class DefaultTableT50Servlet extends SetupServlet {

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try { 
            ApiFuture<QuerySnapshot> top_50_tc_snapshot = db.collection("fec_data")
                .orderBy("2019-2020.totalContributions").limit(50).get(); //need to make total_contributions field
            String htmlResponse = new String();
            for (DocumentSnapshot document : top_50_tc_snapshot.get().getDocuments()) {
                String name = document.getId();
                String party = document.getString("2019-2020.affiliation");
                Double totalContribution = document.getDouble("2019-2020.totalContributions"); // make contribution a double        
                htmlResponse += String.format("<tr><td>%s</td><td>%s</td><td>%.2f</td></tr>",name,party,totalContribution);
            }
            response.setContentType("application/json;");
            response.getWriter().println(htmlResponse);
        }
        catch (Exception e) {
            System.out.println("Database query failed: \n"+e);
        }
    }
}
