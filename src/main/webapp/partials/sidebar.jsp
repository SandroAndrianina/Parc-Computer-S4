<%-- partials/sidebar.jsp
     Sidebar réutilisable pour toutes les pages JSP.
     Détecte la page active via l'URI courante, ne dépend d'aucun servlet
     supplémentaire (aucune logique métier modifiée). --%>
<%
    String uri = request.getRequestURI();
    java.util.function.Function<String,String> act =
        (key) -> uri.contains(key) ? "active" : "";
%>
<aside class="app-sidebar">
    <div class="brand">
        <i class="fa-solid fa-server"></i>
        <span>Parc Computer ETU4110</span>
    </div>

    <div class="nav-section-label">General</div>
    <a class="nav-link <%= act.apply("/dashboard") %>" href="dashboard.php">
        <i class="fa-solid fa-gauge"></i> Dashboard
    </a>

    <div class="nav-section-label">Ordinateurs</div>
    <a class="nav-link <%= uri.endsWith("/list") ? "active" : "" %>"
       href="list">
        <i class="fa-solid fa-desktop"></i> Liste des ordinateurs
    </a>
    <a class="nav-link <%= uri.contains("/form") && !uri.contains("modele") ? "active" : "" %>"
       href="ordinateur?action=add">
        <i class="fa-solid fa-plus"></i> Ajouter un ordinateur
    </a>
    <a class="nav-link <%= act.apply("/historique") %>" href="historique.php">
        <i class="fa-solid fa-clock-rotate-left"></i> Historique des etats
    </a>
    <a class="nav-link <%= act.apply("insertion_etat") %>" href="insertion_etat.php">
        <i class="fa-solid fa-pen-to-square"></i> Changer un etat
    </a>

    <div class="nav-section-label">Modèles</div>
    <a class="nav-link <%= act.apply("list_modele") %>" href="list_modele">
        <i class="fa-solid fa-layer-group"></i> Liste des modeles
    </a>
    <a class="nav-link <%= act.apply("form_modele") %>" href="modele?action=add">
        <i class="fa-solid fa-circle-plus"></i> Ajouter un modele
    </a>

    <div class="sidebar-footer">
        <a class="nav-link" href="logout">
            <i class="fa-solid fa-right-from-bracket"></i> Deconnexion
        </a>
    </div>
</aside>
