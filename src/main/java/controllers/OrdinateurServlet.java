package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Modele;
import models.Ordinateur;
import utils.DatabaseConnection;
import jakarta.servlet.ServletException;
import com.google.gson.Gson;
import java.sql.Timestamp;
import models.HistoriqueEtatDTO;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import models.OrdinateurOptionDTO;
import models.EtatDTO;
import models.DashboardDTO;
import models.EvolutionPanneDTO;
import models.KPIDTO;

public class OrdinateurServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String idParam = request.getParameter("id");

        HttpSession session = request.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("login") != null);

        // --- Nouvelle route JSON : insertion d'un changement d'état ---
        if ("saveEtat".equals(action)) {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            if (!isLoggedIn) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"success\":false,\"message\":\"Vous devez être connecté.\"}");
                return;
            }

            response.getWriter().write(saveEtat(request));
            return;
        }
        // --- fin nouvelle route ---

        if (!isLoggedIn) {
            response.sendRedirect("login?error=Vous+devez+etre+connecte+pour+effectuer+cette+action");
            return;
        }

        if ("update".equals(action)) {
            int id = Integer.parseInt(idParam);
            Ordinateur ordinateur = mapper(request);
            ordinateur.setIdOrdinateur(id);
            modifierOrdinateur(ordinateur);
        } else if ("delete".equals(action)) {
            int id = Integer.parseInt(idParam);
            supprimerOrdinateur(id);
        } else {
            Ordinateur ordinateur = mapper(request);
            ajouterOrdinateur(ordinateur);
        }
        
        response.sendRedirect("list");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String idParam = request.getParameter("id");
        String dateKPI = request.getParameter("KPI_date");

                // --- Nouvelle route REST JSON ---
        if ("KPI_panne".equals(action) && dateKPI != null) {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new Gson().toJson(getKPIAt(dateKPI)));
            return;
        }

        if ("dashboard".equals(action)) {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new Gson().toJson(getDashboardData()));
            return;
        }

        if ("historique".equals(action)) {
            // Autoriser les appels cross-origin (ex: si historique.php est sur un autre serveur)
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            List<HistoriqueEtatDTO> historique = getHistoriqueEtats();
            Gson gson = new Gson();
            String json = gson.toJson(historique);

            response.getWriter().write(json);
            return;
        }

        if ("ordinateurs".equals(action)) {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new Gson().toJson(getOrdinateursForForm()));
            return;
        }

        if ("etats".equals(action)) {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new Gson().toJson(getEtats()));
            return;
        }

        HttpSession session = request.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("login") != null);

        // "delete" est une action modifiante même en GET : on la protège
        if ("delete".equals(action) && idParam != null) {
            if (!isLoggedIn) {
                response.sendRedirect("login?error=Vous+devez+etre+connecte+pour+effectuer+cette+action");
                return;
            }
            supprimerOrdinateur(Integer.parseInt(idParam));
            response.sendRedirect("list");
            return;
        }

        // Récupérer les modèles pour le formulaire
        List<Modele> modeles = getModeles();
        request.setAttribute("modeles", modeles);
        
        if ("edit".equals(action) && idParam != null) {
            int id = Integer.parseInt(idParam);
            Ordinateur ordinateur = getOrdinateurById(id);
            request.setAttribute("ordinateur", ordinateur);
            request.setAttribute("mode", "edit");
        } else {
            request.setAttribute("mode", "add");
        }
        
        request.getRequestDispatcher("form.jsp").forward(request, response);
    }
    
    private Ordinateur mapper(HttpServletRequest request) {
        Ordinateur ordinateur = new Ordinateur();
        Enumeration<String> nomsParametres = request.getParameterNames();
        
        while (nomsParametres.hasMoreElements()) {
            String nomParam = nomsParametres.nextElement();
            String valeur = request.getParameter(nomParam);
            
            if ("id_modele".equals(nomParam)) {
                ordinateur.setIdModele(Integer.parseInt(valeur));
            } else if ("ram".equals(nomParam)) {
                ordinateur.setRam(Integer.parseInt(valeur));
            } else if ("processeur".equals(nomParam)) {
                ordinateur.setProcesseur(valeur);
            } else if ("disque_dur".equals(nomParam)) {
                ordinateur.setDisqueDur(Integer.parseInt(valeur));
            }
        }
        
        return ordinateur;
    }
      
    private void ajouterOrdinateur(Ordinateur ordinateur) {
        Connection conn = DatabaseConnection.getConnection();
        try {
            String sql = "INSERT INTO ordinateur (id_modele, ram, processeur, disque_dur) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, ordinateur.getIdModele());
            pstmt.setInt(2, ordinateur.getRam());
            pstmt.setString(3, ordinateur.getProcesseur());
            pstmt.setInt(4, ordinateur.getDisqueDur());
            pstmt.executeUpdate();
            System.out.println("Ordinateur ajouté avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur insertion : " + e.getMessage());
        }
    }
    
    private List<Modele> getModeles() {
        List<Modele> modeles = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        
        try {
            String sql = "SELECT m.id_modele, m.libelle, m.reference, ma.libelle as marque " +
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
    
    private Ordinateur getOrdinateurById(int id) {
        Ordinateur ordinateur = null;
        Connection conn = DatabaseConnection.getConnection();
        
        try {
            String sql = "SELECT o.id_ordinateur, o.id_modele, o.ram, o.processeur, o.disque_dur, " +
                         "m.libelle as modele, m.reference, ma.libelle as marque " +
                         "FROM ordinateur o " +
                         "JOIN modele m ON o.id_modele = m.id_modele " +
                         "JOIN marque ma ON m.id_marque = ma.id_marque " +
                         "WHERE o.id_ordinateur = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                ordinateur = new Ordinateur();
                ordinateur.setIdOrdinateur(rs.getInt("id_ordinateur"));
                ordinateur.setIdModele(rs.getInt("id_modele"));
                ordinateur.setRam(rs.getInt("ram"));
                ordinateur.setProcesseur(rs.getString("processeur"));
                ordinateur.setDisqueDur(rs.getInt("disque_dur"));
                ordinateur.setLibelleModele(rs.getString("modele"));
                ordinateur.setReference(rs.getString("reference"));
                ordinateur.setLibelleMarque(rs.getString("marque"));
            }
        } catch (SQLException e) {
            System.err.println("Erreur getOrdinateurById : " + e.getMessage());
        }
        
        return ordinateur;
    }

    private void modifierOrdinateur(Ordinateur ordinateur) {
        Connection conn = DatabaseConnection.getConnection();
        try {
            String sql = "UPDATE ordinateur SET id_modele = ?, ram = ?, processeur = ?, disque_dur = ? " +
                         "WHERE id_ordinateur = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, ordinateur.getIdModele());
            pstmt.setInt(2, ordinateur.getRam());
            pstmt.setString(3, ordinateur.getProcesseur());
            pstmt.setInt(4, ordinateur.getDisqueDur());
            pstmt.setInt(5, ordinateur.getIdOrdinateur());
            pstmt.executeUpdate();
            System.out.println("Ordinateur modifié avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur modification : " + e.getMessage());
        }
    }

    private void supprimerOrdinateur(int idOrdinateur) {
        Connection conn = DatabaseConnection.getConnection();
        try {
            String sql = "DELETE FROM ordinateur WHERE id_ordinateur = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idOrdinateur);
            pstmt.executeUpdate();
            System.out.println("Ordinateur supprimé avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur suppression : " + e.getMessage());
        }
    }

    private List<HistoriqueEtatDTO> getHistoriqueEtats() {
        List<HistoriqueEtatDTO> liste = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();

        try {
            String sql = "SELECT h.id_historique, h.id_ordinateur, o.processeur, " +
                        "e.libelle, m.libelle panne, h.date_changement " +
                        "FROM historique_etat h " +
                        "JOIN ordinateur o ON h.id_ordinateur = o.id_ordinateur " +
                        "JOIN etat e ON h.id_etat = e.id_etat " +
                        "JOIN motifs_panne m ON h.id_motif = m.id_motif " +
                        "ORDER BY h.date_changement DESC";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                HistoriqueEtatDTO dto = new HistoriqueEtatDTO(
                    rs.getInt("id_historique"),
                    rs.getInt("id_ordinateur"),
                    rs.getString("processeur"),
                    rs.getString("libelle"),
                    rs.getString("panne"),
                    rs.getTimestamp("date_changement").toString()
                );
                liste.add(dto);
            }
        } catch (SQLException e) {
            System.err.println("Erreur getHistoriqueEtats : " + e.getMessage());
        }

        return liste;
    }

    private List<OrdinateurOptionDTO> getOrdinateursForForm() {
        List<OrdinateurOptionDTO> liste = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();

        try {
            String sql = "SELECT o.id_ordinateur, o.processeur, m.libelle AS modele, " +
                        "(SELECT MAX(h.date_changement) FROM historique_etat h " +
                        " WHERE h.id_ordinateur = o.id_ordinateur) AS derniere " +
                        "FROM ordinateur o JOIN modele m ON o.id_modele = m.id_modele";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String libelle = rs.getString("modele") + " - " + rs.getString("processeur");
                Timestamp derniere = rs.getTimestamp("derniere");
                String derniereDate = (derniere != null)
                    ? derniere.toLocalDateTime().toLocalDate().toString()
                    : null;

                liste.add(new OrdinateurOptionDTO(rs.getInt("id_ordinateur"), libelle, derniereDate));
            }
        } catch (SQLException e) {
            System.err.println("Erreur getOrdinateursForForm : " + e.getMessage());
        }

        return liste;
    }

    private List<EtatDTO> getEtats() {
        List<EtatDTO> liste = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();

        try {
            String sql = "SELECT id_etat, libelle FROM etat";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                liste.add(new EtatDTO(rs.getInt("id_etat"), rs.getString("libelle")));
            }
        } catch (SQLException e) {
            System.err.println("Erreur getEtats : " + e.getMessage());
        }

        return liste;
    }

    private String saveEtat(HttpServletRequest request) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<>();

        try {
            int idOrdinateur = Integer.parseInt(request.getParameter("id_ordinateur"));
            int idEtat = Integer.parseInt(request.getParameter("id_etat"));
            String dateStr = request.getParameter("date_changement"); // format attendu : yyyy-MM-dd

            LocalDate dateChangement = LocalDate.parse(dateStr);
            LocalDate aujourdHui = LocalDate.now();

            // Règle 1 : pas de date dans le futur
            if (dateChangement.isAfter(aujourdHui)) {
                result.put("success", false);
                result.put("message", "La date ne peut pas être dans le futur.");
                return gson.toJson(result);
            }

            // Règle 2 : pas avant le dernier changement d'état de cet ordinateur
            LocalDate derniereDate = getDerniereDateEtat(idOrdinateur);
            if (derniereDate != null && dateChangement.isBefore(derniereDate)) {
                result.put("success", false);
                result.put("message", "La date doit être postérieure au dernier changement d'état (" 
                                    + derniereDate + ").");
                return gson.toJson(result);
            }

            Connection conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO historique_etat (id_ordinateur, id_etat, date_changement) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idOrdinateur);
            pstmt.setInt(2, idEtat);
            pstmt.setTimestamp(3, Timestamp.valueOf(dateChangement.atStartOfDay()));
            pstmt.executeUpdate();

            result.put("success", true);
            result.put("message", "État enregistré avec succès.");

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Erreur : " + e.getMessage());
        }

        return gson.toJson(result);
    }

    private LocalDate getDerniereDateEtat(int idOrdinateur) {
        Connection conn = DatabaseConnection.getConnection();
        try {
            String sql = "SELECT MAX(date_changement) AS derniere FROM historique_etat WHERE id_ordinateur = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idOrdinateur);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getTimestamp("derniere") != null) {
                return rs.getTimestamp("derniere").toLocalDateTime().toLocalDate();
            }
        } catch (SQLException e) {
            System.err.println("Erreur getDerniereDateEtat : " + e.getMessage());
        }
        return null;
    }

    private DashboardDTO getDashboardData() {
        int totalOrdinateurs = 0;
        int totalFonctionnels = 0;
        int totalDysfonctionnels = 0;

        Connection conn = DatabaseConnection.getConnection();

        // Total ordinateurs
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) AS total FROM ordinateur");
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) totalOrdinateurs = rs.getInt("total");
        } catch (SQLException e) {
            System.err.println("Erreur count ordinateurs : " + e.getMessage());
        }

        // Répartition par état ACTUEL (dernier changement enregistré par ordinateur)
        try {
            String sql = "SELECT e.libelle, COUNT(*) AS nb " +
                        "FROM ordinateur o " +
                        "JOIN (SELECT id_ordinateur, MAX(date_changement) AS derniere " +
                        "      FROM historique_etat GROUP BY id_ordinateur) dernier " +
                        "  ON o.id_ordinateur = dernier.id_ordinateur " +
                        "JOIN historique_etat h " +
                        "  ON h.id_ordinateur = dernier.id_ordinateur AND h.date_changement = dernier.derniere " +
                        "JOIN etat e ON h.id_etat = e.id_etat " +
                        "GROUP BY e.libelle";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String libelle = rs.getString("libelle");
                int nb = rs.getInt("nb");
                if ("fonctionnel".equalsIgnoreCase(libelle)) totalFonctionnels = nb;
                else if ("dysfonctionnel".equalsIgnoreCase(libelle)) totalDysfonctionnels = nb;
            }
        } catch (SQLException e) {
            System.err.println("Erreur repartition etat : " + e.getMessage());
        }

        // Évolution des pannes par mois
        List<EvolutionPanneDTO> evolution = new ArrayList<>();
        try {
            String sql = "SELECT DATE_FORMAT(h.date_changement, '%Y-%m') AS mois, COUNT(*) AS nb " +
                        "FROM historique_etat h " +
                        "JOIN etat e ON h.id_etat = e.id_etat " +
                        "WHERE e.libelle = 'dysfonctionnel' " +
                        "GROUP BY mois ORDER BY mois";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                evolution.add(new EvolutionPanneDTO(rs.getString("mois"), rs.getInt("nb")));
            }
        } catch (SQLException e) {
            System.err.println("Erreur evolution pannes : " + e.getMessage());
        }

        return new DashboardDTO(totalOrdinateurs, totalFonctionnels, totalDysfonctionnels, evolution);
    }

    private KPIDTO getKPIAt(String date) {
        int totalOrdinateurs = 0;
        int totalFonctionnels = 0;
        int totalDysfonctionnels = 0;
        //panne
        int totalAlimentation = 0;
        int totalDisqueDur = 0;
        int totalCarteMere = 0;

        Connection conn = DatabaseConnection.getConnection();

        // Total ordinateurs
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) AS total FROM ordinateur");
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) totalOrdinateurs = rs.getInt("total");
        } catch (SQLException e) {
            System.err.println("Erreur count ordinateurs : " + e.getMessage());
        }

        // Répartition par état 
        try {
            String sql = "SELECT e.libelle, COUNT(*) AS nb"  +
                        "FROM ordinateur o"  +
                        "JOIN (SELECT id_ordinateur, date_changement AS derniere FROM historique_etat" +
                                "WHERE date_changement= ?" +
                                "GROUP BY id_ordinateur) dernier"  +
                          "ON o.id_ordinateur = dernier.id_ordinateur"  +
                        "JOIN historique_etat h"  +
                          "ON h.id_ordinateur = dernier.id_ordinateur AND h.date_changement = dernier.derniere"  +
                        "JOIN etat e ON h.id_etat = e.id_etat"  +
                        "GROUP BY e.libelle";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, date);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String libelle = rs.getString("libelle");
                int nb = rs.getInt("nb");
                if ("fonctionnel".equalsIgnoreCase(libelle)) totalFonctionnels = nb;
                else if ("dysfonctionnel".equalsIgnoreCase(libelle)) totalDysfonctionnels = nb;
            }
        } catch (SQLException e) {
            System.err.println("Erreur repartition etat : " + e.getMessage());
        }

        // Répartition par panne
        try {
            String sql = "SELECT m.libelle panne, COUNT(*) AS nb" + 
                        "FROM ordinateur o" +  
                        "JOIN (SELECT id_ordinateur, date_changement AS derniere FROM historique_etat" + 
                                "WHERE date_changement= ?" + 
                                "GROUP BY id_ordinateur) dernier" + 
                          "ON o.id_ordinateur = dernier.id_ordinateur"  + 
                        "JOIN historique_etat h"  + 
                          "ON h.id_ordinateur = dernier.id_ordinateur AND h.date_changement = dernier.derniere"  + 
                        "JOIN motifs_panne m ON h.id_motif = m.id_motif"  + 
                        "GROUP BY m.libelle";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, date);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String libelle = rs.getString("panne");
                int nb = rs.getInt("nb");
                if ("alimentation".equalsIgnoreCase(libelle)) totalAlimentation = nb;
                if ("carte_mere".equalsIgnoreCase(libelle)) totalDisqueDur = nb;
                if ("disque_dur".equalsIgnoreCase(libelle)) totalCarteMere = nb;
            }
        } catch (SQLException e) {
            System.err.println("Erreur repartition etat : " + e.getMessage());
        }

        return new KPIDTO(totalOrdinateurs, totalFonctionnels, totalDysfonctionnels, totalAlimentation, totalDisqueDur, totalCarteMere, date);
    }


}
