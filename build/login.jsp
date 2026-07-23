<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Connexion — ParcInfo</title>
    <%@ include file="partials/head-libs.jsp" %>
</head>
<body class="login-page">
    <div class="login-card-modern">
        <div class="brand-mark">
            <i class="fa-solid fa-server"></i> ParcInfo
        </div>
        <h1>Content de vous revoir</h1>
        <p class="sub">Connectez-vous pour gérer le parc informatique.</p>

        <%
            String error = request.getParameter("error");
            if (error != null) {
        %>
            <div class="login-error-modern">
                <i class="fa-solid fa-circle-exclamation"></i> <%= error.replace("+", " ") %>
            </div>
        <% } %>

        <form action="login" method="post">
            <label class="form-label-modern" for="login">Identifiant</label>
            <input class="form-control-modern" type="text" id="login" name="login" required>

            <label class="form-label-modern" for="password">Mot de passe</label>
            <input class="form-control-modern" type="password" id="password" name="password" required>

            <button type="submit" class="btn-ink w-100 justify-content-center">
                <i class="fa-solid fa-arrow-right-to-bracket"></i> Se connecter
            </button>
        </form>
    </div>
</body>
</html>
