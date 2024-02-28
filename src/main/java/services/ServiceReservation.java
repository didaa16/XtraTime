package services;

import entities.Reservation;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceReservation implements IService<Reservation> {
    static Connection connection;

    public ServiceReservation() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Reservation res) throws SQLException {
        String req = "INSERT INTO reservation ( equipements, date, duree) " +
                "VALUES (?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setString(1, res.getEquipements());
        ps.setString(2, res.getDate());
        ps.setString(3, res.getDuree());
        ps.executeUpdate();
    }

    @Override
    public void modifier(Reservation res) throws SQLException {
        String req = "UPDATE reservation SET terrainid=?, clientPseudo=?, equipements=?, prix=?, date=?, duree=? WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setInt(1, res.getTerrainId());
        ps.setString(2, res.getClientPseudo());
        ps.setString(3, res.getEquipements());
        ps.setInt(4, res.getPrix());
        ps.setString(5, res.getDate());
        ps.setString(6, res.getDuree()); // Changement ici
        ps.setInt(7, res.getId());
        ps.executeUpdate();
        System.out.println("Réservation modifiée");
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM `reservation` WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setInt(1, id);
        ps.executeUpdate();
        System.out.println("Réservation supprimée avec succès");
    }

    public List<Reservation> afficher() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String req = "SELECT * FROM reservation";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(req);
        while (rs.next()) {
            Reservation r = new Reservation();
            r.setId(rs.getInt("id"));
            r.setTerrainId(rs.getInt("terrainid"));
            r.setClientPseudo(rs.getString("clientPseudo"));
            r.setEquipements(rs.getString("equipements"));
            r.setPrix(rs.getInt("prix")); // Changement ici
            r.setDate(rs.getString("date"));
            r.setDuree(rs.getString("duree")); // Changement ici
            reservations.add(r);
        }
        return reservations;
    }
}
