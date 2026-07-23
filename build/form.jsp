<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="models.Modele" %>
<%@ page import="models.Ordinateur" %>
<%
    String mode = (String) request.getAttribute("mode");
    Ordinateur ordinateur = (Ordinateur) request.getAttribute("ordinateur");
    boolean isEdit = "edit".equals(mode);
    request.setAttribute("pageTitle", isEdit ? "Modifier un ordinateur" : "Ajouter un ordinateur");
    request.setAttribute("pageSubtitle", "Fiche machine");
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

            <div class="card-surface form-card">
                <div class="page-heading mb-4">
                    <h1><i class="fa-solid <%= isEdit ? "fa-pen" : "fa-plus" %> me-2"></i>
                        <%= isEdit ? "Modifier un ordinateur" : "Ajouter un ordinateur" %>
                    </h1>
                </div>

                <form action="ordinateur" method="post">
                    <% if (isEdit) { %>
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" name="id" value="<%= ordinateur.getIdOrdinateur() %>">
                    <% } else { %>
                        <input type="hidden" name="action" value="add">
                    <% } %>

                    <label class="form-label-modern">Modèle</label>
                    <select name="id_modele" class="form-control-modern" required>
                        <option value="">-- Choisir un modèle --</option>
                        <%
                            List<Modele> modeles = (List<Modele>) request.getAttribute("modeles");
                            if (modeles != null) {
                                for (Modele m : modeles) {
                                    boolean selected = (isEdit && ordinateur != null && m.getIdModele() == ordinateur.getIdModele());
                        %>
                            <option value="<%= m.getIdModele() %>" <%= selected ? "selected" : "" %>>
                                <%= m.getLibelleMarque() %> - <%= m.getLibelle() %> (<%= m.getReference() %>)
                            </option>
                        <%
                                }
                            }
                        %>
                    </select>

                    <label class="form-label-modern">RAM (Go)</label>
                    <input class="form-control-modern" type="number" name="ram" required
                           value="<%= isEdit && ordinateur != null ? ordinateur.getRam() : "" %>">

                    <label class="form-label-modern">Processeur</label>
                    <input class="form-control-modern" type="text" name="processeur" required
                           value="<%= isEdit && ordinateur != null ? ordinateur.getProcesseur() : "" %>">

                    <label class="form-label-modern">Disque dur (Go)</label>
                    <input class="form-control-modern" type="number" name="disque_dur" required
                           value="<%= isEdit && ordinateur != null ? ordinateur.getDisqueDur() : "" %>">

                    <div class="d-flex gap-2 mt-2">
                        <button type="submit" class="btn-ink">
                            <i class="fa-solid fa-check"></i> <%= isEdit ? "Modifier" : "Ajouter" %> l'ordinateur
                        </button>
                        <% if (isEdit) { %>
                            <a href="list" class="btn-outline-soft">Annuler</a>
                        <% } %>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>
