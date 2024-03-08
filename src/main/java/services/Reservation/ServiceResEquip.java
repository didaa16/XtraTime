package services.Reservation;

import entities.Reservation.ResEquip;
import javafx.collections.ObservableList;
import utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceResEquip implements IService<ResEquip> {
    Connection connection;

    public ServiceResEquip() {
        connection = MyDatabase.getInstance().getConnection();
    }


    @Override
    public void ajouter(ResEquip resEquip) throws SQLException {
        String req = "INSERT INTO reservation_equipement (idreservation, idequipement) VALUES (?, ?)";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setInt(1, resEquip.getIdReservation());
        ps.setInt(2, resEquip.getIdEquipement());
        ps.executeUpdate();
        System.out.println("Relation réservation-équipement ajoutée");
    }

    @Override
    public void modifier(ResEquip resEquip) throws SQLException {
        System.out.println("La modification de la relation réservation-équipement n'est pas implémentée");
    }

    @Override
    public void supprimer(int id) throws SQLException {
        // La suppression d'une relation réservation-équipement peut dépendre de votre logique métier
        // Par exemple, vous pourriez ne pas permettre de supprimer cette relation directement.
        // Si nécessaire, implémentez cette méthode en conséquence.
        System.out.println("La suppression de la relation réservation-équipement n'est pas implémentée");
    }

    @Override
    public ObservableList<ResEquip> afficher() throws SQLException {
        ObservableList<ResEquip> relations = null;
        String req = "SELECT * FROM reservation_equipement";
        try (PreparedStatement ps = connection.prepareStatement(req);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ResEquip relation = new ResEquip();
                relation.setIdReservation(rs.getInt("idreservation"));
                relation.setIdEquipement(rs.getInt("idequipement"));
                relations.add(relation);
            }
        }
        return relations;
    }

    // Si nécessaire, vous pouvez ajouter des méthodes supplémentaires pour rechercher des relations spécifiques, etc.
}
