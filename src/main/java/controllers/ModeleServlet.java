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
import jakarta.servlet.http.HttpSession;
import models.Marque;
import models.Modele;
import utils.DatabaseConnection;

public class ModeleServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String idParam = request.getParameter("id");

        HttpSession session = request.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("login") != null);

        if (!isLoggedIn) {
            response.sendRedirect("login?error=Vous+devez+etre+connecte+pour+effectuer+cette+action");
            return;
        }

        if ("update".equals(action)) {
            int id = Integer.parseInt(idParam);
            Modele modele = mapper(request);
            modele.setIdModele(id);
            modifierModele(modele);
        } else if ("delete".equals(action)) {
            supprimerModele(Integer.parseInt(idParam));
        } else {
            ajouterModele(mapper(request));
        }

        response.sendRedirect("list_modele");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String idParam = request.getParameter("id");

        HttpSession session = request.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("login") != null);

        if ("delete".equals(action) && idParam != null) {
            if (!isLoggedIn) {
                response.sendRedirect("login?error=Vous+devez+etre+connecte+pour+effectuer+cette+action");
                return;
            }
            supprimerModele(Integer.parseInt(idParam));
            response.sendRedirect("list_modele");
            return;
        }

        request.setAttribute("marques", getMarques());

        if ("edit".equals(action) && idParam != null) {
            request.setAttribute("modele", getModeleById(Integer.parseInt(idParam)));
            request.setAttribute("mode", "edit");
        } else {
            request.setAttribute("mode", "add");
        }

        request.getRequestDispatcher("form_modele.jsp").forward(request, response);
    }

    private Modele mapper(HttpServletRequest request) {
        Modele modele = new Modele();
        modele.setLibelle(request.getParameter("libelle"));
        modele.setReference(request.getParameter("reference"));
        modele.setIdMarque(Integer.parseInt(request.getParameter("id_marque")));
        return modele;
    }

    private void ajouterModele(Modele modele) {
        Connection conn = DatabaseConnection.getConnection();
        try {
            String sql = "INSERT INTO modele (libelle, reference, id_marque) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, modele.getLibelle());
            pstmt.setString(2, modele.getReference());
            pstmt.setInt(3, modele.getIdMarque());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur insertion modele : " + e.getMessage());
        }
    }

    private void modifierModele(Modele modele) {
        Connection conn = DatabaseConnection.getConnection();
        try {
            String sql = "UPDATE modele SET libelle = ?, reference = ?, id_marque = ? WHERE id_modele = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, modele.getLibelle());
            pstmt.setString(2, modele.getReference());
            pstmt.setInt(3, modele.getIdMarque());
            pstmt.setInt(4, modele.getIdModele());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur modification modele : " + e.getMessage());
        }
    }

    private void supprimerModele(int idModele) {
        Connection conn = DatabaseConnection.getConnection();
        try {
            String sql = "DELETE FROM modele WHERE id_modele = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idModele);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur suppression modele (probablement lié à des ordinateurs existants) : " + e.getMessage());
        }
    }

    private Modele getModeleById(int id) {
        Modele modele = null;
        Connection conn = DatabaseConnection.getConnection();
        try {
            String sql = "SELECT id_modele, libelle, reference, id_marque FROM modele WHERE id_modele = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                modele = new Modele();
                modele.setIdModele(rs.getInt("id_modele"));
                modele.setLibelle(rs.getString("libelle"));
                modele.setReference(rs.getString("reference"));
                modele.setIdMarque(rs.getInt("id_marque"));
            }
        } catch (SQLException e) {
            System.err.println("Erreur getModeleById : " + e.getMessage());
        }
        return modele;
    }

    private List<Marque> getMarques() {
        List<Marque> marques = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try {
            String sql = "SELECT id_marque, libelle FROM marque";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Marque m = new Marque();
                m.setIdMarque(rs.getInt("id_marque"));
                m.setLibelle(rs.getString("libelle"));
                marques.add(m);
            }
        } catch (SQLException e) {
            System.err.println("Erreur getMarques : " + e.getMessage());
        }
        return marques;
    }
}