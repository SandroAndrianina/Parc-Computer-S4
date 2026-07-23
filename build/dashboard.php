<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Dashboard</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@500;600;700;800&family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootswatch@5.3.3/dist/zephyr/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link rel="stylesheet" href="css/theme.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
    <div class="app-shell">
        <div id="sidebar-container"></div>

        <div class="app-main">
            <div class="app-topbar">
                <div>
                    <p class="welcome">Dashboard<span>Vue d'ensemble du parc informatique</span></p>
                </div>
                <div class="topbar-search d-none d-md-block">
                    <i class="fa-solid fa-magnifying-glass"></i>
                    <input type="text" placeholder="Rechercher un ordinateur, un modèle...">
                </div>
                <div class="topbar-actions">
                    <span class="icon-btn"><i class="fa-regular fa-bell"></i></span>
                    <span class="avatar-badge">U</span>
                </div>
            </div>

            <div class="kpi-row">
                <div class="kpi-card">
                    <div class="kpi-label">Total ordinateurs</div>
                    <div class="kpi-value" id="kpiTotal">--</div>
                </div>
                <div class="kpi-card fonctionnel">
                    <div class="kpi-label">Fonctionnels</div>
                    <div class="kpi-value" id="kpiFonctionnel">--</div>
                </div>
                <div class="kpi-card dysfonctionnel">
                    <div class="kpi-label">Dysfonctionnels</div>
                    <div class="kpi-value" id="kpiDysfonctionnel">--</div>
                </div>
            </div>

            <div class="charts-row">
                <div class="chart-card">
                    <h3>Répartition état actuel</h3>
                    <canvas id="chartEtat"></canvas>
                </div>
                <div class="chart-card">
                    <h3>Évolution des pannes par mois</h3>
                    <canvas id="chartEvolution"></canvas>
                </div>
            </div>
        </div>
    </div>

    <script src="js/load-sidebar.js"></script>
    <script>
        const BASE_URL = "http://localhost:8080/ordinateur/ordinateur";

        fetch(`${BASE_URL}?action=dashboard`)
            .then(r => r.json())
            .then(data => {
                document.getElementById("kpiTotal").textContent = data.totalOrdinateurs;
                document.getElementById("kpiFonctionnel").textContent = data.totalFonctionnels;
                document.getElementById("kpiDysfonctionnel").textContent = data.totalDysfonctionnels;

                new Chart(document.getElementById("chartEtat"), {
                    type: "doughnut",
                    data: {
                        labels: ["Fonctionnel", "Dysfonctionnel"],
                        datasets: [{
                            data: [data.totalFonctionnels, data.totalDysfonctionnels],
                            backgroundColor: ["#16a34a", "#dc2626"],
                            borderWidth: 0
                        }]
                    },
                    options: { plugins: { legend: { position: "bottom" } }, cutout: "70%" }
                });

                new Chart(document.getElementById("chartEvolution"), {
                    type: "line",
                    data: {
                        labels: data.evolutionPannes.map(e => e.mois),
                        datasets: [{
                            label: "Nombre de pannes",
                            data: data.evolutionPannes.map(e => e.nombre),
                            borderColor: "#121214",
                            backgroundColor: "rgba(18,18,20,0.06)",
                            fill: true,
                            tension: 0.35,
                            pointBackgroundColor: "#121214"
                        }]
                    },
                    options: {
                        plugins: { legend: { display: false } },
                        scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } }
                    }
                });
            })
            .catch(err => console.error("Erreur chargement dashboard :", err));
    </script>
</body>
</html>
