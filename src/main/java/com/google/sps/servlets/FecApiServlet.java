package com.google.sps.servlets;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Handles requests sent to the /hello URL. Try running a server and navigating
 * to /hello!
 */
@WebServlet("/fec")
public class FecApiServlet extends SetupServlet {

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        // build fec query/rquest
        final String query = URLEncoder.encode(request.getParameter("query"), StandardCharsets.UTF_8);
        final HttpRequest fec_request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create("https://api.open.fec.gov/v1/names/candidates/?q=" + query + "&api_key=" + fec_api_key))
            .header("accept", "application/json").build();

        // use the client to send the request
        try {
            final HttpResponse<String> fec_response = httpClient.send(fec_request, HttpResponse.BodyHandlers.ofString());

            //get candidate info
            final Gson req_json = new Gson();
            final JsonObject fec_json = req_json.fromJson(fec_response.body(), JsonObject.class);
            final JsonArray candidates = fec_json.get("results").getAsJsonArray();
            final ArrayList<String> cand_responses = new ArrayList<String>();       
            for (int i = 0; i < candidates.size(); i++) {
                final JsonObject candidate = candidates.get(i).getAsJsonObject();
                final String cand_id = candidate.get("id").getAsString();

                final HttpRequest candidate_request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://api.open.fec.gov/v1/candidate/" +cand_id + "/?q=" + query + "&api_key=" + fec_api_key))
                .header("accept", "application/json").build();
                final HttpResponse<String> cand_response = httpClient.send(candidate_request, HttpResponse.BodyHandlers.ofString());
                cand_responses.add(cand_response.body());
            }

            System.out.println(cand_responses.toString());
            response.setContentType("application/json;");
            response.getWriter().println(cand_responses.toString());
        } catch (final InterruptedException e) {
            System.out.println("error");
        }
  }
}
