INSERT INTO marque (libelle) VALUES 
('Dell'),
('HP'),
('Lenovo'),
('Acer');

INSERT INTO modele (libelle, id_marque, reference) VALUES 
('Opliplex', 1, 'INS15-5510'),
('Prodesk', 2, 'LAT5420'),
('ThinkCentre', 3, 'PAV15-EG'),
('ProBook', 2, 'EB840-G8'),
('Latitude', 1, 'IP5-15'),
('Precision', 1, 'ZB14-OLED'),
('ThinkCentre', 3, 'ROG-G15'),
('Vertion', 4, 'ROG-G15');

INSERT INTO user (login_name, password_login, role_login) VALUES
('admin', 'admin123', 'ADMIN'),
('john', 'john123', 'USER'),
('alice', 'alice123', 'USER');

INSERT INTO ordinateur (id_modele, ram, processeur, disque_dur) VALUES 
(1, 16, 'Intel Core i7', 512),
(2, 8, 'Intel Core i7', 256),
(3, 8, 'Intel Core i5', 512),
(4, 16, 'Intel Core i9', 512),
(5, 16, 'Intel Core i5', 512),
(6, 16, 'Intel Core i7', 256),
(7, 8, 'Intel Core i9', 256),
(8, 8, 'Intel Core i7', 256);

INSERT INTO etat (libelle) VALUES ('fonctionnel'), ('dysfonctionnel');

INSERT INTO motifs_panne (libelle) VALUES ('alimentation'), ('carte_mere'), ('disque_dur'), ('rien');

INSERT INTO historique_etat (id_ordinateur, id_etat, id_motif, date_changement) VALUES 
-- Ordinateur 1 (Dell Inspiron 15)
(1, 1, 4, '2026-07-21 00:00:00'),
(1, 2, 3,'2026-07-22 00:00:00'),
(1, 1, 4,'2026-07-23 00:00:00'),

-- Ordinateur 2 (Dell Inspiron 15)
(2, 1, 4,'2026-07-22 00:00:00'),
(2, 2, 1,'2026-07-23 00:00:00'),

-- Ordinateur 3 (Dell Latitude 5420)
(3, 2, 1,'2026-07-21 00:00:00'),
(3, 1, 4,'2026-07-22 00:00:00'),
(3, 2, 2,'2026-07-23 00:00:00'),

-- Ordinateur 4 (HP Pavilion 15)
(4, 1, 4,'2026-07-23 00:00:00'),

-- Ordinateur 5 (HP EliteBook 840)
(5, 1, 4,'2026-07-21 00:00:00'),
(5, 2, 3,'2026-07-23 00:00:00'),

-- Ordinateur 6 (Lenovo ThinkPad T14)
(6, 2, 1,'2026-07-23 00:00:00'),

-- Ordinateur 7 (Lenovo IdeaPad 5)
(7, 1, 4,'2026-07-21 00:00:00'),
(7, 2, 2,'2026-07-23 00:00:00'),

-- Ordinateur 8 (Asus ZenBook 14)
(8, 1, 4,'2026-07-23 00:00:00');