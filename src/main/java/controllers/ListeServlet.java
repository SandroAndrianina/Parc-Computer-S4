package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Ordinateur;
import utils.DatabaseConnection;
import jakarta.servlet.ServletException;

public class ListeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        List<Ordinateur> ordinateurs = getOrdinateurs();
        request.setAttribute("ordinateurs", ordinateurs);
        request.getRequestDispatcher("list.jsp").forward(request, response);
    }
    
    private List<Ordinateur> getOrdinateurs() {
        List<Ordinateur> ordinateurs = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        
        try {
            String sql = "SELECT o.id_ordinateur, o.ram, o.processeur, o.disque_dur, " +
                         "m.libelle as modele, m.reference, ma.libelle as marque " +
                         "FROM ordinateur o " +
                         "JOIN modele m ON o.id_modele = m.id_modele " +
                         "JOIN marque ma ON m.id_marque = ma.id_marque " +
                         "ORDER BY o.id_ordinateur DESC";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Ordinateur ordi = new Ordinateur();
                ordi.setIdOrdinateur(rs.getInt("id_ordinateur"));
                ordi.setRam(rs.getInt("ram"));
                ordi.setProcesseur(rs.getString("processeur"));
                ordi.setDisqueDur(rs.getInt("disque_dur"));
                ordi.setLibelleModele(rs.getString("modele"));
                ordi.setReference(rs.getString("reference"));
                ordi.setLibelleMarque(rs.getString("marque"));
                ordinateurs.add(ordi);
            }
        } catch (SQLException e) {
            System.err.println("Erreur getOrdinateurs : " + e.getMessage());
        }
        
        return ordinateurs;
    }
}