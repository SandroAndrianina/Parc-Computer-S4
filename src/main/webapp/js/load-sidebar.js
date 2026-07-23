// js/load-sidebar.js
// Charge partials/sidebar.html dans #sidebar-container (les pages PHP statiques
// n'ont pas de mécanisme d'include côté serveur) puis surligne le lien actif.
fetch("partials/sidebar.html")
    .then(r => r.text())
    .then(html => {
        const container = document.getElementById("sidebar-container");
        if (!container) return;
        container.outerHTML = html;

        const current = window.location.pathname.split("/").pop().split(".")[0] || "dashboard";
        document.querySelectorAll(".app-sidebar .nav-link[data-page]").forEach(link => {
            if (link.dataset.page === current) {
                link.classList.add("active");
            }
        });
    })
    .catch(err => console.error("Erreur chargement sidebar :", err));
