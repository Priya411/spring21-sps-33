package com.google.sps;

import java.net.URL;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;

/**
 * Starts up the server, including a DefaultServlet that handles static files, and any servlet
 * classes annotated with the @WebServlet annotation.
 */
public class ServerMain {
  public static void main(final String[] args) throws Exception {
    // Initialize an instance of Firestore
    Database db = new Database(); 

    db.initialize();

    // Add bulk data to database with its relative path 
    db.addData("src/main/java/com/google/sps/Bulk Data/2019-2020.txt"); 
    db.addData("src/main/java/com/google/sps/Bulk Data/2021-2022.txt");

    /*
    db.addData("src/main/java/com/google/sps/Bulk Data/2017-2018.txt"); 
    db.addData("src/main/java/com/google/sps/Bulk Data/2015-2016.txt"); 
    db.addData("src/main/java/com/google/sps/Bulk Data/2013-2014.txt"); 
    db.addData("src/main/java/com/google/sps/Bulk Data/2011-2012.txt"); 
    db.addData("src/main/java/com/google/sps/Bulk Data/2009-2010.txt"); 
    db.addData("src/main/java/com/google/sps/Bulk Data/2007-2008.txt"); 
    db.addData("src/main/java/com/google/sps/Bulk Data/2005-2006.txt"); 
    db.addData("src/main/java/com/google/sps/Bulk Data/2003-2004.txt");
    db.addData("src/main/java/com/google/sps/Bulk Data/2001-2002.txt"); 
    */

    // Create a server that listens on port 8080.
    final Server server = new Server(8080);
    final WebAppContext webAppContext = new WebAppContext();
    server.setHandler(webAppContext);

    // Load static content from inside the jar file.
    final URL webAppDir = ServerMain.class.getClassLoader().getResource("META-INF/resources");
    webAppContext.setResourceBase(webAppDir.toURI().toString());

    // Enable annotations so the server sees classes annotated with @WebServlet.
    webAppContext.setConfigurations(
        new Configuration[] {
          new AnnotationConfiguration(), new WebInfConfiguration(),
        });

    // Look for annotations in the classes directory (dev server) and in the jar file (live server)
    webAppContext.setAttribute(
        "org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
        ".*/target/classes/|.*\\.jar");

    // Handle static resources, e.g. html files.
    webAppContext.addServlet(DefaultServlet.class, "/");

    // Start the server! 🚀
    server.start();
    System.out.println("Server started!");

    // Keep the main thread alive while the server is running.
    server.join();
  }
}