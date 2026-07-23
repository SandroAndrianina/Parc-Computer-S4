package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Modele;
import utils.DatabaseConnection;

public class ListeModeleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("modeles", getModeles());
        request.getRequestDispatcher("list_modele.jsp").forward(request, response);
    }

    private List<Modele> getModeles() {
        List<Modele> modeles = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try {
            String sql = "SELECT m.id_modele, m.libelle, m.reference, ma.libelle AS marque " +
                         "FROM modele m JOIN marque ma ON m.id_marque = ma.id_marque";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Modele modele = new Modele();
                modele.setIdModele(rs.getInt("id_modele"));
                modele.setLibelle(rs.getString("libelle"));
                modele.setReference(rs.getString("reference"));
                modele.setLibelleMarque(rs.getString("marque"));
                modeles.add(modele);
            }
        } catch (SQLException e) {
            System.err.println("Erreur getModeles : " + e.getMessage());
        }
        return modeles;
    }
}