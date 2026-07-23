<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="models.Modele" %>
<%
    request.setAttribute("pageTitle", "Liste des modèles");
    request.setAttribute("pageSubtitle", "Catalogue des modèles de machine");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Gestion des Modèles</title>
    <%@ include file="partials/head-libs.jsp" %>
</head>
<body>
    <div class="app-shell">
        <%@ include file="partials/sidebar.jsp" %>

        <div class="app-main">
            <%@ include file="partials/topbar.jsp" %>

            <div class="card-surface">
                <div class="page-heading mb-3">
                    <h1><i class="fa-solid fa-layer-group me-2"></i>Modèles</h1>
                    <a href="modele?action=add" class="btn-ink">
                        <i class="fa-solid fa-plus"></i> Ajouter un modèle
                    </a>
                </div>

                <%
                    List<Modele> modeles = (List<Modele>) request.getAttribute("modeles");
                    if (modeles != null && !modeles.isEmpty()) {
                %>
                <div class="table-responsive">
                    <table class="table-modern">
                        <thead>
                            <tr><th>ID</th><th>Marque</th><th>Modèle</th><th>Référence</th><th></th></tr>
                        </thead>
                        <tbody>
                        <%
                            for (Modele m : modeles) {
                        %>
                            <tr>
                                <td>#<%= m.getIdModele() %></td>
                                <td><%= m.getLibelleMarque() %></td>
                                <td><%= m.getLibelle() %></td>
                                <td><%= m.getReference() %></td>
                                <td>
                                    <div class="row-actions">
                                        <a href="modele?action=edit&id=<%= m.getIdModele() %>" title="Modifier">
                                            <i class="fa-solid fa-pen"></i>
                                        </a>
                                        <a class="danger" href="modele?action=delete&id=<%= m.getIdModele() %>"
                                           title="Supprimer" onclick="return confirm('Supprimer ce modèle ?');">
                                            <i class="fa-solid fa-trash"></i>
                                        </a>
                                    </div>
                                </td>
                            </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
                <% } else { %>
                <div class="empty-state">
                    <i class="fa-regular fa-folder-open"></i>
                    Aucun modèle enregistré pour le moment.<br>
                    <a href="modele?action=add" class="btn-ink mt-3">
                        <i class="fa-solid fa-plus"></i> Ajouter le premier modèle
                    </a>
                </div>
                <% } %>
            </div>
        </div>
    </div>
</body>
</html>
