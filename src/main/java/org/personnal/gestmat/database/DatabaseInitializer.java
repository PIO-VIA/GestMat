package org.personnal.gestmat.database;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe utilitaire pour initialiser la base de données
 */
public class DatabaseInitializer {

    /**
     * Initialise la base de données si nécessaire
     */
    public static void initialize() {
        try {
            // Vérifier si le fichier de la base de données existe
            File dbFile = new File("GESTBD.db");
            boolean isNew = !dbFile.exists();

            // Obtenir une connexion (cela créera la BD si elle n'existe pas)
            Connection conn = DatabaseConnection.getConnection();

            // Toujours créer les tables pour s'assurer qu'elles sont bien structurées
            createTables(conn);

            // Vérifier les tables après création
            verifyTables(conn);

            if (isNew) {
                System.out.println("Base de données initialisée avec succès");
            } else {
                System.out.println("Connexion à la base de données réussie - Tables vérifiées");
            }

            conn.close();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'initialisation de la base de données: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Vérifie que les tables nécessaires existent
     */
    private static void verifyTables(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            // Vérifier les tables
            String[] tables = {"Salle", "VideoProjecteur", "Responsable", "Reservation", "Ordinateur", "Enseignant"};

            for (String table : tables) {
                try {
                    stmt.executeQuery("SELECT 1 FROM " + table + " LIMIT 1");
                    System.out.println("Table " + table + " existe et est accessible");
                } catch (SQLException e) {
                    System.err.println("Problème avec la table " + table + ": " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification des tables: " + e.getMessage());
        }
    }

    /**
     * Crée les tables dans la base de données
     */
    private static void createTables(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            // Table pour stocker les salles
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS Salle (" +
                            "CodeSalle TEXT PRIMARY KEY, " +
                            "nomSalle TEXT NOT NULL, " +
                            "capacite INTEGER NOT NULL, " +
                            "disponible BOOLEAN NOT NULL DEFAULT 1" +
                            ")"
            );

            // Table pour stocker les Video Projecteur
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS VideoProjecteur (" +
                            "CodeMaterielV INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "marque TEXT NOT NULL, " +
                            "resolution TEXT NOT NULL, " +
                            "disponible BOOLEAN NOT NULL DEFAULT 1" +
                            ")"
            );

            // Table pour stocker les responsables
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS Responsable (" +
                            "idResponsable INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "nom TEXT NOT NULL, " +
                            "prenom TEXT NOT NULL, " +
                            "password TEXT NOT NULL" +
                            ")"
            );

            // Table pour stocker les enseignants
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS Enseignant (" +
                            "idEnseignant INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "nom TEXT NOT NULL, " +
                            "prenom TEXT NOT NULL" +
                            ")"
            );

            // Table pour stocker les ordinateurs
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS Ordinateur (" +
                            "codeMaterielO INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "marque TEXT NOT NULL, " +
                            "capacite INTEGER, " +
                            "ecran TEXT, " +
                            "disponible BOOLEAN NOT NULL DEFAULT 1" +
                            ")"
            );

            // Table pour stocker les réservations
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS Reservation (" +
                            "idReservation TEXT PRIMARY KEY, " +
                            "idEnseignant INTEGER NOT NULL, " +
                            "CodeMateriel INTEGER, " +
                            "CodeSalle TEXT NOT NULL, " +
                            "dateReservation DATE NOT NULL, " +
                            "dateFinReservation DATE NOT NULL, " +
                            "FOREIGN KEY (idEnseignant) REFERENCES Enseignant(idEnseignant), " +
                            "FOREIGN KEY (CodeMateriel) REFERENCES VideoProjecteur(CodeMaterielV) ON DELETE SET NULL, " +
                            "FOREIGN KEY (CodeMateriel) REFERENCES Ordinateur(codeMaterielO) ON DELETE SET NULL, " +
                            "FOREIGN KEY (CodeSalle) REFERENCES Salle(CodeSalle)" +
                            ")"
            );

            System.out.println("Tables et index créés avec succès");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création des tables: " + e.getMessage());
            throw e;
        }
    }
}