<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Enregistrer un changement d'état</title>
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
                    <p class="welcome">Changer un état<span>Enregistrer un changement d'état pour une machine</span></p>
                </div>
                <div class="topbar-actions">
                    <span class="icon-btn"><i class="fa-regular fa-bell"></i></span>
                    <span class="avatar-badge">U</span>
                </div>
            </div>

            <div class="card-surface form-card">
                <form id="formEtat">
                    <label class="form-label-modern" for="selectOrdinateur">Ordinateur</label>
                    <select id="selectOrdinateur" class="form-control-modern" required></select>

                    <label class="form-label-modern" for="selectEtat">État</label>
                    <select id="selectEtat" class="form-control-modern" required></select>

                    <label class="form-label-modern" for="dateChangement">Date du changement</label>
                    <input type="date" id="dateChangement" class="form-control-modern" required>

                    <button type="submit" class="btn-ink mt-2">
                        <i class="fa-solid fa-check"></i> Enregistrer
                    </button>
                </form>

                <p id="message" class="mt-3 mb-0 fw-semibold"></p>
            </div>
        </div>
    </div>

    <script src="js/load-sidebar.js"></script>

    <script>
        const BASE_URL = "http://localhost:8080/ordinateur/ordinateur";
        let dernieresDates = {}; // { id_ordinateur: "yyyy-mm-dd" ou null }

        const selectOrdinateur = document.getElementById("selectOrdinateur");
        const selectEtat = document.getElementById("selectEtat");
        const inputDate = document.getElementById("dateChangement");
        const messageEl = document.getElementById("message");

        // Date max = aujourd'hui (empêche déjà les dates futures via le calendrier)
        const aujourdHui = new Date().toISOString().split("T")[0];
        inputDate.setAttribute("max", aujourdHui);

        // Charger les ordinateurs
        fetch(`${BASE_URL}?action=ordinateurs`)
            .then(r => r.json())
            .then(data => {
                data.forEach(o => {
                    dernieresDates[o.idOrdinateur] = o.derniereDate; // peut être null
                    const opt = document.createElement("option");
                    opt.value = o.idOrdinateur;
                    opt.textContent = o.libelle;
                    selectOrdinateur.appendChild(opt);
                });
                mettreAJourMinDate();
            })
            .catch(err => console.error("Erreur chargement ordinateurs :", err));

        // Charger les états
        fetch(`${BASE_URL}?action=etats`)
            .then(r => r.json())
            .then(data => {
                data.forEach(e => {
                    const opt = document.createElement("option");
                    opt.value = e.idEtat;
                    opt.textContent = e.libelle;
                    selectEtat.appendChild(opt);
                });
            })
            .catch(err => console.error("Erreur chargement états :", err));

        // Met à jour la date minimale autorisée selon l'ordinateur sélectionné
        function mettreAJourMinDate() {
            const idSelectionne = selectOrdinateur.value;
            const derniereDate = dernieresDates[idSelectionne];
            if (derniereDate) {
                inputDate.setAttribute("min", derniereDate);
            } else {
                inputDate.removeAttribute("min");
            }
        }

        selectOrdinateur.addEventListener("change", mettreAJourMinDate);

        // Soumission du formulaire
        document.getElementById("formEtat").addEventListener("submit", function (e) {
            e.preventDefault();
            messageEl.textContent = "";
            messageEl.className = "mt-3 mb-0 fw-semibold";

            const idOrdinateur = selectOrdinateur.value;
            const idEtat = selectEtat.value;
            const date = inputDate.value;

            // Validation JS (confort utilisateur — la vraie validation est côté serveur)
            if (date > aujourdHui) {
                messageEl.textContent = "La date ne peut pas être dans le futur.";
                messageEl.style.color = "#dc2626";
                return;
            }
            const derniereDate = dernieresDates[idOrdinateur];
            if (derniereDate && date < derniereDate) {
                messageEl.textContent = "La date doit être postérieure au " + derniereDate + ".";
                messageEl.style.color = "#dc2626";
                return;
            }

            const body = new URLSearchParams();
            body.append("action", "saveEtat");
            body.append("id_ordinateur", idOrdinateur);
            body.append("id_etat", idEtat);
            body.append("date_changement", date);

            fetch(BASE_URL, {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                credentials: "include", // envoie le cookie de session pour la vérification login
                body: body
            })
                .then(r => r.json())
                .then(data => {
                    messageEl.textContent = data.message;
                    messageEl.style.color = data.success ? "#16a34a" : "#dc2626";
                    if (data.success) {
                        dernieresDates[idOrdinateur] = date; // met à jour localement
                        mettreAJourMinDate();
                    }
                })
                .catch(err => {
                    messageEl.textContent = "Erreur réseau : " + err;
                    messageEl.style.color = "#dc2626";
                });
        });
    </script>
</body>
</html>
