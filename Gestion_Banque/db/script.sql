CREATE DATABASE gestion_banque;
\c gestion_banque;

CREATE TABLE client (
    id_client SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE compte_courant (
    id_compte SERIAL PRIMARY KEY,
    id_client INT REFERENCES client(id_client) ON DELETE CASCADE,
    taux_annuel NUMERIC(5,2) DEFAULT 0,
    solde NUMERIC(15,2) DEFAULT 0,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE compte_depot (
    id_compte SERIAL PRIMARY KEY,
    id_client INT REFERENCES client(id_client) ON DELETE CASCADE,
    taux_annuel NUMERIC(5,2) DEFAULT 0,
    seuil_minimum NUMERIC(15,2),
    solde NUMERIC(15,2) DEFAULT 0,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Pret (
    id_pret SERIAL PRIMARY KEY,
    id_client INT REFERENCES client(id_client) ON DELETE CASCADE,
    montant NUMERIC(15,2) NOT NULL,
    taux_annuel NUMERIC(5,2) NOT NULL,
    duree_mois INT NOT NULL,
    date_debut TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_fin TIMESTAMP
);

CREATE TABLE transaction (
    id_transaction SERIAL PRIMARY KEY,
    id_client INT REFERENCES client(id_client) ON DELETE CASCADE,
    type_compte VARCHAR(20) CHECK (type_compte IN ('courant', 'depot')),
    id_compte INT NOT NULL,
    montant NUMERIC(15,2) NOT NULL,
    type_transaction VARCHAR(10) CHECK (type_transaction IN ('depot', 'retrait')),
    date_transaction TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
