package org.personnal.gestmat.database.DAO;

import org.personnal.gestmat.database.DatabaseConnection;
import org.personnal.gestmat.model.VideoProjecteur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VideoProjecteurDAO {

    // CREATE
    public void create(VideoProjecteur videoProjecteur) throws SQLException {
        String sql = "INSERT INTO VideoProjecteur (marque, resolution, disponible) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, videoProjecteur.getMarque());
            statement.setString(2, videoProjecteur.getResolution());
            statement.setBoolean(3, videoProjecteur.isDisponible());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    videoProjecteur.setCodeMateriel(generatedKeys.getLong(1));
                }
            }
        }
    }

    // READ (by CodeMaterielV)
    public VideoProjecteur read(long codeMateriel) throws SQLException {
        String sql = "SELECT * FROM VideoProjecteur WHERE CodeMaterielV = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, codeMateriel);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new VideoProjecteur(
                            resultSet.getLong("CodeMaterielV"),
                            resultSet.getBoolean("disponible"),
                            resultSet.getString("marque"),
                            resultSet.getString("resolution")
                    );
                }
            }
        }
        return null;
    }

    // READ ALL
    public List<VideoProjecteur> readAll() throws SQLException {
        List<VideoProjecteur> projecteurs = new ArrayList<>();
        String sql = "SELECT * FROM VideoProjecteur";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                projecteurs.add(new VideoProjecteur(
                        resultSet.getLong("CodeMaterielV"),
                        resultSet.getBoolean("disponible"),
                        resultSet.getString("marque"),
                        resultSet.getString("resolution")
                ));
            }
        }
        return projecteurs;
    }

    // UPDATE
    public void update(VideoProjecteur videoProjecteur) throws SQLException {
        String sql = "UPDATE VideoProjecteur SET marque = ?, resolution = ?, disponible = ? WHERE CodeMaterielV = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, videoProjecteur.getMarque());
            statement.setString(2, videoProjecteur.getResolution());
            statement.setBoolean(3, videoProjecteur.isDisponible());
            statement.setLong(4, videoProjecteur.getCodeMateriel());
            statement.executeUpdate();
        }
    }

    // DELETE
    public void delete(long codeMateriel) throws SQLException {
        String sql = "DELETE FROM VideoProjecteur WHERE CodeMaterielV = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, codeMateriel);
            statement.executeUpdate();
        }
    }

    // Méthode supplémentaire pour trouver les projecteurs disponibles
    public List<VideoProjecteur> findAvailable() throws SQLException {
        List<VideoProjecteur> projecteurs = new ArrayList<>();
        String sql = "SELECT * FROM VideoProjecteur WHERE disponible = true";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                projecteurs.add(new VideoProjecteur(
                        resultSet.getLong("CodeMaterielV"),
                        true,
                        resultSet.getString("marque"),
                        resultSet.getString("resolution")
                ));
            }
        }
        return projecteurs;
    }
}
