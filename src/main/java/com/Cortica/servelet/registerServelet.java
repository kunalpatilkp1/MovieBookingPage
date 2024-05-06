package com.Cortica.servelet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/register")
public class registerServelet extends HttpServlet {
	
    final static String QUERY = "INSERT INTO movie(name, mobile, movie, time) VALUES (?, ?, ?, ?)";
    final static String SELECT_QUERY = "SELECT movie, COUNT(*) FROM movie GROUP BY movie ORDER BY COUNT(*) DESC";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter pw = res.getWriter();
        res.setContentType("text/html");

        String name = req.getParameter("name");
        String mobile = req.getParameter("mobile");
        String[] movies = req.getParameterValues("movie[]");
        String[] times = req.getParameterValues("time[]");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException cnf) {
            cnf.printStackTrace();
        }

        try (Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "kunal", "kunal");
             PreparedStatement insertPs = con.prepareStatement(QUERY);
             PreparedStatement selectPs = con.prepareStatement(SELECT_QUERY)) {

            // Insert records into the database for each selected movie and time
            for (int i = 0; i < movies.length; i++) {
                insertPs.setString(1, name);
                insertPs.setString(2, mobile);
                insertPs.setString(3, movies[i]);
                insertPs.setString(4, times[i]);
                insertPs.executeUpdate();
            }

            // Retrieve the most selected movie from the database
            ResultSet rs = selectPs.executeQuery();
            if (rs.next()) {
                String mostSelectedMovie = rs.getString(1);
                pw.println("<h2>Most Selected Movie: " + mostSelectedMovie + "</h2>");
            } else {
                pw.println("<h2>No movie selections yet.</h2>");
            }

        } catch (SQLException se) {
            se.printStackTrace();
            pw.println("<h1>" + se.getMessage() + "</h1>");
        } catch (Exception e) {
            e.printStackTrace();
            pw.println("<h1>" + e.getMessage() + "</h1>");
        }

        pw.println("<a href='registration.html'>New Registration</a><br>");
        pw.println("<a href='movielist'>Movie List</a>");
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		doGet(req, res);
	}
}
