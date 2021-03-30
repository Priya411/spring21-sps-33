package com.google.sps.servlets;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Handles requests sent to the /hello URL. Try running a server and navigating
 * to /hello!
 */
@WebServlet("/fec")
public class FecApiServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();

        // get api key
        BufferedReader api_file = new BufferedReader(new FileReader("config/fec_api_key.txt"));
        String api_key = api_file.readLine();
        api_file.close();

        // build fec query/rquest
        String query = request.getParameter("query");
        HttpRequest fec_request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create("https://api.open.fec.gov/v1/names/candidates/?q=" + query + "&api_key=" + api_key))
            .header("accept", "application/json").build();

        // use the client to send the request
        try {
            HttpResponse<String> fec_response = httpClient.send(fec_request, HttpResponse.BodyHandlers.ofString());

            //get candidate info
            Gson req_json = new Gson();
            JsonObject fec_json = req_json.fromJson(fec_response.body(), JsonObject.class);
            JsonArray candidates = fec_json.get("results").getAsJsonArray();
            ArrayList<String> cand_responses = new ArrayList<String>();       
            for (int i = 0; i < candidates.size(); i++) {
                JsonObject candidate = candidates.get(i).getAsJsonObject();
                String cand_id = candidate.get("id").getAsString();

                HttpRequest candidate_request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://api.open.fec.gov/v1/candidate/" +cand_id + "/?q=" + query + "&api_key=" + api_key))
                .header("accept", "application/json").build();
                HttpResponse<String> cand_response = httpClient.send(candidate_request, HttpResponse.BodyHandlers.ofString());
                cand_responses.add(cand_response.body());
            }

            System.out.println(cand_responses.toString());
            response.setContentType("application/json;");
            response.getWriter().println(cand_responses.toString());
        } catch (InterruptedException e) {
            System.out.println("error");
        }
  }
}
