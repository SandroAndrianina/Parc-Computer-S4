<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="models.Modele" %>
<%@ page import="models.Ordinateur" %>
<%
    request.setAttribute("pageTitle", "Liste des ordinateurs");
    request.setAttribute("pageSubtitle", "Parc informatique — vue d'ensemble");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Gestion des Ordinateurs</title>
    <%@ include file="partials/head-libs.jsp" %>
</head>
<body>
    <div class="app-shell">
        <%@ include file="partials/sidebar.jsp" %>

        <div class="app-main">
            <%@ include file="partials/topbar.jsp" %>

            <div class="card-surface">
                <div class="page-heading mb-3">
                    <h1><i class="fa-solid fa-desktop me-2"></i>Ordinateurs</h1>
                    <a href="ordinateur?action=add" class="btn-ink">
                        <i class="fa-solid fa-plus"></i> Ajouter un ordinateur
                    </a>
                </div>

                <%
                    List<Ordinateur> ordinateurs = (List<Ordinateur>) request.getAttribute("ordinateurs");
                    if (ordinateurs != null && !ordinateurs.isEmpty()) {
                %>
                <div class="table-responsive">
                    <table class="table-modern">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Marque</th>
                                <th>Modèle</th>
                                <th>Référence</th>
                                <th>RAM (Go)</th>
                                <th>Processeur</th>
                                <th>Disque dur (Go)</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                        <%
                            for (Ordinateur o : ordinateurs) {
                        %>
                            <tr>
                                <td>#<%= o.getIdOrdinateur() %></td>
                                <td><%= o.getLibelleMarque() %></td>
                                <td><%= o.getLibelleModele() %></td>
                                <td><%= o.getReference() %></td>
                                <td><%= o.getRam() %></td>
                                <td><%= o.getProcesseur() %></td>
                                <td><%= o.getDisqueDur() %></td>
                                <td>
                                    <div class="row-actions">
                                        <a href="ordinateur?action=edit&id=<%= o.getIdOrdinateur() %>" title="Modifier">
                                            <i class="fa-solid fa-pen"></i>
                                        </a>
                                        <a class="danger" href="ordinateur?action=delete&id=<%= o.getIdOrdinateur() %>"
                                           title="Supprimer"
                                           onclick="return confirm('Supprimer cet ordinateur ?');">
                                            <i class="fa-solid fa-trash"></i>
                                        </a>
                                    </div>
                                </td>
                            </tr>
                        <%
                            }
                        %>
                        </tbody>
                    </table>
                </div>
                <%
                    } else {
                %>
                <div class="empty-state">
                    <i class="fa-regular fa-folder-open"></i>
                    Aucun ordinateur enregistré pour le moment.<br>
                    <a href="ordinateur?action=add" class="btn-ink mt-3">
                        <i class="fa-solid fa-plus"></i> Ajouter le premier ordinateur
                    </a>
                </div>
                <% } %>
            </div>
        </div>
    </div>
</body>
</html>
