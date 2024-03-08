package services.Reservation;

import controllers.Reservation.ModifierReservationController;
import entities.Reservation.Equipement;
import entities.Reservation.Reservation;
import utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ServiceModifierReservation {
    Connection connection;

    public ServiceModifierReservation(){
        connection= MyDatabase.getInstance().getConnection();

    }


    public void modifier(Reservation reservationModifiee) throws SQLException {
        if (reservationModifiee == null) {
            throw new IllegalArgumentException("La réservation à modifier ne peut pas être nulle.");
        }

        String req = "UPDATE `reservation` SET `duree`=?, `equipements`=?, `date`=?, `prix`=?, `terrainId`=?, `clientPseudo`=? WHERE `id` = ?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, reservationModifiee.getDuree());
            ps.setString(2, reservationModifiee.getEquipements());
            ps.setString(3, reservationModifiee.getDate());
            ps.setInt(4, reservationModifiee.getPrix());
            ps.setInt(5, reservationModifiee.getTerrainId());
            ps.setString(6, reservationModifiee.getClientPseudo());
            ps.setInt(7, reservationModifiee.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Réservation modifiée avec succès");
            } else {
                System.out.println("Aucune réservation n'a été modifiée. Vérifiez l'identifiant de la réservation.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification de la réservation : " + e.getMessage());
            throw e;
        }
    }



}