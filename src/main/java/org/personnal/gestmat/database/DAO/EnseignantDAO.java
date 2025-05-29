package org.personnal.gestmat.database.DAO;


import org.personnal.gestmat.database.DatabaseConnection;
import org.personnal.gestmat.model.Enseignant;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnseignantDAO {

    // CREATE
    public void create(Enseignant enseignant) throws SQLException {
        String sql = "INSERT INTO Enseignant (nom, prenom) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);) {
            statement.setString(1, enseignant.getNom());
            statement.setString(2, enseignant.getPrenom());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    enseignant.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    // READ (by ID)
    public Enseignant read(long id) throws SQLException {
        String sql = "SELECT * FROM Enseignant WHERE idEnseignant = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Enseignant(
                            resultSet.getLong("idEnseignant"),
                            resultSet.getString("nom"),
                            resultSet.getString("prenom")
                    );
                }
            }
        }
        return null;
    }

    // READ ALL
    public List<Enseignant> readAll() throws SQLException {
        List<Enseignant> enseignants = new ArrayList<>();
        String sql = "SELECT * FROM Enseignant";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                enseignants.add(new Enseignant(
                        resultSet.getLong("idEnseignant"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom")
                ));
            }
        }
        return enseignants;
    }

    // UPDATE
    public void update(Enseignant enseignant) throws SQLException {
        String sql = "UPDATE Enseignant SET nom = ?, prenom = ? WHERE idEnseignant = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);) {
            statement.setString(1, enseignant.getNom());
            statement.setString(2, enseignant.getPrenom());
            statement.setLong(3, enseignant.getId());
            statement.executeUpdate();
        }
    }

    // DELETE
    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM Enseignant WHERE idEnseignant = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }
}