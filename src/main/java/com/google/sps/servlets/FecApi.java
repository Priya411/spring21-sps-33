package com.google.sps.servlets;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.Headers;
import java.net.http.HttpRequest;
import java.net.http.HttpClient;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Handles requests sent to the /hello URL. Try running a server and navigating to /hello! */
@WebServlet("/fec")
public class FecApiServlet extends HttpServlet {


  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html;");
    response.getWriter().println("<h1>Hello world!</h1>");

    var client = HttpClient.newHttpClient();

    // create a request
    var request = HttpRequest.newBuilder(
        URI.create("https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY"))
    .header("accept", "application/json")
    .build();

    // use the client to send the request
    var response = client.send(request, new JsonBodyHandler<>(APOD.class));

    // the response:
    System.out.println(response.body().get().title);
  }
}
