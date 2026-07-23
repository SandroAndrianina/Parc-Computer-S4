<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="models.Marque" %>
<%@ page import="models.Modele" %>
<%
    String mode = (String) request.getAttribute("mode");
    Modele modele = (Modele) request.getAttribute("modele");
    boolean isEdit = "edit".equals(mode);
    request.setAttribute("pageTitle", isEdit ? "Modifier un modèle" : "Ajouter un modèle");
    request.setAttribute("pageSubtitle", "Fiche modèle");
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

            <div class="card-surface form-card">
                <div class="page-heading mb-4">
                    <h1><i class="fa-solid <%= isEdit ? "fa-pen" : "fa-plus" %> me-2"></i>
                        <%= isEdit ? "Modifier un modèle" : "Ajouter un modèle" %>
                    </h1>
                </div>

                <form action="modele" method="post">
                    <% if (isEdit) { %>
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" name="id" value="<%= modele.getIdModele() %>">
                    <% } else { %>
                        <input type="hidden" name="action" value="add">
                    <% } %>

                    <label class="form-label-modern">Marque</label>
                    <select name="id_marque" class="form-control-modern" required>
                        <option value="">-- Choisir une marque --</option>
                        <%
                            List<Marque> marques = (List<Marque>) request.getAttribute("marques");
                            if (marques != null) {
                                for (Marque ma : marques) {
                                    boolean selected = (isEdit && modele != null && ma.getIdMarque() == modele.getIdMarque());
                        %>
                            <option value="<%= ma.getIdMarque() %>" <%= selected ? "selected" : "" %>><%= ma.getLibelle() %></option>
                        <% } } %>
                    </select>

                    <label class="form-label-modern">Libellé</label>
                    <input class="form-control-modern" type="text" name="libelle" required
                           value="<%= isEdit && modele != null ? modele.getLibelle() : "" %>">

                    <label class="form-label-modern">Référence</label>
                    <input class="form-control-modern" type="text" name="reference" required
                           value="<%= isEdit && modele != null ? modele.getReference() : "" %>">

                    <div class="d-flex gap-2 mt-2">
                        <button type="submit" class="btn-ink">
                            <i class="fa-solid fa-check"></i> <%= isEdit ? "Modifier" : "Ajouter" %>
                        </button>
                        <% if (isEdit) { %>
                            <a href="list_modele" class="btn-outline-soft">Annuler</a>
                        <% } %>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>
