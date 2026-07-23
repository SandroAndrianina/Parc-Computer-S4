-- prendre les historiques
SELECT h.id_historique, h.id_ordinateur, o.processeur,  
                        e.libelle, m.libelle, h.date_changement  
                        FROM historique_etat h  
                        JOIN ordinateur o ON h.id_ordinateur = o.id_ordinateur  
                        JOIN etat e ON h.id_etat = e.id_etat  
                        JOIN motifs_panne m ON h.id_motif = m.id_motif  
                        ORDER BY h.date_changement DESC;

-- prendre KPI actuelle
SELECT e.libelle, COUNT(*) AS nb  
                        FROM ordinateur o  
                        JOIN (SELECT id_ordinateur, MAX(date_changement) AS derniere  
                              FROM historique_etat GROUP BY id_ordinateur) dernier  
                          ON o.id_ordinateur = dernier.id_ordinateur  
                        JOIN historique_etat h  
                          ON h.id_ordinateur = dernier.id_ordinateur AND h.date_changement = dernier.derniere  
                        JOIN etat e ON h.id_etat = e.id_etat  
                        GROUP BY e.libelle

-- prendre les KPI selon une date precis: 
-- nb fonctionnel, nb dysfonctionnel, nb selon chaque panne
SELECT e.libelle, COUNT(*) AS nb  
                        FROM ordinateur o  
                        JOIN (SELECT id_ordinateur, date_changement AS derniere FROM historique_etat 
                                WHERE date_changement BETWEEN '2026-01-02 00:00:00' AND '2026-01-02 23:59:59' 
                                GROUP BY id_ordinateur) dernier  
                          ON o.id_ordinateur = dernier.id_ordinateur  
                        JOIN historique_etat h  
                          ON h.id_ordinateur = dernier.id_ordinateur AND h.date_changement = dernier.derniere  
                        JOIN etat e ON h.id_etat = e.id_etat  
                        GROUP BY e.libelle;

--nombre par type de pannes
SELECT m.libelle panne, COUNT(*) AS nb  
                        FROM ordinateur o  
                        JOIN (SELECT id_ordinateur, date_changement AS derniere FROM historique_etat 
                                WHERE date_changement BETWEEN '2026-01-02 00:00:00' AND '2026-01-02 23:59:59' 
                                GROUP BY id_ordinateur) dernier 
                          ON o.id_ordinateur = dernier.id_ordinateur  
                        JOIN historique_etat h  
                          ON h.id_ordinateur = dernier.id_ordinateur AND h.date_changement = dernier.derniere  
                        JOIN motifs_panne m ON h.id_motif = m.id_motif  
                        GROUP BY m.libelle;

-- sans BETWEEN
-- nb fonctionnel, nb dysfonctionnel
SELECT e.libelle, COUNT(*) AS nb  
                        FROM ordinateur o  
                        JOIN (SELECT id_ordinateur, date_changement AS derniere FROM historique_etat 
                                WHERE date_changement='2026-02-01 08:45:00'
                                GROUP BY id_ordinateur) dernier  
                          ON o.id_ordinateur = dernier.id_ordinateur  
                        JOIN historique_etat h  
                          ON h.id_ordinateur = dernier.id_ordinateur AND h.date_changement = dernier.derniere  
                        JOIN etat e ON h.id_etat = e.id_etat  
                        GROUP BY e.libelle;

--nombre par type de pannes
SELECT m.libelle panne, COUNT(*) AS nb  
                        FROM ordinateur o  
                        JOIN (SELECT id_ordinateur, date_changement AS derniere FROM historique_etat 
                                WHERE date_changement='2026-02-01 08:45:00'
                                GROUP BY id_ordinateur) dernier 
                          ON o.id_ordinateur = dernier.id_ordinateur  
                        JOIN historique_etat h  
                          ON h.id_ordinateur = dernier.id_ordinateur AND h.date_changement = dernier.derniere  
                        JOIN motifs_panne m ON h.id_motif = m.id_motif  
                        GROUP BY m.libelle;

-- prendre selon une seule date
SELECT id_ordinateur, date_changement FROM historique_etat 
WHERE date_changement='2026-01-02 09:15:00' 
GROUP BY id_ordinateur; 

-- prendre selon une seule journee
(SELECT id_ordinateur, date_changement AS derniere FROM historique_etat 
WHERE date_changement BETWEEN '2026-01-02 00:00:00' AND '2026-01-02 23:59:59' 
GROUP BY id_ordinateur) dernier

--nombre selon les pannes
SELECT m.libelle panne, COUNT(*) AS nb  
                        FROM ordinateur o  
                        JOIN (SELECT id_ordinateur, date_changement AS derniere FROM historique_etat 
                                WHERE date_changement BETWEEN '2026-01-02 00:00:00' AND '2026-01-02 23:59:59' 
                                GROUP BY id_ordinateur) dernier 
                          ON o.id_ordinateur = dernier.id_ordinateur  
                        JOIN historique_etat h  
                          ON h.id_ordinateur = dernier.id_ordinateur AND h.date_changement = dernier.derniere  
                        JOIN motifs_panne m ON h.id_motif = m.id_motif  
                        GROUP BY m.libelle;