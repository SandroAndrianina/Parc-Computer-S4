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