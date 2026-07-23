DROP DATABASE ordinateur;
CREATE DATABASE ordinateur;
USE ordinateur;

CREATE TABLE marque (
    id_marque INT AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL
);

CREATE TABLE modele (
    id_modele INT AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL,
    id_marque INT NOT NULL,
    reference VARCHAR(50),
    FOREIGN KEY (id_marque) REFERENCES marque(id_marque)
);

CREATE TABLE ordinateur (
    id_ordinateur INT AUTO_INCREMENT PRIMARY KEY,
    id_modele INT NOT NULL,
    ram INT,
    processeur VARCHAR(50),
    disque_dur INT,
    FOREIGN KEY (id_modele) REFERENCES modele(id_modele)
);

CREATE TABLE user (
    id_user INT AUTO_INCREMENT PRIMARY KEY,
    login_name VARCHAR(50),
    password_login VARCHAR(50),
    role_login VARCHAR(50)
);

CREATE TABLE etat (
    id_etat INT AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE motifs_panne (
    id_motif INT AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE historique_etat (
    id_historique INT AUTO_INCREMENT PRIMARY KEY,
    id_ordinateur INT NOT NULL,
    id_etat INT NOT NULL,
    id_motif INT NOT NULL,
    date_changement DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_ordinateur) REFERENCES ordinateur(id_ordinateur),
    FOREIGN KEY (id_etat) REFERENCES etat(id_etat),
    FOREIGN KEY (id_motif) REFERENCES motifs_panne(id_motif)
);