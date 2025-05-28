package org.personnal.gestmat.database.DAO;

import org.personnal.gestmat.database.DatabaseConnection;
import org.personnal.gestmat.model.Ordinateur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdinateurDAO {


    // CREATE
    public void create(Ordinateur ordinateur) throws SQLException {
        String sql = "INSERT INTO Ordinateur (marque, capacite, ecran, disponible) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, ordinateur.getMarque());
            statement.setInt(2, ordinateur.getCapacite());
            statement.setString(3, ordinateur.getEcran());
            statement.setBoolean(4, ordinateur.isDisponible());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ordinateur.setCodeMateriel(generatedKeys.getLong(1));
                }
            }
        }
    }

    // READ (by codeMaterielO)
    public Ordinateur read(long codeMateriel) throws SQLException {
        String sql = "SELECT * FROM Ordinateur WHERE codeMaterielO = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, codeMateriel);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Ordinateur(
                            resultSet.getLong("codeMaterielO"),
                            resultSet.getBoolean("disponible"),
                            resultSet.getString("marque"),
                            resultSet.getInt("capacite"),
                            resultSet.getString("ecran")
                    );
                }
            }
        }
        return null;
    }

    // READ ALL
    public List<Ordinateur> readAll() throws SQLException {
        List<Ordinateur> ordinateurs = new ArrayList<>();
        String sql = "SELECT * FROM Ordinateur";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                ordinateurs.add(new Ordinateur(
                        resultSet.getLong("codeMaterielO"),
                        resultSet.getBoolean("disponible"),
                        resultSet.getString("marque"),
                        resultSet.getInt("capacite"),
                        resultSet.getString("ecran")
                ));
            }
        }
        return ordinateurs;
    }

    // UPDATE
    public void update(Ordinateur ordinateur) throws SQLException {
        String sql = "UPDATE Ordinateur SET marque = ?, capacite = ?, ecran = ?, disponible = ? WHERE codeMaterielO = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, ordinateur.getMarque());
            statement.setInt(2, ordinateur.getCapacite());
            statement.setString(3, ordinateur.getEcran());
            statement.setBoolean(4, ordinateur.isDisponible());
            statement.setLong(5, ordinateur.getCodeMateriel());
            statement.executeUpdate();
        }
    }

    // DELETE
    public void delete(long codeMateriel) throws SQLException {
        String sql = "DELETE FROM Ordinateur WHERE codeMaterielO = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, codeMateriel);
            statement.executeUpdate();
        }
    }

    // Méthodes supplémentaires utiles

    // Trouver les ordinateurs disponibles
    public List<Ordinateur> findAvailable() throws SQLException {
        List<Ordinateur> ordinateurs = new ArrayList<>();
        String sql = "SELECT * FROM Ordinateur WHERE disponible = true";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                ordinateurs.add(new Ordinateur(
                        resultSet.getLong("codeMaterielO"),
                        true,
                        resultSet.getString("marque"),
                        resultSet.getInt("capacite"),
                        resultSet.getString("ecran")
                ));
            }
        }
        return ordinateurs;
    }

    // Trouver les ordinateurs par marque
    public List<Ordinateur> findByMarque(String marque) throws SQLException {
        List<Ordinateur> ordinateurs = new ArrayList<>();
        String sql = "SELECT * FROM Ordinateur WHERE marque LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, "%" + marque + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ordinateurs.add(new Ordinateur(
                            resultSet.getLong("codeMaterielO"),
                            resultSet.getBoolean("disponible"),
                            resultSet.getString("marque"),
                            resultSet.getInt("capacite"),
                            resultSet.getString("ecran")
                    ));
                }
            }
        }
        return ordinateurs;
    }
}