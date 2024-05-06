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

@WebServlet("/movielist")
public class movielistservlet extends HttpServlet {
    
    final static  String QUERY = "SELECT NAME,MOBILE,MOVIE,TIME FROM MOVIE";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        
        PrintWriter pw = res.getWriter();
        
        res.setContentType("text/html");
        
        // Including CSS in the HTML Header
        pw.println("<html><head><title>Movie List</title>");
        pw.println("<style>");
        pw.println("body { font-family: Arial, sans-serif; background-color: #f4f4f4; }");
        pw.println("table { width: 80%; margin: 20px auto; border-collapse: collapse; }");
        pw.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        pw.println("th { background-color: #4CAF50; color: white; }");
        pw.println("tr:nth-child(even) { background-color: #f2f2f2; }");
        pw.println("a { color: #4CAF50; text-decoration: none; font-size: 16px; }");
        pw.println("</style></head><body>");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");  
        } catch(ClassNotFoundException cnf) {
            cnf.printStackTrace();
        }
        
        try (Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","kunal","kunal");
             PreparedStatement ps = con.prepareStatement(QUERY);) {
            
            pw.println("<table>");
            pw.println("<tr>");
            pw.println("<th>NAME</th>");
            pw.println("<th>MOBILE</th>");
            pw.println("<th>MOVIE</th>");
            pw.println("<th>TIME</th>");
            pw.println("</tr>");

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                pw.println("<tr>");
                pw.println("<td>"+rs.getString(1)+"</td>");
                pw.println("<td>"+rs.getString(2)+"</td>");
                pw.println("<td>"+rs.getString(3)+"</td>");
                pw.println("<td>"+rs.getString(4)+"</td>");
                pw.println("</tr>");
            }
            pw.println("</table>");
            
        } catch(SQLException se) {
            se.printStackTrace();
            pw.println("<h1>"+ se.getMessage()+"</h1>");
        } catch(Exception e) {
            e.printStackTrace();
            pw.println("<h1>"+ e.getMessage()+"</h1>");
        }
        
        pw.println("<a href='registration.html'>Back to Registration</a>");
        pw.println("</body></html>");
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        
        doGet(req, res);
    }
}
