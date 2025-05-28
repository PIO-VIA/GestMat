package org.personnal.gestmat.database.DAO;

import org.personnal.gestmat.database.DatabaseConnection;
import org.personnal.gestmat.model.Salle;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalleDAO {


    // CREATE
    public void create(Salle salle) throws SQLException {
        String sql = "INSERT INTO Salle (CodeSalle, nomSalle, capacite, disponible) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, salle.getCodeSalle());
            statement.setString(2, salle.getNomSalle());
            statement.setInt(3, salle.getNbPlacesDispo());
            statement.setBoolean(4, salle.isDispo());
            statement.executeUpdate();
        }
    }

    // READ (by CodeSalle)
    public Salle read(String codeSalle) throws SQLException {
        String sql = "SELECT * FROM Salle WHERE CodeSalle = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, codeSalle);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Salle(
                            resultSet.getString("CodeSalle"),
                            resultSet.getString("nomSalle"),
                            resultSet.getInt("capacite"),
                            resultSet.getBoolean("disponible")
                    );
                }
            }
        }
        return null;
    }

    // READ ALL
    public List<Salle> readAll() throws SQLException {
        List<Salle> salles = new ArrayList<>();
        String sql = "SELECT * FROM Salle";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                salles.add(new Salle(
                        resultSet.getString("CodeSalle"),
                        resultSet.getString("nomSalle"),
                        resultSet.getInt("capacite"),
                        resultSet.getBoolean("disponible")
                ));
            }
        }
        return salles;
    }

    // UPDATE
    public void update(Salle salle) throws SQLException {
        String sql = "UPDATE Salle SET nomSalle = ?, capacite = ?, disponible = ? WHERE CodeSalle = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);) {
            statement.setString(1, salle.getNomSalle());
            statement.setInt(2, salle.getNbPlacesDispo());
            statement.setBoolean(3, salle.isDispo());
            statement.setString(4, salle.getCodeSalle());
            statement.executeUpdate();
        }
    }

    // DELETE
    public void delete(String codeSalle) throws SQLException {
        String sql = "DELETE FROM Salle WHERE CodeSalle = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);) {
            statement.setString(1, codeSalle);
            statement.executeUpdate();
        }
    }
}
