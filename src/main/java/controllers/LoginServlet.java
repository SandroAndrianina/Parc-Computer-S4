package controllers;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import models.Ordinateur;
import utils.DatabaseConnection;
import jakarta.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
                throws ServletException, IOException {

        String login = request.getParameter("login");    
        String pwd = request.getParameter("password");  
        
        boolean check = checkLogin(login, pwd);

        if (check) {
            HttpSession session = request.getSession();
            session.setAttribute("login", login);
            response.sendRedirect("ordinateur");
            return;

        } else {
            response.sendRedirect("login");
            return;
        }
        
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    public boolean checkLogin(String login, String pwd) {
        Connection conn = DatabaseConnection.getConnection();

        try {
            String sql = "SELECT * FROM user WHERE login_name = ? AND password_login = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, login);
            pstmt.setString(2, pwd);

            ResultSet rs = pstmt.executeQuery();

            return rs.next(); // true si un utilisateur est trouvé
        } catch (SQLException e) {
            System.err.println("Erreur de connexion : " + e.getMessage());
            return false;
        }
    }
    
}
