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
            File dbFile = new File("client_chat.db");
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
            // Vérifier les tables users, messages et files
            String[] tables = {"Salle", "VideoProjecteur", "Responsable","Reservation","Ordinateur","Enseignant"};

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
                            "CodeSalle INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "username TEXT NOT NULL UNIQUE, " +
                            "capacite INTEGER NOT NULL, " +
                            "status BOOLEAN NOT NULL DEFAULT 1" +
                            ")"
            );

            // Table pour stocker les messages
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS VideoProjecteur (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "sender TEXT NOT NULL, " +
                            "receiver TEXT NOT NULL, " +
                            "content TEXT NOT NULL, " +
                            "timestamp TEXT DEFAULT CURRENT_TIMESTAMP, " +
                            "read BOOLEAN NOT NULL DEFAULT 0" +
                            ")"
            );

            // Table pour stocker les fichiers
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS files (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "sender TEXT NOT NULL, " +
                            "receiver TEXT NOT NULL, " +
                            "filename TEXT NOT NULL, " +
                            "filepath TEXT, " +
                            "timestamp TEXT DEFAULT CURRENT_TIMESTAMP, " +
                            "read BOOLEAN NOT NULL DEFAULT 0" +
                            ")"
            );

            // Créer des index pour optimiser les recherches
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_users_username ON users(username)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_messages_sender ON messages(sender)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_messages_receiver ON messages(receiver)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_files_sender ON files(sender)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_files_receiver ON files(receiver)");

            System.out.println("Tables et index créés avec succès");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création des tables: " + e.getMessage());
            throw e;
        }
    }
}