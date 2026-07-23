<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Historique des états</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@500;600;700;800&family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootswatch@5.3.3/dist/zephyr/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link rel="stylesheet" href="css/theme.css">
</head>
<body>
    <div class="app-shell">
        <div id="sidebar-container"></div>

        <div class="app-main">
            <div class="app-topbar">
                <div>
                    <p class="welcome">Historique des états<span>Suivi des changements d'état des machines</span></p>
                </div>
                <div class="topbar-actions">
                    <span class="icon-btn"><i class="fa-regular fa-bell"></i></span>
                    <span class="avatar-badge">U</span>
                </div>
            </div>

            <div class="card-surface">
                <div class="table-responsive">
                    <table class="table-modern" id="tableHistorique">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Processeur</th>
                                <th>État</th>
                                <th>Motif</th>
                                <th>Date</th>
                            </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
                <div class="empty-state d-none" id="emptyHistorique">
                    <i class="fa-regular fa-clock"></i>
                    Aucun historique disponible pour le moment.
                </div>
            </div>
        </div>
    </div>

    <script src="js/load-sidebar.js"></script>
    <script>
        fetch("http://localhost:8080/ordinateur/ordinateur?action=historique")
            .then(response => {
                if (!response.ok) {
                    throw new Error("Erreur réseau : " + response.status);
                }
                return response.json();
            })
            .then(data => {
                const tbody = document.querySelector("#tableHistorique tbody");
                if (!data || data.length === 0) {
                    document.getElementById("emptyHistorique").classList.remove("d-none");
                    return;
                }
                data.forEach(item => {
                    const etatClass = item.libelleEtat && item.libelleEtat.toLowerCase().includes("dys")
                        ? "dysfonctionnel" : "fonctionnel";
                    const tr = document.createElement("tr");
                    tr.innerHTML = `
                        <td>#${item.idHistorique}</td>
                        <td>${item.processeur}</td>
                        <td><span class="badge-etat ${etatClass}">${item.libelleEtat}</span></td>
                        <td>${item.libelleMotifsPanne}</td>
                        <td>${item.dateChangement}</td>
                    `;
                    tbody.appendChild(tr);
                });
            })
            .catch(error => console.error("Erreur lors du fetch :", error));
    </script>
</body>
</html>
