<%-- partials/topbar.jsp
     Barre supérieure réutilisable.
     Attend éventuellement l'attribut de requête "pageTitle" et "pageSubtitle",
     positionnés par la page appelante (sinon valeurs par défaut). --%>
<%
    String pageTitle = (String) request.getAttribute("pageTitle");
    String pageSubtitle = (String) request.getAttribute("pageSubtitle");
    if (pageTitle == null) pageTitle = "Bonjour !";
    String loginName = (String) session.getAttribute("login");
%>
<div class="app-topbar">
    <div>
        <p class="welcome"><%= pageTitle %></p>
        <% if (pageSubtitle != null) { %>
            <span><%= pageSubtitle %></span>
        <% } %>
    </div>

    <div class="topbar-search d-none d-md-block">
        <i class="fa-solid fa-magnifying-glass"></i>
        <input type="text" placeholder="Rechercher un ordinateur, un modèle...">
    </div>

    <div class="topbar-actions">
        <span class="icon-btn"><i class="fa-regular fa-bell"></i></span>
        <span class="avatar-badge">
            <%= (loginName != null && loginName.length() > 0) ? loginName.substring(0,1).toUpperCase() : "U" %>
        </span>
    </div>
</div>
