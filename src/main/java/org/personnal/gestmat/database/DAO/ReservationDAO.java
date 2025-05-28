package org.personnal.gestmat.database.DAO;

import org.personnal.gestmat.database.DatabaseConnection;
import org.personnal.gestmat.model.Reservation;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {


    // CREATE
    public void create(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO Reservation (idReservation, idEnseignant, CodeMateriel, CodeSalle, dateReservation, dateFinReservation) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, reservation.getIdReservation());
            statement.setLong(2, reservation.getIdEnseignant());
            statement.setObject(3, reservation.getCodeMateriel() == 0 ? null : reservation.getCodeMateriel());
            statement.setString(4, reservation.getCodeSalle());
            statement.setDate(5, Date.valueOf(reservation.getDateReservation()));
            statement.setDate(6, Date.valueOf(reservation.getDateFinReservation()));
            statement.executeUpdate();
        }
    }

    // READ (by idReservation)
    public Reservation read(String idReservation) throws SQLException {
        String sql = "SELECT * FROM Reservation WHERE idReservation = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, idReservation);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Reservation(
                            resultSet.getString("idReservation"),
                            resultSet.getLong("idEnseignant"),
                            resultSet.getString("CodeSalle"),
                            resultSet.getLong("CodeMateriel"),
                            resultSet.getDate("dateReservation").toLocalDate(),
                            resultSet.getDate("dateFinReservation").toLocalDate()
                    );
                }
            }
        }
        return null;
    }

    // READ ALL
    public List<Reservation> readAll() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM Reservation";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                reservations.add(new Reservation(
                        resultSet.getString("idReservation"),
                        resultSet.getLong("idEnseignant"),
                        resultSet.getString("CodeSalle"),
                        resultSet.getLong("CodeMateriel"),
                        resultSet.getDate("dateReservation").toLocalDate(),
                        resultSet.getDate("dateFinReservation").toLocalDate()
                ));
            }
        }
        return reservations;
    }

    // UPDATE
    public void update(Reservation reservation) throws SQLException {
        String sql = "UPDATE Reservation SET idEnseignant = ?, CodeMateriel = ?, CodeSalle = ?, " +
                "dateReservation = ?, dateFinReservation = ? WHERE idReservation = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, reservation.getIdEnseignant());
            statement.setObject(2, reservation.getCodeMateriel() == 0 ? null : reservation.getCodeMateriel());
            statement.setString(3, reservation.getCodeSalle());
            statement.setDate(4, Date.valueOf(reservation.getDateReservation()));
            statement.setDate(5, Date.valueOf(reservation.getDateFinReservation()));
            statement.setString(6, reservation.getIdReservation());
            statement.executeUpdate();
        }
    }

    // DELETE
    public void delete(String idReservation) throws SQLException {
        String sql = "DELETE FROM Reservation WHERE idReservation = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, idReservation);
            statement.executeUpdate();
        }
    }

    // Méthode supplémentaire pour trouver les réservations par enseignant
    public List<Reservation> findByEnseignant(long idEnseignant) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM Reservation WHERE idEnseignant = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, idEnseignant);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    reservations.add(new Reservation(
                            resultSet.getString("idReservation"),
                            resultSet.getLong("idEnseignant"),
                            resultSet.getString("CodeSalle"),
                            resultSet.getLong("CodeMateriel"),
                            resultSet.getDate("dateReservation").toLocalDate(),
                            resultSet.getDate("dateFinReservation").toLocalDate()
                    ));
                }
            }
        }
        return reservations;
    }
}